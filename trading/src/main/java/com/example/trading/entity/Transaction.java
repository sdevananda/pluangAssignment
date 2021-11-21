package com.example.trading.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @ManyToOne
    @JoinColumn(
            name = "sell_order_id",
            referencedColumnName = "id"
    )
    private SellOrder sellOrder;

    private double quantity;
    private double price ;

    @ManyToOne
    @JoinColumn(
            name = "buy_order_id",
            referencedColumnName = "id"
    )
    private BuyOrder buyOrder;


    public Transaction(SellOrder sellOrder, BuyOrder buyOrder, double quantity, double price) {
        this.sellOrder = sellOrder;
        this.buyOrder = buyOrder;
        this.quantity = quantity;
        this.price = price;
    }
}
