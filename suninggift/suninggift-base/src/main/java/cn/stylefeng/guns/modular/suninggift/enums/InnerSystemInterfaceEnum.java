package cn.stylefeng.guns.modular.suninggift.enums;

/**
 * 环境配置枚举
 */
public enum InnerSystemInterfaceEnum {

    ADD_FTP("add.ftp","新增ftp数据");

    private String code;
    private String dec;

    private InnerSystemInterfaceEnum(String code, String dec){
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

    public static InnerSystemInterfaceEnum getByCode(String code){
        InnerSystemInterfaceEnum[] processStatesEnumArray = InnerSystemInterfaceEnum.values();
        for(InnerSystemInterfaceEnum processStatesEnum : processStatesEnumArray){
            if(processStatesEnum.getCode().equals(code)){
                return processStatesEnum;
            }
        }

        return null;
    }

}
