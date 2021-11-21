package com.example.trading.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo {
    private String orderType;
    private String name;
    private double quantity;
    private double price;
    private String time;

}
