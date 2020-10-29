package cn.stylefeng.guns.modular.suninggift.service.impl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.service.TmllOrderOprationLogService;
import cn.stylefeng.guns.modular.suninggift.entity.TmllOrderOprationLog;
import cn.stylefeng.guns.modular.suninggift.mapper.TmllOrderOprationLogMapper;
import cn.stylefeng.guns.modular.suninggift.model.params.TmllOrderOprationLogParam;
import cn.stylefeng.guns.modular.suninggift.model.result.TmllOrderOprationLogResult;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tangxiong
 * @since 2020-04-03
 */
@Service
public class TmllOrderOprationLogServiceImpl extends ServiceImpl<TmllOrderOprationLogMapper, TmllOrderOprationLog> implements TmllOrderOprationLogService {

    @Async
    @Override
    public void add(TmllOrderOprationLogParam param){
        TmllOrderOprationLog entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(TmllOrderOprationLogParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(TmllOrderOprationLogParam param){
        TmllOrderOprationLog oldEntity = getOldEntity(param);
        TmllOrderOprationLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TmllOrderOprationLogResult findBySpec(TmllOrderOprationLogParam param){
        return null;
    }

    @Override
    public List<TmllOrderOprationLogResult> findListBySpec(TmllOrderOprationLogParam param){
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(TmllOrderOprationLogParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(TmllOrderOprationLogParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private TmllOrderOprationLog getOldEntity(TmllOrderOprationLogParam param) {
        return this.getById(getKey(param));
    }

    private TmllOrderOprationLog getEntity(TmllOrderOprationLogParam param) {
        TmllOrderOprationLog entity = new TmllOrderOprationLog();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
