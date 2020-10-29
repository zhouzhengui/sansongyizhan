package cn.stylefeng.guns.modular.suninggift.enums;

import org.apache.commons.compress.utils.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ProcessStatesEnum {
//    init初始化 three_cert_checking运营商三户校验中 dissatisfy_business_handling不满足业务办理 satisfy_business_handling满足业务办理 business_handling业务办理中
//    business_handling_failed业务办理失败 business_handling_success业务办理成功 debit_create_success调拨创建成功 debit_subscribe_success调拨订阅成功
//    debit_loan_success调拨放款成功 debit_loan_failed调拨放款失败
    INIT("init","初始化"),
    THREE_CERT_CHECKING("three_cert_checking","运营商三户校验中"),
    DISSATISFY_BUSINESS_HANDLING("dissatisfy_business_handling","不满足业务办理"),
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
    DEBIT_TRADE_PAY_SUCCESS("debit_trade_pay_success","调拨交易支付结果成功"),
    DEBIT_TRADE_PAY_FAILED("debit_trade_pay_failed","调拨交易支付结果失败"),
    DEBIT_SETTLE_PAYABLES_SUCCESS("debit_settle_payables_success","调拨放款成功"),
    SEND_EQUITY_SUCCESS("send_equity_success","权益派发成功"),
    SEND_EQUITY_FAILED("send_equity_failed","权益派发失败"),
    DEFAULT_REFUND_ORDER("default_refund_order","违约退款退单"),
    DEFAULT_UNFREEZE_REFUND_ORDER("default_unfreeze_refund_order","违约解冻退单"),
    UNFREEZE_REFUND_ORDER("unfreeze_refund_order","正常解冻订单"),
    KEEPERESULT_COMPLETE("keeperesult_complete","履约完成"),
    DEFAULT_CLOSED("default_closed","违约关闭"),
    SYSTEM_ERROR("system_error","系统异常");


    private String code;
    private String dec;

    private ProcessStatesEnum(String code, String dec){
        this.code = code;
        this.dec = dec;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String msg) {
        this.dec = msg;
    }

    public static ProcessStatesEnum byCode(String code){
        ProcessStatesEnum[] processStatesEnumArray = ProcessStatesEnum.values();
        for(ProcessStatesEnum processStatesEnum : processStatesEnumArray){
            if(processStatesEnum.getCode().equals(code)){
                return processStatesEnum;
            }
        }

        return null;
    }

    public static List toList() {
        List list = Lists.newArrayList();
        for (ProcessStatesEnum processStatesEnum : ProcessStatesEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", processStatesEnum.getCode());
            map.put("dec", processStatesEnum.getDec());
            list.add(map);
        }
        return list;
    }

}
