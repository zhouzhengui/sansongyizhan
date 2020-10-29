package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

import java.util.Date;

/**
 * 账户响应类
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2019-08-03 14:08
 */
@Data
public class QueryCarrierInfoResponseBizContent {

    private String carrierNo;

    //private Integer rankNo;

    private String carrierName;

    private String outSysNo;

    private String carrierAdminName;

    private String carrierAdminPhone;

    private Integer carrierType;

    private Integer carrierStatus;

    private String carrierIsvNo;

    private String carrierOrgNo;

    private String carrierOrg;

    private String carrierDept;

    private String carrierAccount;

    private String carrierUserid;

    private Integer carrierValid;

    private String gatewayUrl;

    private Integer notifyType;

    private String notifyUrl;

    private String signType;

    private String format;

    private String charset;

    private String publicKey;

    private Integer publicValid;

    private String privateKey;

    private Integer privateValid;

    private String whiteLps;

    private Date instDate;

    private Date updtDate;

    private String approveName;

    private String extString;

    private String outString;

    private Integer authStatus;

    private String carrierAuthAppid;

    private String carrierAppAuthToken;

    private String isvAppId;

    private Date authStart;

    private Date authEnd;

    private String gzlpPublicKey;

    private Integer gzlpPublicValid;

    private String gzlpPrivateKey;

    private Integer gzlpPrivateValid;

    private Integer isGroup;

    private String systemId;

    private String encrypt;

    private String md5Key;

    private String aesKey;

    private String outCarrierNo;

    private String outPublicKey;

}
