package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.TmllOrderOprationLog;
import cn.stylefeng.guns.modular.suninggift.model.params.TmllOrderOprationLogParam;
import cn.stylefeng.guns.modular.suninggift.model.result.TmllOrderOprationLogResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tangxiong
 * @since 2020-04-03
 */
public interface TmllOrderOprationLogService extends IService<TmllOrderOprationLog> {

    /**
     * 新增
     *
     * @author tangxiong
     * @Date 2020-04-03
     */
    void add(TmllOrderOprationLogParam param);

    /**
     * 删除
     *
     * @author tangxiong
     * @Date 2020-04-03
     */
    void delete(TmllOrderOprationLogParam param);

    /**
     * 更新
     *
     * @author tangxiong
     * @Date 2020-04-03
     */
    void update(TmllOrderOprationLogParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author tangxiong
     * @Date 2020-04-03
     */
    TmllOrderOprationLogResult findBySpec(TmllOrderOprationLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author tangxiong
     * @Date 2020-04-03
     */
    List<TmllOrderOprationLogResult> findListBySpec(TmllOrderOprationLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author tangxiong
     * @Date 2020-04-03
     */
     LayuiPageInfo findPageBySpec(TmllOrderOprationLogParam param);

}
