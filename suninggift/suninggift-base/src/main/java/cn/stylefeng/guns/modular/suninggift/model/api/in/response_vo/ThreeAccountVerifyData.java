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
public class ThreeAccountVerifyData {

    /**
     * 证件信息
     */
    private ThreeAccountVerifyDataCustInfo custInfo;

    /**
     * 套餐信息
     */
    private ThreeAccountVerifyDataUserInfo userInfo;

    /**
     * 用户所在省编码
     */
    private String provinceCode;

}
