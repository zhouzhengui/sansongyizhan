package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.PhoneOperatorProvinceCity;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestBaseofferGetData;
import cn.stylefeng.guns.modular.suninggift.model.params.PhoneOperatorProvinceCityParam;
import cn.stylefeng.guns.modular.suninggift.model.result.PhoneOperatorProvinceCityResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 号码跟运营商跟省市关系表 服务类
 * </p>
 *
 * @author cms
 * @since 2020-06-11
 */
public interface PhoneOperatorProvinceCityService extends IService<PhoneOperatorProvinceCity> {

    /**
     * 新增
     *
     * @author cms
     * @Date 2020-06-11
     */
    void add(PhoneOperatorProvinceCityParam param);

    /**
     * 异步新增
     *
     * @author cms
     * @Date 2020-06-11
     */
    void addSync(OutBizRequestBaseofferGetData outBizRequestBaseofferGetData);

    /**
     * 删除
     *
     * @author cms
     * @Date 2020-06-11
     */
    void delete(PhoneOperatorProvinceCityParam param);

    /**
     * 更新
     *
     * @author cms
     * @Date 2020-06-11
     */
    void update(PhoneOperatorProvinceCityParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cms
     * @Date 2020-06-11
     */
    PhoneOperatorProvinceCityResult findBySpec(PhoneOperatorProvinceCityParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cms
     * @Date 2020-06-11
     */
    List<PhoneOperatorProvinceCityResult> findListBySpec(PhoneOperatorProvinceCityParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cms
     * @Date 2020-06-11
     */
     LayuiPageInfo findPageBySpec(PhoneOperatorProvinceCityParam param);

}
