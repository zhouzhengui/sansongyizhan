package cn.stylefeng.guns.modular.suninggift.enums;

/**
 * 环境配置枚举
 */
public enum IspEnum {

//    cmcc移动 unicom联通 ct电信
    //0-代付款 1-支付成功 2-合约办理成功 3-合约办理失败
    cmcc("中国移动","lpgdcmcc"),
    unicom("中国联通","lpunicom"),
    telecom("中国电信","lptelecom");

    private String code;
    private String operatorEnumCode;

    private IspEnum(String code, String operatorEnumCode){
        this.code = code;
        this.operatorEnumCode = operatorEnumCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOperatorEnumCode() {
        return operatorEnumCode;
    }

    public void setOperatorEnumCode(String msg) {
        this.operatorEnumCode = msg;
    }

    public static IspEnum getByCode(String code){
        IspEnum[] processStatesEnumArray = IspEnum.values();
        for(IspEnum processStatesEnum : processStatesEnumArray){
            if(processStatesEnum.getCode().equals(code)){
                return processStatesEnum;
            }
        }

        return null;
    }

}
