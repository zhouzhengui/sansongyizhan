package cn.stylefeng.guns.modular.suninggift.model.constant;

import lombok.Data;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-03-06 14:35
 */
@Data
public class CardCenterInterfInfo {


    /**
     * 政策中心的域名
     */
    private String policyUrl;

    /**
     * 政策外部编码
     */
    private String outPortNo;

    /**
     * 流量包政策外部编码
     */
    private String flowOutPortNo;

    /**
     * 联通新入网用户政策外部编码
     */
    private String newOutPortNo;

    /**
     * 商家外部编码
     */
    private String outAgencyNo;

    /**
     * 商城id
     */
    private String merchantId;

    /**
     * 商户账户编码
     */
    private String accountNo;

    /**
     * 商户编码
     */
    private String carrierNo;

    /**
     * 账号类型:垫资垫付-4、空白模式-5、销售营收-0
     */
    private String gathType;

    /**
     * 描述
     */
    private String des;


    /**
     * 号卡的请求地址
     */
    private String haokaUrl;

    /**
     * 号卡接口请求身份标识符
     */
    private String clientId;

    /**
     * 运营商编码
     */
    private String operatorCode;

    /**
     * 号卡接口请求密钥
     */
    private String clientKey;

    /**
     * 联通商城提供给乐芃的全国触点编码
     */
    private String channel;

    /**
     * 流量叠加包政策外部编码
     */
    private String flowPlusOutPortNo;
}
