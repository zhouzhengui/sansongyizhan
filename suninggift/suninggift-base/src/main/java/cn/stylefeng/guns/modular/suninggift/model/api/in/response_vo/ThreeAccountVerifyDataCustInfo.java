package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-02-25 16:46
 */
@Data
public class ThreeAccountVerifyDataCustInfo {

    private String certAddr;//证件地址

    private String certCode;//证件编码

    private String certType;//证件有效时间

    private String certEndDate;//证件类型编码

    private String certTypeCode;//证件类型编码

    /**
     * 客户id
     */
    private String custId;

    /**
     * 客户姓名
     */
    private String custName;

}
