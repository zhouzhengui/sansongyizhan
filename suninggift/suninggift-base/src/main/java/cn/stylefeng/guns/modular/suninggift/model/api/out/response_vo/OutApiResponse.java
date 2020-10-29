package cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-02-19 17:19
 */
@Data
public class OutApiResponse<T> implements Serializable {

    private Boolean isSuccess;

    private String retCode;

    private String retMessage;

    private T data;

    public OutApiResponse(){}

    public OutApiResponse(boolean isSuccess, String retCode, String retMessage){
        this.isSuccess = isSuccess;
        this.retCode = retCode;
        this.retMessage = retMessage;
    }
}
