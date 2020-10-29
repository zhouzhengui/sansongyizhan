package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

import java.util.Date;

@Data
public class QueryAgencyOrStoreResponseBizContentCdisStoreGaths {

	private String gathNo;

	private String storeNo;

	private String staskNo;

	private Integer authType;

	private String accountName;

	private String accountNo;

	private String accountId;

	private String provinceCode;

	private String cityCode;

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

	private Integer tradeStatus = 0;

	private Integer auditStatus;

	private Integer gathStatus;

	private Date instDate;

	private Date updtDate;

	private String approver;

	private String extString;

	private Integer merchantId;



}
