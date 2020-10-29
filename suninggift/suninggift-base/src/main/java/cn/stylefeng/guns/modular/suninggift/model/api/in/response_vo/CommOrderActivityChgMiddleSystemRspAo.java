package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

@Data
public class CommOrderActivityChgMiddleSystemRspAo {

    /**
     * 乐芃订单号/下单流水号
     */
    private String outOrderNo;

    /**
     * 外部系统订单号（运营商）
     */
    private String contractOrderId;

    /**
     * 响应时间 2020-03-27 14:09:30
     */
    private String responseTime;

}
