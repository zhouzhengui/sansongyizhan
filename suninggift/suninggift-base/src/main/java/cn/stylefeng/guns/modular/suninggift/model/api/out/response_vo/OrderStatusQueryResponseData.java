package cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo;

import lombok.Data;

@Data
public class OrderStatusQueryResponseData {

    /**
     * 订购状态 31008：订购中、10000:订购成功、20000:订购失败
     */
    private String orderStatus;

}
