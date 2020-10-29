package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

import java.util.List;

/**
 * 调拨订阅接口请求体
 */
@Data
public class DebitSubscribeAo {


    /**
     * freezeDetails :
     * operationStatus : SUCCESS
     * outOrderNo : 20191016170929139924101662
     */

    private String freezeDetails;
//    private List<DebitSubscribeAoFreezeDetail> freezeDetails;

    private String operationStatus;
    
    private String outOrderNo;

}
