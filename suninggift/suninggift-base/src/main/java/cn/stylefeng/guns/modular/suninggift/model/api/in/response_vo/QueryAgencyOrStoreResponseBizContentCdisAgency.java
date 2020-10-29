package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

import java.util.Date;

@Data
public class QueryAgencyOrStoreResponseBizContentCdisAgency {

	/**
	 * 地址
	 */
	private String address;//地址

	/**
	 * 商家名
	 */
	private String agencyName;//明细类型：1-下单明细2-垫支明细3-趸交明细4-分期明细5-退款明细

	/**
	 * 商家编码
	 */
	private String agencyNo;

	/**
	 * 商家状态
	 */
	private Integer  agencyStatus;

	/**
	 * 商家类型
	 */
	private Integer agencyType;//明细等级	0-下单明细，政策明细开始

	/**
	 * 审批者
	 */
	private String approver;

	/**
	 * 审批状态
	 */
	private Integer auditStatus;

	private Date blaEndTime;

	private Date blaStartTime;

	/**
	 * 营业执照
	 */
	private String businessLicense;

	/**
	 * 证件类型
	 */
	private String businessType;

	/**
	 * 渠道编码
	 */
	private String channel;

	/**
	 * 地市名
	 */
	private String city;

	/**
	 * 地市编码
	 */
	private String cityCode;

	private Date claEndTime;

	private Date claStartTime;

	/**
	 * 联系人邮箱
	 */
	private String contactEmail;

	/**
	 * 联系人手机
	 */
	private String contactMobile;

	/**
	 * 联系人姓名
	 */
	private String contactName;

	/**
	 * 联系人电话
	 */
	private String contactPhone;

	/**
	 * 联系人类型
	 */
	private String contactType;

	/**
	 * 区名
	 */
	private String district;

	/**
	 * 区编码
	 */
	private String districtCode;

	/**
	 *
	 */
	private Integer employeeNo;

	/**
	 *
	 */
	private String extString;

	/**
	 *
	 */
	private String imgBusiness;

	/**
	 *
	 */
	private Integer imgBusinessStatus;

	/**
	 *
	 */
	private String imgCertBack;

	/**
	 *
	 */
	private Integer imgCertBackStatus;

	/**
	 *
	 */
	private String imgCertFront;

	/**
	 *
	 */
	private Integer imgCertFrontStatus;

	/**
	 *
	 */
	private Date instDate;

	/**
	 *
	 */
	private String legalCertNo;

	/**
	 *
	 */
	private String legalName;

	/**
	 *
	 */
	private String loginName;

	/**
	 *
	 */
	private String loginPwd;

	/**
	 *
	 */
	private Integer merchantId;

	/**
	 *
	 */
	private Integer modelStatus;

	/**
	 *
	 */
	private Integer modelType;

	/**
	 *
	 */
	private String outAgencyId;

	/**
	 *
	 */
	private String outAgencyNo;

	/**
	 *
	 */
	private String outString;

	/**
	 *
	 */
	private String province;

	/**
	 *
	 */
	private String provinceCode;

	/**
	 *
	 */
	private String registerFund;

	/**
	 *
	 */
	private Integer riskStatus;

	/**
	 *
	 */
	private Date updtDate;

}
