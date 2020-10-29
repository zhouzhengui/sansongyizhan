package cn.stylefeng.guns.modular.suninggift.enums;


/**
 * 业务参数类与类地址关系枚举
 * @author CMS
 * @time 2019年1月16日下午5:14:35
 */
public enum OutApiMethodAndClassEnum {

    QIMEN_ALIBABA_ALICOM_OPENTRADE_BASEOFFER_GET("qimen.alibaba.alicom.opentrade.baseoffer.get","cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestBaseofferGetData"),
    QIMEN_ALIBABA_ALICOM_OPENTRADE_IDENTIFY_DEAL("qimen.alibaba.alicom.opentrade.identify.deal","cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestIdentifyDealData"),
    GIFT_GZLPLINK_OPENTRADE_BASEOFFER_GET("gift.gzlplink.opentrade.baseoffer.get","cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestBaseofferGetData"),
    GIFT_GZLPLINK_OPENTRADE_IDENTIFY_DEAL("gift.gzlplink.opentrade.identify.deal","cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestIdentifyDealData"),
    GIFT_GZLPLINK_OPENTRADE_CREATEORDER("gift.gzlplink.opentrade.createorder","cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestCreateOrder"),
    GIFT_GZLPLINK_THREE_ACCOUNT_VERIFY("gift.gzlplink.three.account.verify","cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestThreeAccountVerify"),
    GIFT_GZLPLINK_OPENTRADE_ORDER_STATUS_QUERY("gift.gzlplink.opentrade.order.status.query","cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestOrderStatusQuery");

    private String method;

    private String classPath;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public static String getClassPathByMethod(String method) {
        for (OutApiMethodAndClassEnum c : OutApiMethodAndClassEnum.values()) {
            if (c.getMethod().equalsIgnoreCase(method)) {
                return c.getClassPath();
            }
        }
        return null;
    }

    // 构造方法
    private OutApiMethodAndClassEnum(String method, String classPath) {
        this.method = method;
        this.classPath = classPath;
    }

}
