package cn.stylefeng.guns.modular.suninggift.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author cms
 * @since 2020-04-22
 */
@TableName("order_info")
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 天猫订单号
     */
    @TableId(value = "out_trade_no", type = IdType.ID_WORKER)
    private String outTradeNo;

    /**
     * 淘宝id
     */
    @TableField("taobao_id")
    private String taobaoId;

    /**
     * 天猫订单状态 0-代付款 1-支付成功 2-合约办理成功 3-合约办理失败
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 订单名称
     */
    @TableField("name")
    private String name;

    /**
     * 运营商名称
     */
    @TableField("operator")
    private String operator;

    /**
     * 手机
     */
    @TableField("phone")
    private String phone;

    /**
     * 充值金额，单位元(冻结金额)
     */
    @TableField("total_fee")
    private BigDecimal totalFee;

    /**
     * 请求流水号
     */
    @TableField("transfer_id")
    private String transferId;

    /**
     * 支付宝UID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 商品id
     */
    @TableField("item_id")
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
    @TableField("process_states")
    private String processStates;

    /**
     * 运营商订单id
     */
    @TableField("operator_order_id")
    private String operatorOrderId;

    /**
     * 调拨订单id
     */
    @TableField("debit_order_id")
    private String debitOrderId;

    /**
     * 合约id，由话费宝平台分配
     */
    @TableField("contract_id")
    private String contractId;

    /**
     * 授权码
     */
    @TableField("auth_no")
    private String authNo;

    /**
     * 收款账号
     */
    @TableField("payee_logon_id")
    private String payeeLogonId;

    /**
     * 收款账号pid
     */
    @TableField("payee_user_id")
    private String payeeUserId;

    /**
     * 冻结期数
     */
    @TableField("freeze_month")
    private Integer freezeMonth;

    /**
     * 首付金额
     */
    @TableField("order_pay_amount")
    private BigDecimal orderPayAmount;

    /**
     * 阿里侧虚拟产品Id
     */
    @TableField("product_id")
    private String productId;

    /**
     * 政策外部编号
     */
    @TableField("out_port_no")
    private String outPortNo;

    /**
     * 子政策编码
     */
    @TableField("spec_psn_id")
    private String specPsnId;

    /**
     * 拓展字段1
     * json policyNo政策编码
     */
    @TableField("field1")
    private String field1;

    /**
     * 拓展字段2
     */
    @TableField("field2")
    private String field2;

    /**
     * 拓展字段3
     */
    @TableField("field3")
    private String field3;

    /**
     * cb订单号
     */
    @TableField("order_line_id")
    private String orderLineId;

    /**
     * 手机所属省份编码
     */
    @TableField("province_code")
    private String provinceCode;

    /**
     * 手机所属地市编码
     */
    @TableField("city_code")
    private String cityCode;

    /**
     * 订单所属appid
     */
    @TableField("app_id")
    private String appId;

    /**
     * 运营商编码
     */
    @TableField("operator_code")
    private String operatorCode;

    /**
     * 业务类型编码
     */
    @TableField("business_type")
    private String businessType;

    /**
     * 用户姓名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 用户姓名
     */
    @TableField("auth_order_no")
    private String authOrderNo;
    /**
     * 用户姓名
     */
    @TableField("operation_id")
    private String operationId;
    /**
     * 用户姓名
     */
    @TableField("auth_request_no")
    private String authRequestNo;
    /**
     * 用户姓名
     */
    @TableField("is_pay_success")
    private String isPaySuccess;
    /**
     * 用户姓名
     */
    @TableField("out_agency_no")
    private String outAgencyNo;

    /**
     * 用户表主键
     */
    @TableField("cust_no")
    private String custNo;

     /**
     * 用户表主键
     */
    @TableField("contract_time")
    private Date contractTime;

    /**
     *是否推送WSN系统：0-不推送、1-推送
     */
    @TableField("go_wsn")
    private String goWsn;

    //发券状态
    @TableField("send_status")
    private Integer sendStatus;

    //省份
    @TableField("province")
    private String province;

    //地市
    @TableField("city")
    private String city;

    /**
     * 订单来源
     */
    @TableField("order_from")
    private String orderFrom;

    /**
     * 订单完成时间
     */
    @TableField("finish_date")
    private Date finishDate;

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTaobaoId() {
        return taobaoId;
    }

    public void setTaobaoId(String taobaoId) {
        this.taobaoId = taobaoId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getProcessStates() {
        return processStates;
    }

    public void setProcessStates(String processStates) {
        this.processStates = processStates;
    }

    public String getOperatorOrderId() {
        return operatorOrderId;
    }

    public void setOperatorOrderId(String operatorOrderId) {
        this.operatorOrderId = operatorOrderId;
    }

    public String getDebitOrderId() {
        return debitOrderId;
    }

    public void setDebitOrderId(String debitOrderId) {
        this.debitOrderId = debitOrderId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getAuthNo() {
        return authNo;
    }

    public void setAuthNo(String authNo) {
        this.authNo = authNo;
    }

    public String getPayeeLogonId() {
        return payeeLogonId;
    }

    public void setPayeeLogonId(String payeeLogonId) {
        this.payeeLogonId = payeeLogonId;
    }

    public String getPayeeUserId() {
        return payeeUserId;
    }

    public void setPayeeUserId(String payeeUserId) {
        this.payeeUserId = payeeUserId;
    }

    public Integer getFreezeMonth() {
        return freezeMonth;
    }

    public void setFreezeMonth(Integer freezeMonth) {
        this.freezeMonth = freezeMonth;
    }

    public BigDecimal getOrderPayAmount() {
        return orderPayAmount;
    }

    public void setOrderPayAmount(BigDecimal orderPayAmount) {
        this.orderPayAmount = orderPayAmount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOutPortNo() {
        return outPortNo;
    }

    public void setOutPortNo(String outPortNo) {
        this.outPortNo = outPortNo;
    }

    public String getSpecPsnId() {
        return specPsnId;
    }

    public void setSpecPsnId(String specPsnId) {
        this.specPsnId = specPsnId;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(String orderLineId) {
        this.orderLineId = orderLineId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }


    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthOrderNo() {
        return authOrderNo;
    }

    public void setAuthOrderNo(String authOrderNo) {
        this.authOrderNo = authOrderNo;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getAuthRequestNo() {
        return authRequestNo;
    }

    public void setAuthRequestNo(String authRequestNo) {
        this.authRequestNo = authRequestNo;
    }

    public String getIsPaySuccess() {
        return isPaySuccess;
    }

    public void setIsPaySuccess(String isPaySuccess) {
        this.isPaySuccess = isPaySuccess;
    }

    public String getOutAgencyNo() {
        return outAgencyNo;
    }

    public void setOutAgencyNo(String outAgencyNo) {
        this.outAgencyNo = outAgencyNo;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
        "outTradeNo=" + outTradeNo +
        ", taobaoId=" + taobaoId +
        ", status=" + status +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", name=" + name +
        ", operator=" + operator +
        ", phone=" + phone +
        ", totalFee=" + totalFee +
        ", transferId=" + transferId +
        ", userId=" + userId +
        ", itemId=" + itemId +
        ", processStates=" + processStates +
        ", operatorOrderId=" + operatorOrderId +
        ", debitOrderId=" + debitOrderId +
        ", contractId=" + contractId +
        ", authNo=" + authNo +
        ", payeeLogonId=" + payeeLogonId +
        ", payeeUserId=" + payeeUserId +
        ", freezeMonth=" + freezeMonth +
        ", orderPayAmount=" + orderPayAmount +
        ", productId=" + productId +
        ", outPortNo=" + outPortNo +
        ", specPsnId=" + specPsnId +
        ", field1=" + field1 +
        ", field2=" + field2 +
        ", field3=" + field3 +
        ", orderLineId=" + orderLineId +
        ", provinceCode=" + provinceCode +
        ", cityCode=" + cityCode +
        ", appId=" + appId +
        "}";
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public Date getContractTime() {
        return contractTime;
    }

    public void setContractTime(Date contractTime) {
        this.contractTime = contractTime;
    }

    public String getGoWsn() {
        return goWsn;
    }

    public void setGoWsn(String goWsn) {
        this.goWsn = goWsn;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
}
