package cn.stylefeng.guns.modular.suninggift.service.impl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.PhoneOperatorProvinceCity;
import cn.stylefeng.guns.modular.suninggift.mapper.PhoneOperatorProvinceCityMapper;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestBaseofferGetData;
import cn.stylefeng.guns.modular.suninggift.model.params.PhoneOperatorProvinceCityParam;
import cn.stylefeng.guns.modular.suninggift.model.result.PhoneOperatorProvinceCityResult;
import cn.stylefeng.guns.modular.suninggift.service.PhoneOperatorProvinceCityService;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 号码跟运营商跟省市关系表 服务实现类
 * </p>
 *
 * @author cms
 * @since 2020-06-11
 */
@Slf4j
@Service
public class PhoneOperatorProvinceCityServiceImpl extends ServiceImpl<PhoneOperatorProvinceCityMapper, PhoneOperatorProvinceCity> implements PhoneOperatorProvinceCityService {

    @Override
    public void add(PhoneOperatorProvinceCityParam param){
        PhoneOperatorProvinceCity entity = getEntity(param);
        this.save(entity);
    }

    @Async
    @Override
    public void addSync(OutBizRequestBaseofferGetData outBizRequestBaseofferGetData) {
        PhoneOperatorProvinceCity phoneOperatorProvinceCity = new PhoneOperatorProvinceCity();
        phoneOperatorProvinceCity.setCity(outBizRequestBaseofferGetData.getCity());
        phoneOperatorProvinceCity.setCreateTime(new Date());
        phoneOperatorProvinceCity.setOperator(outBizRequestBaseofferGetData.getIsp());
        phoneOperatorProvinceCity.setPhone(outBizRequestBaseofferGetData.getMobile());
        phoneOperatorProvinceCity.setProvince(outBizRequestBaseofferGetData.getProvince());
        boolean b = this.saveOrUpdate(phoneOperatorProvinceCity);
        log.info("异步落库情况" + b);
    }

    @Override
    public void delete(PhoneOperatorProvinceCityParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PhoneOperatorProvinceCityParam param){
        PhoneOperatorProvinceCity oldEntity = getOldEntity(param);
        PhoneOperatorProvinceCity newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PhoneOperatorProvinceCityResult findBySpec(PhoneOperatorProvinceCityParam param){
        return null;
    }

    @Override
    public List<PhoneOperatorProvinceCityResult> findListBySpec(PhoneOperatorProvinceCityParam param){
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(PhoneOperatorProvinceCityParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(PhoneOperatorProvinceCityParam param){
        return param.getPhone();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private PhoneOperatorProvinceCity getOldEntity(PhoneOperatorProvinceCityParam param) {
        return this.getById(getKey(param));
    }

    private PhoneOperatorProvinceCity getEntity(PhoneOperatorProvinceCityParam param) {
        PhoneOperatorProvinceCity entity = new PhoneOperatorProvinceCity();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
