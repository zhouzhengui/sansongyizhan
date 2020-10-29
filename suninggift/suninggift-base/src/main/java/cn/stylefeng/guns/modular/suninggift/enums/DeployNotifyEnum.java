package cn.stylefeng.guns.modular.suninggift.enums;

/**
 * 调拨通知类型
 */
public enum DeployNotifyEnum {

    LOAN_RESULT_EVENT("loanResultEvent","放款通知"),
    TRADE_CREATE_EVENT("tradeCreateEvent","交易创建通知"),
    TRADE_CANCEL_EVENT("tradeCancelEvent","交易取消通知"),
    TRADE_PAY_EVENT("tradePayEvent","交易支付结果通知"),
    SETTLE_PAYABLES_EVENT("settlePayablesEvent","结清结果通知");


    private String code;
    private String dec;

    private DeployNotifyEnum(String code, String dec){
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

    public static DeployNotifyEnum byCode(String code){
        DeployNotifyEnum[] processStatesEnumArray = DeployNotifyEnum.values();
        for(DeployNotifyEnum processStatesEnum : processStatesEnumArray){
            if(processStatesEnum.getCode().equals(code)){
                return processStatesEnum;
            }
        }
        return null;
    }

}
