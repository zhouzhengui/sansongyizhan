package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

/**
 * 三户校验响应体
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-02-25 16:46
 */
@Data
public class ThreeAccountVerifyMiddleSystemData {

    /**
     *业务号码
     */
    private String userPhone;

    /**
     * 号码归属运营商
     * 广东移动：lpgdcmcc
     * 联通：lpunicom
     * 电信：lptelecom
     */
    private String operator;

    /**
     * 号码状态:
     * 在网：ONLINE
     * 异常：EXCEPTION
     * 已有合约：
     */
    private String phoneStatus;

    /**
     * 套餐名称
     */
    private String productName;

    /**
     * 套餐月费，单位:分
     */
    private String productFee;

    /**
     * 当前套餐编码
     */
    private String productNo;

    /**
     * 归属省分，代码
     */
    private String provinceCode;

    /**
     * 套餐查询流水号（联通必传）
     */
    private String contractOrder;
}
