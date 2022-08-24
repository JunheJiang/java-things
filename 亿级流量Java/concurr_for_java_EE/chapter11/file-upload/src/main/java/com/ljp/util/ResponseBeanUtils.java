package com.ljp.util;

import com.ljp.vo.ResponseBean;

import java.util.Date;

public class ResponseBeanUtils {

    public static ResponseBean buildSuccessBean() {

        ResponseBean responseBean = new ResponseBean();
        String code = ResponseEnum.SUCCESS_EN.getCode();
        responseBean.setRspCode(code);
        responseBean.setRspMsg(getDescByCode(code));
        responseBean.setSysTime(DateUtils.formatDateToString(new Date(),DateUtils.DATE_FORMAT_FULL));

        return responseBean;
    }

    public static ResponseBean buildErrorBean() {

        ResponseBean responseBean = new ResponseBean();
        String code = ResponseEnum.FALSE_EN.getCode();
        responseBean.setRspCode(code);
        responseBean.setRspMsg("未知错误");
        responseBean.setSysTime(DateUtils.formatDateToString(new Date(),DateUtils.DATE_FORMAT_FULL));

        return responseBean;
    }

    public static ResponseBean buildErrorBean(String rspMessage) {

        ResponseBean responseBean = new ResponseBean();
        String code = ResponseEnum.FALSE_EN.getCode();
        responseBean.setRspCode(code);
        responseBean.setRspMsg(rspMessage);
        responseBean.setSysTime(DateUtils.formatDateToString(new Date(),DateUtils.DATE_FORMAT_FULL));

        return responseBean;
    }


    public static String getDescByCode(String code) {
        Class<ResponseEnum> clasz = ResponseEnum.class;
        return (String) EnumUtil.getEnumDescriotionByValue(code, clasz);
    }

    public static void setCodeAndDesc(String rspCode, ResponseBean responseBean) {
        responseBean.setRspCode(rspCode);
        responseBean.setRspMsg(getDescByCode(rspCode));
    }

}
