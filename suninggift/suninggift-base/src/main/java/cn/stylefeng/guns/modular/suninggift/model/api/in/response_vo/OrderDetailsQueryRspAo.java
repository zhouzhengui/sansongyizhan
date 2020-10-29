package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailsQueryRspAo {

    /**
     * commonInfo : {"cityCode":"910","departId":"13a0192","eparchyCode":"910","provinceCode":"91","staffId":"v-zhulei6-2"}
     * createDate : 2020-02-07 12:51:14.0
     * inModeCode : 2009
     * isPay : 1
     * orderId : 2002070206713222
     * orderLine : [{"cancelTag":"0","netTypeCode":"10","offerInfo":[{"belongPlatform":"1002","brandName":"电信商品","count":"1","isMainOffer":"1","offerDivide":"01","offerId":"1579658589757150","offerName":"99元畅爽冰激凌红包折扣套餐","offerSupplier":"8888","offerTypeCode":"33","outRsId":"90657927","parentOffer":"0","settPrice":"0","startDate":"2020-02-07 12:51:14.0"}],"orderLineId":"2002070206654584","orderNodeCode":"preSubmitDepositFee","orderNodeName":"存费送费预提交","orderNodeState":"04","serialNumber":"13125430174","tradeTypeCode":"2102"}]
     * orderState : 00
     * payInfo : {"money":"350","payChannel":"20","payId":"2002070206561979","payMode":"1","realPayMoney":"0"}
     */

    private OrderDetailsQueryRspAoCommonInfoBean commonInfo;

    private String createDate;

    private String inModeCode;

    private String isPay;

    private String orderId;

    private String orderState;

    private OrderDetailsQueryRspAoPayInfoBean payInfo;

    private List<OrderDetailsQueryRspAoOrderLineBean> orderLine;


}
