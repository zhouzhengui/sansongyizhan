package cn.stylefeng.guns.modular.suninggift.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 号码跟运营商跟省市关系表
 * </p>
 *
 * @author cms
 * @since 2020-06-11
 */
@TableName("phone_operator_province_city")
public class PhoneOperatorProvinceCity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    @TableId(value = "phone", type = IdType.ID_WORKER)
    private String phone;

    /**
     * 运营商名称
     */
    @TableField("operator")
    private String operator;

    /**
     * 号码所属省份
     */
    @TableField("province")
    private String province;

    /**
     * 号码所属地市
     */
    @TableField("city")
    private String city;

    /**
     * 数据创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PhoneOperatorProvinceCity{" +
        "phone=" + phone +
        ", operator=" + operator +
        ", province=" + province +
        ", city=" + city +
        ", createTime=" + createTime +
        "}";
    }
}
