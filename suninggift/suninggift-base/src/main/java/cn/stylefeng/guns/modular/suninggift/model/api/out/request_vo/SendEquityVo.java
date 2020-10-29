package cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo;

import java.util.List;

public class SendEquityVo {
	
	//请求订单流水号
	private String outOrderNo;
	
	//支付宝流水号
	private String outTradeNo;
	
	//订单交易状态
	private String outOrderStatus;
	
	//派券机构
	private String sendOrg;
	
	//派券方式  phone-手机号 、alipay-支付宝
	private String sendWay;
	
	//付款支付宝userid
	private String buyerUserId;
	
	//外部订单金额
	private String orderAmount;
	
	//组合类型 单个--SINGLE，组合--COMBINATION
	private String combineType;
	
	//订单标题
	private String outOrderTitle;
	
	//派发手机号
	private String sendOrgValue;
	
	//调拨创建相关参数
	private String deployCreateString;
	
	//调拨订阅相关参数
	private String deploySynchronieString;
	
	//异步通知地址
	private String notifyUrl;

	//商户编码
	private String merchantId;

	//商户名称
	private String merchantName;

	//门店编码
	private String storeCode;

	//门店名称
	private String storeName;

	//权益账户编码
	private String memberNo;
	
	//权益编码 List
	private List<EquitycodeVo> equityEntityList;

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOutOrderStatus() {
		return outOrderStatus;
	}

	public void setOutOrderStatus(String outOrderStatus) {
		this.outOrderStatus = outOrderStatus;
	}

	public String getSendOrg() {
		return sendOrg;
	}

	public void setSendOrg(String sendOrg) {
		this.sendOrg = sendOrg;
	}

	public String getSendWay() {
		return sendWay;
	}

	public void setSendWay(String sendWay) {
		this.sendWay = sendWay;
	}

	public String getBuyerUserId() {
		return buyerUserId;
	}

	public void setBuyerUserId(String buyerUserId) {
		this.buyerUserId = buyerUserId;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getCombineType() {
		return combineType;
	}

	public void setCombineType(String combineType) {
		this.combineType = combineType;
	}

	public List<EquitycodeVo> getEquityEntityList() {
		return equityEntityList;
	}

	public void setEquityEntityList(List<EquitycodeVo> equityEntityList) {
		this.equityEntityList = equityEntityList;
	}

	public String getOutOrderTitle() {
		return outOrderTitle;
	}

	public void setOutOrderTitle(String outOrderTitle) {
		this.outOrderTitle = outOrderTitle;
	}

	public String getSendOrgValue() {
		return sendOrgValue;
	}

	public void setSendOrgValue(String sendOrgValue) {
		this.sendOrgValue = sendOrgValue;
	}

	public String getDeployCreateString() {
		return deployCreateString;
	}

	public void setDeployCreateString(String deployCreateString) {
		this.deployCreateString = deployCreateString;
	}

	public String getDeploySynchronieString() {
		return deploySynchronieString;
	}

	public void setDeploySynchronieString(String deploySynchronieString) {
		this.deploySynchronieString = deploySynchronieString;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

}
