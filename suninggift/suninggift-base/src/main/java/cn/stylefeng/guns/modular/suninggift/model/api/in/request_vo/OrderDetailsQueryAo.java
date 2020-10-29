package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

import java.io.Serializable;

/**  
 * ClassName:OrderDetailsQueryAo <br/>  
 * Function: 订单详情查询实体类 <br/>  
 * Date:     2020年2月23日 12:13:27 <br/>  
 * @version    
 * @since    JDK 1.8  
 * @see        
 */
@Data
public class OrderDetailsQueryAo implements Serializable{

	private static final long serialVersionUID = -4621429184805637736L;

	/**
	 * 订单编码(同步给调拨系统对应的订单编码、编码需要保证唯一)、对应天猫系统的auth_order_no编码
	 */
	private String outOrderNo;

	/**
	 * 请求冻结流水、对应天猫系统的auth_request_no编码
	 */
	private String outRequestNo;

	/**
	 * 支付宝资金操作流水号
	 */
	private String operationId;

	/**
	 * 预授权冻结开始时间
	 */
	private String freezeDate;

//	/**
//	 * 订单编码
//	 */
//	private String outOrderNo;

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
	 * 外部系统订单号（运营商）
	 */
	private String contractOrderId;

	/**
	 * 业务号码
	 */
	private String userPhone;

	/**
	 * 2020-03-27 14:09:30	发送时间
	 */
	private String requestTime;

	/**
	 * 支付宝冻结授信编码
	 */
	private String authNo;

	/**
	 * 冻结期数
	 */
	private String month;

	/**
	 * 推广商账号名
	 */
	private String payeeLogonId;

	/**
	 * 推广商账号pid
	 */
	private String payeeUserId;

	/**
	 * 支付宝userId
	 */
	private  String payerUserId;
	/**
	 * 政策编码
	 */
	private  String policyNo;

	/**
	 * 业务商城编码
	 */
	private String merchantId;

	/**
	 * 业务商户编码
	 */
	private String carrierNo;
}
