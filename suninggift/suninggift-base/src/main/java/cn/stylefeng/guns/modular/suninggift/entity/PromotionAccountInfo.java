package cn.stylefeng.guns.modular.suninggift.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 推广商信息表，收集跟支付宝、微信等平台交互的账户信息
 * </p>
 *
 * @author cms
 * @since 2020-02-21
 */
@TableName("promotion_account_info")
public class PromotionAccountInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("account_name")
    private String accountName;

    @TableField("account_pid")
    private String accountPid;

    @TableField("account_nick_name")
    private String accountNickName;

    @TableField("app_id")
    private String appId;

    @TableField("private_key")
    private String privateKey;

    @TableField("public_key")
    private String publicKey;

    @TableField("platform_public_key")
    private String platformPublicKey;

    @TableField("charset")
    private String charset;

    @TableField("sign_type")
    private String signType;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField("approve_name")
    private String approveName;

    @TableField("remark")
    private String remark;

    @TableField("field1")
    private String field1;

    @TableField("field2")
    private String field2;

    @TableField("field3")
    private String field3;

    /**
     * 1可以0不可以
     */
    @TableField("status")
    private Integer status;

    @TableField("sys_service_pid")
    private String sysServicePid;

    @TableField("platform_type")
    private String platformType;

    @TableField("format")
    private String format;

    @TableField("royalt_pids")
    private String royaltPids;

    @TableField("default_royalt_pid")
    private String defaultRoyaltPid;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPid() {
        return accountPid;
    }

    public void setAccountPid(String accountPid) {
        this.accountPid = accountPid;
    }

    public String getAccountNickName() {
        return accountNickName;
    }

    public void setAccountNickName(String accountNickName) {
        this.accountNickName = accountNickName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPlatformPublicKey() {
        return platformPublicKey;
    }

    public void setPlatformPublicKey(String platformPublicKey) {
        this.platformPublicKey = platformPublicKey;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getApproveName() {
        return approveName;
    }

    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSysServicePid() {
        return sysServicePid;
    }

    public void setSysServicePid(String sysServicePid) {
        this.sysServicePid = sysServicePid;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRoyaltPids() {
        return royaltPids;
    }

    public void setRoyaltPids(String royaltPids) {
        this.royaltPids = royaltPids;
    }

    public String getDefaultRoyaltPid() {
        return defaultRoyaltPid;
    }

    public void setDefaultRoyaltPid(String defaultRoyaltPid) {
        this.defaultRoyaltPid = defaultRoyaltPid;
    }

    @Override
    public String toString() {
        return "PromotionAccountInfo{" +
        "id=" + id +
        ", accountName=" + accountName +
        ", accountPid=" + accountPid +
        ", accountNickName=" + accountNickName +
        ", appId=" + appId +
        ", privateKey=" + privateKey +
        ", publicKey=" + publicKey +
        ", platformPublicKey=" + platformPublicKey +
        ", charset=" + charset +
        ", signType=" + signType +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", approveName=" + approveName +
        ", remark=" + remark +
        ", field1=" + field1 +
        ", field2=" + field2 +
        ", field3=" + field3 +
        ", status=" + status +
        ", sysServicePid=" + sysServicePid +
        ", platformType=" + platformType +
        ", format=" + format +
        ", royaltPids=" + royaltPids +
        ", defaultRoyaltPid=" + defaultRoyaltPid +
        "}";
    }
}
