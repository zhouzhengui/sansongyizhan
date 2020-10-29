package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
import cn.stylefeng.guns.modular.suninggift.model.params.PromotionAccountInfoParam;
import cn.stylefeng.guns.modular.suninggift.model.result.PromotionAccountInfoResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 推广商信息表，收集跟支付宝、微信等平台交互的账户信息 服务类
 * </p>
 *
 * @author cms
 * @since 2020-02-21
 */
public interface PromotionAccountInfoService extends IService<PromotionAccountInfo> {

    /**
     * 新增
     *
     * @author cms
     * @Date 2020-02-21
     */
    void add(PromotionAccountInfoParam param);

    /**
     * 删除
     *
     * @author cms
     * @Date 2020-02-21
     */
    void delete(PromotionAccountInfoParam param);

    /**
     * 更新
     *
     * @author cms
     * @Date 2020-02-21
     */
    void update(PromotionAccountInfoParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cms
     * @Date 2020-02-21
     */
    PromotionAccountInfoResult findBySpec(PromotionAccountInfoParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cms
     * @Date 2020-02-21
     */
    List<PromotionAccountInfoResult> findListBySpec(PromotionAccountInfoParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cms
     * @Date 2020-02-21
     */
     LayuiPageInfo findPageBySpec(PromotionAccountInfoParam param);

    /**
     * 根据appid获取数据
     *
     * @author cms
     * @Date 2020-02-21
     */
    PromotionAccountInfo getByAppId(String appId);

}
