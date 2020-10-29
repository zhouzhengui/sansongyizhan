package cn.stylefeng.guns.modular.suninggift.model.constant;

import lombok.Data;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-03-06 14:35
 */
@Data
public class DeployInterfInfo {

    /**
     * 政策的域名
     */
    private String url;

    /**
     * 签名key
     */
    private String innerAuthSecretKey;

    /**
     * 签名方式
     */
    private String signType;

    /**
     * 接口版本
     */
    private String version;

    /**
     * 权益派发基础URL
     */
    private String equityBaseUrl;

}
