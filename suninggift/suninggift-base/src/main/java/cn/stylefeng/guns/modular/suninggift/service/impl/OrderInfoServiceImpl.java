package cn.stylefeng.guns.modular.suninggift.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.CustomerInfo;
import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.enums.OperatorEnum;
import cn.stylefeng.guns.modular.suninggift.enums.ProcessStatesEnum;
import cn.stylefeng.guns.modular.suninggift.service.OrderInfoService;
import cn.stylefeng.guns.modular.suninggift.mapper.OrderInfoMapper;
import cn.stylefeng.guns.modular.suninggift.model.params.OrderInfoParam;
import cn.stylefeng.guns.modular.suninggift.model.result.OrderInfoResult;
import cn.stylefeng.guns.modular.suninggift.utils.DateUtil;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zba
 * @since 2020-02-24
 */
@Slf4j
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Override
    public void add(OrderInfoParam param){
        OrderInfo entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OrderInfoParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(OrderInfoParam param){
        OrderInfo oldEntity = getOldEntity(param);
        OrderInfo newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OrderInfo findBySpec(OrderInfoParam param){
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("out_trade_no",param.getOutTradeNo());
        OrderInfo orderInfo = this.baseMapper.selectOne(queryWrapper);
        return orderInfo;
    }

    @Override
    public List<OrderInfo> queryOrderList(OrderInfoParam param){
        List<OrderInfo> orderInfoList = new ArrayList<>();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("auth_order_no", param.getOutTradeNo());
        //queryWrapper.eq("phone", param.getPhone());
        orderInfoList = this.baseMapper.selectList(queryWrapper);
        return orderInfoList;
    }

    @Override
    public List<OrderInfoResult> queryOrderListExport(OrderInfoParam param){
        List<OrderInfoResult> orderInfoList = new ArrayList<>();
        orderInfoList = this.baseMapper.customList(param);
        return orderInfoList;
    }

    @Override
    public List<OrderInfoResult> findListBySpec(OrderInfoParam param){
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(OrderInfoParam param){
        Page pageContext = new Page();
        if(param.getLimit() != null && param.getPage() != null){
            pageContext = new Page(param.getPage(), param.getLimit());
        }else{
            pageContext = getPageContext();
        }
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    @Override
    public OrderInfo getByAuthOrderNo(String authOrderNo) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("auth_order_no", authOrderNo);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public OrderInfo queryOrderInfoByMobileAndUnicomOrderNo(String mobile, String unicomOrderNo) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", mobile);
        queryWrapper.eq("operator_order_id", unicomOrderNo);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public OrderInfo getByAuthOrderNoAndAuthRequestNo(String authOrderNo ,String authRequestNo) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("auth_order_no", authOrderNo);
        queryWrapper.eq("auth_request_no", authRequestNo);
        return this.baseMapper.selectOne(queryWrapper);
    }

    private Serializable getKey(OrderInfoParam param){
        return param.getOutTradeNo();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private OrderInfo getOldEntity(OrderInfoParam param) {
        return this.getById(getKey(param));
    }

    private OrderInfo getEntity(OrderInfoParam param) {
        OrderInfo entity = new OrderInfo();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<OrderInfo> queryOrderListInfo(OrderInfoParam param){
        List<OrderInfo> orderInfoList = this.baseMapper.queryOrderListInfo(param);
        return orderInfoList;
    }

    /**
     * 保存苏宁新入网订单信息
     * @param jsonBodyObject
     * @return
     */
    @Override
    public Map<String, String> saveSuNingOrderInfo(JSONObject jsonBodyObject) {
        Map<String, String> resultMap = new HashMap<String, String>();

        //订单实体
        OrderInfo orderInfo = new OrderInfo();

        //订单号：商户授权资金订单号
        //String outTradeNo = DateUtil.commontime + RandomUtil.randomNumbers(8);
        String outTradeNo = jsonBodyObject.getString("outOrderNo");
        orderInfo.setOutTradeNo(outTradeNo);
        //订单状态 0-待付款 1-支付成功 2-合约办理成功 3-合约办理失败
        orderInfo.setStatus(0);
        //创建时间
        orderInfo.setCreateTime(new Date());
        //订单名称
        orderInfo.setName(jsonBodyObject.getString("orderTitle"));
        //运营商名称
        String operator = OperatorEnum.lpsuningunicom.getDec();
        orderInfo.setOperator(operator);
        //签约手机号
        orderInfo.setPhone(jsonBodyObject.getString("contractPhone"));
        //充值金额，单位元(冻结金额)
        BigDecimal totalFee = new BigDecimal(jsonBodyObject.getString("freezePrice"));
        orderInfo.setTotalFee(totalFee);
        //请求流水号:商户本次资金操作的请求流水号
        String transferId = jsonBodyObject.getString("outRequestNo");
        orderInfo.setTransferId(transferId);
        //支付宝UID
        orderInfo.setUserId("0");
        //商品id
        //orderInfo.setItemId();
        /**
         * 流程状态
         * init初始化,three_cert_checking运营商三户校验中,DISSATISFY_BUSINESS_HANDLING("dissatisfy_business_handling","不满足业务办理"),
         *     SATISFY_BUSINESS_HANDLING("satisfy_business_handling","满足业务办理"),
         *     BUSINESS_HANDLING("business_handling","业务办理中"),
         *     BUSINESS_HANDLING_FAILED("business_handling_failed","业务办理失败"),
         *     BUSINESS_HANDLING_SUCCESS("business_handling_success","业务办理成功"),
         *     DEBIT_CREATE_SUCCESS("debit_create_success","调拨创建成功"),
         *     DEBIT_CREATE_FAILED("debit_create_failed","调拨创建失败"),
         *     DEBIT_SUBSCRIBE_SUCCESS("debit_subscribe_success","调拨订阅成功"),
         *     DEBIT_SUBSCRIBE_FAILED("debit_subscribe_failed","调拨订阅失败"),
         *     DEBIT_LOAN_SUCCESS("debit_loan_success","调拨放款成功"),
         *     DEBIT_LOAN_FAILED("debit_loan_failed","调拨放款失败"),
         *     DEBIT_TRADE_CREATE_SUCCESS("debit_trade_create_success","调拨交易创建成功"),
         *     DEBIT_TRADE_CREATE_FAILED("debit_trade_create_failed","调拨交易创建失败"),
         *     DEBIT_TRADE_CANCEL_SUCCESS("debit_trade_cancel_success","调拨交易取消成功"),
         *     DEBIT_TRADE_CANCEL_FAILED("debit_trade_cancel_failed","调拨交易取消失败"),
         */
        orderInfo.setProcessStates(ProcessStatesEnum.INIT.getCode());
        //运营商订单id
        //orderInfo.setOperatorOrderId();
        //调拨订单id
        //orderInfo.setDebitOrderId();
        //合约id
        //orderInfo.setContractId();
        //授权码
        //orderInfo.setAuthNo();
        //商户的授权资金订单号
        orderInfo.setAuthOrderNo(jsonBodyObject.getString("outOrderNo"));
        //支付宝的授权资金操作流水号
        //orderInfo.setOperationId();
        //商户的授权资金操作流水号
        orderInfo.setAuthRequestNo(jsonBodyObject.getString("outRequestNo"));
        //收款账号
        orderInfo.setPayeeLogonId(jsonBodyObject.getString("payeeLogonId"));
        //收款账号pid
        orderInfo.setPayeeUserId(jsonBodyObject.getString("payeeUserId"));
        //冻结期数
        Integer freezeMonth = Integer.valueOf(jsonBodyObject.getString("freezeMonth"));
        orderInfo.setFreezeMonth(freezeMonth);
        //首付金额
        BigDecimal orderPayAmount = new BigDecimal("0.00");
        orderInfo.setOrderPayAmount(orderPayAmount);
        //阿里侧虚拟产品Id
        orderInfo.setProductId(jsonBodyObject.getString("outProtNo"));
        //政策外部编号/主政策编码
        orderInfo.setOutPortNo(jsonBodyObject.getString("policyNo"));
        //子政策编码
        orderInfo.setSpecPsnId(jsonBodyObject.getString("orderDetailsPsnId"));
        //拓展字段1:保存一些调拨系统和派券使用的信息
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("agencyNo",jsonBodyObject.getString("agencyNo"));
        jsonBody.put("agencyName",jsonBodyObject.getString("agencyName"));
        jsonBody.put("storeNo",jsonBodyObject.getString("storeNo"));
        jsonBody.put("storeName",jsonBodyObject.getString("storeName"));
        jsonBody.put("accountNo",jsonBodyObject.getString("accountNo"));
        jsonBody.put("carrierNo",jsonBodyObject.getString("carrierNo"));
        jsonBody.put("specPsnId",jsonBodyObject.getString("specPsnId"));
        jsonBody.put("isvPid",jsonBodyObject.getString("isvPid"));
        orderInfo.setField1(jsonBody.toJSONString());
        //cb订单号
        //orderInfo.setOrderLineId();

        String phoneBlong = jsonBodyObject.getString("phoneBelong");
        String[] phoneBlongArray = phoneBlong.split(",");
        if(phoneBlongArray.length == 1){
            //手机所属省份编码
            orderInfo.setProvinceCode(phoneBlongArray[0]);
        }else if(phoneBlongArray.length == 2){
            //手机所属省份编码
            orderInfo.setProvinceCode(phoneBlongArray[0]);
            //手机所属地市编码
            orderInfo.setCityCode(phoneBlongArray[1]);
        }

        //订单所属appid
        orderInfo.setAppId(jsonBodyObject.getString("acountAppId"));
        //所属运营商编码
        String operatorCode = OperatorEnum.lpsuningunicom.getCode();
        orderInfo.setOperatorCode(operatorCode);
        //业务类型 clsd存量升档 llb流量包 xyhrw新用户入网
        orderInfo.setBusinessType("xyhrw");
        //用户姓名
        orderInfo.setUserName(jsonBodyObject.getString("custName"));
        //是否成功支付过 0否1是
        orderInfo.setIsPaySuccess("0");
        //商家编码
        orderInfo.setOutAgencyNo(jsonBodyObject.getString("outAgencyNo"));
        //用户编码
        orderInfo.setCustNo(jsonBodyObject.getString("custNo"));
        //是否推送WSN系统：订单默认不推送
        orderInfo.setGoWsn("0");
        //设置field3的值
        orderInfo.setField3(jsonBodyObject.getString("field3"));

        try {
            boolean save = this.save(orderInfo);
            if(save){
                resultMap.put("status" ,"10000");
                resultMap.put("msg" ,"保存订单信息成功");
                resultMap.put("jsonBodyObject" ,jsonBodyObject.toJSONString());
            }else{
                resultMap.put("status" ,"30001");
                resultMap.put("msg" ,"保存订单信息失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存订单信息异常,{}" ,e);
            resultMap.put("status" ,"30002");
            resultMap.put("msg" ,"保存订单信息异常");
        }

        log.info("保存订单信息结果,{}" ,JSONObject.toJSONString(resultMap));
        return resultMap;
    }

}
