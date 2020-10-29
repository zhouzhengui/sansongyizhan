package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

import java.io.Serializable;

/**  
 * ClassName:OrderSyncAo <br/>  
 * Function: 订单同步服务实体类 <br/>  
 * Date:     2020年2月26日 下午2:13:27 <br/>  
 * @author   zzg
 * @version    
 * @since    JDK 1.8  
 * @see        
 */
@Data
public class OrderSyncAo implements Serializable{

	private static final long serialVersionUID = 6612892561299514638L;
	
	/**
	 * 地市编码(三位)
	 */
	private String cityCode;
	
	/**
	 * 省份编码（两位）
	 */
	private String provinceCode;
	
	/**
	 * 邮递地址
	 */
	private String address;
	
	/**
	 * 邮递姓名
	 */
	private String postName;
	
	/**
	 * 邮递地市编码（6位）
	 */
	private String webCity;
	
	/**
	 * 联系电话
	 */
	private String postPhone;
	
	/**
	 *邮递省份编码（6位） 
	 */
	private String webProvince;
	
	/**
	 * 邮递区县编码（6位）
	 */
	private String webCounty;
	
	/**
	 * 合约id
	 */
	private String contractId;
	
	/**
	 * 合约名称
	 */
	private String contractName;
	
	/**
	 * 合约期(正整数)
	 */
	private String contractPeriod;
	
	/**
	 * 发展人编码
	 */
	private String devCode;
	
	/**
	 * 渠道/触点编码
	 */
	private String channel;
	
	/**
	 * 基本产品名称
	 */
	private String baseProductName;
	
	/**
	 * 商户id
	 */
	private String unicomMerchantId;
	
	/**
	 * 首月资费方式id A000011V000001 （全月）A000011V000002（半月）A000011V000003（套外）
	 */
	private String firstMonthId;
	
	/**
	 * 商品id
	 */
	private String goodsId;
	
	/**
	 * 基本产品id
	 */
	private String baseProductId;
	
	/**
	 * 商品价格(单位：厘)
	 */
	private String goodsPrice;
	
	/**
	 * 首月资费方式名称
	 */
	private String firstMonthName;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 号码
	 */
	private String contractPhone;
	
	/**
	 * 号码预占到期时间(例如：20591231235959)
	 */
	private String occupyTime;
	
	/**
	 * 靓号标记 1靓号 0普号
	 */
	private String niceTag;
	
	/**
	 * 合约期
	 */
	private String monthLimit;
	
	/**
	 * 号码预占关键字（与号码状态变更接口的号码预占关键字proKey保持传值一致）
	 */
	private String proKey;
	
	/**
	 * 校验身份证结果描述
	 */
	private String certCheckMsg;
	
	/**
	 * 号主身份证姓名
	 */
	private String certName;
	
	/**
	 * 号主身份证id
	 */
	private String certNo;
	
	/**
	 * 校验身份证结果编码
	 */
	private String certCheckCode;

}
