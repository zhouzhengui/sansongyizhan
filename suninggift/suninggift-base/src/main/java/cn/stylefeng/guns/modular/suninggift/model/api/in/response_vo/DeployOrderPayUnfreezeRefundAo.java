package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class DeployOrderPayUnfreezeRefundAo implements Serializable {

  /**
   *订单号
   */
  private String outOrderNo;

  /**
   *履约类型
   */
  private String violateHandleType;

  /**
   *金额
   */
  private BigDecimal violateReceivables;



}
