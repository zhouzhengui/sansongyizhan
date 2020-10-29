package cn.stylefeng.guns.modular.suninggift.enums;

/**
 * 环境配置枚举
 */
public enum BusinessTypeEnum {

    clsd("clsd","存量升档"),
    llb("llb","流量包");

    private String code;
    private String dec;

    private BusinessTypeEnum(String code, String dec){
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

    public static BusinessTypeEnum getByCode(String code){
        BusinessTypeEnum[] processStatesEnumArray = BusinessTypeEnum.values();
        for(BusinessTypeEnum processStatesEnum : processStatesEnumArray){
            if(processStatesEnum.getCode().equals(code)){
                return processStatesEnum;
            }
        }

        return null;
    }

}
