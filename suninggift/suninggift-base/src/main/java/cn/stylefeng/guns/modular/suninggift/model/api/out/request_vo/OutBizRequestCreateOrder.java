package cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo;

import lombok.Data;

/**
 * 奇门预下单请求体
 *
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2019-08-03 14:06
 */
@Data
public class OutBizRequestCreateOrder {

    /**
     * 调用方身份标识
     */
    private String app_id;

    /**
     * 调用方身份标识 等同于app_id
     */
    private String app_key;

    /**
     * 合约id，由话费宝平台分配
     */
    private String contract_id;

    /**
     * 订单名称
     */
    private String name;

    /**
     * 运营商名称
     */
    private String operator;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 订单状态:
     * 0-代付款 1-支付成功 2-合约办理成功 3-合约办理失败
     */
    private String status;

    /**
     * 淘宝id
     */
    private String taobao_id;

    /**
     * 天猫订单号
     */
    private String tmall_order_id;

    /**
     * 充值金额，单位分
     */
    private String total_fee;

    /**
     * 请求流水号
     */
    private String transfer_id;

    /**
     * 支付宝UID
     */
    private String user_id;

    /**
     * 阿里侧虚拟产品Id
     */
    private String product_id;

    /**
     * 商品id
     */
    private String item_id;

    /**
     * 预授权冻结编号
     */
    private String auth_order_no;

    /**
     * 冻结操作流水号
     */
    private String auth_request_no;

    /**
     * 预授权冻结编号
     */
    private String auth_no;

    /**
     * 支付宝的授权资金操作流水号
     */
    private String operation_id;

    /**
     * 商户授权资金订单号(拉起支付宝冻结接口时的值)
     */
    private String out_order_no;

    /**
     * 商户本次资金操作的请求流水号(拉起支付宝冻结接口时的值)
     */
    private String out_request_no;

    /**
     * 对接方订单号
     */
    private String out_trade_no;


}
