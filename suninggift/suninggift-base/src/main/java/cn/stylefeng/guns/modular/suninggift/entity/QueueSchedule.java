package cn.stylefeng.guns.modular.suninggift.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 任务调度表
 * </p>
 *
 * @author cms
 * @since 2020-03-02
 */
@TableName("queue_schedule")
public class QueueSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知流水号
     */
    @TableId(value = "flow_no", type = IdType.ID_WORKER)
    private String flowNo;

    /**
     * 订单号
     */
    @TableField("out_trade_no")
    private String outTradeNo;

    /**
     * 通知状态1成功 0失败
     */
    @TableField("status")
    private Integer status;

    /**
     * 通知类型
     */
    @TableField("type")
    private String type;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 下次通知时间
     */
    @TableField("next_notify_time")
    private Date nextNotifyTime;

    /**
     * 成功时间
     */
    @TableField("success_time")
    private Date successTime;

    /**
     * 通知数据
     */
    @TableField("request_data")
    private String requestData;

    /**
     * 响应数据
     */
    @TableField("response_data")
    private String responseData;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 通知地址
     */
    @TableField("notify_url")
    private String notifyUrl;

    /**
     * 通知次数
     */
    @TableField("notify_count")
    private Integer notifyCount;


    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getNextNotifyTime() {
        return nextNotifyTime;
    }

    public void setNextNotifyTime(Date nextNotifyTime) {
        this.nextNotifyTime = nextNotifyTime;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Integer getNotifyCount() {
        return notifyCount;
    }

    public void setNotifyCount(Integer notifyCount) {
        this.notifyCount = notifyCount;
    }

    @Override
    public String toString() {
        return "QueueSchedule{" +
        "flowNo=" + flowNo +
        ", outTradeNo=" + outTradeNo +
        ", status=" + status +
        ", type=" + type +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", nextNotifyTime=" + nextNotifyTime +
        ", successTime=" + successTime +
        ", requestData=" + requestData +
        ", responseData=" + responseData +
        ", remark=" + remark +
        ", notifyUrl=" + notifyUrl +
        ", notifyCount=" + notifyCount +
        "}";
    }
}
