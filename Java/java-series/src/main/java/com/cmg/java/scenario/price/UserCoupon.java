package com.cmg.java.scenario.price;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 优惠券
 */
@Data
public class UserCoupon {

    private int id; //优惠券ID
    private int userId; //领取优惠券用户ID
    private String sku; //商品SKU
    private BigDecimal coupon; //优惠金额
}