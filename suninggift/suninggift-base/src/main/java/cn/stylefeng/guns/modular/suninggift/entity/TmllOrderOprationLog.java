package cn.stylefeng.guns.modular.suninggift.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author tangxiong
 * @since 2020-04-03
 */
@Data
@TableName("tmll_order_opration_log")
public class TmllOrderOprationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键（雪花）
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;

    /**
     * 天猫订单号
     */
    @TableField("tmall_order_no")
    private String tmallOrderNo;

    /**
     * 请求流水号
     */
    @TableField("out_trade_no")
    private String outTradeNo;

    /**
     * cb订单号，关联订单表 order_line_id
     */
    @TableField("our_order_no")
    private String ourOrderNo;

    /**
     * 乐芃订单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * UNFREEZE-解冻、PAY-转支付
     */
    @TableField("operation_type")
    private String operationType;

    /**
     * 期数
     */
    @TableField("month")
    private String month;

    /**
     * 金额
     */
    @TableField("found_amount")
    private String foundAmount;

    /**
     * 签约手机号
     */
    @TableField("phone_no")
    private String phoneNo;

    /**
     * 天猫接口响应状态
     */
    @TableField("tmall_status")
    private String tmallStatus;

    /**
     * 调拨系统响应状态
     */
    @TableField("deploy_status")
    private String deployStatus;

    /**
     * 天猫接口请求参数
     */
    @TableField("tmall_request")
    private String tmallRequest;

    /**
     * 天猫接口响应参数
     */
    @TableField("tmall_response")
    private String tmallResponse;

    /**
     * 调拨系统请求参数
     */
    @TableField("deploy_request")
    private String deployRequest;

    /**
     * 调拨系统响应参数
     */
    @TableField("deploy_response")
    private String deployResponse;

    /**
     * 创建时间
     */
    @TableField("inst_date")
    private Date instDate;

    /**
     * 更新时间
     */
    @TableField("update_date")
    private Date updateDate;

}
