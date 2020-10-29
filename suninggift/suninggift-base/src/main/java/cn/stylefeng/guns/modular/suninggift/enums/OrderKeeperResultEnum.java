package cn.stylefeng.guns.modular.suninggift.enums;

public enum OrderKeeperResultEnum {
    RESULTCODE_0000("0000","用户正常缴费单期履约解冻"),
    RESULTCODE_1111("1111","用户缴费异常单期扣罚转支付"),
    RESULTCODE_9999("9999","用户毁约剩余全额扣罚转支付"),
    DEPLOYORDERTYPE_UNFREEZE("UNFREEZE", "订单解冻"),
    DEPLOYORDERTYPE_PAY("PAY", "订单转支付"),
    DEPLOYORDERTYPE_SETTLE("SETTLE", "订单结清");

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    private OrderKeeperResultEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }
    public static OrderKeeperResultEnum byCode(String code){
        OrderKeeperResultEnum[] responseStatusEnumArray = OrderKeeperResultEnum.values();
        for(OrderKeeperResultEnum responseStatusEnum : responseStatusEnumArray){
            if(responseStatusEnum.getCode().equals(code)){
                return responseStatusEnum;
            }
        }

        return null;
    }
}
