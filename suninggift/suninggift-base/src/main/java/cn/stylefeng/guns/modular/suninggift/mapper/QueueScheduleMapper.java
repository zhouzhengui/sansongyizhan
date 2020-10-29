package cn.stylefeng.guns.modular.suninggift.mapper;

import cn.stylefeng.guns.modular.suninggift.entity.QueueSchedule;
import cn.stylefeng.guns.modular.suninggift.model.params.QueueScheduleParam;
import cn.stylefeng.guns.modular.suninggift.model.result.QueueScheduleResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务调度表 Mapper 接口
 * </p>
 *
 * @author cms
 * @since 2020-03-02
 */
public interface QueueScheduleMapper extends BaseMapper<QueueSchedule> {

    /**
     * 获取列表
     *
     * @author cms
     * @Date 2020-03-02
     */
    List<QueueScheduleResult> customList(@Param("paramCondition") QueueScheduleParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cms
     * @Date 2020-03-02
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QueueScheduleParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cms
     * @Date 2020-03-02
     */
    Page<QueueScheduleResult> customPageList(@Param("page") Page page, @Param("paramCondition") QueueScheduleParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cms
     * @Date 2020-03-02
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QueueScheduleParam paramCondition);

}
