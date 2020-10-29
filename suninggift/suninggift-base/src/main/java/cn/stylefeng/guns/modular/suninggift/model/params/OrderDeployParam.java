package cn.stylefeng.guns.modular.suninggift.model.params;

import lombok.Data;

/**
 * @ClassNameOrderDeployParam
 * @Description TODO调拨订单更新入参
 * @Author tangxiong
 * @Date 2020/3/9 11:00
 **/
@Data
public class OrderDeployParam {

    private String outOrderNo;

    private Integer month;

    private String unfrzType;

    private String oprationId;
}
