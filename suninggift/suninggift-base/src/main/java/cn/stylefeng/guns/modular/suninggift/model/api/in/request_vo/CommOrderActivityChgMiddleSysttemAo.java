package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class CommOrderActivityChgMiddleSysttemAo implements Serializable{

	private static final long serialVersionUID = -5656197698660467460L;

	/**
	 * 乐芃订单号/下单流水号
	 */
	private String outOrderNo;

	/**
	 * 号码归属运营商
	 */
	private String operator;

	/**
	 * 接入商户编码（POT系统）
	 */
	private String clientId;

	/**
	 * 业务类型 默认存量升档
	 * 新用户：NEWUSER
	 * 存量升档：STOCKUSER
	 */
	private String orderType = "STOCKUSER";

	/**
	 * 业务号码
	 */
	private String userPhone;

	/**
	 * 套餐查询流水号（联通必传）
	 */
	private String custId;

	/**
	 * 套餐唯一码（读取政策）
	 * 联通:主套餐编码；移动:外部套餐编码
	 */
	private String productNo;

	/**
	 * 套餐名称（读取政策）
	 */
	private String productName;

	/**
	 * 套餐月费，单位:分（读取政策）
	 */
	private String productFee;

	/**
	 * 合约期（读取政策）
	 */
	private String contractPeriod;

	/**
	 * 合约活动编码（读取政策）
	 */
	private String contractId;

	/**
	 * 活动名称（读取政策）
	 */
	private String preProductName;

	/**
	 * 交易（冻结）金额，单位:分（读取政策）
	 */
	private String productPrice;

	/**
	 * 权益类型（读取政策）
	 * suning:苏宁卡/COUPON:支付宝通用红包/TmallDirCharge:天猫通用购物券
	 */
	private String equityType;

	/**
	 * 权益编码（读取政策）
	 */
	private String equityActivityCode;

	/**
	 * 权益（直降）金额，单位:分（读取政策）
	 */
	private String preAmount;

	/**
	 * 发送时间 2020-03-27 14:09:30
	 */
	private String payDate;

}
