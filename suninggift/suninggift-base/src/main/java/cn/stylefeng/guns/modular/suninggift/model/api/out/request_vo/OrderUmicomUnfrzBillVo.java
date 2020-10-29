package cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 调用调拨系统进行对应订单的解冻/转支付操作请求实体
 */
public class OrderUmicomUnfrzBillVo implements Serializable {

  /**
   * 乐芃订单号
   */
  private String outOrderNo;

  //解冻：UNFREEZE、转支付：PAY
  private String unfrzType;

  //操作金额
  private BigDecimal amount;

  //期数
  private Integer month;

  public String getOutOrderNo() {
    return outOrderNo;
  }

  public void setOutOrderNo(String outOrderNo) {
    this.outOrderNo = outOrderNo;
  }

  public String getUnfrzType() {
    return unfrzType;
  }

  public void setUnfrzType(String unfrzType) {
    this.unfrzType = unfrzType;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Integer getMonth() {
    return month;
  }

  public void setMonth(Integer month) {
    this.month = month;
  }

}
