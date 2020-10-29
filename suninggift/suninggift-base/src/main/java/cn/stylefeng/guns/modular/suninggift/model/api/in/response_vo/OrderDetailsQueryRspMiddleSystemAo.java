package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailsQueryRspMiddleSystemAo {

    /**
     *乐芃订单号/下单流水号
     */
    private String outOrderNo;

    /**
     *中台订单号
     */
    private String contractOrderId;

    /**
     *cb订单号
     */
    private String orderLineId;

    /**
     *业务号码
     */
    private String userPhone;

    /**
     *订单订购状态：
     * 订购成功:ORDER_SUCCESS
     * 订购中:ORDER_LOADING
     * 订购失败:ORDER_FAIL
     */
    private String orderStatus;

    /**
     * 响应时间
     */
    private String responseTime;


}
