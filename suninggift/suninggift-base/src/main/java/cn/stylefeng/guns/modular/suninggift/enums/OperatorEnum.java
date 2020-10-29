package cn.stylefeng.guns.modular.suninggift.enums;

/**
 * 环境配置枚举
 */
public enum OperatorEnum {

//    cmcc移动 unicom联通 ct电信
    //0-代付款 1-支付成功 2-合约办理成功 3-合约办理失败
    lpgdcmcc("lpgdcmcc","广东移动"),
    lpunicom("lpunicom","联通存量升档"),
    lpsuningunicom("lpsuningunicom","苏宁联通新用户入网"),
    lptelecom("lptelecom","电信");

    private String code;
    private String dec;

    private OperatorEnum(String code, String dec){
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

    public static OperatorEnum getByCode(String code){
        OperatorEnum[] processStatesEnumArray = OperatorEnum.values();
        for(OperatorEnum processStatesEnum : processStatesEnumArray){
            if(processStatesEnum.getCode().equals(code)){
                return processStatesEnum;
            }
        }

        return null;
    }

}
