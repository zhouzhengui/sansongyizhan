package cn.stylefeng.guns.modular.suninggift.model.constant;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-04-15 11:19
 */
public class SysConstant {

    /**
     * 查询数据redis有效时间 单位：秒
     */
    public static final Long QUERY_DATA_VALID_TIME = 60L * 60; //5分钟

    /**
     * 查询政策列表
     */
    public static final String QUERY_POLICY_LIST = "queryPolicyList";

    /**
     * 查询商家门店
     */
    public static final String QUERY_AGENCY_OR_STORE = "queryAgencyOrStore";

    /**
     * 查询商家gath
     */
    public static final String QUERY_AGENCYGATH = "queryAgencygath";

    /**
     * 查询商户账户查询
     */
    public static final String QUERY_CARRIER_ACCOUNT_INFO = "queryCarrierAccountInfo";

    /**
     * 查询商户查询
     */
    public static final String QUERY_CARRIER_INFO = "queryCarrierInfo";

    /**
     * 查询商户查询
     */
    public static final String QUERY_PRODUCT_POLICY_DETAIL = "queryProductPolicyDetail";

    /**
     * 政策外部编码集合
     */
    public static final String QUERY_POLICY_LIST_MAP = "queryPolicyListMap";

    /**
     * 政策外部编码集合
     */
    public static final String QUERY_AGENCY_OR_STORE_MAP = "queryAgencyOrStoreMap";

    /**
     * 政策外部编码集合
     */
    public static final String QUERY_AGENCYGATH_MAP = "queryAgencygathMap";

    /**
     * 政策外部编码集合
     */
    public static final String QUERY_CARRIER_ACCOUNT_INFO_MAP = "queryCarrierAccountInfoMap";

    /**
     * 政策外部编码集合
     */
    public static final String QUERY_CARRIER_INFO_MAP = "queryCarrierInfoMap";
    /**
     * 政策外部编码集合
     */
    public static final String QUERY_PRODUCT_POLICY_DETAIL_MAP = "queryProductPolicyDetailMap";

    /**
     * 阿里网关地址
     */
    public static final String ALI_URL = "https://openapi.alipay.com/gateway.do";


    /**
     * 已经办理过订单的号码
     */
    public static final String HAS_ORDER_MOBILE = "hasOrderMobile:";

//    /**
//     * 全国ProvinceCode
//     */
//    public static final String QUANGUO_CODE = "98";

    /**
     * 已经办理过订单的号码有效时间
     */
    public static final long HAS_ORDER_MOBILE_VERIFY_TIME = 60 * 60 * 24 * 7;//7天
}
