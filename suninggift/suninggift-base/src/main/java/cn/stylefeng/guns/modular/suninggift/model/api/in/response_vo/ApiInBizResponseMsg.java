package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

/**
 * 调拨创建用
 */
@Data
public class ApiInBizResponseMsg{
//    code :10000-成功
    /**
     * code :10000-成功
     */
    private String code;

    private String msg;

    /**
     * subCode:10001:入库成功、50001-订单已存在
     */
    private String subCode;

    private String subMsg;

    private String result;

    private String listResult;

}
