package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2019-08-03 14:08
 */
@Data
public class QueryProductPolicyDetailRequestBizContent {

    /**
     * 政策编码
     */
    private String policyNo;

    /**
     * 0级子政策编码
     */
    private String psnId;

    
}
