package cn.stylefeng.guns.modular.suninggift.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 推广商信息表，收集跟支付宝、微信等平台交互的账户信息
 * </p>
 *
 * @author cms
 * @since 2020-02-21
 */
@Data
public class PromotionAccountInfoResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    private String accountName;

    private String accountPid;

    private String accountNickName;

    private String appId;

    private String privateKey;

    private String publicKey;

    private String platformPublicKey;

    private String charset;

    private String signType;

    private Date createTime;

    private Date updateTime;

    private String approveName;

    private String remark;

    private String field1;

    private String field2;

    private String field3;

    /**
     * 1可以0不可以
     */
    private Integer status;

    private String sysServicePid;

    private String platformType;

    private String format;

    private String royaltPids;

    private String defaultRoyaltPid;

}
