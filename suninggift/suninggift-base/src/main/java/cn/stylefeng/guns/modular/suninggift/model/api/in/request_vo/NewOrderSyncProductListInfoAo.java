package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import java.io.Serializable;

public class NewOrderSyncProductListInfoAo implements Serializable {

    private static final long serialVersionUID = 2128513016796120844L;

    /**
     * 附加产品id
     */
    private String productId;

    public NewOrderSyncProductListInfoAo() {
    }

    public NewOrderSyncProductListInfoAo(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
