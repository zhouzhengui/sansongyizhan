package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

/**
 * （对接外部支付工程）
 * 内部接口请求参数封装类
 */
@Data
public class ApiInBizResponse<T> {

    private String flowNo;//响应流水

    private String code;//响应码

    private String message;//响应描述

    private String msg;//调拨用

    private String time;//响应时间

    private String sign;//签名  flowNo+time

    private T bizContent;//响应实体

}
