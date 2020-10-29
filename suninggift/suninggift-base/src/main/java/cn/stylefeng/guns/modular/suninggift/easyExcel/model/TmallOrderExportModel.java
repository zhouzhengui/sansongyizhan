package cn.stylefeng.guns.modular.suninggift.easyExcel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassNameTmallOrderExportModel
 * @Description TODO
 * @Author tangxiong
 * @Date 2020/5/8 11:30
 **/
@Data
public class TmallOrderExportModel extends BaseRowModel {
    /**
     * 天猫订单号
     */
    @ExcelProperty(value = "订单号", index = 0)
    private String outTradeNo;

    /**
     * 淘宝id
     */
    @ExcelProperty(value = "淘宝id", index = 1)
    private String taobaoId;

    /**
     * 天猫订单状态 0-代付款 1-支付成功 2-合约办理成功 3-合约办理失败
     */
    @ExcelProperty(value = "订单状态", index = 2)
    private String status;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 3 ,format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间", index = 4 ,format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 订单名称
     */
    @ExcelProperty(value = "订单名称", index = 5)
    private String name;

    /**
     * 运营商名称
     */
    @ExcelProperty(value = "运营商名称", index = 6)
    private String operator;

    /**
     * 手机
     */
    @ExcelProperty(value = "业务手机号", index = 7)
    private String phone;

    /**
     * 充值金额，单位元(冻结金额)
     */
    @ExcelProperty(value = "充值金额", index = 8)
    private BigDecimal totalFee;

    /**
     * 请求流水号
     */
    @ExcelProperty(value = "请求流水号", index = 9)
    private String transferId;

    /**
     * 支付宝UID
     */
    @ExcelProperty(value = "支付宝uid", index = 10)
    private String userId;

    /**
     * 商品id
     */
    @ExcelProperty(value = "商品id", index = 11)
    private String itemId;

    /**
     * 流程状态
     * INIT("init","初始化"),
     *     THREE_CERT_CHECKING("three_cert_checking","运营商三户校验中"),
     *     DISSATISFY_BUSINESS_HANDLING("dissatisfy_business_handling","不满足业务办理"),
     *     SATISFY_BUSINESS_HANDLING("satisfy_business_handling","满足业务办理"),
     *     BUSINESS_HANDLING("business_handling","业务办理中"),
     *     BUSINESS_HANDLING_FAILED("business_handling_failed","业务办理失败"),
     *     BUSINESS_HANDLING_SUCCESS("business_handling_success","业务办理成功"),
     *     DEBIT_CREATE_SUCCESS("debit_create_success","调拨创建成功"),
     *     DEBIT_CREATE_FAILED("debit_create_failed","调拨创建失败"),
     *     DEBIT_SUBSCRIBE_SUCCESS("debit_subscribe_success","调拨订阅成功"),
     *     DEBIT_SUBSCRIBE_FAILED("debit_subscribe_failed","调拨订阅失败"),
     *     DEBIT_LOAN_SUCCESS("debit_loan_success","调拨放款成功"),
     *     DEBIT_LOAN_FAILED("debit_loan_failed","调拨放款失败"),
     *     DEBIT_TRADE_CREATE_SUCCESS("debit_trade_create_success","调拨交易创建成功"),
     *     DEBIT_TRADE_CREATE_FAILED("debit_trade_create_failed","调拨交易创建失败"),
     *     DEBIT_TRADE_CANCEL_SUCCESS("debit_trade_cancel_success","调拨交易取消成功"),
     *     DEBIT_TRADE_CANCEL_FAILED("debit_trade_cancel_failed","调拨交易取消失败"),
     *     DEBIT_TRADE_PAY_SUCCESS("debit_trade_pay_success","调拨交易支付结果成功"),
     *     DEBIT_TRADE_PAY_FAILED("debit_trade_pay_failed","调拨交易支付结果失败"),
     *     DEBIT_SETTLE_PAYABLES_SUCCESS("debit_settle_payables_success","调拨放款成功"),
     *     SYSTEM_ERROR("system_error","系统异常");
     */
    @ExcelProperty(value = "流程状态", index = 12)
    private String processStates;

    /**
     * 运营商订单id
     */
    @ExcelProperty(value = "运营商订单id", index = 13)
    private String operatorOrderId;

    /**
     * 调拨订单id
     */
    @ExcelProperty(value = "调拨订单id", index = 14)
    private String debitOrderId;

    /**
     * 合约id，由话费宝平台分配
     */
    @ExcelProperty(value = "合约id", index = 15)
    private String contractId;

    /**
     * 授权码
     */
    @ExcelProperty(value = "授权码", index = 16)
    private String authNo;

    /**
     * 收款账号
     */
    @ExcelProperty(value = "收款账号", index = 17)
    private String payeeLogonId;

    /**
     * 收款账号pid
     */
    @ExcelProperty(value = "收款账号pid", index = 18)
    private String payeeUserId;

    /**
     * 冻结期数
     */
    @ExcelProperty(value = "冻结期数", index = 19)
    private Integer freezeMonth;

    /**
     * 首付金额
     */
    @ExcelProperty(value = "首付金额", index = 20)
    private BigDecimal orderPayAmount;

    /**
     * 阿里侧虚拟产品Id
     */
    @ExcelProperty(value = "阿里侧虚拟产品Id", index = 21)
    private String productId;

    /**
     * 政策外部编号
     */
    @ExcelProperty(value = "政策外部编号", index = 22)
    private String outPortNo;

    /**
     * 子政策编码
     */
    @ExcelProperty(value = "子政策编码", index = 23)
    private String specPsnId;

    /**
     * 拓展字段1
     * json policyNo政策编码
     */
    @ExcelProperty(value = "field1", index = 24)
    private String field1;

    /**
     * 拓展字段2
     */
    @ExcelProperty(value = "field2", index = 25)
    private String field2;

    /**
     * 拓展字段3
     */
    @ExcelProperty(value = "field3", index = 26)
    private String field3;

    /**
     * cb订单号
     */
    @ExcelProperty(value = "cb订单号", index = 27)
    private String orderLineId;

    /**
     * 手机所属省份编码
     */
    @ExcelProperty(value = "手机所属省份编码", index = 28)
    private String provinceCode;

    /**
     * 手机所属地市编码
     */
    @ExcelProperty(value = "手机所属地市编码", index = 29)
    private String cityCode;

    /**
     * 订单所属appid
     */
    @ExcelProperty(value = "订单所属appid", index = 30)
    private String appId;

    /**
     * 运营商编码
     */
    @ExcelProperty(value = "运营商编码", index = 31)
    private String operatorCode;

    /**
     * 业务类型编码
     */
    @ExcelProperty(value = "业务类型编码", index = 32)
    private String businessType;

    /**
     * 用户姓名
     */
    @ExcelProperty(value = "用户姓名", index = 33)
    private String userName;
    /**
     * 授权订单号
     */
    @ExcelProperty(value = "商户授权资金订单号", index = 34)
    private String authOrderNo;
    /**
     * 支付宝授权资金操作流水号
     */
    @ExcelProperty(value = "支付宝授权资金操作流水号", index = 35)
    private String operationId;
    /**
     * 商户授权资金操作流水号
     */
    @ExcelProperty(value = "商户授权资金操作流水号", index = 36)
        private String authRequestNo;
}
