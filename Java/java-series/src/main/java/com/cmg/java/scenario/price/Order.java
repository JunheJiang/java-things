package com.cmg.java.scenario.price;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 主订单
 *
 */
@Data
public class Order {

    private int id; //订单ID
    private String orderNo; //订单号
    private BigDecimal totalPayMoney; //总支付金额
    private List<OrderDetail> list; //详细订单列表
}