package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2019-08-03 14:08
 */
@Data
public class QueryAgencyOrStoreRequestBizContent {

    /**
     *商家外部编码
     */
    private String outAgencyNo;

    /**
     * 查询类型：商家-agency、门店-store、商家、门店-all
     */
    private String queryType;

    /**
     * 商城id
     */
    private String merchantId;

    
}
