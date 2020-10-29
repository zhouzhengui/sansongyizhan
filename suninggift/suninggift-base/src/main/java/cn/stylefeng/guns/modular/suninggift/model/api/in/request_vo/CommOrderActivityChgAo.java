package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

import java.io.Serializable;

/**  
 * ClassName:CommOrderActivityChgAo <br/>  
 * Function: 订单同步实体类 <br/>  
 * Date:     2020年2月21日 10:13:21 <br/>  
 * @version    
 * @since    JDK 1.8  
 * @see        
 */
@Data
public class CommOrderActivityChgAo implements Serializable{

	private static final long serialVersionUID = -5656197698660467460L;


	/**
	 * 合约号码
	 */
	private String userPhone;

	/**
	 * 合约编码
	 */
	private String contractId;

	/**
	 * 产品编码
	 */
	private String productNo;

	/**
	 * 三户校验custId
	 */
	private String custId;

	/**
	 * 金额
	 */
	private String productPrice;

	/**
	 * 支付时间 yyyy-MM-dd HH:mm:ss
	 */
	private String payDate;

	/**
	 * 订单编码
	 */
	private String outOrderNo;

	/**
	 * 实际支付金额
	 */
	private String realPayMoney;


}
