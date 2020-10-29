package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.QueueSchedule;
import cn.stylefeng.guns.modular.suninggift.model.params.QueueScheduleParam;
import cn.stylefeng.guns.modular.suninggift.model.result.QueueScheduleResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 任务调度表 服务类
 * </p>
 *
 * @author cms
 * @since 2020-03-02
 */
public interface QueueScheduleService extends IService<QueueSchedule> {

    /**
     * 新增
     *
     * @author cms
     * @Date 2020-03-02
     */
    void add(QueueScheduleParam param);

    /**
     * 删除
     *
     * @author cms
     * @Date 2020-03-02
     */
    void delete(QueueScheduleParam param);

    /**
     * 更新
     *
     * @author cms
     * @Date 2020-03-02
     */
    void update(QueueScheduleParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cms
     * @Date 2020-03-02
     */
    QueueScheduleResult findBySpec(QueueScheduleParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cms
     * @Date 2020-03-02
     */
    List<QueueScheduleResult> findListBySpec(QueueScheduleParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cms
     * @Date 2020-03-02
     */
     LayuiPageInfo findPageBySpec(QueueScheduleParam param);

    /**
     * 插入
     * @param queueSchedule
     * @return
     */
     int myAdd(QueueSchedule queueSchedule);

}
