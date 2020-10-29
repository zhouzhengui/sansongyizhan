package cn.stylefeng.guns.modular.suninggift.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 联通ftp推送表
 * </p>
 *
 * @author cms
 * @since 2020-05-07
 */
@Data
@TableName("wopay_ftp")
public class WopayFtp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易订单号（cbss与乐芃交互唯一订单号）
     */
    @TableId(value = "trade_no", type = IdType.ID_WORKER)
    private String tradeNo;

    /**
     * CBSS订单号
     */
    @TableField("trade_flow_no")
    private String tradeFlowNo;

    /**
     * 手机号
     */
    @TableField("phone_no")
    private String phoneNo;

    /**
     * 分期方编码 1400：天猫商城
     */
    @TableField("stages_code")
    private String stagesCode = "1400";

    /**
     * 产品ID
     */
    @TableField("product_id")
    private String productId;

    /**
     * 订单创建时间
     */
    @TableField("credit_date")
    private Date creditDate;

    /**
     * 订单支付完成时间
     */
    @TableField("finish_date")
    private Date finishDate;

    /**
     * 交易状态 1.支付成功 2.退款成功 3.提前结清
     */
    @TableField("trade_status")
    private String tradeStatus;

    /**
     * 退款成功时间
     */
    @TableField("refund_date")
    private Date refundDate;

    /**
     * 身份证姓名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 身份证
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 产品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 订单描述：手机[TYPE:产品类型|MODEL:品牌|SPEC:型号|IMEI:串码]
     */
    @TableField("product_description")
    private String productDescription;

    /**
     * 分期数
     */
    @TableField("fq_num")
    private Integer fqNum;

    /**
     * 订单金额
     */
    @TableField("order_amount")
    private BigDecimal orderAmount;

    /**
     * 卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持
     */
    @TableField("fq_seller_percent")
    private BigDecimal fqSellerPercent;

    /**
     * 费率
     */
    @TableField("fq_rate")
    private BigDecimal fqRate;

    /**
     * 付款方用户id
     */
    @TableField("pay_user_id")
    private String payUserId;

    /**
     * 省份编码
     */
    @TableField("province")
    private String province;

    /**
     * 城市编码
     */
    @TableField("city")
    private String city;

    /**
     * 业务类型
        1.新用户入网 2.老用户提档
     */
    @TableField("business_type")
    private String businessType;

    /**
     * 数据创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 数据更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 订单版本
     */
    @TableField("order_version")
    private String orderVersion;

    @Override
    public String toString() {
        return "WopayFtp{" +
        "tradeNo=" + tradeNo +
        ", tradeFlowNo=" + tradeFlowNo +
        ", phoneNo=" + phoneNo +
        ", stagesCode=" + stagesCode +
        ", productId=" + productId +
        ", creditDate=" + creditDate +
        ", finishDate=" + finishDate +
        ", tradeStatus=" + tradeStatus +
        ", refundDate=" + refundDate +
        ", userName=" + userName +
        ", idCard=" + idCard +
        ", productName=" + productName +
        ", productDescription=" + productDescription +
        ", fqNum=" + fqNum +
        ", orderAmount=" + orderAmount +
        ", fqSellerPercent=" + fqSellerPercent +
        ", fqRate=" + fqRate +
        ", payUserId=" + payUserId +
        ", province=" + province +
        ", city=" + city +
        ", businessType=" + businessType +
        ", createTime=" + createTime +
        "}";
    }
}
