package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2019-08-03 14:08
 */
@Data
public class QueryAgencygathResponseBizContent {

    private String gathNo;

    private String agencyNo;

    private String ataskNo;

    private Integer authType;

    private String accountNo;

    private String accountName;//新增字段，供应商名称

    private String accountId;

    private String provinceCode;

    private String cityCode;

    private String province;

    private String city;

    private String bankName;

    private String bankShortName;

    private String bankCnaps;

    private String cardBranchName;

    private String usageType;

    private String cardType;

    private Integer gathType;

    private String imgAccount;

    private Integer imgAccountStatus;

    private String imgConfirm;

    private Integer imgConfirmStatus;

    private Integer modelStatus;

    private Integer tradeStatus;

    private Integer auditStatus;

    private Integer gathStatus;

    private Date instDate;

    private Date updtDate;

    private String approver;

    private String extString;

    private String outString;

    private Integer modelType;
}
