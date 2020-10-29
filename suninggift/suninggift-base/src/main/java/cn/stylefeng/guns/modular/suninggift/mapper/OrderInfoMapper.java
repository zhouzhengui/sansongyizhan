package cn.stylefeng.guns.modular.suninggift.mapper;

import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.model.params.OrderInfoParam;
import cn.stylefeng.guns.modular.suninggift.model.result.OrderInfoResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author cms
 * @since 2020-04-22
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 获取列表
     *
     * @author cms
     * @Date 2020-04-22
     */
    List<OrderInfoResult> customList(@Param("paramCondition") OrderInfoParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cms
     * @Date 2020-04-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OrderInfoParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cms
     * @Date 2020-04-22
     */
    Page<OrderInfoResult> customPageList(@Param("page") Page page, @Param("paramCondition") OrderInfoParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cms
     * @Date 2020-04-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OrderInfoParam paramCondition);

    /**
     * @Author
     * @Description //根据时间查询订单
     * @Date
     * @Param ${param}
     * @return ${return}
     **/
    List<OrderInfo> queryOrderListInfo(@Param("paramCondition") OrderInfoParam paramCondition);

}
