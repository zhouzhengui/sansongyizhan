package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.config.springRedisCache.CacheDuration;
import cn.stylefeng.guns.modular.suninggift.entity.*;
import cn.stylefeng.guns.modular.suninggift.enums.*;
import cn.stylefeng.guns.modular.suninggift.model.InBizRespond;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.CommOrderActivityChgAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.OrderDetailsQueryAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.RspData;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.*;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.*;
import cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo.BaseofferGetResponseData;
import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.modular.suninggift.tools.InInterfaceServiceTool;
import cn.stylefeng.guns.modular.suninggift.tools.QimenServiceTool;
import cn.stylefeng.guns.modular.suninggift.tools.SysConfigServiceTool;
import cn.stylefeng.guns.modular.suninggift.utils.ChatbotSendUtil;
import cn.stylefeng.guns.modular.suninggift.utils.IdGeneratorSnowflake;
//import cn.stylefeng.guns.modular.suninggift.utils.KafkaProducer;
import cn.stylefeng.guns.sys.core.shiro.ShiroKit;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import cn.stylefeng.guns.sys.modular.utils.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.gzlplink.cloud.messagesend.util.JsonResult;
import com.gzlplink.cloud.messagesend.vo.OutBizResponse;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaWtOrderGiftQueryorderdetailRequest;
import com.taobao.api.response.AlibabaWtOrderGiftQueryorderdetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 奇门业务server类
 *
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-02-18 18:30
 */
//@AllArgsConstructor
@Slf4j
@Service
public class QimenService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private QimenServiceTool qimenServiceTool;

    @Autowired
    private InInterfaceService inInterfaceService;

    //订单操作
    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private QueueScheduleService queueScheduleService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private SysConfigServiceTool sysConfigServiceTool;

    @Autowired
    private PromotionAccountInfoService promotionAccountInfoService;

   /* @Autowired
    private KafkaProducer kafkaService;*/

    @Autowired
    private InInterfaceServiceTool inInterfaceServiceTool;

    @Autowired
    private ChatbotSendUtil chatbotSendUtil;

    @Autowired
    private WopayFtpService wopayFtpService;

    @Autowired
    private TmllOrderDisposeService tmllOrderDisposeService;

    @Autowired
    private PhoneOperatorProvinceCityService phoneOperatorProvinceCityService;

    @Autowired
    private RightsNotifyService rightsNotifyService;

    private final Long VERIFY_CODE_VALID_TIME = 60 * 2L;//验证码有效时间2分钟

    private final Long VERIFY_CODE_SEND_INTERVAL = 5L;//验证码发送间隔5秒

    private final int ORDER_QUERY_INTERVAL = 3;//单位秒

    private final int QUERY_NOTIY_COUNT = 5;//队列通知次数

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${profile}")
    private String profile;

    /**
     * 可升档套餐查询接口
     *
     * @param outBizRequestBaseofferGetData
     */
    @CacheDuration(duration = 900)
    @Cacheable(value = "BaseofferGetList", key = "'suninggift:BaseofferGetList:mobile:' + #outBizRequestBaseofferGetData.getMobile()",
            unless = "!#result.code.equals('10000')")
    public InBizRespond<List<BaseofferGetResponseData>> baseofferGet(OutBizRequestBaseofferGetData outBizRequestBaseofferGetData) {

        InBizRespond<List<BaseofferGetResponseData>> inBizRespond = new InBizRespond();
        //判读该号码在本系统是否办过单
        String mobile = outBizRequestBaseofferGetData.getMobile();
        if(StringUtils.isEmpty(mobile)){
            inBizRespond.setCode(ResponseStatusEnum.CONTROLLER_ERROR_PARAMS.getCode());
            inBizRespond.setMessage(ResponseStatusEnum.CONTROLLER_ERROR_PARAMS.getMsg());
            return inBizRespond;
        }
        boolean b = qimenServiceTool.hasOrder(outBizRequestBaseofferGetData.getMobile());
        if (b) {
            //没可用套餐
            log.info("该手机号已办理过业务：" + outBizRequestBaseofferGetData.getIsp());
            inBizRespond.setCode(ResponseStatusEnum.MANY_TIMES_ORDER.getCode());
            inBizRespond.setMessage(ResponseStatusEnum.MANY_TIMES_ORDER.getMsg());
            return inBizRespond;
        }
        //异步落库手机号跟运营商关系
        phoneOperatorProvinceCityService.addSync(outBizRequestBaseofferGetData);
        return qimenServiceTool.baseofferGet(outBizRequestBaseofferGetData);
    }

    /**
     * 存送升档套餐验证码处理接口
     *
     * @param outBizRequestIdentifyDealData
     */
    public InBizRespond<CardCenterResponseVoIdentifyDealResponseData> identifyDeal(OutBizRequestIdentifyDealData outBizRequestIdentifyDealData) {
        InBizRespond<CardCenterResponseVoIdentifyDealResponseData> inBizRespond = new InBizRespond();
/*
        OperatorEnum operatorEnum = qimenServiceTool.getOperatorByMobile(outBizRequestIdentifyDealData.getMobile());
        if(null == operatorEnum){
            //没可用套餐
            log.info("请求号卡系统可升档套餐查询接口失败");
            inBizRespond.setCode(ResponseStatusEnum.CLIENT_QUERY_EMPTY.getCode());
            inBizRespond.setMessage(ResponseStatusEnum.CLIENT_QUERY_EMPTY.getMsg());
            return inBizRespond;
        }
        //根据运营商编码获取clientId
        CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(operatorEnum.getCode());
*/

        //SEND_VERIFICATION_CODE发送验证码
        // CHECK_VERIFICATION_CODE校验验证码
        //判断枚举值
        if (outBizRequestIdentifyDealData.getBiz_type().equals("SEND_VERIFICATION_CODE") ||
                outBizRequestIdentifyDealData.getBiz_type().equals("SEND_VERIFICATION_CODE;CHECK_VERIFICATION_CODE")) {
            //如果是发验证码
//            //调用号卡中心三户接口校验
//            CardCenterResponseVo threeAccountVerifyDataCardCenterResponseVo = null;
//            if(operatorEnum.getCode().equals(OperatorEnum.lpunicom.getCode())){
//                //调用号卡中心三户校验获取省编码
//                log.info("联通发送短信触发的三户校验");
//                threeAccountVerifyDataCardCenterResponseVo = inInterfaceService.threeAccountVerify(outBizRequestIdentifyDealData.getMobile(), cardCenterInterfInfo);
//            }else{
//                log.info("广东移动发送短信触发的三户校验");
//                threeAccountVerifyDataCardCenterResponseVo = inInterfaceService.threeAccountVerifyMiddleSystem(outBizRequestIdentifyDealData.getMobile(), cardCenterInterfInfo, operatorEnum.getCode());
//            }


//            if(null == threeAccountVerifyDataCardCenterResponseVo){
//                //异常
//                log.info("请求号卡系统异常");
//                inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
//                inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
//                return inBizRespond;
//            }
//
//            if(!"10000".equals(threeAccountVerifyDataCardCenterResponseVo.getCode())){
//                //三户校验不过
//                log.info("请求号卡系统失败");
////                inBizRespond.setMessage(ProcessStatesEnum.DISSATISFY_BUSINESS_HANDLING.getDec());
//                inBizRespond.setMessage("三户校验不通过");
//                inBizRespond.setCode(ProcessStatesEnum.DISSATISFY_BUSINESS_HANDLING.getCode());
//                return inBizRespond;
//            }

//            inBizRespond.setCode(threeAccountVerifyDataCardCenterResponseVo.getCode());
//            inBizRespond.setMessage(threeAccountVerifyDataCardCenterResponseVo.getMsg());
            String beforeMobileInterval = outBizRequestIdentifyDealData.getMobile() + "mobileInterval";
            Object o = redisUtil.get(beforeMobileInterval);
            if (null != o) {
                //判断是否在间隔内
                inBizRespond.setMessage(ResponseStatusEnum.CONTROLLER_ERROR_VERIFY_CODE_SEND_HIGNER.getCode());
                inBizRespond.setCode(ResponseStatusEnum.CONTROLLER_ERROR_VERIFY_CODE_SEND_HIGNER.getCode());
                return inBizRespond;
            }

            //10000代表可办理 调用短信发送接口
            String salt = ShiroKit.getRandomSaltByNum(6);//生产6位验证码

            //把手机号跟验证码放reids
            String mobileVerifyCode = outBizRequestIdentifyDealData.getMobile() + "_" + salt;
            log.info("验证码：" + mobileVerifyCode);
            //删除老验证码
            redisUtil.deleteByPrex(outBizRequestIdentifyDealData.getMobile() + "_");
            //录入新验证码
            redisUtil.set(mobileVerifyCode, mobileVerifyCode, VERIFY_CODE_VALID_TIME);
            inBizRespond.setMessage("处理成功");
            //调用短信微服务
            JsonResult<OutBizResponse> outBizResponseJsonResult = inInterfaceService.sendVerifyCode(outBizRequestIdentifyDealData.getMobile(), salt);
            if (null == outBizResponseJsonResult) {
                log.info("发送短信失败");
                inBizRespond.setMessage(ResponseStatusEnum.SERVICE_ERROR.getCode());
                inBizRespond.setCode(ResponseStatusEnum.SERVICE_ERROR.getCode());
                return inBizRespond;
            } else if (outBizResponseJsonResult.isSuccess()) {
                log.info("发送短信成功");
                //发送成功则记录该手机发送间隔并且删除旧的短信
                String afterMobileInterval = outBizRequestIdentifyDealData.getMobile() + "mobileInterval";
                redisUtil.set(afterMobileInterval, afterMobileInterval, VERIFY_CODE_SEND_INTERVAL);
                inBizRespond.setMessage(ResponseStatusEnum.SUCCESS.getMsg());
                inBizRespond.setCode(ResponseStatusEnum.SUCCESS.getCode());
                return inBizRespond;
            } else {
                log.info("发送短信失败" + outBizResponseJsonResult.getMsg());
                inBizRespond.setMessage(ResponseStatusEnum.SERVICE_ERROR.getMsg());
                inBizRespond.setCode(ResponseStatusEnum.SERVICE_ERROR.getCode());
                return inBizRespond;
            }
        } else if (outBizRequestIdentifyDealData.getBiz_type().equals("CHECK_VERIFICATION_CODE")) {
            //如果是验证验证码
            String mobileVerifyCode = outBizRequestIdentifyDealData.getMobile() + "_" + outBizRequestIdentifyDealData.getVerification_code();
            Object o = redisUtil.get(mobileVerifyCode);
            if (outBizRequestIdentifyDealData.getVerification_code().equals("888888") && !profile.equals(ProfilesEnum.prod.getCode())) {
                //非生产环境超级验证码
                inBizRespond.setMessage(ResponseStatusEnum.SUCCESS.getMsg());
                inBizRespond.setCode(ResponseStatusEnum.SUCCESS.getCode());
                return inBizRespond;
            }
            if (outBizRequestIdentifyDealData.getVerification_code().equals("lepeng") && profile.equals(ProfilesEnum.prod.getCode())) {
                //生产环境超级验证码
                inBizRespond.setMessage(ResponseStatusEnum.SUCCESS.getMsg());
                inBizRespond.setCode(ResponseStatusEnum.SUCCESS.getCode());
                return inBizRespond;
            }

            if (null == o) {
                //如果为空则需重新发送
                inBizRespond.setMessage(ResponseStatusEnum.CONTROLLER_ERROR_INVALID_VERIFY_CODE.getMsg());
                inBizRespond.setCode(ResponseStatusEnum.CONTROLLER_ERROR_INVALID_VERIFY_CODE.getCode());
                return inBizRespond;
            } else {
                inBizRespond.setMessage(ResponseStatusEnum.SUCCESS.getMsg());
                inBizRespond.setCode(ResponseStatusEnum.SUCCESS.getCode());
                return inBizRespond;
            }
        } else {
            //非法biz_type类型
            inBizRespond.setMessage("非法biz_type类型值");
            inBizRespond.setCode(ResponseStatusEnum.CONTROLLER_ERROR_PARAM.getCode());
            return inBizRespond;
        }

    }

    /**
     * 订购结果查询
     *
     * @param outBizRequestOrderStatusQuery
     */
    public InBizRespond<QueryOrderStatusResponseData> orderStatusQuery(OutBizRequestOrderStatusQuery outBizRequestOrderStatusQuery) {
        InBizRespond<QueryOrderStatusResponseData> inBizRespond = new InBizRespond();
        OrderInfo orderInfo = orderInfoService.getById(outBizRequestOrderStatusQuery.getOut_trade_no());
        if (null == orderInfo) {
            //异常
            log.info("该订单不存在" + outBizRequestOrderStatusQuery.getOut_trade_no());
            inBizRespond.setMessage(ResponseStatusEnum.CONTROLLER_ERROR_ORDER_TRADE_NO_EXIT.getCode());
            inBizRespond.setCode(ResponseStatusEnum.CONTROLLER_ERROR_ORDER_TRADE_NO_EXIT.getCode());
            return inBizRespond;
        }

        //装换状态
        String s = qimenServiceTool.changOwnStatusToOperatorStatus(ProcessStatesEnum.byCode(orderInfo.getProcessStates()));
        if (StringUtils.isBlank(s)) {
            //其他就返回null吧 然后预警
            log.info(orderInfo.getOutTradeNo() + "未知订单状态" + orderInfo.getProcessStates());
            chatbotSendUtil.sendMsg(orderInfo.getOutTradeNo() + "未知订单状态" + orderInfo.getProcessStates());
            inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            return inBizRespond;
        }

        QueryOrderStatusResponseData queryOrderStatusResponseData = new QueryOrderStatusResponseData();
        queryOrderStatusResponseData.setOrderStatus(s);
        inBizRespond.setT(queryOrderStatusResponseData);
        inBizRespond.setMessage(ResponseStatusEnum.SUCCESS.getMsg());
        inBizRespond.setCode(ResponseStatusEnum.SUCCESS.getCode());
        return inBizRespond;
    }

    /**
     * 三户校验
     *
     * @param outBizRequestThreeAccountVerify
     */
    public InBizRespond<ThreeAccountVerifyResponseData> threeAccountVerify(OutBizRequestThreeAccountVerify outBizRequestThreeAccountVerify) {
        InBizRespond<ThreeAccountVerifyResponseData> inBizRespond = new InBizRespond();
//        OperatorEnum operatorEnum = qimenServiceTool.getOperatorByMobile(outBizRequestThreeAccountVerify.getUserPhone());
        OutBizRequestBaseofferGetData outBizRequestBaseofferGetData = new OutBizRequestBaseofferGetData();
        outBizRequestBaseofferGetData.setMobile(outBizRequestThreeAccountVerify.getUserPhone());
        String operatorCode = qimenServiceTool.getOperatorByIsp(outBizRequestBaseofferGetData);
        OperatorEnum operatorEnum = OperatorEnum.getByCode(operatorCode);
        if (null == operatorEnum) {
            //没可用套餐
            log.info("请求号卡系统可升档套餐查询接口失败");
            inBizRespond.setCode(ResponseStatusEnum.CLIENT_QUERY_EMPTY.getCode());
            inBizRespond.setMessage(ResponseStatusEnum.CLIENT_QUERY_EMPTY.getMsg());
            return inBizRespond;
        }

        //根据运营商编码获取clientId
        CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(operatorEnum.getCode());

        CardCenterResponseVo threeAccountVerifyDataCardCenterResponseVo = null;
        if (operatorEnum.getCode().equals(OperatorEnum.lpunicom.getCode())) {
            log.info("联通三户校验触发的三户校验");
            threeAccountVerifyDataCardCenterResponseVo = inInterfaceService.threeAccountVerify(outBizRequestThreeAccountVerify.getUserPhone(), cardCenterInterfInfo);
        } else {
            log.info("移动三户校验触发的三户校验");
            threeAccountVerifyDataCardCenterResponseVo = inInterfaceService.threeAccountVerifyMiddleSystem(outBizRequestThreeAccountVerify.getUserPhone(), cardCenterInterfInfo, operatorEnum.getCode());
        }

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
            inBizRespond.setCode(threeAccountVerifyDataCardCenterResponseVo.getCode());
            inBizRespond.setMessage(threeAccountVerifyDataCardCenterResponseVo.getMsg());
            return inBizRespond;
        }

        inBizRespond.setMessage(ResponseStatusEnum.SUCCESS.getMsg());
        inBizRespond.setCode(ResponseStatusEnum.SUCCESS.getCode());
        return inBizRespond;
    }

    /**
     * 订单创建异步通知业务逻辑
     *
     * @param outBizRequestCreateOrder
     */
    public InBizRespond<CardCenterOrderCreateResponseData> createOrder(OutBizRequestCreateOrder outBizRequestCreateOrder) {
        InBizRespond<CardCenterOrderCreateResponseData> inBizRespond = new InBizRespond();
        if (outBizRequestCreateOrder == null) {
            inBizRespond.setCode(ResponseStatusEnum.CONTROLLER_ERROR_REQUESTPARAM.getCode());
            inBizRespond.setMessage(ResponseStatusEnum.CONTROLLER_ERROR_REQUESTPARAM.getMsg());
            return inBizRespond;
        }
        String result = QimenServiceTool.checkOrderCreateParam(outBizRequestCreateOrder);
        if (!StringUtils.isEmpty(result)) {
            inBizRespond.setCode(ResponseStatusEnum.CONTROLLER_ERROR_PARAMS.getCode());
            inBizRespond.setMessage(result + "不能为空");
            return inBizRespond;
        }

        String processStates = ProcessStatesEnum.INIT.getCode();
        String tmallOrderId = outBizRequestCreateOrder.getOut_trade_no();
        String taobaoId = outBizRequestCreateOrder.getTaobao_id();
        String name = outBizRequestCreateOrder.getName();
        String operator = outBizRequestCreateOrder.getOperator();
        String phone = outBizRequestCreateOrder.getPhone();
        String contractId = outBizRequestCreateOrder.getContract_id();
//        0-代付款 1-支付成功 2-合约办理成功 3-合约办理失败
        String status = outBizRequestCreateOrder.getStatus();
        //BigDecimal totalFee = new BigDecimal(outBizRequestCreateOrder.getTotal_fee()).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);//天猫的是分 得转成元
        String transferId = outBizRequestCreateOrder.getTransfer_id();
        String userId = outBizRequestCreateOrder.getUser_id();
        String appId = outBizRequestCreateOrder.getApp_id();
        if (StringUtils.isBlank(appId)) {
            //如果appid为空则拿appKey
            appId = outBizRequestCreateOrder.getApp_key();
        }
        String itemId = outBizRequestCreateOrder.getItem_id();
        String auth_order_no = outBizRequestCreateOrder.getOut_order_no();
        String auth_request_no = outBizRequestCreateOrder.getOut_request_no();
        String product_id = outBizRequestCreateOrder.getProduct_id();
        String authNo = outBizRequestCreateOrder.getAuth_no();
        String operationId = outBizRequestCreateOrder.getOperation_id();

        String alowRange = null;
        String alowRangePlus = null;
        Integer fqNum = null;
        String psnId = null;
        String freezeMoneyPerMonth = null;
        String policyNo = null;
        BigDecimal totalFee = null;
        String outAgencyNo = null;
        String goWsn = null;

        boolean b = qimenServiceTool.hasOrder(phone);
        if (b) {
            //没可用套餐
            log.info("该手机号已办理过业务：" + phone);
            inBizRespond.setCode(ResponseStatusEnum.MANY_TIMES_ORDER.getCode());
            inBizRespond.setMessage(ResponseStatusEnum.MANY_TIMES_ORDER.getMsg());
            return inBizRespond;
        }
        //判断是否存在,存在则已经在处理中
        OrderInfo queryOrder = orderInfoService.getById(tmallOrderId);
        if (queryOrder != null) {
            //存在提前，更新 支持幂等
            queryOrder.setStatus(Integer.parseInt(status));
            if (QimenOrderStatesEnum.TRADE_SUCCESS.getCode().equals(status)) {
                //支付成功就打个支付成功的标
                queryOrder.setIsPaySuccess("1");
            }

            queryOrder.setUpdateTime(new Date());
            boolean update = orderInfoService.updateById(queryOrder);
            log.info("订单更新情况：" + update);
            CardCenterOrderCreateResponseData createResponseData = new CardCenterOrderCreateResponseData();
            createResponseData.setOut_trade_no(queryOrder.getOutTradeNo());
            createResponseData.setName(queryOrder.getName());
            createResponseData.setPhone(queryOrder.getPhone());
            createResponseData.setStatus(queryOrder.getStatus() + "");
            inBizRespond.setT(createResponseData);
            inBizRespond.setMessage(ResponseStatusEnum.SUCCESS.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.SUCCESS.getCode());
            return inBizRespond;
        }

        if (status.equals("3")) {
//            3-合约办理失败不处理
            log.info("合约办理失败不用处理 " + tmallOrderId);
            inBizRespond.setMessage(ResponseStatusEnum.SUCCESS.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.SUCCESS.getCode());
            return inBizRespond;
        }

        //当没有入库时，报异常，返回办理失败，并钉钉告警
        String operatorByIsp = OperatorEnum.lpunicom.getCode();
        BusinessTypeEnum businessTypeEnum = null;
        CardCenterInterfInfo cardCenterInterfInfo = null;
        try {
            //根据运营商编码获取clientId
            cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(operatorByIsp);
            if (null == cardCenterInterfInfo) {
                //异常
                log.info("找不到运营商配置 " + JSONObject.toJSONString(outBizRequestCreateOrder));
                //回调信息给苏宁
                // qimenServiceTool.notifySuningOrderStatus(tmallOrderId, processStates, ProcessStatesEnum.DISSATISFY_BUSINESS_HANDLING.getDec());
                inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
                inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
                return inBizRespond;
            }

            //获取外部编码及业务类型
            JSONObject outProtNoByMobile = inInterfaceServiceTool.getOutProtNoByMobile(operatorByIsp, outBizRequestCreateOrder.getPhone(), cardCenterInterfInfo, product_id);
            String outPortNo = outProtNoByMobile.getString("outPortNo");
            String businessType = outProtNoByMobile.getString("businessType");
            if (StringUtils.isBlank(outPortNo)) {
                //异常
                log.info(tmallOrderId + "政策配置异常找不到外部政策编码" + JSONObject.toJSONString(outBizRequestCreateOrder));
                inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
                inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
                return inBizRespond;
            }

            if (StringUtils.isBlank(businessType)) {
                //异常
                log.info(tmallOrderId + "政策配置异常找不到业务类型");
                inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
                inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
                return inBizRespond;
            }

            businessTypeEnum = BusinessTypeEnum.getByCode(businessType);


            PromotionAccountInfo promotionAccountInfo = promotionAccountInfoService.getByAppId(appId);
            //调用pot获取政策列表
            ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo, outPortNo, false);

            if (null == policyListResponse || !"10000".equals(policyListResponse.getCode())) {
                //回调信息给天猫
                //qimenServiceTool.notifySuningOrderStatus(tmallOrderId, processStates, ProcessStatesEnum.DISSATISFY_BUSINESS_HANDLING.getDec());
                //异常
                log.info(tmallOrderId + "请求pot政策查询接口异常");
                inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
                inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
                return inBizRespond;
            }

            //根据阿里虚拟id获取获取alowRange
            JSONObject alowRangeByProductId = qimenServiceTool.getAlowRangeByProductId(product_id, policyListResponse.getBizContent());
            alowRange = alowRangeByProductId.getString("alowRange");
            alowRangePlus = alowRangeByProductId.getString("alowRangePlus");
            fqNum = alowRangeByProductId.getInteger("fqNum");
            freezeMoneyPerMonth = alowRangeByProductId.getString("freezeMoneyPerMonth");
            psnId = alowRangeByProductId.getString("psnId");
            policyNo = alowRangeByProductId.getString("policyNo");
            outAgencyNo = alowRangeByProductId.getString("outAgencyNo");
            goWsn = alowRangeByProductId.getString("goWsn");
            name = alowRangeByProductId.getString("name");
            contractId = alowRangeByProductId.getString("contractId");
            outBizRequestCreateOrder.setContract_id(contractId);
            totalFee = alowRangeByProductId.getBigDecimal("totalFee");
            //校验方法返回的字段合法性
            String s1 = qimenServiceTool.verifyAlowRange(alowRangeByProductId);
            if (StringUtils.isNotBlank(s1)) {
                //异常
                log.info(tmallOrderId + s1);
                inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
                inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
                return inBizRespond;
            }

            if (queryOrder == null) {

                //不存在,查询插入
                queryOrder = new OrderInfo();
                queryOrder.setPayeeLogonId(promotionAccountInfo.getAccountName());
                queryOrder.setPayeeUserId(promotionAccountInfo.getAccountPid());
                queryOrder.setAuthOrderNo(auth_order_no);
                queryOrder.setAuthRequestNo(auth_request_no);
                queryOrder.setProductId(product_id);
                queryOrder.setAppId(appId);
                queryOrder.setSpecPsnId(psnId);
//            OrderInfo queryOrderInterface = this.orderQuery(tmallOrderId, taobaoId);//查询订单，获取item_id
//            orderInfo.setItemId(queryOrderInterface.getItemId());
                queryOrder.setItemId(itemId);
                queryOrder.setFreezeMonth(fqNum);
                queryOrder.setOutTradeNo(tmallOrderId);//订单主键
                queryOrder.setTaobaoId(taobaoId);
                queryOrder.setStatus(Integer.parseInt(status));
                queryOrder.setTotalFee(totalFee);
                queryOrder.setUserId(userId);
                queryOrder.setOperator(operator);
                queryOrder.setPhone(phone);
                queryOrder.setName(name);
                queryOrder.setContractId(contractId);
                queryOrder.setTransferId(transferId);
                queryOrder.setOutPortNo(outPortNo);
                queryOrder.setOperatorCode(operatorByIsp);
                queryOrder.setProcessStates(ProcessStatesEnum.INIT.getCode());//初始化
                queryOrder.setCreateTime(new Date());
                queryOrder.setUpdateTime(new Date());
                queryOrder.setBusinessType(businessType);
                queryOrder.setOutAgencyNo(outAgencyNo);
                queryOrder.setAuthNo(authNo);
                queryOrder.setOperationId(operationId);
                queryOrder.setAuthOrderNo(tmallOrderId);
                queryOrder.setAuthRequestNo(auth_request_no);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("policyNo", policyNo);
                if (QimenOrderStatesEnum.TRADE_SUCCESS.getCode().equals(status)) {
                    //支付成功就打个支付成功的标
                    queryOrder.setIsPaySuccess("1");
                }
                queryOrder.setField1(jsonObject.toJSONString());
                queryOrder.setField3(freezeMoneyPerMonth);
                if (queryOrder.getOperatorCode().equals(OperatorEnum.lpunicom.getCode())) {
                    //联通要放设置是否放ftp表
                    queryOrder.setGoWsn(goWsn);
                }
                PhoneOperatorProvinceCity byId = phoneOperatorProvinceCityService.getById(queryOrder.getPhone());
                if (null != byId) {
                    queryOrder.setProvince(byId.getProvince());
                    queryOrder.setCity(byId.getCity());
                }
                boolean save = orderInfoService.save(queryOrder);
                log.info("订单新增情况：" + save);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("订单创建异常outTradeNo:" + outBizRequestCreateOrder.getTmall_order_id(), e);
            //回调信息给奇门
            // qimenServiceTool.notifySuningOrderStatus(tmallOrderId, processStates , ProcessStatesEnum.DISSATISFY_BUSINESS_HANDLING.getDec());
            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            inBizRespond.setMessage("订单创建异常：");
            return inBizRespond;
        }


        try {

            if (QimenOrderStatesEnum.TRADE_SUCCESS.getCode().equals(status) &&
                    (ProcessStatesEnum.INIT.getCode().equals(queryOrder.getProcessStates()) ||
                            ProcessStatesEnum.BUSINESS_HANDLING.getCode().equals(queryOrder.getProcessStates()))) {
                //清除可办单缓存，防止用户重下单
                redisUtil.del("BaseofferGetListtmallgift:BaseofferGetList:mobile:" + outBizRequestCreateOrder.getPhone());

                //记录办过单
                qimenServiceTool.generateMobileAndOrder(queryOrder.getPhone());

                //支付成功 且空或初始化 则设置订单转态为 运营商三户校验中
                processStates = ProcessStatesEnum.THREE_CERT_CHECKING.getCode();
                CardCenterResponseVo threeAccountVerifyDataCardCenterResponseVo = null;

                log.info("联通创建订单触发的三户校验");
                threeAccountVerifyDataCardCenterResponseVo = inInterfaceService.threeAccountVerify(queryOrder.getPhone(), cardCenterInterfInfo);


                if (!"10000".equals(threeAccountVerifyDataCardCenterResponseVo.getCode())) {
                    //更新订单状态为 校验不通过
                    OrderInfo updateOrderInfo = new OrderInfo();
                    processStates = ProcessStatesEnum.DISSATISFY_BUSINESS_HANDLING.getCode();
                    queryOrder.setProcessStates(processStates);
                    updateOrderInfo.setProcessStates(processStates);
                    updateOrderInfo.setOutTradeNo(queryOrder.getOutTradeNo());
                    updateOrderInfo.setField2("号卡中心三户校验不通过");
                    orderInfoService.updateById(updateOrderInfo);
                    //回调信息给奇门
                    //qimenServiceTool.notifySuningOrderStatus(tmallOrderId, processStates, ProcessStatesEnum.DISSATISFY_BUSINESS_HANDLING.getDec());
                    log.info(tmallOrderId + "号卡中心三户校验不通过" + queryOrder.getPhone());
                    inBizRespond.setMessage(ResponseStatusEnum.THREE_ACCOUNT_VERIFY_FAILD.getMsg());
                    inBizRespond.setCode(ResponseStatusEnum.THREE_ACCOUNT_VERIFY_FAILD.getCode());
                    return inBizRespond;
                }


                CommOrderActivityChgAo commOrderActivityChgAo = null;

                //如果是老联通
                ThreeAccountVerifyData threeAccountVerifyData = (ThreeAccountVerifyData) threeAccountVerifyDataCardCenterResponseVo.getData();
                String s = DateUtil.formatDate(queryOrder.getCreateTime(),
                        "yyyy-MM-dd HH:mm:ss");
                commOrderActivityChgAo = new CommOrderActivityChgAo();
                commOrderActivityChgAo.setUserPhone(phone);
                commOrderActivityChgAo.setContractId(queryOrder.getContractId());
                commOrderActivityChgAo.setCustId(threeAccountVerifyData.getCustInfo().getCustId());
                commOrderActivityChgAo.setOutOrderNo(queryOrder.getOutTradeNo());
                commOrderActivityChgAo.setPayDate(s);
                commOrderActivityChgAo.setProductPrice(totalFee + "");
                commOrderActivityChgAo.setRealPayMoney("0.00");
                commOrderActivityChgAo.setProductNo(alowRange);
                if (BusinessTypeEnum.llb == businessTypeEnum) {
                    //如果是流量包业务
                    if (alowRange.equals("0")) {
                        //如果是0则拿三户校验的的用户产品id
                        ThreeAccountVerifyDataUserInfo userInfo = threeAccountVerifyData.getUserInfo();
                        commOrderActivityChgAo.setProductNo(userInfo.getProductId());
                    }
                }
                //更新用户姓名与省份
                ThreeAccountVerifyDataCustInfo custInfo = threeAccountVerifyData.getCustInfo();
                String custName = custInfo.getCustName();
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOutTradeNo(queryOrder.getOutTradeNo());
                orderInfo.setUserName(custName);
                orderInfo.setProvinceCode(threeAccountVerifyData.getProvinceCode());
                orderInfoService.updateById(orderInfo);
                /*WopayFtp wopayFtp = new WopayFtp();
                wopayFtp.setTradeNo(orderInfo.getOutTradeNo());
                wopayFtp.setUserName(orderInfo.getUserName());
                wopayFtpService.updateById(wopayFtp);
*/
                //订单创建,包含轮询下单逻辑，下单失败继续下单
                RspData operatorOrder = null;

                int i = 3; //最多请求次数
                long j = 1000L;//每次请求间隔
                while (i > 0) {
                    log.info("开始尝试第" + (4 - i) + "次调用订单创建");
                    operatorOrder = inInterfaceService.createOrder(commOrderActivityChgAo, cardCenterInterfInfo, businessTypeEnum);
                    if ("10000".equals(operatorOrder.getCode())) {
                        break;
                    }
                    if (!"10000".equals(operatorOrder.getCode())) {//轮询下单失败还是失败
                        //如果失败则等待1s后再重试两次
                        Thread.sleep(j);
                        i = i - 1;
                        if (i == 0) {
                            //轮询下单都是是失败则返回失败
                            OrderInfo updateOrderInfo = new OrderInfo();
                            processStates = ProcessStatesEnum.SATISFY_BUSINESS_HANDLING.getCode();
                            queryOrder.setProcessStates(processStates);
                            updateOrderInfo.setProcessStates(processStates);
                            updateOrderInfo.setOutTradeNo(queryOrder.getOutTradeNo());
                            updateOrderInfo.setField2("轮询下单失败");
                            orderInfoService.updateById(updateOrderInfo);
                            //qimenServiceTool.notifySuningOrderStatus(tmallOrderId, processStates, ProcessStatesEnum.SATISFY_BUSINESS_HANDLING.getCode());
                            log.info(tmallOrderId + "轮询下单失败");
                            inBizRespond.setCode(ResponseStatusEnum.SUBSCRIBE_FAIL.getCode());
                            inBizRespond.setMessage(ResponseStatusEnum.SUBSCRIBE_FAIL.getMsg());

                            //轮询下单失败，订单解冻，并且通知苏宁订单解冻(异步)
                            JSONObject dataObject = new JSONObject();
                            dataObject.put("outOrderNo", tmallOrderId);
                            rightsNotifyService.unicomMallOrderUnfreeze(dataObject.toJSONString(), true);
                            //通知苏宁
                            qimenServiceTool.notifySuningOrderStatus(tmallOrderId, ResponseStatusEnum.SUBSCRIBE_FAIL.getCode(), ResponseStatusEnum.SUBSCRIBE_FAIL.getMsg());
                            return inBizRespond;
                        }
                    }
                }

                String orderId = null;
                CommOrderActivityChgRspAo f = (CommOrderActivityChgRspAo) operatorOrder.getData();
                orderId = f.getOrderId();

                //"运营商下单成功，业务办理中";
                OrderInfo updateOrderInfo = new OrderInfo();
                processStates = ProcessStatesEnum.BUSINESS_HANDLING.getCode();
                queryOrder.setOperatorOrderId(orderId);
                queryOrder.setProcessStates(processStates);
                updateOrderInfo.setOperatorOrderId(queryOrder.getOperatorOrderId());
                updateOrderInfo.setProcessStates(processStates);
                updateOrderInfo.setOutTradeNo(queryOrder.getOutTradeNo());
                updateOrderInfo.setUpdateTime(new Date());
                orderInfoService.updateById(updateOrderInfo);

                //落库队列执行查询操作
                QueueSchedule queueSchedule = new QueueSchedule();
                queueSchedule.setFlowNo(IdGeneratorSnowflake.generateLongId() + "");
                queueSchedule.setCreateTime(new Date());
                queueSchedule.setNotifyCount(0);
                queueSchedule.setOutTradeNo(tmallOrderId);
                queueSchedule.setStatus(0);
                queueSchedule.setNextNotifyTime(queueSchedule.getCreateTime());
                queueSchedule.setType("suning_qimen");
                //插入数据库
                queueScheduleService.myAdd(queueSchedule);
                //插入kafka队列
                /*boolean aBoolean = kafkaService.addToQueue(queueSchedule);
                if (!aBoolean) {
                    log.error("插入队列失败" + JSONObject.toJSONString(queueSchedule));
                }*/
                CardCenterOrderCreateResponseData createResponseData = new CardCenterOrderCreateResponseData();
                createResponseData.setOut_trade_no(queryOrder.getOutTradeNo());
                createResponseData.setName(queryOrder.getName());
                createResponseData.setPhone(queryOrder.getPhone());
                createResponseData.setStatus(queryOrder.getStatus() + "");
                inBizRespond.setT(createResponseData);
                inBizRespond.setMessage(ResponseStatusEnum.SUCCESS.getMsg());
                inBizRespond.setCode(ResponseStatusEnum.SUCCESS.getCode());
                return inBizRespond;
            } else {
                //其他转态
                log.info(status + "其他状态" + queryOrder.getProcessStates());
//                return null;//status + "其他状态" + queryOrder.getProcessStates();
                inBizRespond.setMessage(ResponseStatusEnum.SUCCESS.getMsg());
                inBizRespond.setCode(ResponseStatusEnum.SUCCESS.getCode());
                return inBizRespond;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("订单创建异常", e);
            OrderInfo updateOrderInfo = new OrderInfo();
            queryOrder.setProcessStates(processStates);
            updateOrderInfo.setProcessStates(processStates);
            updateOrderInfo.setOutTradeNo(queryOrder.getOutTradeNo());
            updateOrderInfo.setField2("订单创建异常");
            orderInfoService.updateById(updateOrderInfo);
            log.info(tmallOrderId + "订单创建异常" + e.getMessage());
            inBizRespond.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
            inBizRespond.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            return inBizRespond;
        }
    }

    /**
     * kafka订单处理
     *
     * @param queueSchedule
     * @return 非空代表异常
     */
   /* public String kafkaOrderDispose(QueueSchedule queueSchedule) {
        OrderInfo orderInfo = orderInfoService.getById(queueSchedule.getOutTradeNo());
        if (null == orderInfo) {
            log.info("订单不存在" + queueSchedule.getOutTradeNo());
            return "订单不存在" + queueSchedule.getOutTradeNo();
        }

        //如果已经处理完毕则无需再处理
        if (!orderInfo.getProcessStates().equals(ProcessStatesEnum.INIT.getCode()) &&
                !orderInfo.getProcessStates().equals(ProcessStatesEnum.BUSINESS_HANDLING.getCode())) {
            log.info("当前订单状态为" + orderInfo.getProcessStates());
            return orderInfo.getOutTradeNo() + "非初始化状态不处理，当前订单状态为" + orderInfo.getProcessStates();
        }

        String processStates = "";
        long time1 = queueSchedule.getNextNotifyTime().getTime();
        long curTime = System.currentTimeMillis();
        Integer notifyCount = queueSchedule.getNotifyCount();

        if (notifyCount > QUERY_NOTIY_COUNT) {
            //如果超过通知次数则不处理了
            return null;
        }

        if (time1 > curTime) {
            //如果还没到通知时间则丢回队列
            kafkaService.addToQueue(queueSchedule);
            return null;
        }
        CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(orderInfo.getOperatorCode());
        log.info("orderInfo.getOperatorCode:" + orderInfo.getOperatorCode() + ",CardCenterInterfInfo参数:" + JSONObject.toJSONString(cardCenterInterfInfo));

        OrderDetailsQueryAo orderDetailsQueryAo = new OrderDetailsQueryAo();
        orderDetailsQueryAo.setOutOrderNo(orderInfo.getAuthOrderNo());
        orderDetailsQueryAo.setOutRequestNo(orderInfo.getAuthRequestNo());
        orderDetailsQueryAo.setOperationId(orderInfo.getOperationId());
        orderDetailsQueryAo.setOperator(orderInfo.getOperatorCode());
        orderDetailsQueryAo.setClientId(cardCenterInterfInfo.getClientId());
        orderDetailsQueryAo.setContractOrderId(orderInfo.getOperatorOrderId());
        orderDetailsQueryAo.setUserPhone(orderInfo.getPhone());
        orderDetailsQueryAo.setAuthNo(orderInfo.getAuthNo());
        orderDetailsQueryAo.setRequestTime(sdf.format(new Date()));
        orderDetailsQueryAo.setAuthNo(orderInfo.getAuthNo());
        orderDetailsQueryAo.setMonth(orderInfo.getFreezeMonth() + "");
        orderDetailsQueryAo.setPayeeLogonId(orderInfo.getPayeeLogonId());
        orderDetailsQueryAo.setPayeeUserId(orderInfo.getPayeeUserId());
        orderDetailsQueryAo.setPayerUserId(orderInfo.getUserId());
        orderDetailsQueryAo.setFreezeDate(sdf.format(orderInfo.getCreateTime()));
        if (StringUtils.isNotBlank(orderInfo.getField1())) {
            JSONObject jsonObject = JSONObject.parseObject(orderInfo.getField1());
            orderDetailsQueryAo.setPolicyNo(jsonObject.getString("policyNo"));
        }

        OperatorEnum operatorEnum = OperatorEnum.getByCode(orderInfo.getOperatorCode());

        CardCenterResponseVo operatorQueryOrder = null;

        if (operatorEnum.getCode().equals(OperatorEnum.lpunicom.getCode())) {
            operatorQueryOrder = inInterfaceService.queryOrder(orderDetailsQueryAo, cardCenterInterfInfo);
        } else {
            operatorQueryOrder = inInterfaceService.queryOrderMiddleSystem(orderDetailsQueryAo, cardCenterInterfInfo, operatorEnum.getCode());
        }

        String cardCenterOrderStatusCode = operatorQueryOrder.getCode();
        if ("31008".equals(cardCenterOrderStatusCode)) {
//            31008订购中
            //如果是订购中则丢回队列继续执行
            //更新通知次数
            queueSchedule.setUpdateTime(new Date());
            queueSchedule.setNotifyCount(queueSchedule.getNotifyCount() + 1);
            if (notifyCount.intValue() < QUERY_NOTIY_COUNT) {
                //没超过通知次数更新下次通知时间并丢回队列继续查询

                //更新下次通知时间在原来基础上加3秒
                Calendar instance = Calendar.getInstance();
                instance.setTime(queueSchedule.getNextNotifyTime());
                instance.add(Calendar.SECOND, ORDER_QUERY_INTERVAL);
                queueSchedule.setNextNotifyTime(instance.getTime());
                queueScheduleService.updateById(queueSchedule);
                kafkaService.addToQueue(queueSchedule);
            } else {
                //超过查询次数返回回失败给苏宁
                //办理失败
                OrderInfo updateOrderInfo = new OrderInfo();
                processStates = ProcessStatesEnum.BUSINESS_HANDLING_FAILED.getCode();
                updateOrderInfo.setProcessStates(processStates);
                updateOrderInfo.setOutTradeNo(orderInfo.getOutTradeNo());
                updateOrderInfo.setField2("超过查询次数返回回失败给苏宁");
                updateOrderInfo.setUpdateTime(new Date());
                log.info(updateOrderInfo.getOutTradeNo() + "老状态：" + orderInfo.getProcessStates() + "新状态：" + updateOrderInfo.getProcessStates());
                orderInfoService.updateById(updateOrderInfo);
                //通知苏宁
                qimenServiceTool.notifySuningOrderStatus(queueSchedule.getOutTradeNo(), processStates, ProcessStatesEnum.BUSINESS_HANDLING_FAILED.getDec());
                queueSchedule.setStatus(1);
                queueSchedule.setSuccessTime(queueSchedule.getUpdateTime());
                queueSchedule.setResponseData("success");
                queueScheduleService.updateById(queueSchedule);
            }
            return null;
        }

        if (!"10000".equals(operatorQueryOrder.getCode())) {
            //办理失败
            //通知苏宁办理失败
            OrderInfo updateOrderInfo = new OrderInfo();
            processStates = ProcessStatesEnum.BUSINESS_HANDLING_FAILED.getCode();
            updateOrderInfo.setProcessStates(processStates);
            orderInfo.setProcessStates(processStates);
            updateOrderInfo.setOutTradeNo(orderInfo.getOutTradeNo());
            updateOrderInfo.setUpdateTime(new Date());
            updateOrderInfo.setField2(operatorQueryOrder.getMsg());
            orderInfoService.updateById(updateOrderInfo);
            qimenServiceTool.notifySuningOrderStatus(queueSchedule.getOutTradeNo(), processStates, ProcessStatesEnum.BUSINESS_HANDLING_FAILED.getDec());
            queueSchedule.setStatus(1);
            queueSchedule.setResponseData("success");
            queueScheduleService.updateById(queueSchedule);
            return null;
        }


        if ("10000".equals(operatorQueryOrder.getCode())) {
            //办理成功
            //通知苏宁办理成功
            processStates = ProcessStatesEnum.BUSINESS_HANDLING_SUCCESS.getCode();
            OrderInfo updateOrderInfo = new OrderInfo();
            WopayFtp updateWopayFtp = new WopayFtp();
            updateWopayFtp.setTradeNo(orderInfo.getOutTradeNo());
            updateOrderInfo.setProcessStates(processStates);
            orderInfo.setProcessStates(processStates);
            updateOrderInfo.setUpdateTime(new Date());
            updateOrderInfo.setOutTradeNo(orderInfo.getOutTradeNo());

            if (orderInfo.getOperatorCode().equals(OperatorEnum.lpunicom.getCode())) {
                //如果是联通
                OrderDetailsQueryRspAo orderDetailsQueryRspAo = (OrderDetailsQueryRspAo) operatorQueryOrder.getData();
                if (StringUtils.isBlank(orderInfo.getProvinceCode()) || StringUtils.isBlank(orderInfo.getCityCode())) {
                    OrderDetailsQueryRspAoCommonInfoBean commonInfo = orderDetailsQueryRspAo.getCommonInfo();
                    if (commonInfo != null) {
                        updateOrderInfo.setProvinceCode(commonInfo.getProvinceCode());
                        updateOrderInfo.setCityCode(commonInfo.getCityCode());
                        updateOrderInfo.setOutTradeNo(orderInfo.getOutTradeNo());
                    }
                }

                // 如果不存在，入库cb订单号
                if (StringUtils.isBlank(orderInfo.getOrderLineId())) {
                    if (orderDetailsQueryRspAo.getOrderLine() != null && orderDetailsQueryRspAo.getOrderLine().size() > 0) {
                        updateOrderInfo.setOrderLineId(orderDetailsQueryRspAo.getOrderLine().get(0).getOrderLineId());
                        log.info("天猫订单" + orderInfo.getOutTradeNo() + "入库cb订单号>>>" + updateOrderInfo.getOrderLineId());
                    }
                }


            }

            orderInfoService.updateById(updateOrderInfo);
            if (operatorEnum == OperatorEnum.lpunicom) {
                //联通的要入库
                //更新完后插入ftp表
                OrderInfo byId = orderInfoService.getById(updateOrderInfo.getOutTradeNo());
                WopayFtp wopayFtp = inInterfaceServiceTool.changeToWoapFtp(byId);
                boolean save1 = wopayFtpService.save(wopayFtp);
                log.info("ftp表订单新增情况：" + save1);

                //2020-06-23联通的ftp信息推送到wsn系统--tangxiong
                tmllOrderDisposeService.orderSyncWsnForftp(wopayFtp);
            }

            //先回成功给苏宁
            queueSchedule.setStatus(1);
            queueSchedule.setResponseData("success");
            queueScheduleService.updateById(queueSchedule);
            qimenServiceTool.notifySuningOrderStatus(queueSchedule.getOutTradeNo(), processStates, ProcessStatesEnum.BUSINESS_HANDLING_FAILED.getDec());
        }

        return operatorQueryOrder.getCode();
    }*/

    /**
     * task订单处理
     *
     * @param queueSchedule
     * @return 非空代表异常
     */
    public String taskOrderDispose(QueueSchedule queueSchedule) {
        OrderInfo orderInfo = orderInfoService.getById(queueSchedule.getOutTradeNo());
        if (null == orderInfo) {
            log.info("订单不存在" + queueSchedule.getOutTradeNo());
            return "订单不存在" + queueSchedule.getOutTradeNo();
        }

        //如果已经处理完毕则无需再处理
        if (!orderInfo.getProcessStates().equals(ProcessStatesEnum.INIT.getCode()) &&
                !orderInfo.getProcessStates().equals(ProcessStatesEnum.BUSINESS_HANDLING.getCode())) {
            log.info("当前订单状态为" + orderInfo.getProcessStates());
            return orderInfo.getOutTradeNo() + "非初始化状态不处理，当前订单状态为" + orderInfo.getProcessStates();
        }

        String processStates = "";
        long time1 = queueSchedule.getNextNotifyTime().getTime();
        long curTime = System.currentTimeMillis();
        Integer notifyCount = queueSchedule.getNotifyCount();

        if (notifyCount > QUERY_NOTIY_COUNT) {
            //如果超过通知次数则不处理了
            return null;
        }

        if (time1 > curTime) {
            //如果还没到通知时间则丢回队列
            //kafkaService.addToQueue(queueSchedule);
            return null;
        }
        CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(orderInfo.getOperatorCode());
        log.info("orderInfo.getOperatorCode:" + orderInfo.getOperatorCode() + ",CardCenterInterfInfo参数:" + JSONObject.toJSONString(cardCenterInterfInfo));

        OrderDetailsQueryAo orderDetailsQueryAo = new OrderDetailsQueryAo();
        orderDetailsQueryAo.setOutOrderNo(orderInfo.getAuthOrderNo());
        orderDetailsQueryAo.setOutRequestNo(orderInfo.getAuthRequestNo());
        orderDetailsQueryAo.setOperationId(orderInfo.getOperationId());
        orderDetailsQueryAo.setOperator(orderInfo.getOperatorCode());
        orderDetailsQueryAo.setClientId(cardCenterInterfInfo.getClientId());
        orderDetailsQueryAo.setContractOrderId(orderInfo.getOperatorOrderId());
        orderDetailsQueryAo.setUserPhone(orderInfo.getPhone());
        orderDetailsQueryAo.setAuthNo(orderInfo.getAuthNo());
        orderDetailsQueryAo.setRequestTime(sdf.format(new Date()));
        orderDetailsQueryAo.setAuthNo(orderInfo.getAuthNo());
        orderDetailsQueryAo.setMonth(orderInfo.getFreezeMonth() + "");
        orderDetailsQueryAo.setPayeeLogonId(orderInfo.getPayeeLogonId());
        orderDetailsQueryAo.setPayeeUserId(orderInfo.getPayeeUserId());
        orderDetailsQueryAo.setPayerUserId(orderInfo.getUserId());

        orderDetailsQueryAo.setFreezeDate(sdf.format(orderInfo.getCreateTime()));
        if (StringUtils.isNotBlank(orderInfo.getField1())) {
            JSONObject jsonObject = JSONObject.parseObject(orderInfo.getField1());
            orderDetailsQueryAo.setPolicyNo(jsonObject.getString("policyNo"));
        }
        //珍贵要求新增2020-08-18
        orderDetailsQueryAo.setMerchantId(cardCenterInterfInfo.getMerchantId());
        orderDetailsQueryAo.setCarrierNo(cardCenterInterfInfo.getCarrierNo());
        OperatorEnum operatorEnum = OperatorEnum.getByCode(orderInfo.getOperatorCode());

        CardCenterResponseVo operatorQueryOrder = null;
        operatorQueryOrder = inInterfaceService.queryOrder(orderDetailsQueryAo, cardCenterInterfInfo);
        String cardCenterOrderStatusCode = operatorQueryOrder.getCode();
        if ("31008".equals(cardCenterOrderStatusCode)) {
//            31008订购中
            //如果是订购中则丢回队列继续执行
            //更新通知次数
            queueSchedule.setUpdateTime(new Date());
            queueSchedule.setNotifyCount(queueSchedule.getNotifyCount() + 1);
            if (notifyCount.intValue() < QUERY_NOTIY_COUNT) {
                //没超过通知次数更新下次通知时间并丢回队列继续查询

                //更新下次通知时间在原来基础上加3秒
                Calendar instance = Calendar.getInstance();
                instance.setTime(queueSchedule.getNextNotifyTime());
                instance.add(Calendar.SECOND, ORDER_QUERY_INTERVAL);
                queueSchedule.setNextNotifyTime(instance.getTime());
                queueScheduleService.updateById(queueSchedule);
                //kafkaService.addToQueue(queueSchedule);
            } else {
                //超过查询次数返回回失败给苏宁
                //办理失败
                OrderInfo updateOrderInfo = new OrderInfo();
                processStates = ProcessStatesEnum.BUSINESS_HANDLING_FAILED.getCode();
                updateOrderInfo.setProcessStates(processStates);
                updateOrderInfo.setOutTradeNo(orderInfo.getOutTradeNo());
                updateOrderInfo.setField2("超过查询次数返回回失败给苏宁");
                updateOrderInfo.setUpdateTime(new Date());
                log.info(updateOrderInfo.getOutTradeNo() + "老状态：" + orderInfo.getProcessStates() + "新状态：" + updateOrderInfo.getProcessStates());
                orderInfoService.updateById(updateOrderInfo);
                //通知苏宁
                qimenServiceTool.notifySuningOrderStatus(queueSchedule.getOutTradeNo(), ResponseStatusEnum.SUBSCRIBE_FAIL.getCode(), ResponseStatusEnum.SUBSCRIBE_FAIL.getMsg());
                queueSchedule.setStatus(1);
                queueSchedule.setSuccessTime(queueSchedule.getUpdateTime());
                queueSchedule.setResponseData("success");
                queueScheduleService.updateById(queueSchedule);
            }
            return null;
        }

        if (!"10000".equals(operatorQueryOrder.getCode())) {
            //办理失败
            //通知苏宁办理失败
            OrderInfo updateOrderInfo = new OrderInfo();
            processStates = ProcessStatesEnum.BUSINESS_HANDLING_FAILED.getCode();
            updateOrderInfo.setProcessStates(processStates);
            orderInfo.setProcessStates(processStates);
            updateOrderInfo.setOutTradeNo(orderInfo.getOutTradeNo());
            updateOrderInfo.setUpdateTime(new Date());
            updateOrderInfo.setField2(operatorQueryOrder.getMsg());
            orderInfoService.updateById(updateOrderInfo);
            qimenServiceTool.notifySuningOrderStatus(queueSchedule.getOutTradeNo(), ResponseStatusEnum.SUBSCRIBE_FAIL.getCode(), ResponseStatusEnum.SUBSCRIBE_FAIL.getMsg());
            queueSchedule.setStatus(1);
            queueSchedule.setUpdateTime(new Date());
            queueSchedule.setResponseData("success");
            queueScheduleService.updateById(queueSchedule);
            return null;
        }


        if ("10000".equals(operatorQueryOrder.getCode())) {
            //办理成功
            //通知苏宁办理成功
            processStates = ProcessStatesEnum.BUSINESS_HANDLING_SUCCESS.getCode();
            OrderInfo updateOrderInfo = new OrderInfo();
            WopayFtp updateWopayFtp = new WopayFtp();
            updateWopayFtp.setTradeNo(orderInfo.getOutTradeNo());
            updateOrderInfo.setProcessStates(processStates);
            orderInfo.setProcessStates(processStates);
            updateOrderInfo.setUpdateTime(new Date());
            updateOrderInfo.setOutTradeNo(orderInfo.getOutTradeNo());

            if (orderInfo.getOperatorCode().equals(OperatorEnum.lpunicom.getCode())) {
                //如果是联通
                OrderDetailsQueryRspAo orderDetailsQueryRspAo = (OrderDetailsQueryRspAo) operatorQueryOrder.getData();
                if (StringUtils.isBlank(orderInfo.getProvinceCode()) || StringUtils.isBlank(orderInfo.getCityCode())) {
                    OrderDetailsQueryRspAoCommonInfoBean commonInfo = orderDetailsQueryRspAo.getCommonInfo();
                    if (commonInfo != null) {
                        updateOrderInfo.setProvinceCode(commonInfo.getProvinceCode());
                        updateOrderInfo.setCityCode(commonInfo.getCityCode());
                        updateOrderInfo.setOutTradeNo(orderInfo.getOutTradeNo());
                    }
                }

                // 如果不存在，入库cb订单号
                if (StringUtils.isBlank(orderInfo.getOrderLineId())) {
                    if (orderDetailsQueryRspAo.getOrderLine() != null && orderDetailsQueryRspAo.getOrderLine().size() > 0) {
                        updateOrderInfo.setOrderLineId(orderDetailsQueryRspAo.getOrderLine().get(0).getOrderLineId());
                        log.info("天猫订单" + orderInfo.getOutTradeNo() + "入库cb订单号>>>" + updateOrderInfo.getOrderLineId());
                    }
                }


            }

            orderInfoService.updateById(updateOrderInfo);
            if (operatorEnum == OperatorEnum.lpunicom) {
                //联通的要入库
                //更新完后插入ftp表
                OrderInfo byId = orderInfoService.getById(updateOrderInfo.getOutTradeNo());
                WopayFtp wopayFtp = inInterfaceServiceTool.changeToWoapFtp(byId);
                boolean save1 = wopayFtpService.save(wopayFtp);
                log.info("ftp表订单新增情况：" + save1);

                //2020-06-23联通的ftp信息推送到wsn系统--tangxiong
                tmllOrderDisposeService.orderSyncWsnForftp(wopayFtp);
            }

            //先回成功给苏宁
            queueSchedule.setStatus(1);
            queueSchedule.setResponseData("success");
            queueSchedule.setUpdateTime(new Date());
            queueScheduleService.updateById(queueSchedule);
            qimenServiceTool.notifySuningOrderStatus(queueSchedule.getOutTradeNo(), ResponseStatusEnum.SUCCESS_0000.getCode(), ResponseStatusEnum.SUCCESS_0000.getMsg());
        }

        return null;
    }

    /**
     * 礼包查询接口 暂时没用 2020-03-26 cms
     *
     * @param tmallOrderId
     * @param taobaoId
     * @return
     */
    public OrderInfo queryTaobaoItemId(String tmallOrderId, String taobaoId) {
        String appkey = sysConfigService.getByCode("appkey");
        String qimenUrl = sysConfigService.getByCode("qimenUrl");
        PromotionAccountInfo byAppId = promotionAccountInfoService.getByAppId(appkey);
        String secret = byAppId.getPrivateKey();

        TaobaoClient client = new DefaultTaobaoClient(qimenUrl, appkey, secret);
        AlibabaWtOrderGiftQueryorderdetailRequest req = new AlibabaWtOrderGiftQueryorderdetailRequest();
        req.setOrderId(tmallOrderId);
        req.setTaobaoId(taobaoId);
        AlibabaWtOrderGiftQueryorderdetailResponse rsp = null;

        try {
            log.info("礼包查询接口请求参数->>>" + JSONObject.toJSONString(req));
            rsp = client.execute(req);
            log.info("礼包查询接口响应参数->>>" + JSONObject.toJSONString(rsp));
            if (!rsp.isSuccess()) {
                throw new ApiException("礼包查询订单失败!!tmallOrderId:" + tmallOrderId);
            }
        } catch (ApiException e) {
            e.printStackTrace();
            log.error("礼包查询订单接口异常", e);
        }

        AlibabaWtOrderGiftQueryorderdetailResponse.GiftSimpleOrderModel model = rsp.getResult().getModel();

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setItemId(model.getItemId());

        return orderInfo;
    }
}
