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
public class DebitCreateAoSettleDetail {

    private String amount;

    private String endMonth;

    private String firstTriggerMonth;

    private String ruleDayPerMonth;

    private String settleAmount;

    private String settleTypeEnum;

    private String startMonth;

    private String tranIn;

    private String tranInType;



}
