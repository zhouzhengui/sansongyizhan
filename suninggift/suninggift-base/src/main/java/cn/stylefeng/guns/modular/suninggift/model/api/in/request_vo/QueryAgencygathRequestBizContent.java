package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2019-08-03 14:08
 */
@Data
public class QueryAgencygathRequestBizContent {

    /**
     *商家外部编码
     */
    private String outAgencyNo;

    /**
     * 账号类型:垫资垫付-4、空白模式-5、销售营收-0
     */
    private String gathType;

    /**
     * 商城id
     */
    private String merchantId;

    
}
