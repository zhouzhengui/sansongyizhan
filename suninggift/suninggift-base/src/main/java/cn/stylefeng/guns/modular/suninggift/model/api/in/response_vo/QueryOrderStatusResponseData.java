package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

@Data
public class QueryOrderStatusResponseData {

    /**
     * 订购状态31008：订购中、10000:订购成功、20000:订购失败
     */
    private String orderStatus;

}
