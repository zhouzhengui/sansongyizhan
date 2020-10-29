package cn.stylefeng.guns.modular.suninggift.model.params;

import cn.stylefeng.roses.kernel.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author tangxiong
 * @since 2020-04-03
 */
@Data
public class TmllOrderOprationLogParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 主键（雪花）
     */
    private String id;

    /**
     * 天猫订单号
     */
    private String tmallOrderNo;

    /**
     * 请求流水号
     */
    private String outTradeNo;

    /**
     * cb订单号，关联订单表 order_line_id
     */
    private String ourOrderNo;

    /**
     * 乐芃订单号
     */
    private String orderNo;

    /**
     * UNFREEZE-解冻、PAY-转支付
     */
    private String operationType;

    /**
     * 期数
     */
    private String month;

    /**
     * 金额
     */
    private String foundAmount;

    /**
     * 签约手机号
     */
    private String phoneNo;

    /**
     * 天猫接口响应状态
     */
    private String tmallStatus;

    /**
     * 调拨系统响应状态
     */
    private String deployStatus;

    /**
     * 天猫接口请求参数
     */
    private String tmallRequest;

    /**
     * 天猫接口响应参数
     */
    private String tmallResponse;

    /**
     * 调拨系统请求参数
     */
    private String deployRequest;

    /**
     * 调拨系统响应参数
     */
    private String deployResponse;

    /**
     * 创建时间
     */
    private Date instDate;

    /**
     * 更新时间
     */
    private Date updateDate;

    @Override
    public String checkParam() {
        return null;
    }

}
