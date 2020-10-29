package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;

/**
 * （对接外部支付工程）
 * 内部接口请求参数封装类
 */
@Data
public class ApiInBizRequest<T> {

    private String flowNo;//请求流水

    private String method;//请求方法

    private String time;//请求时间 yyyyMMddHHmmss

    private String sign;//签名  flowNo + method + time

    private T bizContent;//业务参数实体

    public ApiInBizRequest(String flowNo, String method, String time,
                             String sign, T bizContent){
        this.flowNo = flowNo;
        this.method = method;
        this.time = time;
        this.sign = sign;
        this.bizContent = bizContent;
    }

    public ApiInBizRequest(){}

}
