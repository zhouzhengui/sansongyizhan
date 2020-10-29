package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PolicyListResponseBizContentProductPolicy {

	private String accountNo;

	private Date activityEnd;

	private String activityNo;//活动编码

	private Date activityStart;//活动编码

	private String approve;

	private String carrierNo;//机构编码

	private String carrierOrgNo;//机构编码

	private Integer freezeWay;

	private Date instDate;

	private String merchantId;//商城ID

	private Integer orderType;

	private String outProtNo;//外部产品编号

	private String outString;

	private String policyLevel;

	private String policyName;

	private String policyNo;//产品政策编号

	private String policyStatus;//内部产品编码

	private String policyType;//内部产品编码

	private String productCode;//内部产品编码

	private String productId;//内部产品编码

	private String productName;//内部产品编码

	private Integer publishStatus;

	private Date updtDate;

}
