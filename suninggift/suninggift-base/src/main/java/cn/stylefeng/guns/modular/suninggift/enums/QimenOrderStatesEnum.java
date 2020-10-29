package cn.stylefeng.guns.modular.suninggift.enums;

import org.apache.commons.compress.utils.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum QimenOrderStatesEnum {

    //0-代付款 1-支付成功 2-合约办理成功 3-合约办理失败
    WAILPAY("0","代付款"),
    TRADE_SUCCESS("1","支付成功"),
    CONTRACT_SUCCESS("2","合约办理成功"),
    CONTRACT_FAIL("3","合约办理失败");

    private String code;
    private String dec;

    private QimenOrderStatesEnum(String code, String dec){
        this.code = code;
        this.dec = dec;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String msg) {
        this.dec = msg;
    }

    public static QimenOrderStatesEnum byCode(String code){
        QimenOrderStatesEnum[] processStatesEnumArray = QimenOrderStatesEnum.values();
        for(QimenOrderStatesEnum processStatesEnum : processStatesEnumArray){
            if(processStatesEnum.getCode().equals(code)){
                return processStatesEnum;
            }
        }

        return null;
    }

    public static List toList() {
        List list = Lists.newArrayList();
        for (QimenOrderStatesEnum qimenOrderStatesEnum : QimenOrderStatesEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", qimenOrderStatesEnum.getCode());
            map.put("dec", qimenOrderStatesEnum.getDec());
            list.add(map);
        }
        return list;
    }

}
