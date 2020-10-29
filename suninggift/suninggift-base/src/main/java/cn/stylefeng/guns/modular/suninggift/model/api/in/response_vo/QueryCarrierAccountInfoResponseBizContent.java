package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户响应类
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2019-08-03 14:08
 */
@Data
public class QueryCarrierAccountInfoResponseBizContent {

    private String accountNo;

    private String accountName;

    private String carrierNo;

    private String accountAdminName;

    private String accountAdminPhone;

    private Integer accountType;

    private Integer accountStatus;

    private Integer receiveStatus;

    private String carrierIsvNo;

    private String payeeLogonId;

    private String payeeUserId;

    private Integer accountValid;

    private String accountAppId;

    private String appAuthToken;

    private String gatewayUrl;

    private String notifyUrl;

    private String signType;

    private String charset;

    private String publicKey;

    private Integer publicValid;

    private String privateKey;

    private Integer privateValid;

    private String outPublicKey;

    private String whiteLps;

    private Date instDate;

    private Date updtDate;

    private String approve;

    private String extString;

    private String outString;

    private Integer transactType;

    /**
     * 授权状态1启用，0不启用
     */
    private Integer authStatus;

    /**
     * gzlp_private_valid
     */
    private Integer gzlpPrivateValid;

    /**
     * gzlp_public_valid
     */
    private int gzlpPublicValid;

    /**
     * 乐芃公钥
     */
    private String gzlpPublicKey;

    /**
     * 乐芃私钥
     */
    private String gzlpPrivateKey;

    /**
     * format
     */
    private String format;

    /**
     * 授权过期时间
     */
    private Date authEndDate;

    /**
     * 账户模式:1收款，2获取用户信息
     */
    private Integer modelStatus;

    private Integer authType;

    private String md5Key;

    private String aesKey;

    private Integer gathType;

    private Integer appAuthType;

    private String systemId;

    private BigDecimal receiveRate;

    private Integer receiveType;

}
