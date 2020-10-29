package cn.stylefeng.guns.modular.suninggift.service.impl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
import cn.stylefeng.guns.modular.suninggift.entity.TmllOrderOprationLog;
import cn.stylefeng.guns.modular.suninggift.enums.OrderKeeperResultEnum;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ApiInBizResponseMsg;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.DeployOrderPayUnfreezeRefundAo;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OrderUmicomUnfrzBillVo;
import cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo.OutApiResponse;
import cn.stylefeng.guns.modular.suninggift.model.params.OrderDeployParam;
import cn.stylefeng.guns.modular.suninggift.model.params.OrderInfoParam;
import cn.stylefeng.guns.modular.suninggift.model.params.SuningOrderKeepresultParam;
import cn.stylefeng.guns.modular.suninggift.model.params.TmllOrderOprationLogParam;
import cn.stylefeng.guns.modular.suninggift.service.*;
import cn.stylefeng.guns.modular.suninggift.utils.ChatbotSendUtil;
import cn.stylefeng.guns.modular.suninggift.utils.HttpUtil;
import cn.stylefeng.guns.modular.suninggift.utils.IdGeneratorSnowflake;
import cn.stylefeng.guns.modular.suninggift.utils.SignUtil;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.taobao.api.request.AlibabaAlicomOpentradeOrderKeepresultRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassNameTmallOrderKeepresultServiceImpl
 * @Description TODO苏宁存送业务履约、毁约通知接口
 * @Author tangxiong
 * @Date 2020/2/24 14:55
 **/
@Slf4j
@Service
public class TmallOrderKeepresultServiceImpl implements TmallOrderKeepresultService {
    private static final String url = "https://eco.taobao.com/router/rest";
    private static final String appkey = "28315549";
    private static final String secret = "a44159b142642bec2104ce7f729ece70";
    private static final String sessionKey = "610151572f8a4ddacd98cb51880f89bc3fb78c26549c7332201230566418";


    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private TmllOrderDisposeService tmllOrderDisposeService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private PromotionAccountInfoService promotionAccountInfoService;

    @Autowired
    private TmllOrderOprationLogService tmllOrderOprationLogService;

    @Autowired
    private ChatbotSendUtil chatbotSendUtil;

    @Autowired
    private InInterfaceService inInterfaceService;


    @Override
    public Map<String, Object> orderKeepresult(Map<String, Object> paramMap) {
        Map<String, Object> resultMap = new HashMap<>();
        String dingMsg = "";//异常情况发送信信息到钉钉
        String flag = checkParamMap(paramMap);
        if (!StringUtils.isEmpty(flag)) {
            resultMap.put("code", 20000);
            resultMap.put("msg", "参数无效");
            resultMap.put("subCode", 20000);
            resultMap.put("subMsg", flag);
            return resultMap;
        }

        try {
            String outOrderNo = (String) paramMap.get("orderNo");//直接传乐芃订单号-即苏宁订单号
            String phoneNo = (String) paramMap.get("phoneNo");//必填、手机号码(业务系统的校验依据)

            OrderInfoParam orderInfoParam = new OrderInfoParam();
            orderInfoParam.setOutTradeNo(outOrderNo);
            orderInfoParam.setPhone(phoneNo);
            List<OrderInfo> orderInfoResult = orderInfoService.queryOrderList(orderInfoParam);
            if (orderInfoResult == null || orderInfoResult.size() == 0) {
                log.info("苏宁订单不存在：" + outOrderNo);
                resultMap.put("code", 20000);
                resultMap.put("msg", "订单不存在");
                resultMap.put("subCode", 20000);
                resultMap.put("subMsg", "苏宁订单不存在===" + outOrderNo);
                return resultMap;
            } else if (orderInfoResult.size() != 1) {
                log.info("苏宁订单存在多条：" + outOrderNo);
                resultMap.put("code", 20000);
                resultMap.put("msg", "苏宁订单存在多条");
                resultMap.put("subCode", 20000);
                resultMap.put("subMsg", "苏宁订单存在多条===" + outOrderNo);
                return resultMap;
            }
            OrderInfo orderInfo = orderInfoResult.get(0);
            String tmllOrderNo = orderInfo.getOutTradeNo();//苏宁订单号
            log.info("开始调用苏宁履约、毁约接口：{}", paramMap);
            SuningOrderKeepresultParam suningOrderKeepresultParam = assemblySuningOrder(paramMap);
            if (suningOrderKeepresultParam == null) {
                log.info("不支持的订单操作类型,无法通知苏宁系统：" + outOrderNo);
                resultMap.put("code", 20000);
                resultMap.put("msg", "不支持的订单操作类型");
                resultMap.put("subCode", 20000);
                resultMap.put("subMsg", "不支持的订单操作类型===" + outOrderNo);
                return resultMap;
            }
            suningOrderKeepresultParam.setOut_trade_no(tmllOrderNo);
            log.info("苏宁履约、毁约接口请求参数：{}", JSONObject.toJSONString(suningOrderKeepresultParam));
            OutApiResponse outApiResponse = this.suningOrderKeepresultDispose(suningOrderKeepresultParam);
            log.info("苏宁履约、毁约接口响应参数：{}", JSONObject.toJSONString(outApiResponse));
            String retCode = outApiResponse == null ? "40000" : outApiResponse.getRetCode();
            //通知苏宁之后，响应参数进行入库，记录流水状态
            TmllOrderOprationLogParam tmllOrderOprationLogParam = assemblyTmallOpreationParam(paramMap);
            tmllOrderOprationLogParam.setTmallOrderNo(tmllOrderNo);
            tmllOrderOprationLogParam.setTmallStatus(retCode);
            tmllOrderOprationLogParam.setTmallRequest(JSONObject.toJSONString(suningOrderKeepresultParam));
            tmllOrderOprationLogParam.setTmallResponse(JSONObject.toJSONString(outApiResponse));
            tmllOrderOprationLogService.add(tmllOrderOprationLogParam);
            String oprationId = tmllOrderOprationLogParam.getId();//日志主键id
            log.info("苏宁订单操作日志入库成功：" + tmllOrderNo);
            //通知苏宁成功，通知调拨系统更新订单状态
            if ("0000".equals(retCode)) {
                //调拨解冻/转支付所需参数
                OrderUmicomUnfrzBillVo orderDeployParam = assemblyDeployOrder(paramMap);
                if (orderDeployParam == null) {
                    log.info("不支持的订单操作类型，无法通知调拨系统：" + outOrderNo);
                    resultMap.put("code", 20000);
                    resultMap.put("msg", "不支持的订单操作类型");
                    resultMap.put("subCode", 20000);
                    resultMap.put("subMsg", "不支持的订单操作类型===" + outOrderNo);
                    return resultMap;
                }
                orderDeployParam.setOutOrderNo(tmllOrderNo);
                //1.如何判断是解冻还是结清？？？？？ 2.如何判断是解冻还是转支付 3.如果是最后一个月履约，直接结清还是先解冻在结清
                //履约、毁约信息推送苏宁成功，更新调拨系统订单信息======异步进行
                log.info("调用调拨系统更新订单状态=========");
                //如果是结清
                ApiInBizResponseMsg apiInBizResponseMsg = null;
                TmllOrderOprationLog tmllOrderOprationLog = new TmllOrderOprationLog();
                if (OrderKeeperResultEnum.DEPLOYORDERTYPE_SETTLE.getCode().equals(orderDeployParam.getUnfrzType())) {
                    DeployOrderPayUnfreezeRefundAo deployOrderPayUnfreezeRefundAo = new DeployOrderPayUnfreezeRefundAo();
                    deployOrderPayUnfreezeRefundAo.setOutOrderNo(tmllOrderNo);
                    deployOrderPayUnfreezeRefundAo.setViolateReceivables(orderDeployParam.getAmount());
                    deployOrderPayUnfreezeRefundAo.setViolateHandleType("COMMON_MODEL");
                    log.info("调拨返销请求参数：{}", JSONObject.toJSONString(deployOrderPayUnfreezeRefundAo));
                    apiInBizResponseMsg = inInterfaceService.deployOrderPayAndUnfreezeRefund(deployOrderPayUnfreezeRefundAo);
                   if(apiInBizResponseMsg != null){
                       tmllOrderOprationLog.setId(oprationId);
                       tmllOrderOprationLog.setDeployStatus(apiInBizResponseMsg.getCode());
                       tmllOrderOprationLog.setDeployRequest(JSONObject.toJSONString(deployOrderPayUnfreezeRefundAo));
                       tmllOrderOprationLog.setDeployResponse(apiInBizResponseMsg.getMsg());
                       tmllOrderOprationLogService.updateById(tmllOrderOprationLog);
                   }
                    log.info("调拨返销响应参数：{}", JSONObject.toJSONString(apiInBizResponseMsg));
                } else {
                    log.info("调拨冻结转支付请求参数：{}", JSONObject.toJSONString(orderDeployParam));
                    apiInBizResponseMsg = inInterfaceService.cardOrderPerformance(orderDeployParam);
                    if(apiInBizResponseMsg != null){
                        tmllOrderOprationLog.setId(oprationId);
                        tmllOrderOprationLog.setDeployStatus(apiInBizResponseMsg.getCode());
                        tmllOrderOprationLog.setDeployRequest(JSONObject.toJSONString(orderDeployParam));
                        tmllOrderOprationLog.setDeployResponse(apiInBizResponseMsg.getMsg());
                        tmllOrderOprationLogService.updateById(tmllOrderOprationLog);
                    }
                    log.info("调拨冻结转支付响应参数：{}", JSONObject.toJSONString(apiInBizResponseMsg));
                }

                //1.推送成功，推送信息入库。。。。。。。？？待定
                //2.订单解冻/转支付成功，或者结清成功，是否需要更新主订单的状态
                resultMap.put("code", "10000");
                resultMap.put("msg", outApiResponse.getRetMessage());
                resultMap.put("subCode", outApiResponse.getRetCode());
                resultMap.put("subMsg", outApiResponse.getRetMessage());
            } else {
                dingMsg = "调用苏宁履约接口失败：" + outApiResponse.getRetMessage();
                chatbotSendUtil.sendMsg(dingMsg);
                resultMap.put("code", outApiResponse.getRetCode());
                resultMap.put("msg", outApiResponse.getRetMessage());
                resultMap.put("subCode", outApiResponse.getRetCode());
                resultMap.put("subMsg", outApiResponse.getRetMessage());
            }

        } catch (Exception e) {
            dingMsg = "苏宁履约、毁约接口异常" + e.getMessage();
            chatbotSendUtil.sendMsg(dingMsg);
            log.error("调用苏宁履约、毁约接口异常：{}", e);
            resultMap.put("code", 40000);
            resultMap.put("msg", "接口异常");
            resultMap.put("subCode", 40000);
            resultMap.put("subMsg", "处理异常");
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> cmccOrderKeepresult(Map<String, Object> paramMap) {
        Map<String, Object> resultMap = new HashMap<>();
        String dingMsg = "";//异常情况发送信信息到钉钉
        String flag = checkParamMap(paramMap);
        if (!StringUtils.isEmpty(flag)) {
            resultMap.put("code", 20000);
            resultMap.put("msg", "参数无效");
            resultMap.put("subCode", 20000);
            resultMap.put("subMsg", flag);
            return resultMap;
        }

        try {
            String outOrderNo = (String) paramMap.get("orderNo");//直接传乐芃订单号-即苏宁订单号
            String phoneNo = (String) paramMap.get("phoneNo");//必填、手机号码(业务系统的校验依据)

            OrderInfoParam orderInfoParam = new OrderInfoParam();
            orderInfoParam.setOutTradeNo(outOrderNo);
            orderInfoParam.setPhone(phoneNo);
            List<OrderInfo> orderInfoResult = orderInfoService.queryOrderList(orderInfoParam);
            if (orderInfoResult == null || orderInfoResult.size() == 0) {
                log.info("苏宁订单不存在：" + outOrderNo);
                resultMap.put("code", 20000);
                resultMap.put("msg", "订单不存在");
                resultMap.put("subCode", 20000);
                resultMap.put("subMsg", "苏宁订单不存在===" + outOrderNo);
                return resultMap;
            } else if (orderInfoResult.size() != 1) {
                log.info("苏宁订单存在多条：" + outOrderNo);
                resultMap.put("code", 20000);
                resultMap.put("msg", "苏宁订单存在多条");
                resultMap.put("subCode", 20000);
                resultMap.put("subMsg", "苏宁订单存在多条===" + outOrderNo);
                return resultMap;
            }
            OrderInfo orderInfo = orderInfoResult.get(0);
            String tmllOrderNo = orderInfo.getOutTradeNo();//苏宁订单号
            log.info("开始调用苏宁履约、毁约接口：{}", paramMap);
            SuningOrderKeepresultParam suningOrderKeepresultParam = assemblySuningOrder(paramMap);
            if (suningOrderKeepresultParam == null) {
                log.info("不支持的订单操作类型,无法通知苏宁系统：" + outOrderNo);
                resultMap.put("code", 20000);
                resultMap.put("msg", "不支持的订单操作类型");
                resultMap.put("subCode", 20000);
                resultMap.put("subMsg", "不支持的订单操作类型===" + outOrderNo);
                return resultMap;
            }
            suningOrderKeepresultParam.setOut_trade_no(tmllOrderNo);
            log.info("苏宁履约、毁约接口请求参数：{}", JSONObject.toJSONString(suningOrderKeepresultParam));
            OutApiResponse outApiResponse = this.suningOrderKeepresultDispose(suningOrderKeepresultParam);
            log.info("苏宁履约、毁约接口响应参数：{}", JSONObject.toJSONString(outApiResponse));
            String retCode = outApiResponse == null ? "40000" : outApiResponse.getRetCode();
            //通知苏宁之后，响应参数进行入库，记录流水状态
            TmllOrderOprationLogParam tmllOrderOprationLogParam = assemblyTmallOpreationParam(paramMap);
            tmllOrderOprationLogParam.setTmallOrderNo(tmllOrderNo);
            tmllOrderOprationLogParam.setTmallStatus(retCode);
            tmllOrderOprationLogParam.setTmallRequest(JSONObject.toJSONString(suningOrderKeepresultParam));
            tmllOrderOprationLogParam.setTmallResponse(JSONObject.toJSONString(outApiResponse));
            tmllOrderOprationLogService.add(tmllOrderOprationLogParam);
            log.info("苏宁订单操作日志入库成功：" + tmllOrderNo);
            //通知苏宁成功，通知调拨系统更新订单状态
            if ("0000".equals(retCode)) {
                //中国移动直接返回成功
                //1.推送成功，推送信息入库。。。。。。。？？待定
                //2.订单解冻/转支付成功，或者结清成功，是否需要更新主订单的状态
                resultMap.put("code", "10000");
                resultMap.put("msg", outApiResponse.getRetMessage());
                resultMap.put("subCode", outApiResponse.getRetCode());
                resultMap.put("subMsg", outApiResponse.getRetMessage());
            } else {
                dingMsg = "调用苏宁履约接口失败：" + outApiResponse.getRetMessage();
                chatbotSendUtil.sendMsg(dingMsg);
                resultMap.put("code", "20000");
                resultMap.put("msg", outApiResponse.getRetMessage());
                resultMap.put("subCode", outApiResponse.getRetCode());
                resultMap.put("subMsg", outApiResponse.getRetMessage());
            }

        } catch (Exception e) {
            dingMsg = "苏宁履约、毁约接口异常" + e.getMessage();
            chatbotSendUtil.sendMsg(dingMsg);
            log.error("调用苏宁履约、毁约接口异常：{}", e);
            resultMap.put("code", 40000);
            resultMap.put("msg", "接口异常");
            resultMap.put("subCode", 40000);
            resultMap.put("subMsg", "处理异常");
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> orderQuery(Map<String, Object> paramMap) {
        Map<String, Object> resultMap = new HashMap<>();
        log.info("苏宁订单查询：{}", JSONObject.toJSONString(paramMap));
        try {
            HttpServletRequest request = null;
            String outOrderNo = (String) paramMap.get("outOrderNo");
            OrderInfoParam orderInfoParam = new OrderInfoParam();
            orderInfoParam.setOutTradeNo(outOrderNo);
            //  SpiUtils.checkSign(request, "");


            LayuiPageInfo orderInfoPage = orderInfoService.findPageBySpec(orderInfoParam);
            if (orderInfoPage != null) {
                // int status = orderInfoResult.getStatus();
                resultMap.put("code", 10000);
                resultMap.put("msg", "处理成功");
                resultMap.put("orderInfoPage", orderInfoPage);
                resultMap.put("subCode", 10000);
                resultMap.put("subMsg", "处理成功");
            } else {
                resultMap.put("code", 30000);
                resultMap.put("msg", "订单不存在");
                resultMap.put("subCode", 30000);
                resultMap.put("subMsg", "订单不存在");
            }
        } catch (Exception e) {
            log.error("调用苏宁履约、毁约订单查询异常：{}", e);
            resultMap.put("code", 40000);
            resultMap.put("msg", "系统异常");
            resultMap.put("subCode", 40000);
            resultMap.put("subMsg", "系统异常");
        }
        return resultMap;
    }

    //参数校验
    public static String checkParamMap(Map<String, Object> paramMap) {

        String outTradeNo = (String) paramMap.get("outTradeNo");//必填、请求流水号
        String outOrderNo = (String) paramMap.get("outOrderNo");//必填、订单号(cb订单号、业务系统的校验依据[存在cb订单号不一致的情况])
        String orderNo = (String) paramMap.get("orderNo");//必填、订单号(乐芃订单号cdis_cont_order主键)
        //Boolean success = (boolean) paramMap.get("success");//预授权履约结果（true正常履约；false毁约）
        //String resultCode = (String) paramMap.get("resultCode");//预授权履约结果（0000正常履约）
        //String resultDesc = (String) paramMap.get("resultDesc");//预授权履约结果描述
        String unfrzType = (String) paramMap.get("unfrzType");//预授权履约结果描述
        String month = (String) paramMap.get("month");//期数1.实际期数、2.若全部则填ALL(建议忽略大小写判断)
        String phoneNo = (String) paramMap.get("phoneNo");//必填、手机号码(业务系统的校验依据)

        if (StringUtils.isEmpty(outTradeNo)) {
            return "预授权履约订单号" + "不能为空";
        }

        if (StringUtils.isEmpty(month)) {
            return "期数" + "不能为空";
        } else {
            paramMap.put("month", month.toLowerCase());
        }

        /*if (StringUtils.isEmpty(outOrderNo)) {
            return "cb订单号不能为空";
        }*/

        if (StringUtils.isEmpty(orderNo)) {
            return "乐芃订单号不能为空";
        }

        if (StringUtils.isEmpty(unfrzType)) {
            return "订单操作类型" + "不能为空";
        }

        if (StringUtils.isEmpty(phoneNo)) {
            return "签约号码不能为空";
        }

        return null;
    }

    //组装苏宁订单请求参数
    public static AlibabaAlicomOpentradeOrderKeepresultRequest.OrderResultDto assemblyTmallOrder(Map<String, Object> paramMap) {
        AlibabaAlicomOpentradeOrderKeepresultRequest.OrderResultDto orderResultDto = new AlibabaAlicomOpentradeOrderKeepresultRequest.OrderResultDto();

        boolean success = false;
        String resultCode = "";//预授权履约结果（0000正常履约）
        String resultDesc = "";//预授权履约结果描述
        String unfrzType = (String) paramMap.get("unfrzType");//预授权履约结果描述
        String month = (String) paramMap.get("month");//期数1.实际期数、2.若全部则填ALL(建议忽略大小写判断)
        if (OrderKeeperResultEnum.DEPLOYORDERTYPE_UNFREEZE.getCode().equals(unfrzType) && !"all".equals(month)) {
            success = true;
            resultCode = OrderKeeperResultEnum.RESULTCODE_0000.getCode();
            resultDesc = OrderKeeperResultEnum.RESULTCODE_0000.getDesc();
        } else if (OrderKeeperResultEnum.DEPLOYORDERTYPE_PAY.getCode().equals(unfrzType) && !"all".equals(month)) {
            success = true;
            resultCode = OrderKeeperResultEnum.RESULTCODE_1111.getCode();
            resultDesc = OrderKeeperResultEnum.RESULTCODE_1111.getDesc();
        } else if (OrderKeeperResultEnum.DEPLOYORDERTYPE_PAY.getCode().equals(unfrzType) && "all".equals(month)) {
            success = false;
            resultCode = OrderKeeperResultEnum.RESULTCODE_9999.getCode();
            resultDesc = OrderKeeperResultEnum.RESULTCODE_9999.getDesc();
        } else {
            return null;
        }
        orderResultDto.setResultCode(resultCode);
        orderResultDto.setResultDesc(resultDesc);
        orderResultDto.setSuccess(success);
        return orderResultDto;
    }

    //组装调拨订单请求参数
    public static OrderUmicomUnfrzBillVo assemblyDeployOrder(Map<String, Object> paramMap) {
        OrderUmicomUnfrzBillVo orderDeployParam = new OrderUmicomUnfrzBillVo();
        String unfrzType = (String) paramMap.get("unfrzType");//预授权履约结果描述
        String month = (String) paramMap.get("month");//期数1.实际期数、2.若全部则填ALL(建议忽略大小写判断)
        String foundAmount = (String) paramMap.get("foundAmount");
        if (OrderKeeperResultEnum.DEPLOYORDERTYPE_PAY.getCode().equals(unfrzType) && "all".equalsIgnoreCase(month)) {
            //全额扣罚转支付====结清
            orderDeployParam.setUnfrzType(OrderKeeperResultEnum.DEPLOYORDERTYPE_SETTLE.getCode());
        } else if (OrderKeeperResultEnum.DEPLOYORDERTYPE_UNFREEZE.getCode().equals(unfrzType) && !"all".equalsIgnoreCase(month)) {
            //单期履约解冻
            orderDeployParam.setMonth(Integer.valueOf(month));
            orderDeployParam.setUnfrzType(unfrzType);
            orderDeployParam.setAmount(new BigDecimal(foundAmount));
        } else if (OrderKeeperResultEnum.DEPLOYORDERTYPE_PAY.getCode().equals(unfrzType) && !"all".equalsIgnoreCase(month)) {
            //单期扣罚转支付
            orderDeployParam.setMonth(Integer.valueOf(month));
            orderDeployParam.setUnfrzType(unfrzType);
            orderDeployParam.setAmount(new BigDecimal(foundAmount));
        } else {
            return null;//错误操作类型，不做处理
        }
        return orderDeployParam;
    }

    //组装苏宁订单履约通知请求参数
    public static SuningOrderKeepresultParam assemblySuningOrder(Map<String, Object> paramMap) {
        SuningOrderKeepresultParam suningOrderKeepresultParam = new SuningOrderKeepresultParam();
        String resultCode = "";//预授权履约结果（0000正常履约）
        String resultDesc = "";//预授权履约结果描述
        String unfrzType = (String) paramMap.get("unfrzType");//预授权履约结果描述
        String month = (String) paramMap.get("month");//期数1.实际期数、2.若全部则填ALL(建议忽略大小写判断)
        String foundAmount = (String) paramMap.get("foundAmount");
        if (OrderKeeperResultEnum.DEPLOYORDERTYPE_PAY.getCode().equals(unfrzType) && "all".equalsIgnoreCase(month)) {
            //全额扣罚转支付====结清
            suningOrderKeepresultParam.setBill_month("all");
            resultCode = OrderKeeperResultEnum.RESULTCODE_9999.getCode();
            resultDesc = OrderKeeperResultEnum.RESULTCODE_9999.getDesc();
        } else if (OrderKeeperResultEnum.DEPLOYORDERTYPE_UNFREEZE.getCode().equals(unfrzType) && !"all".equalsIgnoreCase(month)) {
            //单期履约解冻
            suningOrderKeepresultParam.setBill_month(month);
            resultCode = OrderKeeperResultEnum.RESULTCODE_0000.getCode();
            resultDesc = OrderKeeperResultEnum.RESULTCODE_0000.getDesc();

        } else if (OrderKeeperResultEnum.DEPLOYORDERTYPE_PAY.getCode().equals(unfrzType) && !"all".equalsIgnoreCase(month)) {
            //单期扣罚转支付
            suningOrderKeepresultParam.setBill_month(month);
            resultCode = OrderKeeperResultEnum.RESULTCODE_1111.getCode();
            resultDesc = OrderKeeperResultEnum.RESULTCODE_1111.getDesc();
        } else {
            return null;//错误操作类型，不做处理
        }
        suningOrderKeepresultParam.setBill_money(foundAmount);
        suningOrderKeepresultParam.setResult_code(resultCode);
        suningOrderKeepresultParam.setResult_desc(resultDesc);
        return suningOrderKeepresultParam;
    }

    //组装操作及日志实体
    public static TmllOrderOprationLogParam assemblyTmallOpreationParam(Map<String, Object> paramMap) {
        TmllOrderOprationLogParam tmllOrderOprationLogParam = new TmllOrderOprationLogParam();
        String id = IdGeneratorSnowflake.generateId();
        String outTradeNo = (String) paramMap.get("outTradeNo");//必填、请求流水号
        String outOrderNo = (String) paramMap.get("outOrderNo");//必填、订单号(cb订单号、业务系统的校验依据[存在cb订单号不一致的情况])
        String orderNo = (String) paramMap.get("orderNo");//必填、订单号(乐芃订单号cdis_cont_order主键)
        String unfrzType = (String) paramMap.get("unfrzType");//预授权履约结果描述
        String month = (String) paramMap.get("month");//期数1.实际期数、2.若全部则填ALL(建议忽略大小写判断)
        String phoneNo = (String) paramMap.get("phoneNo");//必填、手机号码(业务系统的校验依据)
        String foundAmount = (String) paramMap.get("foundAmount");
        tmllOrderOprationLogParam.setId(id);
        tmllOrderOprationLogParam.setOutTradeNo(outTradeNo);
        tmllOrderOprationLogParam.setOurOrderNo(outOrderNo);
        tmllOrderOprationLogParam.setOrderNo(orderNo);
        tmllOrderOprationLogParam.setOperationType(unfrzType);
        tmllOrderOprationLogParam.setMonth(month);
        tmllOrderOprationLogParam.setPhoneNo(phoneNo);
        tmllOrderOprationLogParam.setInstDate(new Date());
        tmllOrderOprationLogParam.setUpdateDate(new Date());
        tmllOrderOprationLogParam.setFoundAmount(foundAmount);
        return tmllOrderOprationLogParam;
    }

    //苏宁履约通知接口
    @Override
    public OutApiResponse suningOrderKeepresultDispose(SuningOrderKeepresultParam param){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TreeMap<String, String> params = new TreeMap<>();
        //公共参数
        String sysAppkey = sysConfigService.getByCode("appkey");
        String snOrderKeepresultUrl = sysConfigService.getByCode("snOrderKeepresultUrl");
        PromotionAccountInfo sysByAppId = promotionAccountInfoService.getByAppId(sysAppkey);
        String sysSecret = sysByAppId.getPrivateKey();
        String sign_method = sysByAppId.getSignType();

        params.put("app_key", sysAppkey);
        params.put("target_appkey", sysAppkey);
        params.put("timestamp", sdf.format(new Date()));
        params.put("format", sysByAppId.getFormat());
        params.put("sign_method", sign_method);
        params.put("method", "gift.gzlplink.opentrade.order.keepresult");

        //业务参数
      /*  RESULTCODE_0000("0000","用户正常缴费单期履约解冻"),
          RESULTCODE_1111("1111","用户缴费异常单期扣罚转支付"),
          RESULTCODE_9999("9999","用户毁约剩余全额扣罚转支付"),*/
        //JSONObject jsonObject = new JSONObject();
        params.put("bill_month", param.getBill_month());
        params.put("bill_money", param.getBill_money());
        params.put("out_trade_no", param.getOut_trade_no());
        params.put("result_desc", param.getResult_desc());
        params.put("result_code", param.getResult_code());
        log.info("苏宁履约通知加密前请求参数:{}", JSONObject.toJSONString(params));
        /*String jsonStr = SignUtil.strConvertBase(JSONObject.toJSONString(params));
        params.put("data", jsonStr);*/
        String mySign = null;
        try {
            mySign = SignUtil.signTopRequest(params, sysSecret, sign_method);
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("sign", mySign);
        log.info("苏宁履约通知请求参数:{}", JSONObject.toJSONString(params));
        String result = HttpUtil.hostPost(JSONObject.toJSONString(params), snOrderKeepresultUrl, "utf-8", 60*1000);
        log.info("苏宁履约通知响应:{}", result);
        if(StringUtils.isEmpty( result) || "404".equals(result)){
            return null;
        }
        JSONObject jsonObject1 = JSONObject.parseObject(result);
        OutApiResponse outApiResponse = JSONObject.parseObject(jsonObject1.getString("result"), OutApiResponse.class);
        return outApiResponse;
    }
}
