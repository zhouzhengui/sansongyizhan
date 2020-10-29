package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.entity.WopayFtp;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.WsnOrderInfoVo;
import cn.stylefeng.guns.modular.suninggift.model.params.OrderDeployParam;
import cn.stylefeng.guns.modular.suninggift.model.result.OrderInfoResult;

import java.util.List;
import java.util.Map;

/**
 * @ClassNameTmllOrderDisposeService
 * @Description TODO天猫履约、毁约订单pot,调拨系统订单信息更新处理
 * @Author tangxiong
 * @Date 2020/2/27 14:31
 **/
public interface TmllOrderDisposeService {
    public void tmallOrderCenterDispose(OrderInfo orderInfo);//pot订单信息更新

    public void tmallOrderDeployDispose(OrderDeployParam orderDeployParam);//调拨系统订单信息更新

    public LayuiPageInfo tmallQueryOrderList(Map<String, Object> map);

    public List<OrderInfoResult> tmallQueryOrderListExport(Map<String, Object> map);

    public void tmallOrderDeploySettleRefundDispose(OrderDeployParam orderDeployParam);//调拨系统订单信息更新

    public  Map<String, Object>  queryOrderListInfo(Map<String, Object> map);

    //订单推送到wsn系统
    String orderSyncWsnForftp(WopayFtp wopayFtp);

    public String orderSyncWsnForOrderInfo(WsnOrderInfoVo wsnOrderInfoVo);
    
}
