package cn.stylefeng.guns.modular.suninggift.mapper;

import cn.stylefeng.guns.modular.suninggift.entity.TmllOrderOprationLog;
import cn.stylefeng.guns.modular.suninggift.model.params.TmllOrderOprationLogParam;
import cn.stylefeng.guns.modular.suninggift.model.result.TmllOrderOprationLogResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tangxiong
 * @since 2020-04-03
 */
public interface TmllOrderOprationLogMapper extends BaseMapper<TmllOrderOprationLog> {

    /**
     * 获取列表
     *
     * @author tangxiong
     * @Date 2020-04-03
     */
    List<TmllOrderOprationLogResult> customList(@Param("paramCondition") TmllOrderOprationLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author tangxiong
     * @Date 2020-04-03
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") TmllOrderOprationLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author tangxiong
     * @Date 2020-04-03
     */
    Page<TmllOrderOprationLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") TmllOrderOprationLogParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author tangxiong
     * @Date 2020-04-03
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") TmllOrderOprationLogParam paramCondition);

}
