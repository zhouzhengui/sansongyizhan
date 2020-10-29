package cn.stylefeng.guns.modular.suninggift.tools;

import cn.stylefeng.guns.config.springRedisCache.CacheDuration;
import cn.stylefeng.guns.modular.suninggift.entity.PhoneOperatorProvinceCity;
import cn.stylefeng.guns.modular.suninggift.enums.*;
import cn.stylefeng.guns.modular.suninggift.model.InBizRespond;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.*;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestCreateOrder;
import cn.stylefeng.guns.modular.suninggift.model.constant.SysConstant;
import cn.stylefeng.guns.modular.suninggift.service.PhoneOperatorProvinceCityService;
import cn.stylefeng.guns.modular.suninggift.service.PromotionAccountInfoService;
import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
import cn.stylefeng.guns.modular.suninggift.enums.*;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.UserProductRecommendAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.*;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestBaseofferGetData;
import cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo.BaseofferGetResponseData;
import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.modular.suninggift.service.InInterfaceService;
import cn.stylefeng.guns.modular.suninggift.utils.*;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import cn.stylefeng.guns.sys.modular.utils.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.WtTradeOrderResultcallbackRequest;
import com.taobao.api.response.WtTradeOrderResultcallbackResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * qimenServiceTool类
 *
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-04-24 11:22
 */
@Slf4j
@Component
public class QimenServiceTool {

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    SysConfigServiceTool sysConfigServiceTool;

    @Autowired
    PromotionAccountInfoService promotionAccountInfoService;

    @Autowired
    InInterfaceService inInterfaceService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedisUtilNew redisUtilNew;

    @Autowired
    PhoneOperatorProvinceCityService phoneOperatorProvinceCityService;

    @Autowired
    private ChatbotSendUtil chatbotSendUtil;

    private final Long CIRCULATION_QUERY_INTERVAL = 3 * 1000L;//循环调天猫间隔

    /**
     * 通知天猫订购状态
     */
    @Async
   /* public void notifyTmallOrderStatus(String outTradeNo, String processStates, boolean isSuccess, String desc) {
        int i = 3;
        //如果异常最多通知三次
        while (i > 0) {
            WtTradeOrderResultcallbackResponse rsp = this.customerCallBack(outTradeNo, processStates, isSuccess, desc);
            if (null != rsp && null != rsp.getResult()) {
                WtTradeOrderResultcallbackResponse.CommonRtnDo result = rsp.getResult();
                String code = result.getCode();
                if (null != code) {
                    TmallResponseEnum tmallResponseEnum = TmallResponseEnum.byCode(code);
                    if (null != tmallResponseEnum) {
                        //不为空则不触发5秒轮询
                        break;
                    } else {
                        i--;
                        try {
                            Thread.sleep(CIRCULATION_QUERY_INTERVAL);
                        } catch (Exception e) {
                            log.error("系统异常", e);
                        }
                    }
                } else {
                    i--;
                    try {
                        Thread.sleep(CIRCULATION_QUERY_INTERVAL);
                    } catch (Exception e) {
                        log.error("系统异常", e);
                    }
                }
            } else {
                i--;
                try {
                    Thread.sleep(CIRCULATION_QUERY_INTERVAL);
                } catch (Exception e) {
                    log.error("系统异常", e);
                }
            }
        }

    }
*/
    /**
     * 商家回调订单状态接口
     *
     * @param orderNo
     * @param code
     * @param isSuccess
     * @param desc
     */
    public WtTradeOrderResultcallbackResponse customerCallBack(String orderNo, String code, Boolean isSuccess, String desc) {
        long startTime = System.currentTimeMillis();
        String appkey = sysConfigService.getByCode("appkey");
        String qimenUrl = sysConfigService.getByCode("qimenUrl");
//        qimenUrl = "http://tdev.gzlplink.com:80/index.php/apiManagementPro/Mock/mockApi/WnMCpL61bc4df6101722646f873db996f2778cbe3215122/top/router/rest";
        PromotionAccountInfo byAppId = promotionAccountInfoService.getByAppId(appkey);
        String secret = byAppId.getPrivateKey();
        String sessionKey = byAppId.getField1();

        TaobaoClient client = new DefaultTaobaoClient(qimenUrl, appkey, secret);
        WtTradeOrderResultcallbackRequest req = new WtTradeOrderResultcallbackRequest();
        WtTradeOrderResultcallbackRequest.OrderResultDto obj1 = new WtTradeOrderResultcallbackRequest.OrderResultDto();
        obj1.setDesc(desc);
        obj1.setOrderNo(Long.valueOf(orderNo));
        obj1.setResultCode(code);
        obj1.setSuccess(isSuccess);
        req.setParam0(obj1);
        WtTradeOrderResultcallbackResponse rsp = null;
        try {
            log.info("商家回调接口请求地址->>>" + qimenUrl);
            log.info("商家回调接口请求参数->>>" + JSONObject.toJSONString(req));
            rsp = client.execute(req, sessionKey);
            log.info("商家回调接口响应参数->>>" + JSONObject.toJSONString(rsp));
        } catch (ApiException e) {
            e.printStackTrace();
            log.error("商家回调接口异常", e);
        }

        long endTime = System.currentTimeMillis();
        if ((endTime - startTime) > 6000) {
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("商家回调接口耗时 {} 毫秒", (endTime - startTime));
        return rsp;
    }


    /**
     * 根据政策与套餐列表返回天猫要的套餐列表
     *
     * @param policyListResponse  政策列表
     * @param clsdPackList        套餐列表
     * @param policyListResponse2 流量包政策列表
     * @param llbPackList         流量包套餐列表
     * @return
     */
    public List<BaseofferGetResponseData> plicyListAndPickListDispose(ApiInBizResponse<PolicyListResponseBizContent> policyListResponse,
                                                                      CardCenterResponseVo<List<CardCenterResponseVoGetPackListData>> clsdPackList,
                                                                      ApiInBizResponse<PolicyListResponseBizContent> policyListResponse2,
                                                                      CardCenterResponseVo<List<CardCenterResponseVoGetPackListData>> llbPackList,
                                                                      String provinceCode) {

        List<PolicyListResponseBizContentPolicy> policyListResponseBizContentPolicyList = policyListResponse.getBizContent().getSpecList();
        List<BaseofferGetResponseData> list = new ArrayList<>();
        if (null != clsdPackList && null != clsdPackList.getData() && clsdPackList.getData().size() > 0) {
            List<CardCenterResponseVoGetPackListData> data = clsdPackList.getData();
            for (CardCenterResponseVoGetPackListData packListData : data) {
                //获取可以办理套餐的产品编码
                String alowRange = packListData.getAlowRange();
                for (PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy : policyListResponseBizContentPolicyList) {
                    if (policyListResponseBizContentPolicy.getLevel().intValue() == 2 &&
                            policyListResponseBizContentPolicy.getCheckFlag().equals("1") &&
                            policyListResponseBizContentPolicy.getPolicySpecStatus().intValue() == 1 &&
                            policyListResponseBizContentPolicy.getKeyStr().equals(alowRange)) {
                        plicyListAndPickListDisposeChildren(list, policyListResponseBizContentPolicy, policyListResponseBizContentPolicyList);
                    }
                }
            }
        }

        List<BaseofferGetResponseData> list2 = new ArrayList<>();
        if (llbPackList != null && null != llbPackList.getData() && llbPackList.getData().size() > 0) {
            List<String> alowRanges = new ArrayList<>();
            //流量包allowRange是,号分开所以得split一下
            List<CardCenterResponseVoGetPackListData> data1 = llbPackList.getData();
            for (CardCenterResponseVoGetPackListData cardCenterResponseVoGetPackListData : data1) {
                String[] split = cardCenterResponseVoGetPackListData.getAlowRange().split(",");
                for (String a : split) {
                    alowRanges.add(a);
                }
            }

            //获取可以办理流量
            for (String alowRange : alowRanges) {
                List<PolicyListResponseBizContentPolicy> policyListResponseBizContentPolicyList2 = policyListResponse2.getBizContent().getSpecList();
                for (PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy : policyListResponseBizContentPolicyList2) {
                    if (policyListResponseBizContentPolicy.getLevel().intValue() == 2 &&
                            policyListResponseBizContentPolicy.getCheckFlag().equals("1") &&
                            policyListResponseBizContentPolicy.getPolicySpecStatus().intValue() == 1 &&
                            policyListResponseBizContentPolicy.getRemarkInfo().equals(alowRange) &&
                            policyListResponseBizContentPolicy.getValueStr().equals(provinceCode)) {
                        plicyListAndPickListDisposeChildren(list2, policyListResponseBizContentPolicy, policyListResponseBizContentPolicyList2);
                    }
                }
            }
        }

        //按冻结金额升序
        list.sort(Comparator.comparing(BaseofferGetResponseData::getLock_money));
        list2.sort(Comparator.comparing(BaseofferGetResponseData::getLock_money));

        list.addAll(list2);
        //按冻结金额降序
//        list.sort(Comparator.comparing(BaseofferGetResponseData::getLock_money).reversed());

        return list;
    }

    private void plicyListAndPickListDisposeChildren(List<BaseofferGetResponseData> list,
                                                     PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy,
                                                     List<PolicyListResponseBizContentPolicy> policyListResponseBizContentPolicyList) {
        String numberPrnId1 = null;//序号1的父节点
        String numberPrnId2 = null;//序号2的父节点
        String numberPsnId2 = null;//序号2的主主键
        BaseofferGetResponseData baseofferGetResponseData = new BaseofferGetResponseData();
        baseofferGetResponseData.setOffer_id(policyListResponseBizContentPolicy.getRemarkInfo());
        baseofferGetResponseData.setOffer_name(policyListResponseBizContentPolicy.getName());
        numberPrnId2 = policyListResponseBizContentPolicy.getPrnId();//找到2级父节点
        numberPsnId2 = policyListResponseBizContentPolicy.getPsnId();
        for (PolicyListResponseBizContentPolicy inpolicy : policyListResponseBizContentPolicyList) {
            if (inpolicy.getPsnId().equals(numberPrnId2)) {
                numberPrnId1 = inpolicy.getPrnId();
                baseofferGetResponseData.setExp_month(inpolicy.getName());
                baseofferGetResponseData.setLock_money(new BigDecimal(inpolicy.getValueStr()).multiply(new BigDecimal(100)).intValue() + "");
                //寻找直降金额
                for (PolicyListResponseBizContentPolicy ininpolicy : policyListResponseBizContentPolicyList) {
                    if (ininpolicy.getPrnId().equals(numberPrnId2) &&
                            ininpolicy.getCheckFlag().equals("2") &&
                            ininpolicy.getLevel().intValue() == 2) {
                        baseofferGetResponseData.setSend_award_fee(new BigDecimal(ininpolicy.getValueStr()).multiply(new BigDecimal(100)).intValue() + "");
                        break;
                    }
                }
                break;
            }
        }
        //根据1级父节点获取套餐名与套餐档位
        for (PolicyListResponseBizContentPolicy inpolicy : policyListResponseBizContentPolicyList) {
            if (inpolicy.getPsnId().equals(numberPrnId1)) {
                baseofferGetResponseData.setOffer_in_desc(inpolicy.getRemarkInfo());
                baseofferGetResponseData.setOffer_set(inpolicy.getKeyStr());
                break;
            }
        }
        //获取阿里产品id与套餐产品包外优惠描述
        for (PolicyListResponseBizContentPolicy inpolicy2 : policyListResponseBizContentPolicyList) {
            if (inpolicy2.getLevel().intValue() == 3 && inpolicy2.getCheckFlag().equals("1") &&
                    inpolicy2.getPrnId().equals(policyListResponseBizContentPolicy.getPsnId())) {
                baseofferGetResponseData.setProduct_id(inpolicy2.getKeyStr());
                baseofferGetResponseData.setOffer_out_desc(inpolicy2.getRemarkInfo());
                break;
            }
        }
        //获取
        for (PolicyListResponseBizContentPolicy inpolicy : policyListResponseBizContentPolicyList) {
            if (inpolicy.getLevel().intValue() == 3 && inpolicy.getCheckFlag().equals("1") &&
                    inpolicy.getPrnId().equals(numberPsnId2)) {
                baseofferGetResponseData.setOffer_out_desc(inpolicy.getRemarkInfo());
                break;
            }
        }
        list.add(baseofferGetResponseData);
    }


    /**
     * 传入阿里产品id跟政策子集返回alowRange、分期数、冻结金额、0级子政策编码、合约id、订单标题
     *
     * @param productId
     * @param policyListResponse
     * @return alowRange fqNum psnId
     */
    public JSONObject getAlowRangeByProductId(String productId,
                                              PolicyListResponseBizContent policyListResponse) {
        List<PolicyListResponseBizContentPolicy> specList = policyListResponse.getSpecList();
        PolicyListResponseBizContentProductPolicy cdisProductPolicy = policyListResponse.getCdisProductPolicy();
        JSONObject jsonObject = new JSONObject();
        String contractId = null;//合约id
        String alowRange = null;//运营商合约套餐
        String fqNum = null;
        String psnId = null;
        JSONObject freezeMoneyPerMonth = new JSONObject();//每期解冻金额
        String name = null;//订单标题
        String outAgencyNo = null;//商家编码
        String policyNo = cdisProductPolicy.getPolicyNo();//政策编码
        BigDecimal totalFee = null;
        outFor:
        for (PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy : specList) {
            //获取运营商城套餐id
            if (policyListResponseBizContentPolicy.getPolicySpecStatus().intValue() == 1 &&
                    policyListResponseBizContentPolicy.getCheckFlag().equals("1") &&
                    policyListResponseBizContentPolicy.getLevel().intValue() == 3 &&
                    policyListResponseBizContentPolicy.getKeyStr().equals(productId)) {


                //获取分期数
                for (PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy2 : specList) {
                    if (policyListResponseBizContentPolicy2.getPsnId().equals(policyListResponseBizContentPolicy.getPrnId())) {
                        contractId = policyListResponseBizContentPolicy2.getRemarkInfo();
                        alowRange = policyListResponseBizContentPolicy2.getKeyStr();
                        name = policyListResponseBizContentPolicy2.getName();
                        for (PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy3 : specList) {
                            if (policyListResponseBizContentPolicy3.getPsnId().equals(policyListResponseBizContentPolicy2.getPrnId())) {
                                fqNum = policyListResponseBizContentPolicy3.getName();
                                psnId = policyListResponseBizContentPolicy3.getPrnId();
                                totalFee = new BigDecimal(policyListResponseBizContentPolicy3.getValueStr());

                                //获取每期还款
                                for (PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy4 : specList) {
                                    if (policyListResponseBizContentPolicy3.getPsnId().equals(policyListResponseBizContentPolicy4.getPrnId()) &&
                                            policyListResponseBizContentPolicy4.getPolicySpecStatus().intValue() == 1 &&
                                            policyListResponseBizContentPolicy4.getCheckFlag().equals("2") &&
                                            policyListResponseBizContentPolicy4.getLevel().intValue() == 2) {
                                        //获取每期还款
                                        for (PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy5 : specList) {
                                            if (policyListResponseBizContentPolicy4.getPsnId().equals(policyListResponseBizContentPolicy5.getPrnId()) &&
                                                    policyListResponseBizContentPolicy5.getPolicySpecStatus().intValue() == 1 &&
                                                    policyListResponseBizContentPolicy5.getCheckFlag().equals("2") &&
                                                    policyListResponseBizContentPolicy5.getLevel().intValue() == 3) {
                                                freezeMoneyPerMonth.put(policyListResponseBizContentPolicy5.getName(), policyListResponseBizContentPolicy5.getKeyStr());
                                            }
                                        }

                                    }
                                }

                                //获取商家编码
                                for (PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy4 : specList) {
                                    if (policyListResponseBizContentPolicy3.getPsnId().equals(policyListResponseBizContentPolicy4.getPrnId()) &&
                                            policyListResponseBizContentPolicy4.getPolicySpecStatus().intValue() == 1 &&
                                            policyListResponseBizContentPolicy4.getCheckFlag().equals("6") &&
                                            policyListResponseBizContentPolicy4.getLevel().intValue() == 2) {
                                        outAgencyNo = policyListResponseBizContentPolicy4.getName();
                                        break outFor;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        jsonObject.put("alowRange", alowRange);
        jsonObject.put("fqNum", fqNum);
        jsonObject.put("freezeMoneyPerMonth", freezeMoneyPerMonth.toJSONString());
        jsonObject.put("psnId", psnId);
        jsonObject.put("totalFee", totalFee);
        jsonObject.put("contractId", contractId);
        jsonObject.put("name", name);
        jsonObject.put("policyNo", policyNo);
        jsonObject.put("outAgencyNo", outAgencyNo);
        log.info("入阿里产品id跟政策子集返回alowRange响应:" + jsonObject.toJSONString());
        return jsonObject;
    }


    /**
     * 传入乐芃订单状态放回运营商订购状态
     * 订购状态==> 31008：订购中、10000:订购成功、20000:订购失败
     *
     * @param processStatesEnum
     * @return
     */
    public String changOwnStatusToOperatorStatus(ProcessStatesEnum processStatesEnum) {
        String dgStatus = null;
        if (processStatesEnum.getCode().equals("init") ||
                processStatesEnum.getCode().equals("three_cert_checking") ||
                processStatesEnum.getCode().equals("dissatisfy_business_handling") ||
                processStatesEnum.getCode().equals("satisfy_business_handling") ||
                processStatesEnum.getCode().equals("system_error") ||
                processStatesEnum.getCode().equals("business_handling")) {
            //订购中
            dgStatus = "31008";
        } else if (processStatesEnum.getCode().equals("business_handling_failed")) {
            //订购失败
            dgStatus = "20000";
        } else if (processStatesEnum.getCode().equals("business_handling_success") ||
                processStatesEnum.getCode().equals("debit_create_success") ||
                processStatesEnum.getCode().equals("debit_create_failed") ||
                processStatesEnum.getCode().equals("debit_subscribe_success") ||
                processStatesEnum.getCode().equals("debit_subscribe_failed") ||
                processStatesEnum.getCode().equals("debit_loan_success") ||
                processStatesEnum.getCode().equals("debit_loan_failed") ||
                processStatesEnum.getCode().equals("debit_trade_create_success") ||
                processStatesEnum.getCode().equals("debit_trade_create_failed") ||
                processStatesEnum.getCode().equals("debit_trade_cancel_success") ||
                processStatesEnum.getCode().equals("debit_trade_cancel_failed") ||
                processStatesEnum.getCode().equals("debit_trade_pay_success") ||
                processStatesEnum.getCode().equals("debit_trade_pay_failed") ||
                processStatesEnum.getCode().equals("debit_settle_payables_success")) {
            //订购成功
            dgStatus = "10000";
        }
        return dgStatus;

    }

    /**
     * 根据手机号判断运营商
     *
     * @param mobile
     * @return
     */
    public OperatorEnum getOperatorByMobile(String mobile) {
        if (RegexUtil.isGdCmcc(mobile)) {
            return OperatorEnum.lpgdcmcc;
        } else {
            return OperatorEnum.lpunicom;
        }
    }

    /**
     * 该手机号是否办过单
     *
     * @param mobile
     * @return
     */
    public boolean hasOrder(String mobile) {
        return redisUtilNew.hasKey(SysConstant.HAS_ORDER_MOBILE + mobile);
    }


    /* 根据天猫isp值判断运营商 不缓存结果为空的
     * @param outBizRequestBaseofferGetData
     * @return*/
    @CacheDuration(duration = 3600)
    @Cacheable(value = "GetOperatorByIsp", key = "'suninggift:getOperatorByIsp:mobile:' + #outBizRequestBaseofferGetData.getMobile()",
            unless = "#result == null")
    public String getOperatorByIsp(OutBizRequestBaseofferGetData outBizRequestBaseofferGetData) {
        String isp = outBizRequestBaseofferGetData.getIsp();
        String operatorCode = null;
        if (StringUtils.isNotBlank(isp)) {
            IspEnum byCode = IspEnum.getByCode(isp);
            if (null != byCode) {
                operatorCode = OperatorEnum.getByCode(byCode.getOperatorEnumCode()).getCode();
            }
        }

        if (StringUtils.isBlank(operatorCode)) {
            //如果还是null则去数据库查
            PhoneOperatorProvinceCity byId = phoneOperatorProvinceCityService.getById(outBizRequestBaseofferGetData.getMobile());
            if (byId != null) {
                IspEnum byCode = IspEnum.getByCode(byId.getOperator());
                if (null != byCode) {
                    operatorCode = OperatorEnum.getByCode(byCode.getOperatorEnumCode()).getCode();
                }
            }
        }

        return operatorCode;
    }

//    /**
//     *
//     * @param operatorEnum 运营商
//     * @param productId 阿里产品id
//     * @return
//     */
//    public BusinessTypeEnum getBusinessType(OperatorEnum operatorEnum, String productId, CardCenterInterfInfo cardCenterInterfInfo){
//        if(operatorEnum == OperatorEnum.lpunicom){
//            //获取存量升档的政策
//            ApiInBizResponse<PolicyListResponseBizContent> policyListResponseBizContentApiInBizResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo, cardCenterInterfInfo.getOutPortNo(), SysConstant.QUERY_POLICY_LIST);
//            List<PolicyListResponseBizContentPolicy> specList = policyListResponseBizContentApiInBizResponse.getBizContent().getSpecList();
//            for(PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy : specList){
//                if(policyListResponseBizContentPolicy){
//
//                }
//            }
//
//
//
//
//
//            return BusinessTypeEnum.clsd;
//        }else{
//            return BusinessTypeEnum.clsd;
//        }
//    }

    /**
     * 号卡可升档套餐查询接口
     *
     * @param outBizRequestBaseofferGetData
     */
    public InBizRespond<List<BaseofferGetResponseData>> baseofferGet(OutBizRequestBaseofferGetData outBizRequestBaseofferGetData) {

        InBizRespond<List<BaseofferGetResponseData>> inBizRespond = new InBizRespond();
        OperatorEnum operator = OperatorEnum.lpunicom;
        CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(operator.getCode());

        //调用号卡中心三户校验获取省编码
        log.info("联通获取套餐触发的三户校验");
        CardCenterResponseVo<ThreeAccountVerifyData> threeAccountVerifyDataCardCenterResponseVo = inInterfaceService.threeAccountVerify(outBizRequestBaseofferGetData.getMobile(), cardCenterInterfInfo);
        if (null == threeAccountVerifyDataCardCenterResponseVo) {
            //异常
            log.info("请求号卡系统三户校验接口异常");
            inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            return inBizRespond;
        }
        if (!"10000".equals(threeAccountVerifyDataCardCenterResponseVo.getCode())) {
            //三户校验不过
            log.info("三户校验不通过");
            inBizRespond.setMessage("三户校验不通过");
            inBizRespond.setCode(ResponseStatusEnum.THREE_ACCOUNT_VERIFY_FAILD.getCode());
            return inBizRespond;
        }
        //通过之后缓存5分钟

        String provinceCode = threeAccountVerifyDataCardCenterResponseVo.getData().getProvinceCode();
        //根据省编码映射出可办套餐要的策略库编码(存量的)
        String clsdStrategyId = inInterfaceService.getStrategyIdByProvinceCode(provinceCode, BusinessTypeEnum.clsd);
        if (StringUtils.isBlank(clsdStrategyId)) {
            //异常
            log.info("查询策略库编码异常" + provinceCode);
            inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            return inBizRespond;
        }

        //调用号卡中心查询 可办套餐(存量的)
        UserProductRecommendAo clsdUserProductRecommendAo = new UserProductRecommendAo();
        clsdUserProductRecommendAo.setUserPhone(outBizRequestBaseofferGetData.getMobile());
        clsdUserProductRecommendAo.setStrategyId(clsdStrategyId);
        //调用运营商中心查询 可办套餐
        log.info("联通获取套餐触发的可办单查询存量");
        CardCenterResponseVo<List<CardCenterResponseVoGetPackListData>> clsdPackList = inInterfaceService.getPackList(clsdUserProductRecommendAo, cardCenterInterfInfo);
//        if(null == clsdPackList){
//            //异常
//            log.info("请求号卡系统可升档套餐(存量的)查询接口异常");
//            inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
//            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
//            return inBizRespond;
//        }
//        if(!clsdPackList.getCode().equals("10000")){
//            //没可用套餐
//            log.info("请求号卡系统可升档套餐(存量的)查询接口失败");
//            inBizRespond.setCode(clsdPackList.getCode());
//            inBizRespond.setMessage(clsdPackList.getMsg());
//            return inBizRespond;
//        }


        //根据省编码映射出可办套餐要的策略库编码（流量包的）
        String llbStrategyId = inInterfaceService.getStrategyIdByProvinceCode(provinceCode, BusinessTypeEnum.llb);
        if (StringUtils.isBlank(llbStrategyId)) {
            //异常
            log.info("查询策略库编码异常" + provinceCode);
            inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            return inBizRespond;
        }

        //调用号卡中心查询 可办套餐(流量包的)
        UserProductRecommendAo llbUserProductRecommendAo = new UserProductRecommendAo();
        llbUserProductRecommendAo.setUserPhone(outBizRequestBaseofferGetData.getMobile());
        llbUserProductRecommendAo.setStrategyId(llbStrategyId);
        log.info("联通获取套餐触发的可办单查询流量包");
        CardCenterResponseVo<List<CardCenterResponseVoGetPackListData>> llbPackList = inInterfaceService.getPackList(llbUserProductRecommendAo, cardCenterInterfInfo);
        if (null == llbPackList && null == clsdPackList) {
            //异常
            log.info("请求号卡系统可升档套餐(流量包的)查询接口异常");
            inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            return inBizRespond;
        }

        if (!llbPackList.getCode().equals("10000") && !clsdPackList.getCode().equals("10000")) {
            //没可用套餐
            log.info("请求号卡系统可升档套餐(流量包的)查询接口失败");
            inBizRespond.setCode(clsdPackList.getCode());
            inBizRespond.setMessage(clsdPackList.getMsg());
            return inBizRespond;
        }

        //调用pot获取政策列表
        String outPortNo = cardCenterInterfInfo.getOutPortNo();
        String flowOutPortNo = cardCenterInterfInfo.getFlowOutPortNo();
        ApiInBizResponse<PolicyListResponseBizContent> clsdPolicyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo, outPortNo, false);
        if (null == clsdPolicyListResponse || !"10000".equals(clsdPolicyListResponse.getCode())) {
            //异常
            log.info("请求pot政策查询接口异常" + outPortNo);
            inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            return inBizRespond;
        }

        ApiInBizResponse<PolicyListResponseBizContent> llbPolicyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo, flowOutPortNo, false);
        if (null == llbPolicyListResponse || !"10000".equals(llbPolicyListResponse.getCode())) {
            //异常
            log.info("请求pot政策查询接口异常" + flowOutPortNo);
            inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            return inBizRespond;
        }

        //号卡中心套餐列表与pot政策列表比较
        List<BaseofferGetResponseData> list = plicyListAndPickListDispose(clsdPolicyListResponse, clsdPackList, llbPolicyListResponse, llbPackList, provinceCode);
        inBizRespond.setT(list);
        inBizRespond.setCode("10000");
        inBizRespond.setMessage("请求成功");
//        if("10000".equals(inBizRespond.getCode())){
//            //10000代表可办理
//            inBizRespond.setMessage("请求成功");
//        }

        return inBizRespond;
    }

    /**
     * 中台可升档套餐查询接口
     *
     * @param outBizRequestBaseofferGetData
     */
    public InBizRespond<List<BaseofferGetResponseData>> middleSystemBaseofferGet(OutBizRequestBaseofferGetData outBizRequestBaseofferGetData,
                                                                                 OperatorEnum operatorByMobile) {

        InBizRespond<List<BaseofferGetResponseData>> inBizRespond = new InBizRespond();

        //根据运营商编码获取clientId
        CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(operatorByMobile.getCode());

        //三户校验
        log.info("广东移动套餐查询触发的三户校验");
        CardCenterResponseVo threeAccountVerifyDataCardCenterResponseVo = inInterfaceService.threeAccountVerifyMiddleSystem(outBizRequestBaseofferGetData.getMobile(), cardCenterInterfInfo, OperatorEnum.lpgdcmcc.getCode());

        if (null == threeAccountVerifyDataCardCenterResponseVo) {
            //异常
            log.info("请求号卡系统异常");
            inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            return inBizRespond;
        }

        if (!"10000".equals(threeAccountVerifyDataCardCenterResponseVo.getCode())) {
            //三户校验不过
            log.info("请求号卡系统失败");
            inBizRespond.setMessage("三户校验不通过");
            inBizRespond.setCode(ProcessStatesEnum.DISSATISFY_BUSINESS_HANDLING.getCode());
            return inBizRespond;
        }

        //调用运营商中心查询 可办套餐
        log.info("广东移动获取套餐触发的可办单查询");
        CardCenterResponseVo<GetPackListMiddleSystemResponseData> packList = inInterfaceService.getPackListMiddleSystem(outBizRequestBaseofferGetData.getMobile(), cardCenterInterfInfo, operatorByMobile.getCode());

        if (null == packList) {
            //异常
            log.info("请求中台系统可升档套餐查询接口异常");
            inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            return inBizRespond;
        }

        if (!packList.getCode().equals("10000")) {
            //没可用套餐
            log.info("请求中台系统可升档套餐查询接口失败");
            inBizRespond.setCode(packList.getCode());
            inBizRespond.setMessage(packList.getMsg());
            return inBizRespond;
        }

        //如果不是珍贵的则从响应拿
        GetPackListMiddleSystemResponseData data = packList.getData();
        if (null == data) {
            //没可用套餐
            log.info("无可用套餐");
            inBizRespond.setCode(ResponseStatusEnum.CLIENT_QUERY_EMPTY.getCode());
            inBizRespond.setMessage(ResponseStatusEnum.CLIENT_QUERY_EMPTY.getMsg());
            return inBizRespond;
        }
        String outPortNo = data.getOutProtNo();

        //调用pot获取政策列表
        ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo, outPortNo, false);
        if (null == policyListResponse) {
            //异常
            log.info("请求pot政策查询接口异常1");
            inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            return inBizRespond;
        }

        if (!"10000".equals(policyListResponse.getCode())) {
            //异常
            log.info("请求pot政策查询接口失败");
            inBizRespond.setMessage(policyListResponse.getMessage());
            inBizRespond.setCode(policyListResponse.getCode());
            return inBizRespond;
        }

        CardCenterResponseVo<List<CardCenterResponseVoGetPackListData>> packList2 = new CardCenterResponseVo<>();
        List<PolicyListResponseBizContentPolicy> policyListResponseBizContentPolicyList = policyListResponse.getBizContent().getSpecList();

        //把政策全部怼进去
        List<CardCenterResponseVoGetPackListData> listData = new ArrayList<>();
        for (PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy : policyListResponseBizContentPolicyList) {
            if (policyListResponseBizContentPolicy.getLevel().intValue() == 2 && policyListResponseBizContentPolicy.getCheckFlag().equals("1") &&
                    policyListResponseBizContentPolicy.getPolicySpecStatus().intValue() == 1) {
                CardCenterResponseVoGetPackListData cardCenterResponseVoGetPackListData = new CardCenterResponseVoGetPackListData();
                cardCenterResponseVoGetPackListData.setAlowRange(policyListResponseBizContentPolicy.getKeyStr());
                listData.add(cardCenterResponseVoGetPackListData);
            }
        }

        packList2.setData(listData);
        //中台系统套餐列表与pot政策列表比较
        List<BaseofferGetResponseData> list = this.plicyListAndPickListDispose(policyListResponse, packList2, null, null, null);
        inBizRespond.setT(list);
        inBizRespond.setCode(packList.getCode());
        inBizRespond.setMessage(packList.getMsg());
        if ("10000".equals(packList.getCode())) {
            //10000代表可办理
            inBizRespond.setMessage("请求成功");
        }
        return inBizRespond;
    }

    /**
     * 通知苏宁订购状态
     */
    @Async
    public String notifySuningOrderStatus(String outTradeNo, String processStates, String desc) {
        log.info("开始通知苏宁订单订购结果{}{}{}", outTradeNo, processStates, desc);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String resutl = null;

        long startTime = System.currentTimeMillis();
        String appkey = sysConfigService.getByCode("appkey");
        String suningNotifyUrl = sysConfigService.getByCode("suningNotifyUrl");
        PromotionAccountInfo promotionAccountInfo = promotionAccountInfoService.getByAppId(appkey);
        String platFormPublicKey = promotionAccountInfo.getPlatformPublicKey();
        TreeMap<String, String> params = new TreeMap<>();

        //公共参数
        params.put("app_key", appkey);
        params.put("target_appkey", appkey);
        params.put("timestamp", sdf.format(new Date()));
        params.put("format", promotionAccountInfo.getFormat());
        params.put("sign_method", promotionAccountInfo.getSignType());

        //业务参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("method", "gift.gzlplink.opentrade.order.status.notify");
        jsonObject.put("result_code", processStates);
        jsonObject.put("desc", desc);
        jsonObject.put("out_trade_no", outTradeNo);
        try {
            //参数加签
            String jsonStr = SignUtil.strConvertBase(jsonObject.toJSONString());
            params.put("data", jsonStr);
            log.info("苏宁异步通知参数加签前：{}", JSONObject.toJSONString(params));
            String mySign = SignUtil.signTopRequest(params, platFormPublicKey, promotionAccountInfo.getSignType());
            params.put("sign", mySign);
            log.info("苏宁异步通知请求参数：{}", JSONObject.toJSONString(params));
            resutl = HttpUtil.hostPost(JSONObject.toJSONString(params), suningNotifyUrl, "utf-8", 60 * 1000);
            log.info("苏宁异步通知响应参数：{}", resutl);
        } catch (Exception e) {
            log.info("苏宁订单状态异步通知异常:", e);
            chatbotSendUtil.sendMsg("苏宁订单状态异步通知异常<<<<<<<<<<<<==>>>>>>>>>>>>>" + e);
        }
        long endTime = System.currentTimeMillis();
        log.info("苏宁订单状态异步通知耗时：{}ms", (endTime - startTime));
        return resutl;
    }


    /**
     * 记录手机办单
     * @param mobile
     * @return
     */
    public void generateMobileAndOrder(String mobile){
        redisUtil.set(SysConstant.HAS_ORDER_MOBILE + mobile, mobile, SysConstant.HAS_ORDER_MOBILE_VERIFY_TIME);
    }

    /**
     *
     * @param alowRangeByProductId
     * @return 返回null则正常
     */
    public String verifyAlowRange(JSONObject alowRangeByProductId){

        if(StringUtils.isBlank(alowRangeByProductId.getString("alowRange"))){
            //异常
            return "政策配置异常找不到运营商套餐产品值";
        }

        if(null == alowRangeByProductId.getInteger("fqNum")){
            return "政策配置异常找不到分期数";
        }

        if(StringUtils.isBlank(alowRangeByProductId.getString("contractId"))){
            return "政策配置异常找不到合约id";
        }

        if(StringUtils.isBlank(alowRangeByProductId.getString("name"))){
            return "政策配置异常找不到订单标题";
        }

        if(StringUtils.isBlank(alowRangeByProductId.getString("policyNo"))){
            return "政策配置异常找不到policyNo";
        }

        if(StringUtils.isBlank(alowRangeByProductId.getString("outAgencyNo"))){
            return "政策配置异常找不到outAgencyNo";
        }

        if(null == alowRangeByProductId.getBigDecimal("totalFee")){
            return "政策配置异常找不到totalFee";
        }
        return null;
    }

    public static String checkOrderCreateParam(OutBizRequestCreateOrder outBizRequestCreateOrder){
        String outTradeNo = outBizRequestCreateOrder.getOut_trade_no();
        String name = outBizRequestCreateOrder.getName();
        String operator = outBizRequestCreateOrder.getOperator();
        String phone = outBizRequestCreateOrder.getPhone();
        String contractId = outBizRequestCreateOrder.getContract_id();
        String status = outBizRequestCreateOrder.getStatus();
        String totalFee = outBizRequestCreateOrder.getTotal_fee();
        String transferId = outBizRequestCreateOrder.getTransfer_id();
        String userId = outBizRequestCreateOrder.getUser_id();
        String outOrderNo = outBizRequestCreateOrder.getOut_order_no();
        String outRequestNo = outBizRequestCreateOrder.getOut_request_no();
        String productId = outBizRequestCreateOrder.getProduct_id();
        String authNo = outBizRequestCreateOrder.getAuth_no();
        String operationId = outBizRequestCreateOrder.getOperation_id();
        if(StringUtils.isEmpty(outTradeNo)){
            return "对接方订单号";
        }
        if(StringUtils.isEmpty(name)){
            return "订单名称";
        }
        if(StringUtils.isEmpty(operator)){
            return "运营商名称";
        }
        if(StringUtils.isEmpty(phone)){
            return "手机号码";
        }
        if(StringUtils.isEmpty(status)){
            return "订单状态";
        }
        if(StringUtils.isEmpty(totalFee)){
            return "充值金额";
        }
        if(StringUtils.isEmpty(transferId)){
            return "请求流水号";
        }
        if(StringUtils.isEmpty(userId)){
            return "付款方支付宝userId";
        }
        if(StringUtils.isEmpty(productId)){
            return "对接方产品id";
        }
        if(StringUtils.isEmpty(authNo)){
            return "预授权冻结编号";
        }
        if(StringUtils.isEmpty(operationId)){
            return "支付宝的授权资金操作流水号";
        }
        if(StringUtils.isEmpty(outOrderNo)){
            return "商户授权资金订单号";
        }
        if(StringUtils.isEmpty(outRequestNo)){
            return "商户本次资金操作的请求流水号";
        }
        if(StringUtils.isEmpty(contractId)){
            return "合约id";
        }
        return null;
    }
}
