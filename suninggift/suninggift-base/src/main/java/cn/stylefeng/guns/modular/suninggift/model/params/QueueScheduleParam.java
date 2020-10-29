package cn.stylefeng.guns.modular.suninggift.model.params;

import lombok.Data;
import cn.stylefeng.roses.kernel.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 任务调度表
 * </p>
 *
 * @author cms
 * @since 2020-03-02
 */
@Data
public class QueueScheduleParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 通知流水号
     */
    private String flowNo;

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 通知状态1成功 0失败
     */
    private Integer status;

    /**
     * 通知类型
     */
    private String type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 下次通知时间
     */
    private Date nextNotifyTime;

    /**
     * 成功时间
     */
    private Date successTime;

    /**
     * 通知数据
     */
    private String requestData;

    /**
     * 响应数据
     */
    private String responseData;

    /**
     * 备注
     */
    private String remark;

    /**
     * 通知地址
     */
    private String notifyUrl;

    /**
     * 通知次数
     */
    private Integer notifyCount;

    @Override
    public String checkParam() {
        return null;
    }

}
