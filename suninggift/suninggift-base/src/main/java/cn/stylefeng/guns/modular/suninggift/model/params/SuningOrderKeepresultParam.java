package cn.stylefeng.guns.modular.suninggift.model.params;

import lombok.Data;

/**
 * @ClassNameSuningOrderKeepresultParam
 * @Description TODO苏宁履约通知入参
 * @Author tangxiong
 * @Date 2020/7/13 17:53
 **/
@Data
public class SuningOrderKeepresultParam {
    private String method;
    private String result_desc;
    private String bill_month;
    private String bill_money;
    private String out_trade_no;
    private String result_code;
}
