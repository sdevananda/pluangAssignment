package com.example.trading.controller;

import com.example.trading.service.OrderService;
import com.example.trading.vo.OrderDto;
import com.example.trading.vo.OrderVo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @SneakyThrows
    @PostMapping
    public String order(@RequestBody OrderVo orderVo) {

        if(orderVo.getQuantity()<=0){
            return "Quantity must be greater than zero";
        }
        if(orderVo.getPrice()<=0){
            return "Price must be greater than zero";
        }
        if(!orderVo.getOrderType().equals("sell") || !orderVo.getOrderType().equals("buy")){
            throw new Exception("Order type is wrong");
        }
        return orderService.order(orderVo) ;


    }
    @GetMapping
    public List<OrderDto> orders(){
        return orderService.getOrders();
    }

}
