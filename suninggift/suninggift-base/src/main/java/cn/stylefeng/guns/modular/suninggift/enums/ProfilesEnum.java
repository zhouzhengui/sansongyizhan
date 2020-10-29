package cn.stylefeng.guns.modular.suninggift.enums;

/**
 * 环境配置枚举
 */
public enum ProfilesEnum {

    //0-代付款 1-支付成功 2-合约办理成功 3-合约办理失败
    local("local","本地"),
    dev("dev","测试环境"),
    pressure("pressure","压测"),
    release("release","预生产"),
    prod("prod","生产");

    private String code;
    private String dec;

    private ProfilesEnum(String code, String dec){
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

    public static ProfilesEnum byCode(String code){
        ProfilesEnum[] processStatesEnumArray = ProfilesEnum.values();
        for(ProfilesEnum processStatesEnum : processStatesEnumArray){
            if(processStatesEnum.getCode().equals(code)){
                return processStatesEnum;
            }
        }

        return null;
    }

}
