package com.cmg.java.scenario.price;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 红包
 */
@Data
public class UserRedPacket {

    private int id; //红包ID
    private int userId; //领取用户ID
    private String sku; //商品SKU
    private BigDecimal redPacket; //领取红包金额
}