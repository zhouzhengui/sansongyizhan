package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import java.io.Serializable;
import java.util.List;

public class NewOrderSyncAo implements Serializable {

    private static final long serialVersionUID = -913740305074252742L;

    /**
     * 省份编码（两位）
     */
    private String provinceCode;

    /**
     * 地市编码(三位)
     */
    private String cityCode;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 基本产品id
     */
    private String baseProductId;

    /**
     *首月资费方式id A000011V000001 （全月）A000011V000002（半月）A000011V000003（套外）
     */
    private String firstMonthId;

    /**
     * 附加产品信息（只传可选附加产品）
     */
    private List<NewOrderSyncProductListInfoAo> productListInfo;

    /**
     * 合约id
     */
    private String contractId;

    /**
     *邮递姓名
     */
    private String postName;

    /**
     *联系电话
     */
    private String postNumber;

    /**
     *邮递省份编码（6位）
     */
    private String webProvince;

    /**
     *邮递地市编码（6位）
     */
    private String webCity;

    /**
     *邮递区县编码（6位）
     */
    private String webCounty;

    /**
     * 选择号码信息
     */
    private List<NewOrderSyncNumberListInfoAo> numberListInfo;

    /**
     *邮递地址
     */
    private String address;

    /**
     *订单总费用(单位：厘)
     */
    private String orderTotalFee;

    /**
     *三户返回主卡客户ID
     */
    private String mainCustId;

    /**
     *三户返回是否为主卡标识（1：是，0：不是）
     */
    private String mainCardTag;

    /**
     *三户返回主卡账户id
     */
    private String mainAcctId;

    /**
     *渠道
     */
    private String channel;

    /**
     * 发展人编码
     */
    private String devCode;

    /**
     * 合作方订单id
     */
    private String partnerOrderId;

    /**
     * 合作方流水号
     */
    private String serialNumber;

    /**
     * 金融渠道编码
     */
    private String financeChannelCode;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(String baseProductId) {
        this.baseProductId = baseProductId;
    }

    public String getFirstMonthId() {
        return firstMonthId;
    }

    public void setFirstMonthId(String firstMonthId) {
        this.firstMonthId = firstMonthId;
    }

    public List<NewOrderSyncProductListInfoAo> getProductListInfo() {
        return productListInfo;
    }

    public void setProductListInfo(List<NewOrderSyncProductListInfoAo> productListInfo) {
        this.productListInfo = productListInfo;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(String postNumber) {
        this.postNumber = postNumber;
    }

    public String getWebProvince() {
        return webProvince;
    }

    public void setWebProvince(String webProvince) {
        this.webProvince = webProvince;
    }

    public String getWebCity() {
        return webCity;
    }

    public void setWebCity(String webCity) {
        this.webCity = webCity;
    }

    public String getWebCounty() {
        return webCounty;
    }

    public void setWebCounty(String webCounty) {
        this.webCounty = webCounty;
    }

    public List<NewOrderSyncNumberListInfoAo> getNumberListInfo() {
        return numberListInfo;
    }

    public void setNumberListInfo(List<NewOrderSyncNumberListInfoAo> numberListInfo) {
        this.numberListInfo = numberListInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderTotalFee() {
        return orderTotalFee;
    }

    public void setOrderTotalFee(String orderTotalFee) {
        this.orderTotalFee = orderTotalFee;
    }

    public String getMainCustId() {
        return mainCustId;
    }

    public void setMainCustId(String mainCustId) {
        this.mainCustId = mainCustId;
    }

    public String getMainCardTag() {
        return mainCardTag;
    }

    public void setMainCardTag(String mainCardTag) {
        this.mainCardTag = mainCardTag;
    }

    public String getMainAcctId() {
        return mainAcctId;
    }

    public void setMainAcctId(String mainAcctId) {
        this.mainAcctId = mainAcctId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getFinanceChannelCode() {
        return financeChannelCode;
    }

    public void setFinanceChannelCode(String financeChannelCode) {
        this.financeChannelCode = financeChannelCode;
    }
}
