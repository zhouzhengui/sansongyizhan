package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-02-25 16:46
 */
@Data
public class GetPackListMiddleSystemResponseDataProductInfo {

    private String productId;

    private String productName;

    private String productFee;

    private String contractPeriod;

    private String preProductId;

    private String preProductName;

    private String freezeAmount;

    private String equityType;

    private String equityActivityCode;

    private String preAmount;

    private String otherProductId;

    private String otherActId;

}
