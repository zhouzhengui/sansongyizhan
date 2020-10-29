package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-02-25 16:46
 */
@Data
public class CardCenterResponseVoGetPackListData {

    public CardCenterResponseVoGetPackListData(){};

    public CardCenterResponseVoGetPackListData(String alowRange){
        this.alowRange = alowRange;
    };

    /**
     * 策略中心返回可以办理套餐的产品编码
     */
    private String alowRange;

    /**
     * 服务返回策略中心系统编码
     */
    private String objectId;

    /**
     * 服务返回产品优先级
     */
    private String priority;

    /**
     * 场景类型
     */
    private String sceneType;

    /**
     * 个性化场景编码
     */
    private String strategyId;


}
