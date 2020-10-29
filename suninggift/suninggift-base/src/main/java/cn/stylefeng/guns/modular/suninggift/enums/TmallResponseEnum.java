package cn.stylefeng.guns.modular.suninggift.enums;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-04-21 9:57
 */
public enum TmallResponseEnum {

    /**
     * 这些状态都不用再次5秒轮询
     */
    SUCCESS("0000","发货成功"),
    RESULT_ICCID_PUK_ALLREADY_VERIFY("21010","ICCID 和PUK 号码已认证"),
    RESULT_ORDER_NOT_MATCH("21010","订单号不匹配"),
    RESULT_ORDER_STATUS_CLOSED ("21011","订单号已关闭"),
    RESULT_BIZTYPE_NOT_SUPPORT("21012","订单号已关闭"),
    RESULT_IMEI_CHECK_FAIL("21013","IMEI号 校验失败，请检查您输入的信息"),
    RESULT_IMEI_CHECK_FAIL_DES("21014", "无权限操作该订单"),
    RESULT_IMEI_UPDATE_FAIL("30000","交易订单更新信息异常");
    private String code;
    private String dec;

    private TmallResponseEnum(String code, String dec){
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

    public static TmallResponseEnum byCode(String code){
        TmallResponseEnum[] processStatesEnumArray = TmallResponseEnum.values();
        for(TmallResponseEnum processStatesEnum : processStatesEnumArray){
            if(processStatesEnum.getCode().equals(code)){
                return processStatesEnum;
            }
        }

        return null;
    }

}
