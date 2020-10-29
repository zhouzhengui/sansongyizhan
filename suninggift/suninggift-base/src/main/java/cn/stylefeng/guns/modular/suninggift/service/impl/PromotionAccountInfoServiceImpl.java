package cn.stylefeng.guns.modular.suninggift.service.impl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.service.PromotionAccountInfoService;
import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
import cn.stylefeng.guns.modular.suninggift.mapper.PromotionAccountInfoMapper;
import cn.stylefeng.guns.modular.suninggift.model.params.PromotionAccountInfoParam;
import cn.stylefeng.guns.modular.suninggift.model.result.PromotionAccountInfoResult;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 推广商信息表，收集跟支付宝、微信等平台交互的账户信息 服务实现类
 * </p>
 *
 * @author cms
 * @since 2020-02-21
 */
@Slf4j
@Service
public class PromotionAccountInfoServiceImpl extends ServiceImpl<PromotionAccountInfoMapper, PromotionAccountInfo> implements PromotionAccountInfoService {

    private Map<String, PromotionAccountInfo> map = new HashMap<>();

    @Override
    public void add(PromotionAccountInfoParam param){
        PromotionAccountInfo entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PromotionAccountInfoParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PromotionAccountInfoParam param){
        PromotionAccountInfo oldEntity = getOldEntity(param);
        PromotionAccountInfo newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PromotionAccountInfoResult findBySpec(PromotionAccountInfoParam param){
        return null;
    }

    @Override
    public List<PromotionAccountInfoResult> findListBySpec(PromotionAccountInfoParam param){
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(PromotionAccountInfoParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    @Override
    public PromotionAccountInfo getByAppId(String appId) {
        log.info("appId信息查询:{}", appId);
        QueryWrapper<PromotionAccountInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("app_id", appId);
        List<PromotionAccountInfo> accountInfoList = this.baseMapper.selectList(queryWrapper);
        if(accountInfoList == null || accountInfoList.size() <= 0){
            log.info("查无appId信息");
            return null;
        }else{
            return accountInfoList.get(0);
        }
    }

    private Serializable getKey(PromotionAccountInfoParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private PromotionAccountInfo getOldEntity(PromotionAccountInfoParam param) {
        return this.getById(getKey(param));
    }

    private PromotionAccountInfo getEntity(PromotionAccountInfoParam param) {
        PromotionAccountInfo entity = new PromotionAccountInfo();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    /**
     * 初始化参数信息
     */
    public synchronized void loadConfigDB() {
        List<PromotionAccountInfo> sysConfigs = this.baseMapper.selectList(null);
        map = new HashMap<>();
        for(PromotionAccountInfo conf : sysConfigs) {
            map.put(conf.getAppId(), conf);
        }
    }

}
