package cn.stylefeng.guns.modular.suninggift.model.result;

import lombok.Data;

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
@Data
public class PhoneOperatorProvinceCityResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 手机号
     */
    private String phone;

    /**
     * 运营商名称
     */
    private String operator;

    /**
     * 号码所属省份
     */
    private String province;

    /**
     * 号码所属地市
     */
    private String city;

    /**
     * 数据创建时间
     */
    private Date createTime;

}
