package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

import java.util.List;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-02-25 16:46
 */
@Data
public class GetPackListMiddleSystemResponseData {

    /**
     * 业务号码
     */
    private String userPhone;

    /**
     * 号码归属运营商：
     * 广东移动：lpgdcmcc
     * 联通：lpunicom
     * 电信：lptelecom
     */
    private String operator;

    /**
     * POT政策外部编码（读取政策）
     */
    private String outProtNo;

    /**
     * 套餐内容组，可有多组
     */
    private List<GetPackListMiddleSystemResponseDataProductInfo> productList;

    /**
     * 响应时间
     */
    private String responseTime;

}
