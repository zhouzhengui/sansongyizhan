package cn.stylefeng.guns.modular.suninggift.mapper;

import cn.stylefeng.guns.modular.suninggift.entity.WopayFtp;
import cn.stylefeng.guns.modular.suninggift.model.params.WopayFtpParam;
import cn.stylefeng.guns.modular.suninggift.model.result.WopayFtpResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 联通ftp推送表 Mapper 接口
 * </p>
 *
 * @author cms
 * @since 2020-05-07
 */
public interface WopayFtpMapper extends BaseMapper<WopayFtp> {

    /**
     * 获取列表
     *
     * @author cms
     * @Date 2020-05-07
     */
    List<WopayFtpResult> customList(@Param("paramCondition") WopayFtpParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cms
     * @Date 2020-05-07
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") WopayFtpParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cms
     * @Date 2020-05-07
     */
    Page<WopayFtpResult> customPageList(@Param("page") Page page, @Param("paramCondition") WopayFtpParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cms
     * @Date 2020-05-07
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WopayFtpParam paramCondition);

}
