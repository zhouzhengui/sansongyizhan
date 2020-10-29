package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

/** 流量包升级订单同步接口
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-05-05 14:11
 */
@Data
public class CommOrderActivityChgFlowAo {

    /**
     * 签约电话号码
     */
    private String userPhone;

    /**
     * cbss产品合约编码(对应本次流量包订购新的流量包产品编码)
     */
    private String contractId;

    /**
     * 三户查询返回的custId
     */
    private String custId;

    /**
     * 我们的请求订单号
     */
    private String outOrderNo;

    /**
     * 订单支付时间
     */
    private String payDate;

    /**
     * cbss产品编码(流量包订购的原卡号码对应的主产品编码)
     */
    private String productNo;

    /**
     * 产品金额(冻结金额)
     */
    private String productPrice;

    /**
     * 实际支付金额
     */
    private String realPayMoney;
}
