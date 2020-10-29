package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-04-30 17:11
 */
@Data
public class OrderDetailsQueryRspAoPayInfoBean {
    /**
     * money : 350
     * payChannel : 20
     * payId : 2002070206561979
     * payMode : 1
     * realPayMoney : 0
     */

    private String money;

    private String payChannel;

    private String payId;

    private String payMode;

    private String realPayMoney;
}
