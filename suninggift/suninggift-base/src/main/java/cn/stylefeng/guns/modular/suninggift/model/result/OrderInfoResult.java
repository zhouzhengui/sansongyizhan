package cn.stylefeng.guns.modular.suninggift.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author cms
 * @since 2020-04-22
 */
@Data
public class OrderInfoResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 天猫订单号
     */
    private String outTradeNo;

    /**
     * 淘宝id
     */
    private String taobaoId;

    /**
     * 天猫订单状态 0-代付款 1-支付成功 2-合约办理成功 3-合约办理失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 订单名称
     */
    private String name;

    /**
     * 运营商名称
     */
    private String operator;

    /**
     * 手机
     */
    private String phone;

    /**
     * 充值金额，单位元(冻结金额)
     */
    private BigDecimal totalFee;

    /**
     * 请求流水号
     */
    private String transferId;

    /**
     * 支付宝UID
     */
    private String userId;

    /**
     * 商品id
     */
    private String itemId;

    /**
     * 流程状态
    init初始化,three_cert_checking运营商三户校验中,DISSATISFY_BUSINESS_HANDLING("dissatisfy_business_handling","不满足业务办理"),
    SATISFY_BUSINESS_HANDLING("satisfy_business_handling","满足业务办理"),
    BUSINESS_HANDLING("business_handling","业务办理中"),
    BUSINESS_HANDLING_FAILED("business_handling_failed","业务办理失败"),
    BUSINESS_HANDLING_SUCCESS("business_handling_success","业务办理成功"),
    DEBIT_CREATE_SUCCESS("debit_create_success","调拨创建成功"),
    DEBIT_CREATE_FAILED("debit_create_failed","调拨创建失败"),
    DEBIT_SUBSCRIBE_SUCCESS("debit_subscribe_success","调拨订阅成功"),
    DEBIT_SUBSCRIBE_FAILED("debit_subscribe_failed","调拨订阅失败"),
    DEBIT_LOAN_SUCCESS("debit_loan_success","调拨放款成功"),
    DEBIT_LOAN_FAILED("debit_loan_failed","调拨放款失败"),
    DEBIT_TRADE_CREATE_SUCCESS("debit_trade_create_success","调拨交易创建成功"),
    DEBIT_TRADE_CREATE_FAILED("debit_trade_create_failed","调拨交易创建失败"),
    DEBIT_TRADE_CANCEL_SUCCESS("debit_trade_cancel_success","调拨交易取消成功"),
    DEBIT_TRADE_CANCEL_FAILED("debit_trade_cancel_failed","调拨交易取消失败"),
     */
    private String processStates;

    /**
     * 运营商订单id
     */
    private String operatorOrderId;

    /**
     * 调拨订单id
     */
    private String debitOrderId;

    /**
     * 合约id，由话费宝平台分配
     */
    private String contractId;

    /**
     * 授权码
     */
    private String authNo;

    /**
     * 收款账号
     */
    private String payeeLogonId;

    /**
     * 收款账号pid
     */
    private String payeeUserId;

    /**
     * 冻结期数
     */
    private Integer freezeMonth;

    /**
     * 首付金额
     */
    private BigDecimal orderPayAmount;

    /**
     * 阿里侧虚拟产品Id
     */
    private String productId;

    /**
     * 政策外部编号
     */
    private String outPortNo;

    /**
     * 子政策编码
     */
    private String specPsnId;

    /**
     * 拓展字段1
     */
    private String field1;

    /**
     * 拓展字段2
     */
    private String field2;

    /**
     * 拓展字段3
     */
    private String field3;

    /**
     * cb订单号
     */
    private String orderLineId;

    /**
     * 手机所属省份编码
     */
    private String provinceCode;

    /**
     * 手机所属地市编码
     */
    private String cityCode;

    /**
     * 订单所属appid
     */
    private String appId;

    /**
     * 所属运营商编码
     */
    private String operatorCode;

}
