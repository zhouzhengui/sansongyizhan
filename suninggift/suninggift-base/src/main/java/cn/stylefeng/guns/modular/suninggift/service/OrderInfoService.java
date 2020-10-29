package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.model.params.OrderInfoParam;
import cn.stylefeng.guns.modular.suninggift.model.result.OrderInfoResult;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zba
 * @since 2020-02-24
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 新增
     *
     * @author zba
     * @Date 2020-02-24
     */
    void add(OrderInfoParam param);

    /**
     * 删除
     *
     * @author zba
     * @Date 2020-02-24
     */
    void delete(OrderInfoParam param);

    /**
     * 更新
     *
     * @author zba
     * @Date 2020-02-24
     */
    void update(OrderInfoParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author zba
     * @Date 2020-02-24
     */
    OrderInfo findBySpec(OrderInfoParam param);

    List<OrderInfo> queryOrderList(OrderInfoParam param);

    List<OrderInfoResult> queryOrderListExport(OrderInfoParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author zba
     * @Date 2020-02-24
     */
    List<OrderInfoResult> findListBySpec(OrderInfoParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author zba
     * @Date 2020-02-24
     */
    LayuiPageInfo findPageBySpec(OrderInfoParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author zba
     * @Date 2020-02-24
     */
    OrderInfo getByAuthOrderNo(String authOrderNo);

    /**
     * 查询单条数据，Specification模式
     *
     * @author zba
     * @Date 2020-02-24
     */
    OrderInfo getByAuthOrderNoAndAuthRequestNo(String authOrderNo ,String authRequestNo);

    List<OrderInfo> queryOrderListInfo(OrderInfoParam param);

    /**
     * 保存苏宁新入网订单信息
     * @param jsonBodyObject
     * @return
     */
    public Map<String, String> saveSuNingOrderInfo(JSONObject jsonBodyObject);

    public OrderInfo queryOrderInfoByMobileAndUnicomOrderNo(String mobile, String orderNo);
    
}
