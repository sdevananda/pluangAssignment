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
@Table(name = "buyorder")
public class BuyOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // private String orderType;
    @NotNull()
    private String name;
    @NotNull()
    private double quantity;
    @NotNull()
    private double price;
    @NotNull
    private double originalQuantity;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(style = "HH:mm")
    @NotNull()
    private Date time;

    //@Enumerated(EnumType.STRING)
    //@Column(columnDefinition = "ENUM('QUEUE', 'PARTIAL', 'DONE') DEFAULT 'QUEUE'")
    private String status;

    public BuyOrder(String name, double quantity, double price, Date time, String status) {
        this.name = name;
        this.quantity = quantity;
        this.originalQuantity = quantity;
        this.price = price;
        this.time = time;
        this.status = status;
    }

}
