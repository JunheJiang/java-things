package com.cmg.java.scenario.price;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 计算促销后的支付价格
 */
public class PromotionFactory {

    public static BigDecimal getPayMoney(OrderDetail orderDetail) {

        //获取给商品设定的促销类型
        Map<PromotionType, SupportPromotions> supportPromotionsList = orderDetail.getMerchandise().getSupportPromotions();

        //初始化计算类
        IBaseCount baseCount = new BaseCount();
        if (supportPromotionsList != null && supportPromotionsList.size() > 0) {
            for (PromotionType promotionType : supportPromotionsList.keySet()) {//遍历设置的促销类型，通过装饰器组合促销类型
                baseCount = promotion(supportPromotionsList.get(promotionType), baseCount);
            }
        }
        return baseCount.countPayMoney(orderDetail);
    }

    /**
     * 组合促销类型
     *
     * @param supportPromotions
     * @param baseCount
     * @return
     */
    private static IBaseCount promotion(SupportPromotions supportPromotions, IBaseCount baseCount) {
        if (supportPromotions.getPromotionType() == PromotionType.COUPON) {
            baseCount = new CouponDecorator(baseCount);
        } else if (supportPromotions.getPromotionType() == PromotionType.REDPACKET) {
            baseCount = new RedPacketDecorator(baseCount);
        }
        return baseCount;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Order order = new Order();
//        init(order);
        for (OrderDetail orderDetail : order.getList()) {
            BigDecimal payMoney = PromotionFactory.getPayMoney(orderDetail);
            orderDetail.setPayMoney(payMoney);
            System.out.println("最终支付金额：" + orderDetail.getPayMoney());
        }
    }

}