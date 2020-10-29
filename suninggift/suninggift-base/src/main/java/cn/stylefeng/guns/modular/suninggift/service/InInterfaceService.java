package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.enums.BusinessTypeEnum;
import cn.stylefeng.guns.modular.suninggift.enums.DeployNotifyEnum;
import cn.stylefeng.guns.modular.suninggift.enums.ProcessStatesEnum;
import cn.stylefeng.guns.modular.suninggift.enums.ProfilesEnum;
import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.ApiInBizRequest;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.CheckUserPhoneAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.CommOrderActivityChgAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.CommOrderActivityChgMiddleSysttemAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.DebitCreateAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.DebitSubscribeAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.DeployBizRequestVo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.OrderDetailsQueryAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.PolicyListRequestBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.QueryAgencyOrStoreRequestBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.QueryAgencygathRequestBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.QueryCarrierAccountInfoRequestBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.QueryCarrierInfoRequestBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.QueryProductPolicyDetailRequestBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.RspData;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.UserProductRecommendAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ApiInBizResponse;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ApiInBizResponseMsg;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.CardCenterResponseVo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.CardCenterResponseVoGetPackListData;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.CommOrderActivityChgMiddleSystemRspAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.CommOrderActivityChgRspAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.DeployOrderPayUnfreezeRefundAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.GetPackListMiddleSystemResponseData;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.OrderDetailsQueryRspAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.OrderDetailsQueryRspMiddleSystemAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.PolicyListResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.QueryAgencyOrStoreResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.QueryAgencygathResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.QueryCarrierAccountInfoResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.QueryCarrierInfoResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.QueryProductPolicyDetailResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ThreeAccountVerifyData;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ThreeAccountVerifyMiddleSystemData;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OrderUmicomUnfrzBillVo;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.SendEquityVo;
import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.modular.suninggift.model.constant.DeployInterfInfo;
import cn.stylefeng.guns.modular.suninggift.model.constant.ProvinceAndStrategyId;
import cn.stylefeng.guns.modular.suninggift.model.constant.SysConstant;
import cn.stylefeng.guns.modular.suninggift.utils.CmpayUtil;
import cn.stylefeng.guns.modular.suninggift.utils.DESUtil;
import cn.stylefeng.guns.modular.suninggift.utils.DateUtil;
import cn.stylefeng.guns.modular.suninggift.utils.HttpClientUtil;
import cn.stylefeng.guns.modular.suninggift.utils.LinoHttpUtil;
import cn.stylefeng.guns.modular.suninggift.utils.MD5Util;
import cn.stylefeng.guns.modular.suninggift.utils.RSAEncrypt;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import cn.stylefeng.guns.sys.modular.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gzlplink.cloud.messagesend.feignClient.MessageSendClient;
import com.gzlplink.cloud.messagesend.util.JsonResult;
import com.gzlplink.cloud.messagesend.vo.OutBizRequest;
import com.gzlplink.cloud.messagesend.vo.OutBizResponse;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 内部服务请求类
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-02-25 14:22
 */
@Slf4j
@Service
public class InInterfaceService {

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private MessageSendClient messageSendClient;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${profile}")
    private String profile;

    DateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String VERIFYCODEMETHOD = "tm.invest.message";//发送短信接口方法名

    int TIME_OUT = 1000 * 5;//接口超时时间单位：毫秒

    int DB_TIME_OUT = 1000 * 10;//调拨创建接口超时时间单位：毫秒

    int GD_CMCC_CREATE_TIME_OUT = 1000 * 10;//广东移动创建订单接口超时时间单位：毫秒

    int GD_CMCC_QUERY_TIME_OUT = 1000 * 6;//广东移动查询订单接口超时时间单位：毫秒

    /**
     * 号卡中心用户可办套餐查询
     * @param userProductRecommendAo 用户手机号与策略便秘吗
     */
    public CardCenterResponseVo<List<CardCenterResponseVoGetPackListData>> getPackList(UserProductRecommendAo userProductRecommendAo, CardCenterInterfInfo cardCenterInterfInfo) {
        long startTime = System.currentTimeMillis();
        CardCenterResponseVo<List<CardCenterResponseVoGetPackListData>> cardCenterResponseVo = null;
        String responseString = null;
        String clientId = cardCenterInterfInfo.getClientId();
        String cardCenterUrl = cardCenterInterfInfo.getHaokaUrl();
      /*  if(profile.equals(ProfilesEnum.local.getCode()) || profile.equals(ProfilesEnum.dev.getCode())){
            //本地或侧测试调用生产的
            clientId = "2020031711280001";
            cardCenterUrl = "https://www.gzlplink.com/uniline";
        }*/


        String clientKey = cardCenterInterfInfo.getClientKey();

        String method = "/lpuf/gzlp.unicom.user.product.recommend";
        String repString = cardCenterUrl + method;

        try {

            String reqJson = JSONObject.toJSONString(userProductRecommendAo);
            String json = CmpayUtil.encryptAgReqData(clientId, clientKey, reqJson);

            log.info("号卡中心套餐接口请求ur==" + repString);
            log.info("号卡中心套餐接口请求参数解密==" + reqJson);
            log.info("号卡中心套餐接口请求参数==" + json);
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", TIME_OUT);
            log.info("号卡中心套餐接口响应参数==" + JSONObject.toJSONString(httpPostRsp));
            if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                responseString = String.valueOf(httpPostRsp.get("msg"));
                JSONObject parseObject = JSON.parseObject(responseString);
                String rspData = parseObject.getString("rspData");
                String decryptAgRespData = CmpayUtil.decryptAgRespData(clientKey, rspData);
                log.info("号卡中心套餐接口响应解密>>>" + decryptAgRespData);
                cardCenterResponseVo = JSONObject.parseObject(decryptAgRespData,new TypeReference<CardCenterResponseVo<List<CardCenterResponseVoGetPackListData>>>(){});
            }else {
                log.info("请求异常");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("异常",e);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常",e);
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("号卡中心套餐接口耗时 {} 毫秒",(endTime-startTime));
        return cardCenterResponseVo;
    }

    /**
     * 中台系统用户可办套餐查询
     * @param userPhone 用户手机号
     */
    public CardCenterResponseVo<GetPackListMiddleSystemResponseData> getPackListMiddleSystem(String userPhone, CardCenterInterfInfo cardCenterInterfInfo, String operatorCode) {
        long startTime = System.currentTimeMillis();
        CardCenterResponseVo<GetPackListMiddleSystemResponseData> cardCenterResponseVo = null;
        String responseString = null;
        String cardCenterUrl = cardCenterInterfInfo.getHaokaUrl();
        String clientKey = cardCenterInterfInfo.getClientKey();

        String method = "/lpcenter/gzlp.lpcenter.stockuser.policyquery";
        String  repString = cardCenterUrl + method;

        try {
            UserProductRecommendAo checkUserPhone = new UserProductRecommendAo();
            checkUserPhone.setUserPhone(userPhone);
            checkUserPhone.setClientId(cardCenterInterfInfo.getClientId());
            checkUserPhone.setOperator(operatorCode);
            String reqJson = JSONObject.toJSONString(checkUserPhone);
            String json = CmpayUtil.encryptAgReqData(cardCenterInterfInfo.getClientId(), clientKey, reqJson);

            log.info("中台系统套餐接口请求ur==" + repString);
            log.info("中台系统套餐接口请求参数解密==" + reqJson);
            log.info("中台系统套餐接口请求参数==" + json);
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", TIME_OUT);
            log.info("中台系统套餐接口响应参数==" + JSONObject.toJSONString(httpPostRsp));
            if(null != httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                responseString = String.valueOf(httpPostRsp.get("msg"));
                JSONObject parseObject = JSON.parseObject(responseString);
                String rspData = parseObject.getString("rspData");
                String decryptAgRespData = CmpayUtil.decryptAgRespData(clientKey, rspData);
                log.info("中台系统套餐接口响应解密>>>" + decryptAgRespData);
                cardCenterResponseVo = JSONObject.parseObject(decryptAgRespData,new TypeReference<CardCenterResponseVo<GetPackListMiddleSystemResponseData>>(){});
            }else {
                log.info("请求异常");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("异常",e);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常",e);
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("中台系统可办单套餐接口耗时 {} 毫秒",(endTime-startTime));
        return cardCenterResponseVo;
    }

    /**
     * 号卡中心三户校验接口
     * @param userPhone 手机号
     */
    public CardCenterResponseVo<ThreeAccountVerifyData> threeAccountVerify(String userPhone, CardCenterInterfInfo cardCenterInterfInfo) {
        long startTime = System.currentTimeMillis();
        CardCenterResponseVo<ThreeAccountVerifyData> threeAccountVerifyData = null;
        String responseString = null;
        try {
            CheckUserPhoneAo checkUserPhone = new CheckUserPhoneAo();
            checkUserPhone.setUserPhone(userPhone);
            String cardCenterUrl = cardCenterInterfInfo.getHaokaUrl();
            String clientKey = cardCenterInterfInfo.getClientKey();
            String clientId = cardCenterInterfInfo.getClientId();

        /*    if(profile.equals(ProfilesEnum.dev.getCode()) || profile.equals(ProfilesEnum.local.getCode())){
                //测试环境或本地联通调生产接口
                cardCenterUrl = "https://www.gzlplink.com/uniline";
                clientId = "2020031711280001";
            }*/

            String method = "/lpuf/gzlp.unicom.check.phone";
            String  repString = cardCenterUrl + method;

            String reqJson = JSONObject.toJSONString(checkUserPhone);
            String json = CmpayUtil.encryptAgReqData(clientId, clientKey, reqJson);
            log.info("号卡中心三户校验接口请求url==" + repString);
            log.info("号卡中心三户校验接口请求参数解密==" + reqJson);
            log.info("号卡中心三户校验接口请求参数==" + json);
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", TIME_OUT);
            log.info("号卡中心三户校验接口响应参数==" + JSONObject.toJSONString(httpPostRsp));
            if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                responseString = String.valueOf(httpPostRsp.get("msg"));
                JSONObject parseObject = JSON.parseObject(responseString);
                String rspData = parseObject.getString("rspData");
                log.info(responseString);
                String decryptAgRespData = CmpayUtil.decryptAgRespData(clientKey, rspData);
                log.info("号卡中心三户校验接口响应解密==" + decryptAgRespData);
                threeAccountVerifyData = JSONObject.parseObject(decryptAgRespData,new TypeReference<CardCenterResponseVo<ThreeAccountVerifyData>>(){});
            }else {
                log.error("请求异常");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("异常",e);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常",e);
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("号卡中心三户校验接口耗时 {} 毫秒", (endTime-startTime));
        return threeAccountVerifyData;
    }

    /**
     * 中台系统三户校验接口
     * @param userPhone 手机号
     */
    public CardCenterResponseVo<ThreeAccountVerifyMiddleSystemData> threeAccountVerifyMiddleSystem(String userPhone, CardCenterInterfInfo cardCenterInterfInfo, String operatorCode) {
        long startTime = System.currentTimeMillis();
        CardCenterResponseVo<ThreeAccountVerifyMiddleSystemData> threeAccountVerifyData = null;
        String responseString = null;
        try {
            CheckUserPhoneAo checkUserPhone = new CheckUserPhoneAo();
            checkUserPhone.setUserPhone(userPhone);
            checkUserPhone.setClientId(cardCenterInterfInfo.getClientId());
            checkUserPhone.setOperator(operatorCode);
            String cardCenterUrl = cardCenterInterfInfo.getHaokaUrl();
            String clientKey = cardCenterInterfInfo.getClientKey();

            String method = "/lpcenter/gzlp.lpcenter.stockuser.queryphone";
            String  repString = cardCenterUrl + method;

            String reqJson = JSONObject.toJSONString(checkUserPhone);
            String json = CmpayUtil.encryptAgReqData(cardCenterInterfInfo.getClientId(), clientKey, reqJson);
            log.info("中台系统三户校验接口请求url==" + repString);
            log.info("中台系统三户校验接口请求参数解密==" + reqJson);
            log.info("中台系统三户校验接口请求参数==" + json);
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", TIME_OUT);
            log.info("中台系统三户校验接口响应参数==" + JSONObject.toJSONString(httpPostRsp));
            if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                responseString = String.valueOf(httpPostRsp.get("msg"));
                JSONObject parseObject = JSON.parseObject(responseString);
                String rspData = parseObject.getString("rspData");
                log.info(responseString);
                String decryptAgRespData = CmpayUtil.decryptAgRespData(clientKey, rspData);
                log.info("中台系统三户校验接口响应解密==" + decryptAgRespData);
                threeAccountVerifyData = JSONObject.parseObject(decryptAgRespData,new TypeReference<CardCenterResponseVo<ThreeAccountVerifyMiddleSystemData>>(){});
            }else {
                log.error("请求异常");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("异常",e);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常",e);
        }
        long endTime = System.currentTimeMillis();
        log.info("中台系统三户校验接口耗时 {} 毫秒", (endTime-startTime));
        return threeAccountVerifyData;
    }

    /**
     * 政策系统政策查询
     * @param cardCenterInterfInfo
     * @param outPortNo
     * @return
     */
    public ApiInBizResponse<PolicyListResponseBizContent> queryPolicyList(CardCenterInterfInfo cardCenterInterfInfo, String outPortNo, boolean constraintFlush){
        long startTime = System.currentTimeMillis();

        String url = cardCenterInterfInfo.getPolicyUrl() + "/gzlp/unicom/payment";
        String desKey = sysConfigService.getByCode("systemDesKey");

        String flowNo = "flowNo" + System.currentTimeMillis();
        String method = "query.policyInfo";
        String time = yyyyMMddHHmmss.format(new Date());
        String sign = DESUtil.encryptDES(flowNo + method + time,desKey);
        PolicyListRequestBizContent bizContent = new PolicyListRequestBizContent(outPortNo);

        String cacheName = SysConstant.QUERY_POLICY_LIST + "-" + bizContent.getOutPortNo();
        String bizContentKey = SysConstant.QUERY_POLICY_LIST_MAP + "-" + bizContent.getOutPortNo();

        ApiInBizRequest<PolicyListRequestBizContent> policyListRequest = new ApiInBizRequest(flowNo, method, time,
                sign, bizContent);
        String s = JSONObject.toJSONString(policyListRequest);
        ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = null;
        try{
            //收集缓存过的outProtNo
            Map<String, CardCenterInterfInfo> a = null;
            if(null != redisUtil.get(SysConstant.QUERY_POLICY_LIST_MAP)){
                a = (Map) redisUtil.get(SysConstant.QUERY_POLICY_LIST_MAP);
            }else{
                a = new HashMap<>();
            }

            if(!a.containsKey(bizContentKey)){
                a.put(bizContentKey, cardCenterInterfInfo);
                redisUtil.set(SysConstant.QUERY_POLICY_LIST_MAP, a, -1);
            }

            log.info("政策查询url====>" + url);
            log.info("政策查询请求参数：" + s);
            String s1 = null;
            Object o = redisUtil.get(cacheName);
            if(!constraintFlush && null != o){
                s1 = o.toString();
            }else{
                log.info(cacheName + "缓存无数据，从接口获取,是否属于强制刷新:" + constraintFlush);
                s1 = HttpClientUtil.doPostIgnoreCert(url, s, "utf-8");
                redisUtil.set(cacheName, s1, SysConstant.QUERY_DATA_VALID_TIME);
            }
            log.info("政策查询响应参数====>" + s1);
            s1 = s1.replace("\\","").replace("\"bizContent\":\"","\"bizContent\":").replace("\",\"code\"",",\"code\"");
            policyListResponse = JSONObject.parseObject(s1, new TypeReference<ApiInBizResponse<PolicyListResponseBizContent>>(){});

            //验签
            String respFlowNo = policyListResponse.getFlowNo();
            String respTime = policyListResponse.getTime();
            String respSign = policyListResponse.getSign();
            String s3 = DESUtil.decryptDES(respSign, desKey);
            log.info("s3=="+s3);
            if(!s3.equals(respFlowNo+respTime)){
                policyListResponse.setMessage("验签不通过");
                policyListResponse.setCode("30001");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("系统错误",e);
            policyListResponse.setMessage("支付前商家收款账号处理异常");
            policyListResponse.setCode("50001");
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("政策查询接口耗时 {} 毫秒", (endTime-startTime));
        return policyListResponse;
    }

    /**
     * 获取商户信息/门店信息查询
     * @return
     * @return AgencyReceiveMoneyResponse code10000 ！= 都是不允许交易的
     */
    public ApiInBizResponse<QueryAgencyOrStoreResponseBizContent> queryAgencyOrStore(CardCenterInterfInfo cardCenterInterfInfo, boolean constraintFlush){
        long startTime = System.currentTimeMillis();
        ApiInBizResponse<QueryAgencyOrStoreResponseBizContent> queryAgencyOrStoreResponse = null;
        //获取接口所需参数
        String outAgencyNo = cardCenterInterfInfo.getOutAgencyNo();
        String merchantId = cardCenterInterfInfo.getMerchantId();
        String desKey = sysConfigService.getByCode("systemDesKey");
        String url = cardCenterInterfInfo.getPolicyUrl() + "/gzlp/unicom/payment";

        ApiInBizRequest<QueryAgencyOrStoreRequestBizContent> queryAgencyOrStoreRequest = new ApiInBizRequest<>();
        queryAgencyOrStoreRequest.setFlowNo("flowNo" + System.currentTimeMillis());
        queryAgencyOrStoreRequest.setTime(yyyyMMddHHmmss.format(new Date()));
        queryAgencyOrStoreRequest.setMethod("pcredit.query.agency.or.store");
        QueryAgencyOrStoreRequestBizContent bizContent = new QueryAgencyOrStoreRequestBizContent();
        bizContent.setOutAgencyNo(outAgencyNo);
        bizContent.setQueryType("all");//默认商家门店一起查
        bizContent.setMerchantId(merchantId);
        String cacheName = SysConstant.QUERY_AGENCY_OR_STORE +bizContent.getMerchantId()+bizContent.getOutAgencyNo()+bizContent.getQueryType();
        String bizContentKey = SysConstant.QUERY_AGENCY_OR_STORE_MAP+bizContent.getMerchantId()+bizContent.getOutAgencyNo()+bizContent.getQueryType();

        String jsonString = JSONObject.toJSONString(bizContent);
        queryAgencyOrStoreRequest.setBizContent(bizContent);

        //收集缓存过的QUERY_AGENCY_OR_STORE_MAP
        Map<String, CardCenterInterfInfo> a = null;
        if(null != redisUtil.get(SysConstant.QUERY_AGENCY_OR_STORE_MAP)){
            a = (Map) redisUtil.get(SysConstant.QUERY_AGENCY_OR_STORE_MAP);
        }else{
            a = new HashMap<>();
        }
        if(!a.containsKey(bizContentKey)){
            a.put(bizContentKey, cardCenterInterfInfo);
            redisUtil.set(SysConstant.QUERY_AGENCY_OR_STORE_MAP, a, -1);
        }

        log.info("商户信息/门店信息查询请求url===》" + url);
        log.info("商户信息/门店信息查询请求前报文===》"+jsonString);
        String sign = null;
        try {
            sign = DESUtil.encryptDES(queryAgencyOrStoreRequest.getFlowNo()+queryAgencyOrStoreRequest.getMethod()+queryAgencyOrStoreRequest.getTime(), desKey);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("商户信息/门店信息查询签名错误=============" + e);
        }
        queryAgencyOrStoreRequest.setSign(sign);
        log.info("商户信息/门店信息查询请求报文===" + JSONObject.toJSONString(queryAgencyOrStoreRequest));
        // post请求
        String result = null;
        try {
            Object o = redisUtil.get(cacheName);
            if(!constraintFlush && null != o){
                result = o.toString();
            }else{
                log.info("queryAgencyOrStore缓存无数据,是否属于强制刷新:" + constraintFlush);
                result = HttpClientUtil.doPostIgnoreCert(url,JSONObject.toJSONString(queryAgencyOrStoreRequest), "UTF-8");
                redisUtil.set(cacheName, result, SysConstant.QUERY_DATA_VALID_TIME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常", e);
        }
        log.info("商户信息/门店信息查询<请求结果=================>" + result);
//        result = result.replace("\\","").replace("\"bizContent\":\"","\"bizContent\":").replace("\",\"code\"",",\"code\"");

        // 2020年5月13日 18点28分 zba 优化json转换
        result = result.replace("\\", "").replace("\"{","{").replace(
                "}\"","}").replace("\"[","[").replace(
                "]\"","]");

        queryAgencyOrStoreResponse = JSONObject.parseObject(result, new TypeReference<ApiInBizResponse<QueryAgencyOrStoreResponseBizContent>>(){});
        //响应验签
        JSONObject json = JSONObject.parseObject(result);
        String signStr = queryAgencyOrStoreResponse.getSign();
        String flowNo = queryAgencyOrStoreResponse.getFlowNo();
        String time = queryAgencyOrStoreResponse.getTime();
        String sourceData = flowNo+time;
        String decryptDES = DESUtil.decryptDES(signStr, desKey);
        if(!decryptDES.equals(sourceData)) {
            log.info("验签不过");
            return null;
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("商户信息/门店信息查询接口耗时 {} 毫秒", (endTime-startTime));
        return queryAgencyOrStoreResponse;
    }

    /**
     * 商户网商垫资垫付账户查询
     * @return
     */
    public ApiInBizResponse<QueryAgencygathResponseBizContent> queryAgencygath(CardCenterInterfInfo cardCenterInterfInfo, boolean constraintFlush){
        long startTime = System.currentTimeMillis();
        ApiInBizResponse<QueryAgencygathResponseBizContent> queryAgencyOrStoreResponse = null;

        //获取政策平台接口所需参数
        String outAgencyNo = cardCenterInterfInfo.getOutAgencyNo();
        String merchantId = cardCenterInterfInfo.getMerchantId();
        String gathType = cardCenterInterfInfo.getGathType();
        String desKey = sysConfigService.getByCode("systemDesKey");
        String url = cardCenterInterfInfo.getPolicyUrl() + "/gzlp/unicom/payment";

        QueryAgencygathRequestBizContent bizContent = new QueryAgencygathRequestBizContent();

        bizContent.setOutAgencyNo(outAgencyNo);//商家外部编码
        bizContent.setGathType(gathType);//账号类型:垫资垫付-4、空白模式-5、销售营收-0
        bizContent.setMerchantId(merchantId);//商城id
        String cacheName = SysConstant.QUERY_AGENCYGATH +bizContent.getGathType()+bizContent.getMerchantId()+bizContent.getOutAgencyNo();
        String bizContentKey = SysConstant.QUERY_AGENCYGATH_MAP+bizContent.getGathType()+bizContent.getMerchantId()+bizContent.getOutAgencyNo();

        ApiInBizRequest<QueryAgencygathRequestBizContent> inBizRequest = new ApiInBizRequest<>();
        inBizRequest.setFlowNo("flowNo" + System.currentTimeMillis());
        inBizRequest.setTime(yyyyMMddHHmmss.format(new Date()));
        inBizRequest.setMethod("pcredit.query.agencygath");
        String jsonString = JSONObject.toJSONString(bizContent);
        inBizRequest.setBizContent(bizContent);

        //收集缓存过的QUERY_AGENCYGATH_MAP
        Map<String, CardCenterInterfInfo> a = null;
        if(null != redisUtil.get(SysConstant.QUERY_AGENCYGATH_MAP)){
            a = (Map) redisUtil.get(SysConstant.QUERY_AGENCYGATH_MAP);
        }else{
            a = new HashMap<>();
        }
        if(!a.containsKey(bizContentKey)){
            a.put(bizContentKey, cardCenterInterfInfo);
            redisUtil.set(SysConstant.QUERY_AGENCYGATH_MAP, a, -1);
        }

        log.info("户网商垫资垫付账户查询请求url===》"+url);
        log.info("户网商垫资垫付账户查询请求前报文===》"+jsonString);
        String sign = null;
        try {
            sign = DESUtil.encryptDES(inBizRequest.getFlowNo()+inBizRequest.getMethod()+inBizRequest.getTime(), desKey);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("户网商垫资垫付账户查询签名错误=============");
        }
        inBizRequest.setSign(sign);
        log.info("户网商垫资垫付账户查询请求报文===" + JSONObject.toJSONString(inBizRequest));
        // post请求
        String result = null;
        try {
            Object o = redisUtil.get(cacheName);
            if(!constraintFlush && null != o){
                result = o.toString();
            }else{
                log.info("queryAgencygath缓存无数据,是否属于强制刷新:" + constraintFlush);
                result = HttpClientUtil.doPostIgnoreCert(url,JSONObject.toJSONString(inBizRequest), "UTF-8");
                redisUtil.set(cacheName, result, SysConstant.QUERY_DATA_VALID_TIME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常", e);
        }
        log.info("户网商垫资垫付账户查询<请求结果=================>" + result);
//        result = result.replace("\\","").replace("\"bizContent\":\"","\"bizContent\":").replace("\",\"code\"",",\"code\"");

        // 2020年5月13日 18点28分 zba 优化json转换
        result = result.replace("\\", "").replace("\"{","{").replace(
                "}\"","}").replace("\"[","[").replace(
                "]\"","]");

        queryAgencyOrStoreResponse = JSONObject.parseObject(result, new TypeReference<ApiInBizResponse<QueryAgencygathResponseBizContent>>(){});

        //响应验签
        JSONObject json = JSONObject.parseObject(result);
        String signStr = json.getString("sign");
        String flowNo = json.getString("flowNo");
        String time = json.getString("time");
        String sourceData = flowNo+time;
        String decryptDES = DESUtil.decryptDES(signStr, desKey);
        if(!decryptDES.equals(sourceData)) {
            log.info("验签不过");
        }else{
            log.info("验签通过");
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("户网商垫资垫付账户查询接口耗时 {} 毫秒", (endTime - startTime));
        return queryAgencyOrStoreResponse;
    }

    /**
     * 商户账户查询
     * @return
     */
    public ApiInBizResponse<QueryCarrierAccountInfoResponseBizContent> queryCarrierAccountInfo(CardCenterInterfInfo cardCenterInterfInfo, boolean constraintFlush){
        long startTime = System.currentTimeMillis();
        ApiInBizResponse<QueryCarrierAccountInfoResponseBizContent> queryCarrierAccountInfoResponseBizContentApiInBizResponse = null;
        //获取政策平台接口所需参数
        String accountNo = cardCenterInterfInfo.getAccountNo();

        String desKey = sysConfigService.getByCode("systemDesKey");
        String url = cardCenterInterfInfo.getPolicyUrl() + "/gzlp/unicom/payment";

        QueryCarrierAccountInfoRequestBizContent bizContent = new QueryCarrierAccountInfoRequestBizContent();
        bizContent.setAccountNo(accountNo);

        String cacheName = SysConstant.QUERY_CARRIER_ACCOUNT_INFO+bizContent.getAccountNo();
        String bizContentKey = SysConstant.QUERY_CARRIER_ACCOUNT_INFO_MAP+bizContent.getAccountNo();

        ApiInBizRequest<QueryCarrierAccountInfoRequestBizContent> inBizRequest = new ApiInBizRequest<>();
        inBizRequest.setFlowNo("flowNo" + System.currentTimeMillis());
        inBizRequest.setTime(yyyyMMddHHmmss.format(new Date()));
        inBizRequest.setMethod("pcredit.query.carrier.account");
        String jsonString = JSONObject.toJSONString(bizContent);
        inBizRequest.setBizContent(bizContent);

        //收集缓存过的QUERY_CARRIER_ACCOUNT_INFO_MAP
        Map<String, CardCenterInterfInfo> a = null;
        if(null != redisUtil.get(SysConstant.QUERY_CARRIER_ACCOUNT_INFO_MAP)){
            a = (Map) redisUtil.get(SysConstant.QUERY_CARRIER_ACCOUNT_INFO_MAP);
        }else{
            a = new HashMap<>();
        }
        if(!a.containsKey(bizContentKey)){
            a.put(bizContentKey, cardCenterInterfInfo);
            redisUtil.set(SysConstant.QUERY_CARRIER_ACCOUNT_INFO_MAP, a, -1);
        }

        log.info("商户账户查询url===》"+url);
        log.info("商户账户查询请求前报文===》"+jsonString);
        String sign = null;
        try {
            sign = DESUtil.encryptDES(inBizRequest.getFlowNo()+inBizRequest.getMethod()+inBizRequest.getTime(), desKey);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("商户账户查询签名错误=============");
        }
        inBizRequest.setSign(sign);
        log.info("商户账户查询请求报文===" + JSONObject.toJSONString(inBizRequest));
        // post请求
        String result = null;
        try {
            Object o = redisUtil.get(cacheName);
            if(!constraintFlush && null != o){
                result = o.toString();
            }else{
                log.info("queryCarrierAccountInfo缓存无数据,是否属于强制刷新:" + constraintFlush);
                result = HttpClientUtil.doPostIgnoreCert(url,JSONObject.toJSONString(inBizRequest), "UTF-8");
                redisUtil.set(cacheName, result, SysConstant.QUERY_DATA_VALID_TIME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常", e);
        }
        log.info("商户账户查询<请求结果=================>" + result);
//        result = result.replace("\\","").replace("\"bizContent\":\"","\"bizContent\":").replace("\",\"code\"",",\"code\"");

        // 2020年5月13日 18点28分 zba 优化json转换
        result = result.replace("\\", "").replace("\"{","{").replace(
                "}\"","}").replace("\"[","[").replace(
                "]\"","]");

        queryCarrierAccountInfoResponseBizContentApiInBizResponse = JSONObject.parseObject(result, new TypeReference<ApiInBizResponse<QueryCarrierAccountInfoResponseBizContent>>(){});

        //响应验签
        JSONObject json = JSONObject.parseObject(result);
        String signStr = json.getString("sign");
        String flowNo = json.getString("flowNo");
        String time = json.getString("time");
        String sourceData = flowNo+time;
        String decryptDES = DESUtil.decryptDES(signStr, desKey);
        if(!decryptDES.equals(sourceData)) {
            log.info("验签不过");
        }else{
            log.info("验签通过");
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("商户账户查询接口耗时 {} 毫秒", (endTime - startTime));
        return queryCarrierAccountInfoResponseBizContentApiInBizResponse;
    }

    /**
     * 商户查询
     * @return
     */
    public ApiInBizResponse<QueryCarrierInfoResponseBizContent> queryCarrierInfo(CardCenterInterfInfo cardCenterInterfInfo, boolean constraintFlush){
        long startTime = System.currentTimeMillis();
        ApiInBizResponse<QueryCarrierInfoResponseBizContent> queryCarrierAccountInfoResponseBizContentApiInBizResponse = null;
        //获取政策平台接口所需参数
        String carrierNo = cardCenterInterfInfo.getCarrierNo();
        String desKey = sysConfigService.getByCode("systemDesKey");
        String url = cardCenterInterfInfo.getPolicyUrl() + "/gzlp/unicom/payment";

        QueryCarrierInfoRequestBizContent bizContent = new QueryCarrierInfoRequestBizContent();
        bizContent.setCarrierNo(carrierNo);

        String cacheName = SysConstant.QUERY_CARRIER_INFO+bizContent.getCarrierNo();
        String bizContentKey = SysConstant.QUERY_CARRIER_INFO_MAP + bizContent.getCarrierNo();

        ApiInBizRequest<QueryCarrierInfoRequestBizContent> inBizRequest = new ApiInBizRequest<>();
        inBizRequest.setFlowNo("flowNo" + System.currentTimeMillis());
        inBizRequest.setTime(yyyyMMddHHmmss.format(new Date()));
        inBizRequest.setMethod("pcredit.query.carrier");
        String jsonString = JSONObject.toJSONString(bizContent);
        inBizRequest.setBizContent(bizContent);

        //收集缓存过的QUERY_CARRIER_ACCOUNT_INFO_MAP
        Map<String, CardCenterInterfInfo> a = null;
        if(null != redisUtil.get(SysConstant.QUERY_CARRIER_INFO_MAP)){
            a = (Map) redisUtil.get(SysConstant.QUERY_CARRIER_INFO_MAP);
        }else{
            a = new HashMap<>();
        }
        if(!a.containsKey(bizContentKey)){
            a.put(bizContentKey, cardCenterInterfInfo);
            redisUtil.set(SysConstant.QUERY_CARRIER_INFO_MAP, a, -1);
        }

        log.info("商户查询请求url===》"+url);
        log.info("商户查询请求前报文===》"+jsonString);
        String sign = null;
        try {
            sign = DESUtil.encryptDES(inBizRequest.getFlowNo()+inBizRequest.getMethod()+inBizRequest.getTime(), desKey);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("商户查询签名错误=============");
        }
        inBizRequest.setSign(sign);
        log.info("商户查询请求报文===" + JSONObject.toJSONString(inBizRequest));
        // post请求
        String result = null;
        try {
            Object o = redisUtil.get(cacheName);
            if(!constraintFlush && null != o){
                result = o.toString();
            }else{
                log.info("queryCarrierInfo缓存无数据,是否属于强制刷新:" + constraintFlush);
                result = HttpClientUtil.doPostIgnoreCert(url,JSONObject.toJSONString(inBizRequest), "UTF-8");
                redisUtil.set(cacheName, result, SysConstant.QUERY_DATA_VALID_TIME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常", e);
        }
        log.info("商户查询<请求结果=================>" + result);
//        result = result.replace("\\","").replace("\"bizContent\":\"","\"bizContent\":").replace("\",\"code\"",",\"code\"");

        // 2020年5月13日 18点28分 zba 优化json转换
        result = result.replace("\\", "").replace("\"{","{").replace(
                "}\"","}").replace("\"[","[").replace(
                "]\"","]");

        queryCarrierAccountInfoResponseBizContentApiInBizResponse = JSONObject.parseObject(result, new TypeReference<ApiInBizResponse<QueryCarrierInfoResponseBizContent>>(){});

        //响应验签
        JSONObject json = JSONObject.parseObject(result);
        String signStr = json.getString("sign");
        String flowNo = json.getString("flowNo");
        String time = json.getString("time");
        String sourceData = flowNo+time;
        String decryptDES = DESUtil.decryptDES(signStr, desKey);
        if(!decryptDES.equals(sourceData)) {
            log.info("验签不过");
        }else{
            log.info("验签通过");
        }

        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("商户查询接口耗时 {} 毫秒", (endTime - startTime));
        return queryCarrierAccountInfoResponseBizContentApiInBizResponse;
    }

    /**
     * 查询政策拆分明细
     * @return
     */
    public ApiInBizResponse<QueryProductPolicyDetailResponseBizContent> queryProductPolicyDetail(String policyNo, String psnId, CardCenterInterfInfo cardCenterInterfInfo, boolean constraintFlush){
        long startTime = System.currentTimeMillis();
        ApiInBizResponse<QueryProductPolicyDetailResponseBizContent> queryCarrierAccountInfoResponseBizContentApiInBizResponse = null;
        String desKey = sysConfigService.getByCode("systemDesKey");
        String url = cardCenterInterfInfo.getPolicyUrl() + "/gzlp/unicom/payment";

        QueryProductPolicyDetailRequestBizContent bizContent = new QueryProductPolicyDetailRequestBizContent();
        bizContent.setPolicyNo(policyNo);
        bizContent.setPsnId(psnId);
        String cacheName = SysConstant.QUERY_PRODUCT_POLICY_DETAIL + "-" + bizContent.getPolicyNo() + "-" + bizContent.getPsnId();
        String bizContentKey = SysConstant.QUERY_PRODUCT_POLICY_DETAIL_MAP  + "-" + bizContent.getPolicyNo() + "-" + bizContent.getPsnId();

        ApiInBizRequest<QueryProductPolicyDetailRequestBizContent> inBizRequest = new ApiInBizRequest<>();
        inBizRequest.setFlowNo("flowNo" + System.currentTimeMillis());
        inBizRequest.setTime(yyyyMMddHHmmss.format(new Date()));
        inBizRequest.setMethod("pcredit.query.product.policy.detail");
        String jsonString = JSONObject.toJSONString(bizContent);
        inBizRequest.setBizContent(bizContent);

        //收集缓存过的QUERY_PRODUCT_POLICY_DETAIL_MAP
        Map<String, CardCenterInterfInfo> a = null;
        if(null != redisUtil.get(SysConstant.QUERY_PRODUCT_POLICY_DETAIL_MAP)){
            a = (Map) redisUtil.get(SysConstant.QUERY_PRODUCT_POLICY_DETAIL_MAP);
        }else{
            a = new HashMap<>();
        }
        if(!a.containsKey(bizContentKey)){
            a.put(bizContentKey, cardCenterInterfInfo);
            redisUtil.set(SysConstant.QUERY_PRODUCT_POLICY_DETAIL_MAP, a, -1);
        }

        log.info("查询政策拆分明细请求==》"+url);
        log.info("查询政策拆分明细请求前报文===》"+jsonString);
        String sign = null;
        try {
            sign = DESUtil.encryptDES(inBizRequest.getFlowNo()+inBizRequest.getMethod()+inBizRequest.getTime(), desKey);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("查询政策拆分明细签名错误=============");
        }
        inBizRequest.setSign(sign);
        log.info("查询政策拆分明细请求报文===" + JSONObject.toJSONString(inBizRequest));
        // post请求
        String result = null;
        try {
            Object o = redisUtil.get(cacheName);
            if(!constraintFlush && null != o){
                result = o.toString();
            }else{
                log.info("queryProductPolicyDetail缓存无数据,是否属于强制刷新:" + constraintFlush);
                result = HttpClientUtil.doPostIgnoreCert(url,JSONObject.toJSONString(inBizRequest), "UTF-8");
                redisUtil.set(cacheName, result, SysConstant.QUERY_DATA_VALID_TIME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常", e);
        }
        log.info("查询政策拆分明细<请求结果=================>" + result);
//        result = result.replace("\\","").replace("\"bizContent\":\"","\"bizContent\":").replace("\",\"code\"",",\"code\"");

        // 2020年5月13日 18点28分 zba 优化json转换
        result = result.replace("\\", "").replace("\"{","{").replace(
                "}\"","}").replace("\"[","[").replace(
                "]\"","]");

        queryCarrierAccountInfoResponseBizContentApiInBizResponse = JSONObject.parseObject(result, new TypeReference<ApiInBizResponse<QueryProductPolicyDetailResponseBizContent>>(){});

        //响应验签
        JSONObject json = JSONObject.parseObject(result);
        String signStr = json.getString("sign");
        String flowNo = json.getString("flowNo");
        String time = json.getString("time");
        String sourceData = flowNo+time;
        String decryptDES = DESUtil.decryptDES(signStr, desKey);
        if(!decryptDES.equals(sourceData)) {
            log.info("验签不过");
        }else{
            log.info("验签通过");
        }

        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("查询政策拆分明细查询接口耗时 {} 毫秒", (endTime - startTime));
        return queryCarrierAccountInfoResponseBizContentApiInBizResponse;
    }

    /**
     * 发送短信方法
     * @param mobile 手机号
     * @param vfiCode 验证码
     * @return
     */
    public JsonResult<OutBizResponse> sendVerifyCode(String mobile, String vfiCode){
        long startTime = System.currentTimeMillis();
        //获取发送短信需要公共参数
        String send_message_info = sysConfigService.getByCode("SEND_MESSAGE_INFO");
        JSONObject jsonObject1 = JSONObject.parseObject(send_message_info);
        OutBizRequest vo = new OutBizRequest();
        vo.setOutSysNo(jsonObject1.getString("outSysNo"));
        vo.setFormat(jsonObject1.getString("format"));
        vo.setCharset(jsonObject1.getString("charset"));
        vo.setMethod(VERIFYCODEMETHOD);
        vo.setTimestamp(sdf.format(new Date()));
        vo.setVersion(jsonObject1.getString("version"));
        JSONObject bizContentJSON = new JSONObject();
        JSONObject outContentJSON = new JSONObject();
        bizContentJSON.put("mobile", mobile);
        bizContentJSON.put("vfiCode", vfiCode);


        vo.setCharset(jsonObject1.getString("charset"));
        vo.setSignType(jsonObject1.getString("signType"));
        //封装加签内容
        String signContent = "bizContent=" + JSON.toJSONString(bizContentJSON);
        log.info("<加签内容>" + signContent);
        //加签
        String rsaSign = RSAEncrypt.sign(signContent, jsonObject1.getString("signType"), jsonObject1.getString("privateKey"), jsonObject1.getString("charset"));
        vo.setSign(rsaSign);
        //加密
        JSONObject deCodeBizMap = new JSONObject();
        Set<String> bizContentkey = bizContentJSON.keySet();
        try {
            for (String key : bizContentkey) {
                String keyStr = key;
                String valueStr = RSAEncrypt.encrypt(JSON.toJSONString(bizContentJSON.get(key)), jsonObject1.getString("lpPublicKey"), jsonObject1.getString("charset"));
                deCodeBizMap.put(keyStr, valueStr);
            }
        } catch (Exception e) {
            log.error("加密错误：", e);
        }
        vo.setBizContent(deCodeBizMap);
        vo.setOutContent(outContentJSON);
        log.info("发送短信方法<请求数据>" + JSON.toJSONString(vo));
        JsonResult<OutBizResponse> outBizResponseJsonResult = null;

        if(profile.equals(ProfilesEnum.pressure.getCode())){
            //如果是压测或本地环境不发生短信
            String a = "{\"data\":{\"bizContent\":{\"vfiCode\":\"2truej\",\"mobile\":\"13078857175\"},\"code\":\"10000\",\"msg\":\"OK\",\"sign\":\"VugMApyJl5JYruN7BxWiVn3BSLvkZhjDG+es9jrTq/ixrqCkJ2b4RSE29ErjZnqH0OlvZeaOBkERlKKmC8/mbzVoH6IWU57W5Rces2r1gqbZgXhxKowwH6V810z+tqwKbFYj3qVOmiv5ZIp75Yjp06mQKyWXty78IVBDHxDJATDH0GG3cil5lL6NCPvuYEoVlI3z4bwkDjdPJ9oISy3p5lmBZXgx5IX5uqlpvIDrgHrpOmWMomcQks2YbxehskngkHKFxBwElqkX0oHIuHSdUnjrxanhMx6BuJrIvTlqJDpYhplaV5FFda2BpSYac2HE1r0Y9JxDY17Nu5RdDcvYYg==\",\"signType\":\"RSA2\",\"success\":true},\"msg\":\"OK\",\"status\":200}";
            outBizResponseJsonResult = JSONObject.parseObject(a, JsonResult.class);
        }else{
            outBizResponseJsonResult = messageSendClient.wServiceMessageSend(vo);
        }
        log.info("发送短信方法<请求结果==>" + JSON.toJSONString(outBizResponseJsonResult));
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("发送短信接口耗时 {} 毫秒", (endTime - startTime));
        return outBizResponseJsonResult;
    }

    /**
     * 号卡中心创建订单接口
     * @param commOrderActivityChgAo
     * @return
     */
    public RspData<CommOrderActivityChgRspAo> createOrder(CommOrderActivityChgAo commOrderActivityChgAo,
                                                          CardCenterInterfInfo cardCenterInterfInfo,
                                                          BusinessTypeEnum businessTypeEnum){
        long startTime = System.currentTimeMillis();
        RspData rspDataAo = new RspData();
        try {
            //获取号卡中心接口的信息
            String cardCenterUrl = cardCenterInterfInfo.getHaokaUrl();
            String clientKey = cardCenterInterfInfo.getClientKey();
            String reqJson = JSONObject.toJSONString(commOrderActivityChgAo);
            log.info("号卡中心订单创建接口请求解密>>>" + reqJson);
            String json = CmpayUtil.encryptAgReqData(cardCenterInterfInfo.getClientId(), clientKey, reqJson);
            String method = null;
            if(businessTypeEnum == BusinessTypeEnum.clsd){
                //存量升档
                method = "/lpuf/gzlp.unicom.comm.order.activity.chg";
            }else {
                //流量包
                method = "/lpuf/gzlp.unicom.comm.order.activity.chg.flow";
            }

            String  repString = cardCenterUrl + method;

            log.info("号卡中心订单创建接口url>>>" + repString);
            log.info("号卡中心订单创建接口请求>>>" + JSONObject.toJSONString(json));
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", TIME_OUT);
            log.info("号卡中心订单创建接口响应>>>" + JSONObject.toJSONString(httpPostRsp));
            if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                String responseString = String.valueOf(httpPostRsp.get("msg"));
                JSONObject parseObject = JSON.parseObject(responseString);
                String rspData = parseObject.getString("rspData");
                String decryptAgRespData = CmpayUtil.decryptAgRespData(clientKey, rspData);
                log.info("号卡中心订单创建接口响应解密>>>" + decryptAgRespData);
                rspDataAo = JSONObject.parseObject(decryptAgRespData, new TypeReference<RspData<CommOrderActivityChgRspAo>>(){});
            }else {
                log.info("号卡中心订单创建接口请求异常");
                rspDataAo.setCode("50001");
                rspDataAo.setMsg("号卡中心订单创建接口请求异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常",e);
            rspDataAo.setCode("50002");
            rspDataAo.setMsg("系统异常");
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("号卡中心订单创建接口耗时 {} 毫秒", (endTime - startTime));
        return rspDataAo;
    }

    /**
     * 中台系统创建订单接口
     * @param commOrderActivityChgAo
     * @return
     */
    public RspData<CommOrderActivityChgMiddleSystemRspAo> createOrderMiddleSystem(CommOrderActivityChgMiddleSysttemAo commOrderActivityChgAo, CardCenterInterfInfo cardCenterInterfInfo){
        long startTime = System.currentTimeMillis();
        RspData<CommOrderActivityChgMiddleSystemRspAo> rspDataAo = new RspData();
        try {
            //获取号卡中心接口的信息
            String cardCenterUrl = cardCenterInterfInfo.getHaokaUrl();
            String clientKey = cardCenterInterfInfo.getClientKey();
            String reqJson = JSONObject.toJSONString(commOrderActivityChgAo);
            log.info("中台系统订单创建接口请求解密>>>" + reqJson);
            String json = CmpayUtil.encryptAgReqData(cardCenterInterfInfo.getClientId(), clientKey, reqJson);

            String method = "/lpcenter/gzlp.lpcenter.stockuser.productorder";
            String  repString = cardCenterUrl + method;

            log.info("中台系统订单创建接口url>>>" + repString);
            log.info("中台系统订单创建接口请求>>>" + json);
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", GD_CMCC_CREATE_TIME_OUT);
            log.info("中台系统订单创建接口响应>>>" + JSONObject.toJSONString(httpPostRsp));
            if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                String responseString = String.valueOf(httpPostRsp.get("msg"));
                JSONObject parseObject = JSON.parseObject(responseString);
                String rspData = parseObject.getString("rspData");
                String decryptAgRespData = CmpayUtil.decryptAgRespData(clientKey, rspData);
                log.info("中台系统订单创建接口响应解密>>>" + decryptAgRespData);
                rspDataAo = JSONObject.parseObject(decryptAgRespData, new TypeReference<RspData<CommOrderActivityChgMiddleSystemRspAo>>(){});

            }else {
                log.info("中台系统订单创建接口请求异常");
                rspDataAo.setCode("50001");
                rspDataAo.setMsg("中台系统订单创建接口请求异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常",e);
            rspDataAo.setCode("50002");
            rspDataAo.setMsg("系统异常");
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("中台系统订单创建接口耗时 {} 毫秒", (endTime - startTime));
        return rspDataAo;
    }

    /**
     * 号卡中心订单查询接口
     * @param
     * @return
     */
    public CardCenterResponseVo<OrderDetailsQueryRspAo> queryOrder(OrderDetailsQueryAo orderDetailsQueryAo, CardCenterInterfInfo cardCenterInterfInfo){
        long startTime = System.currentTimeMillis();
        CardCenterResponseVo<OrderDetailsQueryRspAo> rspDataAo = new CardCenterResponseVo<>();
        try {
            String cardCenterUrl = cardCenterInterfInfo.getHaokaUrl();
            String clientKey = cardCenterInterfInfo.getClientKey();

            String reqJson = JSONObject.toJSONString(orderDetailsQueryAo);
            log.info("号卡中心订单查询接口请求解密>>>" + reqJson);
            String json = CmpayUtil.encryptAgReqData(cardCenterInterfInfo.getClientId(), clientKey, reqJson);

            String method = "/lpuf/gzlp.unicom.order.details.query";
            String  repString = cardCenterUrl + method;

            log.info("号卡中心订单查询接口url>>>" + repString);
            log.info("号卡中心订单查询接口请求>>>" + json);
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", TIME_OUT);
            log.info("号卡中心订单查询接口响应>>>" + JSONObject.toJSONString(httpPostRsp));
            if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                String responseString = String.valueOf(httpPostRsp.get("msg"));
                JSONObject parseObject = JSON.parseObject(responseString);
                String rspData = parseObject.getString("rspData");
                String decryptAgRespData = CmpayUtil.decryptAgRespData(clientKey, rspData);
//                decryptAgRespData = "{\"code\":\"31008\",\"msg\":\"订单订购进行中~未查到数据\"}";
                log.info("号卡中心订单查询接口解密响应>>>" + decryptAgRespData);
                rspDataAo = JSONObject.parseObject(decryptAgRespData, new TypeReference<CardCenterResponseVo<OrderDetailsQueryRspAo>>(){});
            }else {
                log.info("号卡中心订单查询接口请求异常");
                rspDataAo.setCode("50001");
                rspDataAo.setMsg("号卡中心订单查询接口请求异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常",e);
            rspDataAo.setCode("50002");
            rspDataAo.setMsg("系统异常");
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("号卡中心订单查询接口耗时 {} 毫秒", (endTime - startTime));
        return rspDataAo;
    }

    /**
     * 中台系统订单查询接口
     * @param
     * @return
     */
    public CardCenterResponseVo<OrderDetailsQueryRspMiddleSystemAo> queryOrderMiddleSystem(OrderDetailsQueryAo orderDetailsQueryAo, CardCenterInterfInfo cardCenterInterfInfo, String operatorCode){
        long startTime = System.currentTimeMillis();
        CardCenterResponseVo<OrderDetailsQueryRspMiddleSystemAo> rspDataAo = new CardCenterResponseVo();
        try {
            String cardCenterUrl = cardCenterInterfInfo.getHaokaUrl();
            String clientKey = cardCenterInterfInfo.getClientKey();

            String reqJson = JSONObject.toJSONString(orderDetailsQueryAo);
            log.info("中台系统订单查询接口解密请求>>>" + reqJson);
            String json = CmpayUtil.encryptAgReqData(cardCenterInterfInfo.getClientId(), clientKey, reqJson);

            String method = "/lpcenter/gzlp.lpcenter.stockuser.queryorderinfo";
            String repString = cardCenterUrl + method;

            log.info("中台系统订单查询接口url>>>" + repString);
            log.info("中台系统订单查询接口请求>>>" + json);
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", GD_CMCC_QUERY_TIME_OUT);
            log.info("中台系统订单查询接口响应>>>" + JSONObject.toJSONString(httpPostRsp));
            if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                String responseString = String.valueOf(httpPostRsp.get("msg"));
                JSONObject parseObject = JSON.parseObject(responseString);
                String rspData = parseObject.getString("rspData");
                String decryptAgRespData = CmpayUtil.decryptAgRespData(clientKey, rspData);
//                decryptAgRespData = "{\"code\":\"31008\",\"msg\":\"订单订购进行中~未查到数据\"}";
                log.info("中台系统订单查询接口解密响应>>>" + decryptAgRespData);
                rspDataAo = JSONObject.parseObject(decryptAgRespData, new TypeReference<CardCenterResponseVo<OrderDetailsQueryRspMiddleSystemAo>>(){});
            }else {
                log.info("中台系统订单查询接口请求异常");
                rspDataAo.setCode("50001");
                rspDataAo.setMsg("中台系统订单查询接口请求异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("系统异常",e);
            rspDataAo.setCode("50002");
            rspDataAo.setMsg("系统异常");
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("中台系统订单查询接口耗时 {} 毫秒", (endTime - startTime));
        return rspDataAo;
    }

    /**
     * 调拨创建
     * @param debitCreateAo
     * @return
     */
    public ApiInBizResponseMsg debitCreate(DebitCreateAo debitCreateAo){
        long startTime = System.currentTimeMillis();
        ApiInBizResponseMsg apiInBizResponseMsg = new ApiInBizResponseMsg();
        DeployInterfInfo deployInfo = JSONObject.parseObject(sysConfigService.getByCode("deployInfo"), DeployInterfInfo.class);
        String fundDeployUrl = deployInfo.getUrl();;
        String version = deployInfo.getVersion();
        String signType = deployInfo.getSignType();
        String innerAuthSecretKey = deployInfo.getInnerAuthSecretKey();
        String deployNotifyUrl = sysConfigService.getByCode("suninggiftUrl") + "/api/debit/callBack";//调拨状态变更接口
        debitCreateAo.setNotifyUrl(deployNotifyUrl);
        //请求地址
        String requestUrl = fundDeployUrl + "/order/insert";
        try {
            JSONObject hashMap = new JSONObject();
            hashMap.put("timestamp", sdf.format(new Date()));
            hashMap.put("method", "order.insert");
            hashMap.put("version", version);
            hashMap.put("signType", signType);

            //根据订单实体，拼装参数
          JSONObject modelObject = JSONObject.parseObject(JSONObject.toJSONString(debitCreateAo));
          //排序
          String md5SignContent = MD5Util.getMD5SignContent(modelObject);
          //加签
          String sign = MD5Util.encodeByMd5(md5SignContent +
              innerAuthSecretKey + String.valueOf(hashMap.get("timestamp")));
          hashMap.put("sign", sign);
          hashMap.put("model", modelObject);
          String str = JSON.toJSONString(hashMap);

            log.info("调拨创建请求url====" + requestUrl);
            log.info("调拨创建请求参数====" + str);
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPostDb(str, requestUrl, "UTF-8", DB_TIME_OUT);
            log.info("调拨创建响应参数====" + JSONObject.toJSONString(httpPostRsp));
            ApiInBizResponse<Object> objectApiInBizResponse = new ApiInBizResponse<>();
            objectApiInBizResponse.setCode(httpPostRsp.get("code"));
            objectApiInBizResponse.setMsg(httpPostRsp.get("msg"));
            apiInBizResponseMsg = JSONObject.parseObject(objectApiInBizResponse.getMsg(), ApiInBizResponseMsg.class);
            //code :10000-成功   subCode:10001:入库成功、50001-订单已存在
            if(null != httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                //无操作
            }else{
                log.error("调拨创建接口调用异常");
                apiInBizResponseMsg.setSubCode("60001");
                apiInBizResponseMsg.setSubMsg("调拨创建接口调用异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调拨创建失败：",e);
            apiInBizResponseMsg.setSubCode("60001");
            apiInBizResponseMsg.setSubMsg("调拨创建失败");
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("调拨创建接口耗时 {} 毫秒", (endTime - startTime));
        return apiInBizResponseMsg;
    }

    /**
     * 调用调拨系统进行对应订单的解冻/转支付操作
     * @param orderUmicomUnfrzBillVo
     * @return
     */
    public ApiInBizResponseMsg cardOrderPerformance(OrderUmicomUnfrzBillVo orderUmicomUnfrzBillVo){

        long startTime = System.currentTimeMillis();
        ApiInBizResponseMsg apiInBizResponseMsg = new ApiInBizResponseMsg();
        DeployInterfInfo deployInfo = JSONObject.parseObject(sysConfigService.getByCode("deployInfo"), DeployInterfInfo.class);
        String fundDeployUrl = deployInfo.getUrl();
        String version = deployInfo.getVersion();;
        String signType = deployInfo.getSignType();
        String innerAuthSecretKey = deployInfo.getInnerAuthSecretKey();
        //访问项目地址
        //请求地址
        String requestUrl = fundDeployUrl + "/orderUmicom/unfrzBill";
        //组装Json串
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("version", version);
        hashMap.put("method", "/orderUmicom/unfrzBill");
        hashMap.put("signType", signType);
        hashMap.put("timestamp", sdf.format(new Date()));

        try {
            //参数拼接
            JSONObject modelObject = JSONObject.parseObject(JSONObject.toJSONString(orderUmicomUnfrzBillVo));
            String md5SignContent = MD5Util.getMD5SignContent(modelObject);
            //加签
            String sign = MD5Util.encodeByMd5(md5SignContent + innerAuthSecretKey + hashMap.get("timestamp"));
            hashMap.put("sign", sign);
            hashMap.put("model", modelObject);
            String str = JSON.toJSONString(hashMap);
            log.info("调用调拨系统进行对应订单的解冻/转支付操作url====" + requestUrl);
            log.info("调用调拨系统进行对应订单的解冻/转支付操作请求====" + str);
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPostDb(str, requestUrl, "UTF-8", DB_TIME_OUT);
            log.info("调用调拨系统进行对应订单的解冻/转支付操作响应====" + JSONObject.toJSONString(httpPostRsp));
            ApiInBizResponse<Object> objectApiInBizResponse = new ApiInBizResponse<>();
            objectApiInBizResponse.setCode(httpPostRsp.get("code"));
            objectApiInBizResponse.setMsg(httpPostRsp.get("msg"));
            apiInBizResponseMsg = JSONObject.parseObject(objectApiInBizResponse.getMsg(), ApiInBizResponseMsg.class);
            if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                //无操作
            }else{
                log.error("调用调拨系统进行对应订单的解冻/转支付操作接口调用异常");
                apiInBizResponseMsg.setSubCode("60001");
                apiInBizResponseMsg.setSubMsg("调用调拨系统进行对应订单的解冻/转支付操作接口调用异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用调拨系统进行对应订单的解冻/转支付操作失败：",e);
            apiInBizResponseMsg.setSubCode("60001");
            apiInBizResponseMsg.setSubMsg("调用调拨系统进行对应订单的解冻/转支付操作失败");
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("调用调拨系统进行对应订单的解冻/转支付操作接口耗时 {} 毫秒", (endTime - startTime));
        return apiInBizResponseMsg;
    }

    /**
     * 调拨订阅
     * @param debitSubscribeAo
     * @return
     */
    public ApiInBizResponseMsg debitSubscribe(DebitSubscribeAo debitSubscribeAo){
        long startTime = System.currentTimeMillis();
        ApiInBizResponseMsg apiInBizResponseMsg = new ApiInBizResponseMsg();
        DeployInterfInfo deployInfo = JSONObject.parseObject(sysConfigService.getByCode("deployInfo"), DeployInterfInfo.class);
        String fundDeployUrl = deployInfo.getUrl();
        String version = deployInfo.getVersion();;
        String signType = deployInfo.getSignType();
        String innerAuthSecretKey = deployInfo.getInnerAuthSecretKey();
        //访问项目地址
        //请求地址
        String requestUrl = fundDeployUrl + "/order/synchronizationOrderStatus";
        //组装Json串
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("version", version);
        hashMap.put("method", "order.synchronizationOrderStatus");
        hashMap.put("signType", signType);
        hashMap.put("timestamp", sdf.format(new Date()));

        try {
            //参数拼接
            JSONObject modelObject = JSONObject.parseObject(JSONObject.toJSONString(debitSubscribeAo));
            String md5SignContent = MD5Util.getMD5SignContent(modelObject);
            //加签
            String sign = MD5Util.encodeByMd5(md5SignContent + innerAuthSecretKey + hashMap.get("timestamp"));
            hashMap.put("sign", sign);
            hashMap.put("model", modelObject);
            String str = JSON.toJSONString(hashMap);
            log.info("调拨订阅url====" + requestUrl);
            log.info("调拨订阅请求====" + str);
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPostDb(str, requestUrl, "UTF-8", DB_TIME_OUT);
            log.info("调拨订阅响应====" + JSONObject.toJSONString(httpPostRsp));
            ApiInBizResponse<Object> objectApiInBizResponse = new ApiInBizResponse<>();
            objectApiInBizResponse.setCode(httpPostRsp.get("code"));
            objectApiInBizResponse.setMsg(httpPostRsp.get("msg"));
            apiInBizResponseMsg = JSONObject.parseObject(objectApiInBizResponse.getMsg(), ApiInBizResponseMsg.class);
            if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                //无操作
            }else{
                log.error("调拨订阅接口调用异常");
                apiInBizResponseMsg.setSubCode("60001");
                apiInBizResponseMsg.setSubMsg("调拨订阅接口调用异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调拨订阅失败：",e);
            apiInBizResponseMsg.setSubCode("60001");
            apiInBizResponseMsg.setSubMsg("调拨订阅失败");
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("调拨订阅接口耗时 {} 毫秒", (endTime - startTime));
        return apiInBizResponseMsg;
    }

    /**
     *调拨订单返销
     * @return
     */
    public ApiInBizResponseMsg deployOrderPayAndUnfreezeRefund(DeployOrderPayUnfreezeRefundAo deployOrderPayUnfreezeRefundAo){

        long startTime = System.currentTimeMillis();
        ApiInBizResponseMsg apiInBizResponseMsg = new ApiInBizResponseMsg();
        DeployInterfInfo deployInfo = JSONObject.parseObject(sysConfigService.getByCode("deployInfo"), DeployInterfInfo.class);
        String fundDeployUrl = deployInfo.getUrl();
        String version = deployInfo.getVersion();;
        String signType = deployInfo.getSignType();
        String innerAuthSecretKey = deployInfo.getInnerAuthSecretKey();
        //访问项目地址
        //请求地址
        String requestUrl = fundDeployUrl + "/orderRefund/refund";
        //组装Json串
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("version", version);
        hashMap.put("method", "/orderRefund/payAndUnfreezeRefund");
        hashMap.put("signType", signType);
        hashMap.put("timestamp", sdf.format(new Date()));

        try {
            //参数拼接
            JSONObject modelObject = JSONObject.parseObject(JSONObject.toJSONString(deployOrderPayUnfreezeRefundAo));
            String md5SignContent = MD5Util.getMD5SignContent(modelObject);
            //加签
            String sign = MD5Util.encodeByMd5(md5SignContent + innerAuthSecretKey + hashMap.get("timestamp"));
            hashMap.put("sign", sign);
            hashMap.put("model", JSONObject.parseObject(md5SignContent ,TreeMap.class));
            String str = JSON.toJSONString(hashMap);
            log.info("调拨返销url====" + requestUrl);
            log.info("调拨返销请求====" + str);
            Map<String, String> httpPostRsp = LinoHttpUtil.httpPostDb(str, requestUrl, "UTF-8", DB_TIME_OUT);
            log.info("调拨返销响应====" + JSONObject.toJSONString(httpPostRsp));
            ApiInBizResponse<Object> objectApiInBizResponse = new ApiInBizResponse<>();
            objectApiInBizResponse.setCode(httpPostRsp.get("code"));
            objectApiInBizResponse.setMsg(httpPostRsp.get("msg"));
            apiInBizResponseMsg = JSONObject.parseObject(objectApiInBizResponse.getMsg(), ApiInBizResponseMsg.class);
            if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
                //无操作
            }else{
                log.error("调拨返销接口调用异常");
                apiInBizResponseMsg.setSubCode("60001");
                apiInBizResponseMsg.setSubMsg("调拨返销接口调用异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调拨返销失败：",e);
            apiInBizResponseMsg.setSubCode("60001");
            apiInBizResponseMsg.setSubMsg("调拨返销失败");
        }
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("调拨订阅接口耗时 {} 毫秒", (endTime - startTime));
        return apiInBizResponseMsg;
    }

    /**
     * 调拨订单状态异步通知处理
     * @param deployBizRequestVo
     * @return
     */
    public String deployNotifyDispose(DeployBizRequestVo deployBizRequestVo){
        log.info("调拨异步通知回来了->>>>>");
        JSONObject bizContent = deployBizRequestVo.getBizContent();
        String notifyType = bizContent.getString("notifyType");//通知类型
        String authOrderNo = bizContent.getString("outOrderNo");//订单号
        String success = bizContent.getString("success");//是否成功
        OrderInfo queryOrder = orderInfoService.getByAuthOrderNo(authOrderNo);
        if(queryOrder == null){
            log.info("订单不存在:" + authOrderNo);
            return "订单不存在:" + authOrderNo;
        }

        String outTradeNo = queryOrder.getOutTradeNo();//订单号
        DeployNotifyEnum deployNotifyEnum = DeployNotifyEnum.byCode(notifyType);
        if(null == deployNotifyEnum){
            return "非法枚举类型";
        }

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOutTradeNo(outTradeNo);
        switch (deployNotifyEnum){
            case LOAN_RESULT_EVENT:
                if("true".equals(success.toLowerCase())){
                    orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_LOAN_SUCCESS.getCode());
                }else{
                    orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_LOAN_FAILED.getCode());
                }
                break;
            case TRADE_CREATE_EVENT:
                if("true".equals(success.toLowerCase())){
                    orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_TRADE_CREATE_SUCCESS.getCode());
                }else{
                    orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_TRADE_CREATE_FAILED.getCode());
                }
                break;
            case TRADE_CANCEL_EVENT:
                if("true".equals(success.toLowerCase())){
                    orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_TRADE_CANCEL_SUCCESS.getCode());
                }else{
                    orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_TRADE_CANCEL_FAILED.getCode());
                }
                break;
            case TRADE_PAY_EVENT:
                if("true".equals(success.toLowerCase())){
                    orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_TRADE_PAY_SUCCESS.getCode());
                }else{
                    orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_TRADE_PAY_FAILED.getCode());
                }
                break;
            case SETTLE_PAYABLES_EVENT:
                orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_SETTLE_PAYABLES_SUCCESS.getCode());
                break;
            default:
                return "非法枚举类型";
        }
        orderInfo.setUpdateTime(new Date());
        boolean b = orderInfoService.updateById(orderInfo);
        log.info("订单更新情况:"+b);
        return null;
    }

    /**
     * 联通传入省编码返回策略编码
     * @param provinceCode 省份编码
     * @param businessTypeEnum 业务类型
     * @return
     */
    public String getStrategyIdByProvinceCode(String provinceCode, BusinessTypeEnum businessTypeEnum){
        String provinceCodeAndStrategyId = sysConfigService.getByCode("provinceCodeAndStrategyId");
        String quanguoCode = sysConfigService.getByCode("quanguoCode");
        List<ProvinceAndStrategyId> aa = JSONObject.parseArray(provinceCodeAndStrategyId, ProvinceAndStrategyId.class);

        if(businessTypeEnum == BusinessTypeEnum.clsd){
//            //全国
            for (ProvinceAndStrategyId provinceAndStrategyId : aa){
                if(provinceAndStrategyId.getProvinceCode().equals(quanguoCode)){
                    return provinceAndStrategyId.getStrategyId();
                }
            }
        }else{
            //非全国
            for (ProvinceAndStrategyId provinceAndStrategyId : aa){
                if(provinceAndStrategyId.getProvinceCode().equals(provinceCode)){
                    return provinceAndStrategyId.getStrategyId();
                }
            }
        }


        return null;
    }

    /**
     * 进行权益派发能力调用
     * @param sendTaoticketVo
     * @param deployInfo
     */
    public MessagerVo sendEquity(SendEquityVo sendTaoticketVo, DeployInterfInfo deployInfo) {
      MessagerVo messagerVo = new MessagerVo();

      //组装Json串
      Map<String, Object> hashMap = new HashMap<>();
      hashMap.put("version", deployInfo.getVersion());
      String method = "UnicomEquityPlatform/reciveRefundOrder";
      hashMap.put("method", method);
      hashMap.put("signType", deployInfo.getSignType());
      hashMap.put("timestamp", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));

      //排序
      String md5SignContent = MD5Util.getMD5SignContent(JSON.parseObject(JSON.toJSONString(sendTaoticketVo)));
      //加签
      String sign = MD5Util.encodeByMd5(md5SignContent +
          deployInfo.getInnerAuthSecretKey() + String.valueOf(hashMap.get("timestamp")));
      hashMap.put("sign", sign);
      hashMap.put("model", JSON.parseObject(JSON.toJSONString(sendTaoticketVo)));
      String str = JSON.toJSONString(hashMap);

      String requestUrl = deployInfo.getEquityBaseUrl() + "/createEquityOrderOther/createEquityOrder";
      log.info("权益派发请求url====" + requestUrl);
      log.info("权益派发请求参数====" + str);
      Map<String, String> httpPostRsp = LinoHttpUtil.httpPostDb(str, requestUrl, "UTF-8", DB_TIME_OUT);
      log.info("权益派发响应参数====" + JSONObject.toJSONString(httpPostRsp));
      ApiInBizResponse<Object> objectApiInBizResponse = new ApiInBizResponse<>();
      objectApiInBizResponse.setCode(httpPostRsp.get("code"));
      objectApiInBizResponse.setMsg(httpPostRsp.get("msg"));
      ApiInBizResponseMsg apiInBizResponseMsg = JSONObject.parseObject(objectApiInBizResponse.getMsg(), ApiInBizResponseMsg.class);
      //code :10000-成功   subCode:10001:入库成功、50001-订单已存在
      if(null != httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
        //无操作
        messagerVo.setSubCode("10000");
        messagerVo.setSubMsg("权益派发接口调用成功");
        messagerVo.setData(apiInBizResponseMsg);
      }else{
        log.error("权益派发接口调用异常");
        messagerVo.setSubCode("60001");
        messagerVo.setSubMsg("权益派发接口调用异常");
      }

        return messagerVo;
    }
}
