package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

import lombok.Data;

import java.util.Date;

@Data
public class QueryAgencyOrStoreResponseBizContentCdisStore {

	/**
	 * store_no
	 */
	private String storeNo;

	private String agencyNo;

	private String storeName;

	private String contactName;

	private String contactMobile;

	private String contactPhone;

	private String outStoreNo;

	private String outStoreId;

	private String channel;

	private String imgOutside;

	private Integer imgOutsideStatus;

	private String imgInside;

	private Integer imgInsideStatus;

	private String province;

	private String provinceCode;

	private String city;

	private String cityCode;

	private String district;

	private String districtCode;

	private String address;

	private Integer modelStatus;

	private Integer modelType;

	private Integer auditStatus;

	private Integer storeStatus;

	private String approver;

	private Date instDate;

	private Date updtDate;

	private String extString;

	private String outString;

	private Integer riskStatus;

	private String clerkNo;

	private Integer merchantId;



}
