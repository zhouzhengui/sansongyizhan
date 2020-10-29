package cn.stylefeng.guns.modular.suninggift.service.impl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.QueueSchedule;
import cn.stylefeng.guns.modular.suninggift.mapper.QueueScheduleMapper;
import cn.stylefeng.guns.modular.suninggift.model.params.QueueScheduleParam;
import cn.stylefeng.guns.modular.suninggift.model.result.QueueScheduleResult;
import cn.stylefeng.guns.modular.suninggift.service.QueueScheduleService;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 任务调度表 服务实现类
 * </p>
 *
 * @author cms
 * @since 2020-03-02
 */
@Service
public class QueueScheduleServiceImpl extends ServiceImpl<QueueScheduleMapper, QueueSchedule> implements QueueScheduleService {

    @Override
    public void add(QueueScheduleParam param){
        QueueSchedule entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QueueScheduleParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(QueueScheduleParam param){
        QueueSchedule oldEntity = getOldEntity(param);
        QueueSchedule newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QueueScheduleResult findBySpec(QueueScheduleParam param){
        return null;
    }

    @Override
    public List<QueueScheduleResult> findListBySpec(QueueScheduleParam param){
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(QueueScheduleParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    @Override
    public int myAdd(QueueSchedule queueSchedule) {
        int insert = this.baseMapper.insert(queueSchedule);
        return insert;
    }

    private Serializable getKey(QueueScheduleParam param){
        return param.getFlowNo();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private QueueSchedule getOldEntity(QueueScheduleParam param) {
        return this.getById(getKey(param));
    }

    private QueueSchedule getEntity(QueueScheduleParam param) {
        QueueSchedule entity = new QueueSchedule();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
