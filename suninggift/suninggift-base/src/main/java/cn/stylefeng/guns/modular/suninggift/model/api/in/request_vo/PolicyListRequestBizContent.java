package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2019-08-03 14:08
 */
@Data
public class PolicyListRequestBizContent {

    public PolicyListRequestBizContent(String outPortNo){
        this.outPortNo = outPortNo;
    }

    private String outPortNo;//联通支付产品id

    
}
