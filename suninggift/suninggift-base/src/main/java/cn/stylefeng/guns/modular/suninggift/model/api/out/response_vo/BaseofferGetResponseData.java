package cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseofferGetResponseData implements Serializable {

    /**
     * 阿里侧虚拟id
     */
    private String product_id;

    /**
     * 产品名称
     */
    private String offer_name;

    /**
     * 外部产品id
     */
    private String offer_id;

    /**
     * 合约期
     */
    private String exp_month;

    /**
     * 需发放额度(分)
     */
    private String send_award_fee;

    /**
     * 质押金额(分)
     */
    private String lock_money;

    /**
     * 套餐档位
     */
    private String offer_set;

    /**
     * 套餐产品包外优惠描述
     */
    private String offer_out_desc;

    /**
     * 套餐产品包内优惠描述
     */
    private String offer_in_desc;
}
