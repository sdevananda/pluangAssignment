package com.example.trading.service;

import com.example.trading.entity.BuyOrder;
import com.example.trading.entity.SellOrder;
import com.example.trading.entity.Transaction;
import com.example.trading.repository.BuyOrderRepo;
import com.example.trading.repository.SellOrderRepo;
//import com.example.trading.repository.TransactionRepo;
import com.example.trading.repository.TransactionRepo;
import com.example.trading.utils.FileUtil;
import com.example.trading.vo.OrderDto;
import com.example.trading.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private BuyOrderRepo buyOrderRepo;
    @Autowired
    private SellOrderRepo sellOrderRepo;
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private FileUtil fileUtil;


    public String order(OrderVo orderVo) throws ParseException, IOException {


       Date timeNow = Date.from(Instant.now()) ;
       String str = timeNow.getHours()+":"+timeNow.getMinutes();
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Date time = (Date)formatter.parse(str);
        if (orderVo.getOrderType().equalsIgnoreCase("buy")){
            System.out.println("calling checksell");
            //Date time = new Date(orderVo.getTime());
            BuyOrder buyOrder = new BuyOrder(orderVo.getName(),orderVo.getQuantity(),orderVo.getPrice(), time,"QUEUE");
            return checkSell(buyOrder);
        }
        else if (orderVo.getOrderType().equalsIgnoreCase("sell")){
            System.out.println("calling checkBuy");
            SellOrder sellOrder = new SellOrder(orderVo.getName(),orderVo.getQuantity(),orderVo.getPrice(), time,"QUEUE");
            return checkBuy(sellOrder);
        }

        else {
            return "Wrong orderType" ;
        }

        //return "";
    }

    @Transactional
    String checkBuy(SellOrder sellOrder) throws IOException {
        List<BuyOrder> sortedOrders = buyOrderRepo.findOrdersGreaterthan(sellOrder.getPrice(), sellOrder.getName());
      //  System.out.println("\n\n\nBUY sorted orders are:"+sortedOrders.toString());
        SellOrder sellOrder1 = sellOrderRepo.save(sellOrder);
        System.out.println("saving sell order"+sellOrder.toString());

        for (BuyOrder buyOrder : sortedOrders){
            System.out.println("\n\n buy order inside fro loop:"+ buyOrder);
            if(sellOrder1.getQuantity() == buyOrder.getQuantity()){
                String text = sellOrder1.getId()+" "+sellOrder1.getQuantity()+" "+sellOrder1.getPrice()+" "+buyOrder.getId() ;
                fileUtil.append(text);
                Transaction transaction = transactionRepo.save(new Transaction(sellOrder1,buyOrder,sellOrder1.getQuantity(),sellOrder.getPrice()));
                sellOrder.setQuantity(Double.valueOf(0));
                sellOrder.setStatus("COMPLETE");
              //  transactionRepo.save(new Transaction(sellOrder,buyOrder));

                System.out.println("Transaction:"+transaction.toString());
                buyOrderRepo.updateQuantityAndStatus(0,"COMPLETE",buyOrder.getId());
                System.out.println("\nsell order complete");
                break;
            }

            else if (sellOrder1.getQuantity() < buyOrder.getQuantity()){
                System.out.println("updating buy order quantity as sell is done");
                String text = sellOrder1.getId()+" "+sellOrder1.getQuantity()+" "+sellOrder1.getPrice()+" "+buyOrder.getId() ;
                fileUtil.append(text);
                Transaction transaction = transactionRepo.save(new Transaction(sellOrder1,buyOrder,sellOrder1.getQuantity(),sellOrder1.getPrice()));
                buyOrderRepo.updateQuantityAndStatus(buyOrder.getQuantity()-sellOrder1.getQuantity(),"PARTIAL",buyOrder.getId());
                sellOrder1.setStatus("COMPLETE");
                System.out.println("sell order complete");
                //transactionRepo.save(new Transaction(sellOrder1,buyOrder));

                System.out.println("Transaction:"+transaction.toString());
                sellOrder1.setQuantity(0);
                break;
                //return "Transaction done";
            }
            else {
                System.out.println("\nDone buyOrder :"+buyOrder);
                buyOrderRepo.updateQuantityAndStatus(0,"COMPLETE",buyOrder.getId());
                String text = sellOrder1.getId()+" "+buyOrder.getQuantity()+" "+sellOrder1.getPrice()+" "+buyOrder.getId() ;
                fileUtil.append(text);
                Transaction transaction = transactionRepo.save(new Transaction(sellOrder1,buyOrder,buyOrder.getQuantity(),sellOrder1.getPrice()));
                System.out.println("Transaction:"+transaction.toString());
                sellOrder1.setQuantity(sellOrder1.getQuantity()-buyOrder.getQuantity());
                sellOrder1.setStatus("PARTIAL");
               // return "Transaction completed";
            }
        }
       // String status = sellOrder1.getQuantity() != sellOrder.getQuantity() ? (sellOrder1.getQuantity()==Double.valueOf(0)? "COMPLETE":"PARTIAL"):"QUEUE";
        sellOrderRepo.updateQuantityAndStatus(sellOrder1.getQuantity(), sellOrder1.getStatus(), sellOrder1.getId());
        return "Order status: "+sellOrder1.getStatus() ;
    }

    private List<BuyOrder> buySort(List<BuyOrder> orders) {
        Comparator<BuyOrder> compareByPriceAndTime = Comparator
                .comparing(BuyOrder::getPrice).reversed()
                .thenComparing(BuyOrder::getTime);

        return
                orders.stream()
                        .sorted(compareByPriceAndTime)
                        .collect(Collectors.toList());
    }

    @Transactional
    String checkSell(BuyOrder buyOrder) throws IOException {
        List<SellOrder> sortedOrders = sellOrderRepo.findOrdersLessthan(buyOrder.getPrice(), buyOrder.getName());
        BuyOrder buyOrder1 = buyOrderRepo.save(buyOrder);
        System.out.println("\n\nsaving buy order"+buyOrder.toString());
        System.out.println("sell sorted orders are:"+sortedOrders.toString());
         //   return "Buy order submitted" ;
       // }

        for (SellOrder sellOrder : sortedOrders){
            if(buyOrder1.getQuantity() == sellOrder.getQuantity()){
               // return "Transaction completed";
                sellOrderRepo.updateQuantityAndStatus(0,"COMPLETE",sellOrder.getId());
                buyOrder1.setQuantity(0);
                buyOrder1.setStatus("COMPLETE");
                String text = sellOrder.getId()+" "+sellOrder.getQuantity()+" "+sellOrder.getPrice()+" "+buyOrder1.getId() ;
                fileUtil.append(text);
                Transaction transaction = transactionRepo.save(new Transaction(sellOrder,buyOrder1,sellOrder.getQuantity(),sellOrder.getPrice()));
                System.out.println("Transaction:"+transaction.toString());
                System.out.println("buy order quantity zero");
                break;
            }
            else if (buyOrder1.getQuantity() > sellOrder.getQuantity()){
                System.out.println("buy order quantiy less");
                buyOrder1.setQuantity(buyOrder1.getQuantity()-sellOrder.getQuantity());
                buyOrder1.setStatus("PARTIAL");
                sellOrderRepo.updateQuantityAndStatus(0,"COMPLETE",sellOrder.getId());
                String text = sellOrder.getId()+" "+sellOrder.getQuantity()+" "+sellOrder.getPrice()+" "+buyOrder1.getId() ;
                fileUtil.append(text);
                Transaction transaction = transactionRepo.save(new Transaction(sellOrder,buyOrder1,sellOrder.getQuantity(),sellOrder.getPrice()));
                System.out.println("Transaction:"+transaction.toString());
            }
            else {
                sellOrderRepo.updateQuantityAndStatus(sellOrder.getQuantity()-buyOrder1.getQuantity(),"PARTIAL", sellOrder.getId());
                System.out.println("buy order completed");
                String text = sellOrder.getId()+" "+buyOrder1.getQuantity()+" "+sellOrder.getPrice()+" "+buyOrder1.getId() ;
                fileUtil.append(text);
                Transaction transaction = transactionRepo.save(new Transaction(sellOrder,buyOrder1,buyOrder1.getQuantity(),sellOrder.getPrice()));
                System.out.println("Transaction:"+transaction.toString());
                buyOrder1.setStatus("COMPLETE");
                buyOrder1.setQuantity(0);
                break;
                //return "Transaction completed";
            }
        }

        buyOrderRepo.updateQuantityAndStatus(buyOrder1.getQuantity(),buyOrder1.getStatus(),buyOrder1.getId());
       // buyOrderRepo.save(buyOrder1);
        return "Order status: "+buyOrder1.getStatus() ;
    }

    private List<SellOrder> sellSort(List<SellOrder> orders) {
        Comparator<SellOrder> compareByPriceAndTime = Comparator
                .comparing(SellOrder::getPrice)
                .thenComparing(SellOrder::getTime);

        return
                orders.stream()
                .sorted(compareByPriceAndTime)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getOrders() {
        List<List<OrderDto>> orderDtos = new ArrayList<>();

        orderDtos.add(buyOrderRepo.findAll().stream().map(order -> new OrderDto(order))
                 .collect((Collectors.toList())));
        orderDtos.add(sellOrderRepo.findAll().stream().map(order -> new OrderDto(order))
                .collect((Collectors.toList())));

      //  roleDtos.add(roleRepo.findAppRoles(appn).stream().map(role -> new RoleDto(role))
        //        .collect(Collectors.toList()));


        return orderDtos.stream().flatMap(List::stream).sorted().collect(Collectors.toList());
     //   return  orderDtos.stream().sorted().collect(Collectors.toList());
       // return orderDtos.stream().sorted((p1, p2)->p1.ge.compareTo(p2.x))
    }
}
