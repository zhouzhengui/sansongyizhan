package cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo;

public class CheckUserPhoneRspAo {

    /**
     * custInfo : {"certAddr":"联通CBSS测试地址测试专用","certCode":"9715040233005710","certEndDate":"20501212000000","certType":"护照","certTypeCode":"08","custId":"9715040233005710","custName":"李师师"}
     * userInfo : {"brandId":"4G00","brandName":"沃4G","productId":"90356341","productName":"4G畅爽冰激凌国内流量套餐-99元/月","serviceClassCode":"0050","userId":"9714060866380923"}
     */

    private CustInfoBean custInfo;
    private UserInfoBean userInfo;

    public CustInfoBean getCustInfo() {
        return custInfo;
    }

    public void setCustInfo(CustInfoBean custInfo) {
        this.custInfo = custInfo;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class CustInfoBean {
        /**
         * certAddr : 联通CBSS测试地址测试专用
         * certCode : 9715040233005710
         * certEndDate : 20501212000000
         * certType : 护照
         * certTypeCode : 08
         * custId : 9715040233005710
         * custName : 李师师
         */

        private String certAddr;
        private String certCode;
        private String certEndDate;
        private String certType;
        private String certTypeCode;
        private String custId;
        private String custName;

        public String getCertAddr() {
            return certAddr;
        }

        public void setCertAddr(String certAddr) {
            this.certAddr = certAddr;
        }

        public String getCertCode() {
            return certCode;
        }

        public void setCertCode(String certCode) {
            this.certCode = certCode;
        }

        public String getCertEndDate() {
            return certEndDate;
        }

        public void setCertEndDate(String certEndDate) {
            this.certEndDate = certEndDate;
        }

        public String getCertType() {
            return certType;
        }

        public void setCertType(String certType) {
            this.certType = certType;
        }

        public String getCertTypeCode() {
            return certTypeCode;
        }

        public void setCertTypeCode(String certTypeCode) {
            this.certTypeCode = certTypeCode;
        }

        public String getCustId() {
            return custId;
        }

        public void setCustId(String custId) {
            this.custId = custId;
        }

        public String getCustName() {
            return custName;
        }

        public void setCustName(String custName) {
            this.custName = custName;
        }
    }

    public static class UserInfoBean {
        /**
         * brandId : 4G00
         * brandName : 沃4G
         * productId : 90356341
         * productName : 4G畅爽冰激凌国内流量套餐-99元/月
         * serviceClassCode : 0050
         * userId : 9714060866380923
         */

        private String brandId;
        private String brandName;
        private String productId;
        private String productName;
        private String serviceClassCode;
        private String userId;

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getServiceClassCode() {
            return serviceClassCode;
        }

        public void setServiceClassCode(String serviceClassCode) {
            this.serviceClassCode = serviceClassCode;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

}
