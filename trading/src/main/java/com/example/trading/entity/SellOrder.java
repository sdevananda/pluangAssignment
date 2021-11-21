package com.example.trading.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sellorder")
public class SellOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // private String orderType;
    @NotNull()
    private String name;
    @NotNull()
    private double quantity;

    @NotNull
    private double originalQuantity;

    @NotNull()
    private double price;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(style = "HH:mm")
    @NotNull()
    private Date time;

    private String status;


    public SellOrder(String name, double quantity, double price, Date time, String status) {
        this.name = name;
        this.quantity = quantity;
        this.originalQuantity= quantity;
        this.price = price;
        this.time = time;
        this.status = status;
    }


}
