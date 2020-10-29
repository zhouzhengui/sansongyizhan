package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import java.io.Serializable;

public class NewOrderSyncNumberListInfoAo implements Serializable {

    private static final long serialVersionUID = 4773769274098484405L;

    /**
     *号码
     */
    private String number;

    /**
     *主卡标记 1 主卡 0 副卡
     */
    private String mainFlag;

    /**
     *新老用户标记 1新用户 2老用户
     */
    private String userFlag;

    /**
     *	号主身份证id
     */
    private String certId;

    /**
     *	号主身份证姓名
     */
    private String certName;

    /**
     * 地市编码(三位)
     */
    private String cityCode;

    /**
     * 号码省份
     */
    private String provinceCode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMainFlag() {
        return mainFlag;
    }

    public void setMainFlag(String mainFlag) {
        this.mainFlag = mainFlag;
    }

    public String getUserFlag() {
        return userFlag;
    }

    public void setUserFlag(String userFlag) {
        this.userFlag = userFlag;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
