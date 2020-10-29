package cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo;

import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import java.io.Serializable;

/**
 * 签约成功的订单推送给wsn系统
 */
public class WsnOrderInfoVo extends OrderInfo implements Serializable {

  /**
   * 订单来源 暂时只有 tmallgift cms 2020-05-27
   */
  private String orderFrom;

  /**
   * 版本号
   */
  private String orderVersion;

  /**
   * cbss订单号
   */
  private String provOrderId;

  public String getOrderFrom() {
    return orderFrom;
  }

  public void setOrderFrom(String orderFrom) {
    this.orderFrom = orderFrom;
  }

  public String getOrderVersion() {
    return orderVersion;
  }

  public void setOrderVersion(String orderVersion) {
    this.orderVersion = orderVersion;
  }

  public String getProvOrderId() {
    return provOrderId;
  }

  public void setProvOrderId(String provOrderId) {
    this.provOrderId = provOrderId;
  }
}
