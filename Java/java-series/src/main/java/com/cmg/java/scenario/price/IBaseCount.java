package com.cmg.java.scenario.price;

import java.math.BigDecimal;

/**
 * 计算支付金额接口类
 *
 */
public interface IBaseCount {

    BigDecimal countPayMoney(OrderDetail orderDetail);

}