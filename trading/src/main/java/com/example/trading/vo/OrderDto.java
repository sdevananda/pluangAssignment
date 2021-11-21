package com.example.trading.vo;

import com.example.trading.entity.BuyOrder;
import com.example.trading.entity.SellOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto implements Comparable<OrderDto>{
    private Long id;
    @DateTimeFormat(style = "HH:MM")
    private Date time;
    private String name;
    private String orderType;
    private double quantity;
    private double price;

    public OrderDto(BuyOrder order) {
        this.id = order.getId();
        this.time = order.getTime();
        this.name = order.getName();
        this.orderType = "buy";
        this.quantity = (int)order.getOriginalQuantity();
        this.price = order.getPrice();
    }

    public OrderDto(SellOrder order) {
        this.id = order.getId();
        this.time = order.getTime();
        this.name = order.getName();
        this.orderType = "sell";
        this.quantity = order.getOriginalQuantity();
        this.price = order.getPrice();
    }

    @Override
    public int compareTo(OrderDto orderDto) {
        return (int) (this.id - orderDto.id);
    }
}
