package cn.stylefeng.guns.modular.suninggift.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 客户信息实体
 */
@TableName("customer_info")
public class CustomerInfo implements Serializable {

  private static final long serialVersionUID = -7229239572045348953L;

  @TableId(value = "cust_no", type = IdType.ID_WORKER)
  private String custNo;

  @TableField("cust_name")
   private String custName;

  @TableField("cust_phone")
   private String custPhone;

  @TableField("cert_no")
   private String certNo;

  @TableField("cert_type")
   private Integer certType;

  @TableField("phone_belong")
   private String phoneBelong;

  @TableField("login_no")
   private String loginNo;

  @TableField("cust_userid")
   private String custUserid;

  @TableField("userid_type")
   private Integer useridType;

  @TableField("cust_type")
   private Integer custType;

  @TableField("fund_type")
   private Integer fundType;

  @TableField("cust_province")
   private String custProvince;

  @TableField("cust_city")
   private String custCity;

  @TableField("cust_district")
   private String custDistrict;

  @TableField("cust_address")
   private String custAddress;

  @TableField("combo")
   private String combo;

  @TableField("cust_id")
   private String custId;

  @TableField("occupation_date")
   private Date occupationDate;

  @TableField("cert_check_code")
   private String certCheckCode;

  @TableField("cert_check_msg")
   private String certCheckMsg;

  @TableField("post_cust_name")
   private String postCustName;

  @TableField("post_cust_phone")
   private String postCustPhone;

  @TableField("post_area_code")
   private String postAreaCode;

  @TableField("post_address")
   private String postAddress;

  @TableField("prokey")
   private String prokey;

  @TableField("first_month_id")
   private String firstMonthId;

  @TableField("create_time")
   private Date createTime;

  @TableField("update_time")
   private Date updateTime;

  @TableField("field1")
   private String field1;

  @TableField("field2")
   private String field2;

  @TableField("field3")
  private String field3;

  public String getCustNo() {
    return custNo;
  }

  public void setCustNo(String custNo) {
    this.custNo = custNo;
  }

  public String getCustName() {
    return custName;
  }

  public void setCustName(String custName) {
    this.custName = custName;
  }

  public String getCustPhone() {
    return custPhone;
  }

  public void setCustPhone(String custPhone) {
    this.custPhone = custPhone;
  }

  public String getCertNo() {
    return certNo;
  }

  public void setCertNo(String certNo) {
    this.certNo = certNo;
  }

  public Integer getCertType() {
    return certType;
  }

  public void setCertType(Integer certType) {
    this.certType = certType;
  }

  public String getPhoneBelong() {
    return phoneBelong;
  }

  public void setPhoneBelong(String phoneBelong) {
    this.phoneBelong = phoneBelong;
  }

  public String getLoginNo() {
    return loginNo;
  }

  public void setLoginNo(String loginNo) {
    this.loginNo = loginNo;
  }

  public String getCustUserid() {
    return custUserid;
  }

  public void setCustUserid(String custUserid) {
    this.custUserid = custUserid;
  }

  public Integer getUseridType() {
    return useridType;
  }

  public void setUseridType(Integer useridType) {
    this.useridType = useridType;
  }

  public Integer getCustType() {
    return custType;
  }

  public void setCustType(Integer custType) {
    this.custType = custType;
  }

  public Integer getFundType() {
    return fundType;
  }

  public void setFundType(Integer fundType) {
    this.fundType = fundType;
  }

  public String getCustProvince() {
    return custProvince;
  }

  public void setCustProvince(String custProvince) {
    this.custProvince = custProvince;
  }

  public String getCustCity() {
    return custCity;
  }

  public void setCustCity(String custCity) {
    this.custCity = custCity;
  }

  public String getCustDistrict() {
    return custDistrict;
  }

  public void setCustDistrict(String custDistrict) {
    this.custDistrict = custDistrict;
  }

  public String getCustAddress() {
    return custAddress;
  }

  public void setCustAddress(String custAddress) {
    this.custAddress = custAddress;
  }

  public String getCombo() {
    return combo;
  }

  public void setCombo(String combo) {
    this.combo = combo;
  }

  public String getCustId() {
    return custId;
  }

  public void setCustId(String custId) {
    this.custId = custId;
  }

  public Date getOccupationDate() {
    return occupationDate;
  }

  public void setOccupationDate(Date occupationDate) {
    this.occupationDate = occupationDate;
  }

  public String getCertCheckCode() {
    return certCheckCode;
  }

  public void setCertCheckCode(String certCheckCode) {
    this.certCheckCode = certCheckCode;
  }

  public String getCertCheckMsg() {
    return certCheckMsg;
  }

  public void setCertCheckMsg(String certCheckMsg) {
    this.certCheckMsg = certCheckMsg;
  }

  public String getPostCustName() {
    return postCustName;
  }

  public void setPostCustName(String postCustName) {
    this.postCustName = postCustName;
  }

  public String getPostCustPhone() {
    return postCustPhone;
  }

  public void setPostCustPhone(String postCustPhone) {
    this.postCustPhone = postCustPhone;
  }

  public String getPostAreaCode() {
    return postAreaCode;
  }

  public void setPostAreaCode(String postAreaCode) {
    this.postAreaCode = postAreaCode;
  }

  public String getPostAddress() {
    return postAddress;
  }

  public void setPostAddress(String postAddress) {
    this.postAddress = postAddress;
  }

  public String getProkey() {
    return prokey;
  }

  public void setProkey(String prokey) {
    this.prokey = prokey;
  }

  public String getFirstMonthId() {
    return firstMonthId;
  }

  public void setFirstMonthId(String firstMonthId) {
    this.firstMonthId = firstMonthId;
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

}
