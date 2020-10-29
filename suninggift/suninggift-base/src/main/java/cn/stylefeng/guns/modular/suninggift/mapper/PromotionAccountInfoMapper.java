package cn.stylefeng.guns.modular.suninggift.mapper;

import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
import cn.stylefeng.guns.modular.suninggift.model.params.PromotionAccountInfoParam;
import cn.stylefeng.guns.modular.suninggift.model.result.PromotionAccountInfoResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 推广商信息表，收集跟支付宝、微信等平台交互的账户信息 Mapper 接口
 * </p>
 *
 * @author cms
 * @since 2020-02-21
 */
public interface PromotionAccountInfoMapper extends BaseMapper<PromotionAccountInfo> {

    /**
     * 获取列表
     *
     * @author cms
     * @Date 2020-02-21
     */
    List<PromotionAccountInfoResult> customList(@Param("paramCondition") PromotionAccountInfoParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cms
     * @Date 2020-02-21
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PromotionAccountInfoParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cms
     * @Date 2020-02-21
     */
    Page<PromotionAccountInfoResult> customPageList(@Param("page") Page page, @Param("paramCondition") PromotionAccountInfoParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cms
     * @Date 2020-02-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PromotionAccountInfoParam paramCondition);

}
