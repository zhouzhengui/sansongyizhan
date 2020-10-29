package cn.stylefeng.guns.modular.suninggift.enums;

public enum UnicomMallOutInterfaceMethodEnum {
	
	HAOKA_OPENNEW_NUM_SELECT("haoka.opennew.num.select", "获取号码列表"),
	HAOKA_OPENNEW_NUM_STATE_CHANGE("haoka.opennew.num.state.change", "号码状态变更服务"),
	HAOKA_OPENNEW_CHECK_IDENTITY_CUST("haoka.opennew.check.identity.cust", "客户资料校验和身份证认证"),
	HAOKA_OPENNEW_CONSERVE_NEW_UNICOM_ORDER("haoka.opennew.conserve.new.unicom.order", "新入网订单信息保存入库"),
	HAOKA_OPENNEW_UNICOM_ORDER_SYNC("haoka.opennew.unicom.order.sync", "新入网订单同步联通集团商城"),
	HAOKA_OPENNEW_UNICOM_SEND_RIGHTS("haoka.opennew.unicom.send.rights", "派发权益");

	private String methodName;
	
	private String desc;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private UnicomMallOutInterfaceMethodEnum(String methodName, String desc) {
		this.methodName = methodName;
		this.desc = desc;
	}
	
	public static UnicomMallOutInterfaceMethodEnum getUnicomMallInterfaceMethod(String method){
		UnicomMallOutInterfaceMethodEnum[] cardOutInterfaceMethodArray = UnicomMallOutInterfaceMethodEnum.values();
		for(UnicomMallOutInterfaceMethodEnum cardOutInterfaceMethodEnum : cardOutInterfaceMethodArray){
			if(cardOutInterfaceMethodEnum.getMethodName().equalsIgnoreCase(method)){
				return cardOutInterfaceMethodEnum;
			}
		}
		
		return null;
	}

}
