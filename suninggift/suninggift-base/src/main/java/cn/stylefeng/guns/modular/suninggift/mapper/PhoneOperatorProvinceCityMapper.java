package cn.stylefeng.guns.modular.suninggift.mapper;

import cn.stylefeng.guns.modular.suninggift.entity.PhoneOperatorProvinceCity;
import cn.stylefeng.guns.modular.suninggift.model.params.PhoneOperatorProvinceCityParam;
import cn.stylefeng.guns.modular.suninggift.model.result.PhoneOperatorProvinceCityResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 号码跟运营商跟省市关系表 Mapper 接口
 * </p>
 *
 * @author cms
 * @since 2020-06-11
 */
public interface PhoneOperatorProvinceCityMapper extends BaseMapper<PhoneOperatorProvinceCity> {

    /**
     * 获取列表
     *
     * @author cms
     * @Date 2020-06-11
     */
    List<PhoneOperatorProvinceCityResult> customList(@Param("paramCondition") PhoneOperatorProvinceCityParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cms
     * @Date 2020-06-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PhoneOperatorProvinceCityParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cms
     * @Date 2020-06-11
     */
    Page<PhoneOperatorProvinceCityResult> customPageList(@Param("page") Page page, @Param("paramCondition") PhoneOperatorProvinceCityParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cms
     * @Date 2020-06-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PhoneOperatorProvinceCityParam paramCondition);

}
