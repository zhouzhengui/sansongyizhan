package cn.stylefeng.guns.modular.suninggift.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 联通ftp推送表
 * </p>
 *
 * @author cms
 * @since 2020-05-07
 */
@Data
public class WopayFtpResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 交易订单号（cbss与乐芃交互唯一订单号）
     */
    private String tradeNo;

    /**
     * CBSS订单号
     */
    private String tradeFlowNo;

    /**
     * 手机号
     */
    private String phoneNo;

    /**
     * 分期方编码 1400：天猫商城
     */
    private String stagesCode;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 订单创建时间
     */
    private Date creditDate;

    /**
     * 订单支付完成时间
     */
    private Date finishDate;

    /**
     * 交易状态 1.支付成功 2.退款成功 3.提前结清
     */
    private String tradeStatus;

    /**
     * 退款成功时间
     */
    private Date refundDate;

    /**
     * 身份证姓名
     */
    private String userName;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 订单描述：手机[TYPE:产品类型|MODEL:品牌|SPEC:型号|IMEI:串码]
     */
    private String productDescription;

    /**
     * 分期数
     */
    private Integer fqNum;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持
     */
    private BigDecimal fqSellerPercent;

    /**
     * 费率
     */
    private BigDecimal fqRate;

    /**
     * 付款方用户id
     */
    private String payUserId;

    /**
     * 省份编码
     */
    private String province;

    /**
     * 城市编码
     */
    private String city;

    /**
     * 业务类型
1.新用户入网 2.老用户提档
     */
    private String businessType;

    /**
     * 数据创建时间
     */
    private Date createTime;

}
