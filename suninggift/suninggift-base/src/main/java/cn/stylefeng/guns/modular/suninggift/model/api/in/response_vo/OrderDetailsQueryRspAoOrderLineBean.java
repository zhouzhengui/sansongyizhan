package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

import java.util.List;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-04-30 17:11
 */
@Data
public class OrderDetailsQueryRspAoOrderLineBean {
    /**
     * cancelTag : 0
     * netTypeCode : 10
     * offerInfo : [{"belongPlatform":"1002","brandName":"电信商品","count":"1","isMainOffer":"1","offerDivide":"01","offerId":"1579658589757150","offerName":"99元畅爽冰激凌红包折扣套餐","offerSupplier":"8888","offerTypeCode":"33","outRsId":"90657927","parentOffer":"0","settPrice":"0","startDate":"2020-02-07 12:51:14.0"}]
     * orderLineId : 2002070206654584
     * orderNodeCode : preSubmitDepositFee
     * orderNodeName : 存费送费预提交
     * orderNodeState : 04
     * serialNumber : 13125430174
     * tradeTypeCode : 2102
     */

    private String cancelTag;
    private String netTypeCode;
    private String orderLineId;
    private String orderNodeCode;
    private String orderNodeName;
    private String orderNodeState;
    private String serialNumber;
    private String tradeTypeCode;
    private List<OrderDetailsQueryRspAoOfferInfoBean> offerInfo;

}
