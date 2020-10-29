package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

import java.util.Date;

@Data
public class QueryAgencyOrStoreResponseBizContentCdisStoreTasks {

	private String staskNo;

	private String storeNo;

	private String ataskNo;

	private String outStoreNo;

	private String outStoreId;

	private String channel;

	private String systemId;

	private Integer authType;

	private String contPartnerNo;

	private String contPartnerId;

	private Integer modelStatus;

	private Integer modelType;

	private Integer auditStatus;

	private Integer auditCount;

	private Integer tasksType;

	private Integer tasksStatus;

	private Integer creditScore;

	private String approver;

	private Date instDate;

	private Date updtDate;

	private Date openDate;

	private String extString;

	private String outString;

	private String partnerNo;

	private String partnerId;

	private Integer riskStatus = 0;

	private Integer tradeStatus;

	private Integer canGath;

	private String clerkNo;

	private Integer merchantId;



}
