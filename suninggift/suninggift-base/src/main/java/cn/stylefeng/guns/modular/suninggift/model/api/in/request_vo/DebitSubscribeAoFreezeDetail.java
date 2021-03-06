package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-03-06 15:54
 */
@Data
public class DebitSubscribeAoFreezeDetail {

    private String accountType;

    private String authNo;

    private String bizNo;

    private String bizNo2;

    private BigDecimal contractAmount;

    private String outTradeNo;

    private String payeeLogonId;

    private String payeeUseridId;

    private String settleTypeEnum;



}
