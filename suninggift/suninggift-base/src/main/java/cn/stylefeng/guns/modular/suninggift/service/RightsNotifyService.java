package cn.stylefeng.guns.modular.suninggift.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.stylefeng.guns.modular.suninggift.entity.CustomerInfo;
import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
import cn.stylefeng.guns.modular.suninggift.entity.WopayFtp;
import cn.stylefeng.guns.modular.suninggift.enums.OperatorEnum;
import cn.stylefeng.guns.modular.suninggift.enums.ProcessStatesEnum;
import cn.stylefeng.guns.modular.suninggift.model.OutBizResponse;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ApiInBizResponse;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ApiInBizResponseMsg;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.DeployOrderPayUnfreezeRefundAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.PolicyListResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.PolicyListResponseBizContentPolicy;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.PolicyListResponseBizContentProductPolicy;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.WsnOrderInfoVo;
import cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo.OutApiResponse;
import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.modular.suninggift.model.constant.DeployInterfInfo;
import cn.stylefeng.guns.modular.suninggift.model.params.SuningOrderKeepresultParam;
import cn.stylefeng.guns.modular.suninggift.model.params.TmllOrderOprationLogParam;
import cn.stylefeng.guns.modular.suninggift.service.impl.TmallOrderKeepresultServiceImpl;
import cn.stylefeng.guns.modular.suninggift.tools.InInterfaceServiceTool;
import cn.stylefeng.guns.modular.suninggift.tools.SysConfigServiceTool;
import cn.stylefeng.guns.modular.suninggift.utils.CommonUtil;
import cn.stylefeng.guns.modular.suninggift.utils.DateUtil;
import cn.stylefeng.guns.modular.suninggift.utils.HttpUtil;
import cn.stylefeng.guns.modular.suninggift.utils.IdGeneratorSnowflake;
import cn.stylefeng.guns.modular.suninggift.utils.MD5Util;
import cn.stylefeng.guns.modular.suninggift.utils.RSAEncrypt;
import cn.stylefeng.guns.modular.suninggift.utils.SignContentUtil;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayFundAuthOrderUnfreezeResponse;
import com.gzlplink.cloud.mps.client.model.access.entity.CdisArea;
import com.gzlplink.cloud.mps.client.model.order.entity.CdisContFreeze;
import com.gzlplink.cloud.mps.client.model.order.entity.CdisContOrder;
import com.gzlplink.cloud.mps.client.model.order.entity.CdisFreezeLog;
import com.gzlplink.cloud.mps.client.util.MpsClientUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RightsNotifyService {

  //生产环境
  private static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALdKMndisdh6gMLiZ2GDpaNZC14J43yOPE4YkKeAjfLYPb/Ma+5wunFC66h4Hof8EC/OQjIAOO5sAivQmW9aHB4XdYttARaMYA5mqJ8AxKqAV5rK6Lomm1lR+y+mG9RFtX0SNKZpOQYmqQw5wydhIHiHFOvlxW8VRHahE6AstqSfAgMBAAECgYBhLOtYtHDJ7xj5OgPJhXx6ACmwr9l2HgsG9Kssw1F+4PrMB1tpzUZr6ij65sR5IECMt/QMgmWssoLsK+pNSR8CRYaZLdMWw3JbUs28x+UtiePPi47dC+uQSjgpWJG9jxmAzROgxv7//j4QYg5LW8Cn7PXAjkiOsOfEy34/Ue1hsQJBAPc4h8bhC/6ipaI65QhqNQlt+x6lYR2uWc4+GCy4NxZB+IRiq+mOUNa5b0B/auzvGxHVsCD2HpKiOG7Qpm6rL80CQQC9zHlWnEHaIp8tTuNZozKOs1PZP2El+s3BH5oODDO3lbR0cUkBQKZF20JC0btZLrrTM/bhSQ1VxoYj+akQbQIbAkAuWXPjTogQKnBBVSdhh4AAD5WYLkth5DFEIJIpuDPjwWSVdWZjuGHhkAySnBDw8PczRcvqshoTRcSsygOaFhA1AkEAmqnuUtrAet5tsgEwGRJ5F3ogoM8Z/lYTpwV2h24AEuEmjSDnqeKh3BkhNMwuDC2dRpB45PqfqD8/fj9rOpxp8wJAWAt05ygQwKPc3+Bq3k/m9lk25GxrSfuNjd2VljVTMAQJy7rhv23ozaSwKSUrt9wFwxQ0H2RZbDzOYzDoK4Qp3g==";
  private static String outSysNo = "LT_10010";
  private static String format = "JSON";
  private static String charset = "UTF-8";
  private static String signType = "RSA2";
  private static String version = "1.0";

  @Autowired
  private SysConfigService sysConfigService;

  @Autowired
  private OrderInfoService orderInfoService;

  @Autowired
  private CustomerInfoService customerInfoService;

  @Autowired
  private PromotionAccountInfoService promotionAccountInfoService;

  @Autowired
  private TmallEquityService tmallEquityService;

  @Autowired
  private AliService aliService;

  @Autowired
  private TmllOrderDisposeService tmllOrderDisposeService;

  @Autowired
  private SysConfigServiceTool sysConfigServiceTool;

  @Autowired
  private InInterfaceService inInterfaceService;

  @Autowired
  private TmallOrderKeepresultService tmallOrderKeepresultService;

  @Autowired
  private TmllOrderOprationLogService tmllOrderOprationLogService;

  /**
   * 拼装加签参数
   * @param jsonBody
   * @return
   */
  public String getMD5SignContent(String jsonBody) {
    JSONObject bizObject = JSONObject.parseObject(jsonBody);
    Map signObject = JSONObject.parseObject(bizObject.getString("model"), Map.class);

    String json = "";
    TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
    treeMap.putAll(signObject);

    if (signObject != null) {
      json += JSON.toJSONString(treeMap);
    }

    return json;
  }

  /**
   * 验签数据
   * @param jsonBody
   * @param signContent
   * @return
   */
  public boolean md5Sign(String jsonBody, String signContent) {

    JSONObject signObject = JSONObject.parseObject(jsonBody);
    //获取签名
    String sign = signObject.getString("sign");

    //进行权益派发能力调用
    DeployInterfInfo deployInfo = JSONObject.parseObject(sysConfigService.getByCode("deployInfo"), DeployInterfInfo.class);
    //加签
    String md5Key = deployInfo.getInnerAuthSecretKey();
    String signNew = MD5Util.encodeByMd5(signContent + md5Key + signObject.getString("timestamp"));

    if(sign.equals(signNew)) {
      return true;
    }

    return false;
  }

  /**
   * 更新派券通知订单状态
   * @param jsonBody
   * @return
   */
  public Map<String, String> updateRightsOrder(String jsonBody) {

    Map<String, String> resultMap = new HashMap<String ,String>();

    //解析请求参数
    JSONObject bizObject = JSONObject.parseObject(jsonBody);
    JSONObject modelObject = bizObject.getJSONObject("model");

    //获取订单号
    String outOrderNo = modelObject.getString("outOrderNo");
    //派券状态
    String contractStatus = modelObject.getString("contractStatus");
    //调拨状态
    String deployStatus = modelObject.getString("deployStatus");
    //领券链接
    String resultUrl = modelObject.getString("resultUrl");

    if(CommonUtil.isEmpty(outOrderNo)) {
      resultMap.put("code", "30001");
      resultMap.put("msg", "订单号为空");
      return resultMap;
    }

    if(CommonUtil.isEmpty(contractStatus) && CommonUtil.isEmpty(deployStatus)) {
      resultMap.put("code", "30002");
      resultMap.put("msg", "派券状态/调拨状态为空");
      return resultMap;
    }

    //-1-解约,0-暂停,1-正常，2-派券成功，-2-派券失败，-3部分成功，-4已退卡
    if(!CommonUtil.isEmpty(contractStatus)) {
      if(!"2".equals(contractStatus) && !"-2".equals(contractStatus) && !"-3".equals(contractStatus)) {
        resultMap.put("code", "30003");
        resultMap.put("msg", "派券状态contractStatus非正确状态");
        return resultMap;
      }
    }

    if(!CommonUtil.isEmpty(deployStatus)) {
      if(!"3".equals(deployStatus) && !"-3".equals(deployStatus) && !"5".equals(deployStatus)) {
        resultMap.put("code", "30004");
        resultMap.put("msg", "调拨状态deployStatus非正确状态");
        return resultMap;
      }
    }

    //查询订单是否存在
    OrderInfo orderInfo = orderInfoService.getById(outOrderNo);
    if(orderInfo == null) {
      resultMap.put("code", "30005");
      resultMap.put("msg", "订单[" + outOrderNo + "]不存在对应的信息");
      return resultMap;
    }

    //同时更新pot主订单信息
    CdisContOrder cdisContOrder = new CdisContOrder();
    cdisContOrder.setOutOrderNo(orderInfo.getAuthOrderNo());
    cdisContOrder.setOutRequestNo(orderInfo.getAuthRequestNo());

    if(!CommonUtil.isEmpty(contractStatus)) {
      //更新订单派券状态
      if("2".equals(contractStatus)) {
        orderInfo.setProcessStates(ProcessStatesEnum.SEND_EQUITY_SUCCESS.getCode());
        JSONObject tmallCodeJS = new JSONObject();
        tmallCodeJS.put("tmallCode", modelObject.getString("tmallCode"));
        orderInfo.setField2(tmallCodeJS.toString());

        cdisContOrder.setContractStatus(Integer.valueOf(contractStatus));
        //派券成功的订单如果是到店券时,把对应的派发链接保存到订单orderBody里面
        if(!CommonUtil.isEmpty(resultUrl)) {
          String orderBody = orderInfo.getField1();
          JSONObject orderBodyObject = JSONObject.parseObject(orderBody);
          orderBodyObject.put("resultUrl", resultUrl);
          orderInfo.setField1(orderBodyObject.toJSONString());
        }

      }else {
        orderInfo.setProcessStates(ProcessStatesEnum.SEND_EQUITY_FAILED.getCode());
        cdisContOrder.setContractStatus(Integer.valueOf(contractStatus));
      }

    }else if(!CommonUtil.isEmpty(deployStatus)) {
      //判断调拨状态,匹配新库状态
      if(3 == Integer.valueOf(deployStatus)){
        //更新订单调拨放款状态
        orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_LOAN_SUCCESS.getCode());
      }else if(-3 == Integer.valueOf(deployStatus)){
        //更新订单调拨放款状态
        orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_LOAN_FAILED.getCode());
      }else if(5 == Integer.valueOf(deployStatus)){
        orderInfo.setProcessStates(ProcessStatesEnum.DEFAULT_UNFREEZE_REFUND_ORDER.getCode());
      }

      //更新订单调拨放款状态
      cdisContOrder.setDeployStatus(Integer.valueOf(deployStatus));
    }

    orderInfo.setUpdateTime(new Date());
    cdisContOrder.setUpdtDate(new Date());
    boolean updateResult = orderInfoService.updateById(orderInfo);
    MpsClientUtil.updateByPrimaryKeySelective(CdisContOrder.class ,cdisContOrder);

    //区分返回响应报文
    if(!CommonUtil.isEmpty(contractStatus)) {
      //更新订单派券状态
      resultMap.put("msg", "订单派券状态更新成功");
    }else if(!CommonUtil.isEmpty(deployStatus)) {
      //更新订单调拨放款状态
      resultMap.put("msg", "订单调拨放款状态更新成功");
    }

    if(updateResult) {
      resultMap.put("code", "10000");
      return resultMap;
    }

    resultMap.put("code", "30006");
    return resultMap;
  }

  //重新调拨创建、调拨订阅
  public Map<String, String> deployCreate(String outOrderNo) {
    Map<String, String> resultMap = new HashMap<String ,String>();

    if(CommonUtil.isEmpty(outOrderNo)) {
      resultMap.put("code", "20001");
      resultMap.put("msg", "订单号为空");
      return resultMap;
    }

    //查询订单是否存在
    OrderInfo orderInfo = orderInfoService.getById(outOrderNo);
    if(orderInfo == null) {
      resultMap.put("code", "30005");
      resultMap.put("msg", "订单[" + outOrderNo + "]不存在对应的信息");
      return resultMap;
    }

    String businessType = orderInfo.getBusinessType();
    //存量订单进行权益派发
    if("clsd".equals(businessType) || "llb".equals(businessType)){

      //调拨创建相关参数
      Map<String, Object> orderCreateMap = tmallEquityService.freezeOrderCreate(orderInfo);
      if(!"10001".equals(orderCreateMap.get("code").toString())) {
        resultMap.put("code", "20001");
        resultMap.put("msg", orderCreateMap.get("msg").toString());
        return resultMap;
      }
    }else if("xyhrw".equals(businessType)){
      //获取订单对应的用户信息
      String custNo = orderInfo.getCustNo();
      if(CommonUtil.isEmpty(custNo)){
        resultMap.put("code", "30003");
        resultMap.put("msg", "订单号[" + outOrderNo + "]对应订单的custNo为空");
        return resultMap;
      }
      CustomerInfo customerInfo = customerInfoService.getById(custNo);
      if(customerInfo == null){
        resultMap.put("code", "30003");
        resultMap.put("msg", "订单号[" + outOrderNo + "]对应订单的用户信息为空");
        return resultMap;
      }

      //调拨创建相关参数
      Map<String, Object> orderCreateMap = tmallEquityService.freezeOrderCreate(orderInfo ,customerInfo);
      if(!"10001".equals(orderCreateMap.get("code").toString())) {
        resultMap.put("code", "20001");
        resultMap.put("msg", orderCreateMap.get("msg").toString());
        return resultMap;
      }

    }

    //重新调拨订阅
    //调拨订阅相关参数
    Map<String, Object> deployStatusMap = tmallEquityService.deployOrderStatus(orderInfo);
    if(!"10001".equals(deployStatusMap.get("code").toString())) {
      resultMap.put("code", "20003");
      resultMap.put("msg", deployStatusMap.get("msg").toString());
      return resultMap;
    }

    resultMap.put("code", "10001");
    resultMap.put("msg", deployStatusMap.get("msg").toString());
    return resultMap;
  }

  /**
   * 重新调拨订阅
   * @param outOrderNo
   * @return
   */
  public Map<String, String> deploySubscriber(String outOrderNo) {
    Map<String, String> resultMap = new HashMap<String ,String>();

    if(CommonUtil.isEmpty(outOrderNo)) {
      resultMap.put("code", "20003");
      resultMap.put("msg", "订单号为空");
      return resultMap;
    }

    //查询订单是否存在
    OrderInfo orderInfo = orderInfoService.getById(outOrderNo);
    if(orderInfo == null) {
      resultMap.put("code", "30005");
      resultMap.put("msg", "订单[" + outOrderNo + "]不存在对应的信息");
      return resultMap;
    }

    //重新调拨订阅
    //调拨订阅相关参数
    String orderStatus = "SUCCESS";// SUCCESS-交易成功 FAIL-交易失败 CLOSE-关闭
    Map<String, Object> deployStatusMap = tmallEquityService.deployOrderStatus(orderInfo);
    if(!"10001".equals(deployStatusMap.get("code").toString())) {
      resultMap.put("code", "20003");
      resultMap.put("msg", deployStatusMap.get("msg").toString());
      return resultMap;
    }

    resultMap.put("code", "10001");
    resultMap.put("msg", deployStatusMap.get("msg").toString());
    return resultMap;
  }

  /**
   * 异步操作能力：苏宁订单解冻能力
   * @param jsonBody
   * @return
   */
  public OutBizResponse unicomMallOrderUnfreeze(String jsonBody) {
    OutBizResponse outBizResponse = new OutBizResponse();

    JSONObject jsonBodyObject = JSONObject.parseObject(jsonBody);
    // 校验必填参数是否为空
    String outOrderNo = jsonBodyObject.getString("outOrderNo");
    if(CommonUtil.isEmpty(outOrderNo)) {
      outBizResponse.setSubCode("30001");
      outBizResponse.setSubMsg("订单编码为空");
      return outBizResponse;
    }

    //获取订单信息
    OrderInfo orderInfo = orderInfoService.getById(outOrderNo);
    if (orderInfo == null) {
      orderInfo = orderInfoService.getByAuthOrderNo(outOrderNo);
    }

    if(orderInfo == null){
      outBizResponse.setSubCode("40001");
      outBizResponse.setSubMsg("订单号[" + outOrderNo + "]对应订单信息为空");
      return outBizResponse;
    }

    //根据订单编码获取pot主订单信息
    CdisContOrder cdisContOrder = MpsClientUtil.selectByPrimaryKey(CdisContOrder.class , orderInfo.getAuthOrderNo());
    if(cdisContOrder == null){
      outBizResponse.setSubCode("40007");
      outBizResponse.setSubMsg("订单信息更新失败,outOrderNo={"+orderInfo.getAuthOrderNo()+"} 订单信息为空");
      return outBizResponse;
    }

    //订单流转状态
    String processStates = orderInfo.getProcessStates();
    if(ProcessStatesEnum.SEND_EQUITY_SUCCESS.getCode().equals(processStates)){
      outBizResponse.setSubCode("30004");
      outBizResponse.setSubMsg("订单[" + outOrderNo + "]已经派发权益成功,不允许进行解冻操作");
      return outBizResponse;
    }

    if(ProcessStatesEnum.DEBIT_LOAN_SUCCESS.getCode().equals(processStates)){
      outBizResponse.setSubCode("30004");
      outBizResponse.setSubMsg("订单[" + outOrderNo + "]已经调拨放款成功,不允许进行解冻操作");
      return outBizResponse;
    }

    //判断订单是否派券成功,派券成功的订单不允许进行解冻操作
    Integer contractStatus = cdisContOrder.getContractStatus();
    if(contractStatus == 2 || -3 == contractStatus) {
      outBizResponse.setSubCode("30004");
      outBizResponse.setSubMsg("订单[" + outOrderNo + "]已经派发权益成功,不允许进行解冻操作");
      return outBizResponse;
    }

    //判断订单是否调拨放款成功,调拨放款成功的订单不允许进行解冻操作
    Integer deployStatus = cdisContOrder.getDeployStatus();
    if(deployStatus == 3) {
      outBizResponse.setSubCode("30005");
      outBizResponse.setSubMsg("订单[" + outOrderNo + "]已经调拨放款成功,不允许进行解冻操作");
      return outBizResponse;
    }

    try {
      // 订单没有同步成功时：同时返销订单解冻用户冻结款
      String freezeAppId = sysConfigService.getByCode("freezeAppId");
      PromotionAccountInfo promotionAccountInfo = promotionAccountInfoService.getByAppId(freezeAppId);
      String authNo = orderInfo.getAuthNo();
      String outRequestNo = DateUtil.formatDate(new Date() ,"yyyyMMddHHmmssSSS") + RandomUtil.randomNumbers(5);
      String amount = orderInfo.getTotalFee().toString();
      String remark = DateUtil.formatDate(new Date() ,"yyyy-MM-dd HH:mm:ss") + " ,解冻 " + amount + " 元";
      AlipayFundAuthOrderUnfreezeResponse fundAuthOrderUnfreezeResponse = aliService.fundAuthOrderUnfreeze(promotionAccountInfo , authNo ,outRequestNo ,amount ,remark);
      if (!fundAuthOrderUnfreezeResponse.isSuccess()) {
        outBizResponse.setSubCode("30006");
        outBizResponse.setSubMsg("订单解冻操作:" + fundAuthOrderUnfreezeResponse.getMsg() + " ," + fundAuthOrderUnfreezeResponse.getSubMsg());
        return outBizResponse;
      }

      //更新订单对应的金额信息
      orderInfo.setProcessStates(ProcessStatesEnum.UNFREEZE_REFUND_ORDER.getCode());
      orderInfo.setIsPaySuccess("0");
      orderInfo.setStatus(3);
      orderInfo.setUpdateTime(new Date());
      orderInfoService.updateById(orderInfo);

      //更新pot相关的订单
      String unFreezeAmount = fundAuthOrderUnfreezeResponse.getAmount();
      cdisContOrder.setFundAmount(Double.valueOf(unFreezeAmount));
      cdisContOrder.setTotalUnfreezeAmount(Double.valueOf(unFreezeAmount));
      cdisContOrder.setRestAmount(Double.valueOf("0.00"));
      //退款
      cdisContOrder.setOrderStatus(9);
      cdisContOrder.setUpdtDate(new Date());
      cdisContOrder.setOperationStatus("SUCCESS");
      cdisContOrder.setReturnDate(new Date());
      //更新主订单
      MpsClientUtil.updateByPrimaryKeySelective(CdisContOrder.class ,cdisContOrder);
      //更新子订单
      CdisContFreeze cdisContFreeze = new CdisContFreeze();
      cdisContFreeze.setOutRequestNo(cdisContOrder.getOutRequestNo());
      cdisContFreeze.setOutOrderNo(cdisContOrder.getOutOrderNo());
      cdisContFreeze.setFundAmount(Double.valueOf(unFreezeAmount));// 本次操作金额
      cdisContFreeze.setTotalUnfreezeAmount(Double.valueOf(unFreezeAmount));
      cdisContFreeze.setRestAmount(Double.valueOf("0.00"));
      cdisContFreeze.setUpdtDate(new Date());
      cdisContFreeze.setOperationStatus("SUCCESS");
      MpsClientUtil.updateByPrimaryKeySelective(CdisContFreeze.class ,cdisContFreeze);

      //添加一条解冻订单日志流水
      CdisFreezeLog cdisFreezeLog = new CdisFreezeLog();
      // 复制属性相同的信息
      BeanUtil.copyProperties(cdisContOrder, cdisFreezeLog);
      cdisFreezeLog.setOutRequestNo(outRequestNo);
      cdisFreezeLog.setFundAmount(Double.valueOf(unFreezeAmount));
      cdisFreezeLog.setTotalUnfreezeAmount(Double.valueOf(unFreezeAmount));
      cdisFreezeLog.setRestAmount(Double.valueOf("0.00"));
      cdisFreezeLog.setOperationType("UNFREEZE");//操作类型
      cdisFreezeLog.setOperationStatus("SUCCESS");
      cdisFreezeLog.setUpdtDate(new Date());
      cdisFreezeLog.setOperationTime(new Date());
      //保存解冻流水日志信息
      MpsClientUtil.insertSelective(CdisFreezeLog.class ,cdisFreezeLog);

      outBizResponse.setSubCode("10000");
      outBizResponse.setSubMsg("outOrderNo=" + outOrderNo + " ,订单解冻操作:" + fundAuthOrderUnfreezeResponse.getSubMsg());
      log.info(JSON.toJSONString(outBizResponse));

    } catch (NumberFormatException e) {
      e.printStackTrace();
      outBizResponse.setSubCode("30006");
      outBizResponse.setSubMsg("订单解冻操作异常");
      log.info("订单解冻操作异常 ," + e);
    }
    return outBizResponse;
  }

  /**
   * 同步操作能力：苏宁订单解冻能力
   * @param jsonBody
   * @param flag
   * @return
   */
  @Async
  public OutBizResponse unicomMallOrderUnfreeze(String jsonBody ,boolean flag) {
    OutBizResponse outBizResponse = new OutBizResponse();

    JSONObject jsonBodyObject = JSONObject.parseObject(jsonBody);
    // 校验必填参数是否为空
    String outOrderNo = jsonBodyObject.getString("outOrderNo");
    if(CommonUtil.isEmpty(outOrderNo)) {
      outBizResponse.setSubCode("30001");
      outBizResponse.setSubMsg("订单编码为空");
      return outBizResponse;
    }

    //获取订单信息
    OrderInfo orderInfo = orderInfoService.getById(outOrderNo);
    if (orderInfo == null) {
      orderInfo = orderInfoService.getByAuthOrderNo(outOrderNo);
    }

    if(orderInfo == null){
      outBizResponse.setSubCode("30002");
      outBizResponse.setSubMsg("订单号[" + outOrderNo + "]对应订单信息为空");
      return outBizResponse;
    }

    //根据订单编码获取pot主订单信息
    CdisContOrder cdisContOrder = MpsClientUtil.selectByPrimaryKey(CdisContOrder.class , orderInfo.getAuthOrderNo());
    if(cdisContOrder == null){
      outBizResponse.setSubCode("30003");
      outBizResponse.setSubMsg("outOrderNo={"+orderInfo.getAuthOrderNo()+"} 订单信息为空");
      return outBizResponse;
    }

    //订单流转状态
    String processStates = orderInfo.getProcessStates();
    if(ProcessStatesEnum.SEND_EQUITY_SUCCESS.getCode().equals(processStates)){
      outBizResponse.setSubCode("30004");
      outBizResponse.setSubMsg("订单[" + outOrderNo + "]已经派发权益成功,不允许进行解冻操作");
      return outBizResponse;
    }

    if(ProcessStatesEnum.DEBIT_LOAN_SUCCESS.getCode().equals(processStates)){
      outBizResponse.setSubCode("30005");
      outBizResponse.setSubMsg("订单[" + outOrderNo + "]已经调拨放款成功,不允许进行解冻操作");
      return outBizResponse;
    }

    //判断订单是否派券成功,派券成功的订单不允许进行解冻操作
    Integer contractStatus = cdisContOrder.getContractStatus();
    if(contractStatus == 2 || -3 == contractStatus) {
      outBizResponse.setSubCode("30006");
      outBizResponse.setSubMsg("订单[" + outOrderNo + "]已经派发权益成功,不允许进行解冻操作");
      return outBizResponse;
    }

    //判断订单是否调拨放款成功,调拨放款成功的订单不允许进行解冻操作
    Integer deployStatus = cdisContOrder.getDeployStatus();
    if(deployStatus == 3) {
      outBizResponse.setSubCode("30007");
      outBizResponse.setSubMsg("订单[" + outOrderNo + "]已经调拨放款成功,不允许进行解冻操作");
      return outBizResponse;
    }

    try {
      // 订单没有同步成功时：同时返销订单解冻用户冻结款
      String freezeAppId = sysConfigService.getByCode("freezeAppId");
      PromotionAccountInfo promotionAccountInfo = promotionAccountInfoService.getByAppId(freezeAppId);
      String authNo = orderInfo.getAuthNo();
      String outRequestNo = DateUtil.formatDate(new Date() ,"yyyyMMddHHmmssSSS") + RandomUtil.randomNumbers(5);
      String amount = orderInfo.getTotalFee().toString();
      String remark = DateUtil.formatDate(new Date() ,"yyyy-MM-dd HH:mm:ss") + " ,解冻 " + amount + " 元";
      AlipayFundAuthOrderUnfreezeResponse fundAuthOrderUnfreezeResponse = aliService.fundAuthOrderUnfreeze(promotionAccountInfo , authNo ,outRequestNo ,amount ,remark);
      if (!fundAuthOrderUnfreezeResponse.isSuccess()) {
        outBizResponse.setSubCode("30008");
        outBizResponse.setSubMsg("订单解冻操作:" + fundAuthOrderUnfreezeResponse.getMsg() + " ," + fundAuthOrderUnfreezeResponse.getSubMsg());
        return outBizResponse;
      }

      //更新订单对应的金额信息
      orderInfo.setProcessStates(ProcessStatesEnum.UNFREEZE_REFUND_ORDER.getCode());
      orderInfo.setIsPaySuccess("0");
      orderInfo.setStatus(3);
      orderInfo.setUpdateTime(new Date());
      orderInfoService.updateById(orderInfo);

      //更新pot相关的订单
      String unFreezeAmount = fundAuthOrderUnfreezeResponse.getAmount();
      cdisContOrder.setFundAmount(Double.valueOf(unFreezeAmount));
      cdisContOrder.setTotalUnfreezeAmount(Double.valueOf(unFreezeAmount));
      cdisContOrder.setRestAmount(Double.valueOf("0.00"));
      //退款
      cdisContOrder.setOrderStatus(9);
      cdisContOrder.setUpdtDate(new Date());
      cdisContOrder.setOperationStatus("SUCCESS");
      cdisContOrder.setReturnDate(new Date());
      //更新主订单
      MpsClientUtil.updateByPrimaryKeySelective(CdisContOrder.class ,cdisContOrder);
      //更新子订单
      CdisContFreeze cdisContFreeze = new CdisContFreeze();
      cdisContFreeze.setOutRequestNo(cdisContOrder.getOutRequestNo());
      cdisContFreeze.setOutOrderNo(cdisContOrder.getOutOrderNo());
      cdisContFreeze.setFundAmount(Double.valueOf(unFreezeAmount));// 本次操作金额
      cdisContFreeze.setTotalUnfreezeAmount(Double.valueOf(unFreezeAmount));
      cdisContFreeze.setRestAmount(Double.valueOf("0.00"));
      cdisContFreeze.setUpdtDate(new Date());
      cdisContFreeze.setOperationStatus("SUCCESS");
      MpsClientUtil.updateByPrimaryKeySelective(CdisContFreeze.class ,cdisContFreeze);

      //添加一条解冻订单日志流水
      CdisFreezeLog cdisFreezeLog = new CdisFreezeLog();
      // 复制属性相同的信息
      BeanUtil.copyProperties(cdisContOrder, cdisFreezeLog);
      cdisFreezeLog.setOutRequestNo(outRequestNo);
      cdisFreezeLog.setFundAmount(Double.valueOf(unFreezeAmount));
      cdisFreezeLog.setTotalUnfreezeAmount(Double.valueOf(unFreezeAmount));
      cdisFreezeLog.setRestAmount(Double.valueOf("0.00"));
      cdisFreezeLog.setOperationType("UNFREEZE");//操作类型
      cdisFreezeLog.setOperationStatus("SUCCESS");
      cdisFreezeLog.setUpdtDate(new Date());
      cdisFreezeLog.setOperationTime(new Date());
      //保存解冻流水日志信息
      MpsClientUtil.insertSelective(CdisFreezeLog.class ,cdisFreezeLog);

      outBizResponse.setSubCode("10000");
      outBizResponse.setSubMsg("outOrderNo=" + outOrderNo + " ,订单解冻操作:" + fundAuthOrderUnfreezeResponse.getSubMsg());
      log.info(JSON.toJSONString(outBizResponse));

    } catch (NumberFormatException e) {
      e.printStackTrace();
      outBizResponse.setSubCode("30009");
      outBizResponse.setSubMsg("订单解冻操作异常");
      log.info("订单解冻操作异常 ," + e);
    }
    return outBizResponse;
  }

  /**
   * 查询联通苏宁新用户入网订单号卡的消息状态
   * @param jsonBody
   * @return
   */
  public OutBizResponse snCardQueryByMobile(String jsonBody) {
    OutBizResponse outBizResponse = new OutBizResponse();

    JSONObject jsonBodyObject = JSONObject.parseObject(jsonBody);
    // 校验必填参数是否为空
    String unicomPhoneNo = jsonBodyObject.getString("unicomPhoneNo");
    if(CommonUtil.isEmpty(unicomPhoneNo)) {
      outBizResponse.setSubCode("30001");
      outBizResponse.setSubMsg("联通电话号码为空");
      return outBizResponse;
    }

    JSONObject actJSON = new JSONObject();
    actJSON.put("outSysNo", outSysNo);
    actJSON.put("method", "card.query.by.mobile");
    actJSON.put("format", format);
    actJSON.put("charset", charset);
    actJSON.put("timestamp", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
    actJSON.put("version", version);
    actJSON.put("signType", signType);

    JSONObject bizContentJSON = new JSONObject();
    JSONObject outContentJSON = new JSONObject();
    bizContentJSON.put("unicomPhoneNo", unicomPhoneNo);

    //封装加签内容
    actJSON.put("bizContent", bizContentJSON);
    actJSON.put("outContent", outContentJSON);
    String signContent = SignContentUtil.getSignContent(0 ,bizContentJSON , outContentJSON);
    //加签
    String rsaSign = RSAEncrypt.sign(signContent , signType , privateKey, charset);

    actJSON.put("sign", rsaSign);
    log.info("<请求数据>" + JSON.toJSONString(actJSON));
    String result = null;
    try {
      String requestUrl = sysConfigService.getByCode("snCardQueryMobileUrl") + "/haoka/card.entrance";
      result = HttpUtil.sendPostBody(requestUrl, JSON.toJSONString(actJSON));
    } catch (IOException e) {
      e.printStackTrace();
      outBizResponse.setSubCode("30002");
      outBizResponse.setSubMsg("获取号卡消息异常");
      return outBizResponse;
    }

    if(CommonUtil.isEmpty(result)){
      outBizResponse.setSubCode("30003");
      outBizResponse.setSubMsg("获取号卡消息为空");
      return outBizResponse;
    }

    //转化响应json
    JSONObject responeObject = JSONObject.parseObject(result);
    if(!"10000".equals(responeObject.getString("subCode")) || !"10000".equals(responeObject.getString("code"))){
      outBizResponse.setSubCode("30004");
      outBizResponse.setSubMsg(responeObject.getString("subMsg"));
      return outBizResponse;
    }

    JSONObject dataObject = responeObject.getJSONObject("data");
    //根据号码和联通订单号查询是不是苏宁的订单信息
    String mobile = dataObject.getString("mobile");
    String orderNo = dataObject.getString("orderNo");
    OrderInfo orderInfo = orderInfoService.queryOrderInfoByMobileAndUnicomOrderNo(mobile ,orderNo);
    if(orderInfo == null){
      outBizResponse.setSubCode("30005");
      outBizResponse.setSubMsg("订单信息为空");
      return outBizResponse;
    }

    String businessType = orderInfo.getBusinessType();
    if(!"xyhrw".equals(businessType)){
      outBizResponse.setSubCode("30005");
      outBizResponse.setSubMsg("该订单不是苏宁充送新入网订单");
      return outBizResponse;
    }

    String custNo = orderInfo.getCustNo();
    CustomerInfo customerInfo = customerInfoService.getById(custNo);
    if(customerInfo == null){
      outBizResponse.setSubCode("30005");
      outBizResponse.setSubMsg("订单用户信息为空");
      return outBizResponse;
    }

    WopayFtp wopayFtp = null;
    if("1".equals(dataObject.getString("state"))){
      //激活
      orderInfo.setOrderLineId(dataObject.getString("openId"));
      orderInfo.setProcessStates(ProcessStatesEnum.KEEPERESULT_COMPLETE.getCode());
      orderInfo.setUpdateTime(new Date());
      orderInfo.setStatus(2);

      //拼接推送WSN系统请求实体
      wopayFtp = InInterfaceServiceTool.orderSyncWsnForftpAo(orderInfo ,customerInfo);

    }else if("2".equals(dataObject.getString("state"))){
      //退卡
//      orderInfo.setProcessStates(ProcessStatesEnum.UNFREEZE_REFUND_ORDER.getCode());
//      orderInfo.setStatus(3);
      orderInfo.setUpdateTime(new Date());
    }else if("C1".equals(dataObject.getString("state"))){
      //开户完成
      if(!CommonUtil.isEmpty(dataObject.getString("openId"))){
        //激活了,有cbss订单号返回系统
        //激活
        orderInfo.setOrderLineId(dataObject.getString("openId"));
        orderInfo.setProcessStates(ProcessStatesEnum.KEEPERESULT_COMPLETE.getCode());
        orderInfo.setStatus(2);
        orderInfo.setUpdateTime(new Date());

        //拼接推送WSN系统请求实体
        wopayFtp = InInterfaceServiceTool.orderSyncWsnForftpAo(orderInfo ,customerInfo);

      }

    }

    //如果为退单状态、派券成功的号卡订单,冻结转支付(调拨系统能力)
//    if("2".equals(dataObject.getString("state"))){
//      DeployOrderPayUnfreezeRefundAo deployOrderPayUnfreezeRefundAo = new DeployOrderPayUnfreezeRefundAo();
//
//      //根据运营商编码获取政策信息
//      String lpunicom = OperatorEnum.lpsuningunicom.getCode();
//      CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpunicom);
//      //调用pot获取苏宁新入网政策列表信息
//      String outProtNo = cardCenterInterfInfo.getOutPortNo();
//      ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo , outProtNo ,false);
//      if(null == policyListResponse || !"10000".equals(policyListResponse.getCode())){
//        outBizResponse.setSubCode("30007");
//        outBizResponse.setSubMsg(policyListResponse.getMessage());
//        return outBizResponse;
//      }
//
//      PolicyListResponseBizContent policyListResponseBizContent = policyListResponse.getBizContent();
//      List<PolicyListResponseBizContentPolicy> childSpecList = policyListResponseBizContent.getSpecList();
//      // 总政策信息
//      PolicyListResponseBizContent productPolicyVo = new PolicyListResponseBizContent();
//
//      // 获取0级、
//      List<PolicyListResponseBizContentPolicy> zeroList = new ArrayList<PolicyListResponseBizContentPolicy>();
//      // 获取1级
//      List<PolicyListResponseBizContentPolicy> firstList = new ArrayList<PolicyListResponseBizContentPolicy>();
//      // 获取2级
//      List<PolicyListResponseBizContentPolicy> twoList = new ArrayList<PolicyListResponseBizContentPolicy>();
//      // 获取3级
//      List<PolicyListResponseBizContentPolicy> thirdList = new ArrayList<PolicyListResponseBizContentPolicy>();
//      // 获取4级
//      List<PolicyListResponseBizContentPolicy> fourthList = new ArrayList<PolicyListResponseBizContentPolicy>();
//
//      // 遍历获取到的所有政策明细:把对应层级的政策添加到对应级别中
//      for (PolicyListResponseBizContentPolicy cdisSpec : childSpecList) {
//        // 把0级的设置到zeroList中
//        if (cdisSpec.getPrnId().equals("0")) {
//          zeroList.add(cdisSpec);
//        }
//
//        // 把1级的设置到firstList中
//        if (cdisSpec.getLevel() == 1) {
//          firstList.add(cdisSpec);
//        }
//
//        // 把2级的设置到twoList中
//        if (cdisSpec.getLevel() == 2) {
//          twoList.add(cdisSpec);
//        }
//
//        // 把3级的设置到thirdList中
//        if (cdisSpec.getLevel() == 3) {
//          thirdList.add(cdisSpec);
//        }
//
//        // 把4级的设置到fourthList中
//        if (cdisSpec.getLevel() == 4) {
//          fourthList.add(cdisSpec);
//        }
//
//      }
//
//      // 把4级对应的政策设置到3级中
//      if (thirdList.size() > 0 && fourthList.size() > 0) {
//        for (PolicyListResponseBizContentPolicy thirdSpec : thirdList) {
//          // 设置子政策集合
//          List<PolicyListResponseBizContentPolicy> childList = new ArrayList<PolicyListResponseBizContentPolicy>();
//          for (PolicyListResponseBizContentPolicy fourthSpec : fourthList) {
//            if (thirdSpec.getPsnId().equalsIgnoreCase(fourthSpec.getPrnId())) {
//              childList.add(thirdSpec);
//            }
//          }
//
//          // 把子政策集合添加到对应的父级政策中
//          thirdSpec.setChildList(childList);
//        }
//      }
//
//      // 把3级对应的政策设置到2级中
//      if (twoList.size() > 0 && thirdList.size() > 0) {
//        for (PolicyListResponseBizContentPolicy twoSpec : twoList) {
//          // 设置子政策集合
//          List<PolicyListResponseBizContentPolicy> childList = new ArrayList<PolicyListResponseBizContentPolicy>();
//          for (PolicyListResponseBizContentPolicy thirdSpec : thirdList) {
//            if (twoSpec.getPsnId().equalsIgnoreCase(thirdSpec.getPrnId())) {
//              childList.add(thirdSpec);
//            }
//          }
//
//          // 把子政策集合添加到对应的父级政策中
//          twoSpec.setChildList(childList);
//        }
//      }
//
//      // 把2级对应的政策设置到1级中
//      if (firstList.size() > 0 && twoList.size() > 0) {
//        for (PolicyListResponseBizContentPolicy firstSpec : firstList) {
//          // 设置子政策集合
//          List<PolicyListResponseBizContentPolicy> childList = new ArrayList<PolicyListResponseBizContentPolicy>();
//          for (PolicyListResponseBizContentPolicy twoSpec : twoList) {
//            if (firstSpec.getPsnId().equalsIgnoreCase(twoSpec.getPrnId())) {
//              childList.add(twoSpec);
//            }
//          }
//
//          // 把子政策集合添加到对应的父级政策中
//          firstSpec.setChildList(childList);
//        }
//      }
//
//      // 把1级对应的政策设置到0级中
//      if (firstList.size() > 0 && zeroList.size() > 0) {
//        for (PolicyListResponseBizContentPolicy zeroSpec : zeroList) {
//          // 设置子政策集合
//          List<PolicyListResponseBizContentPolicy> childList = new ArrayList<PolicyListResponseBizContentPolicy>();
//          for (PolicyListResponseBizContentPolicy firstSpec : firstList) {
//            if (zeroSpec.getPsnId().equalsIgnoreCase(firstSpec.getPrnId())) {
//              childList.add(firstSpec);
//            }
//          }
//
//          // 把子政策集合添加到对应的父级政策中
//          zeroSpec.setChildList(childList);
//        }
//      }
//
//      //把整理的政策信息设置到实体中
//      PolicyListResponseBizContentProductPolicy productPolicy =policyListResponseBizContent.getCdisProductPolicy();
//      productPolicyVo.setCdisProductPolicy(productPolicy);
//      productPolicyVo.setSpecList(zeroList);
//      if (productPolicyVo == null) {
//        outBizResponse.setSubCode("30008");
//        outBizResponse.setSubMsg("产品政策详情为空");
//        return outBizResponse;
//      }
//
//      //获取本订单对应的0级政策编码
//      String zeroSpecPsnId = orderInfo.getSpecPsnId();
//
//      //遍历子元素中所有的二级政策
//      String deployAmount = "";
//      for (PolicyListResponseBizContentPolicy cdisSpec : zeroList) {
//        //获取0级
//        //判断是否是对应套餐的权益政策
//        if(zeroSpecPsnId.equals(cdisSpec.getPsnId())) {
//
//          List<PolicyListResponseBizContentPolicy> childLv1List = cdisSpec.getChildList();
//          //遍历一级政策
//          for (PolicyListResponseBizContentPolicy cdisSpecLv1 : childLv1List) {
//            List<PolicyListResponseBizContentPolicy> childListv2 = cdisSpecLv1.getChildList();
//            //遍历二级子政策所有权益数据
//            for (PolicyListResponseBizContentPolicy cdisSpecLv2 : childListv2) {
//
//              //节点表识，明细类型：1-下单明细，2-垫支明细，3-趸交明细，4-分期明细,5-退款明细，6-权益明细
//              if(5 == Integer.valueOf(cdisSpecLv2.getCheckFlag())) {
//                //退款标识/派券权益标识：“PAY_UNFREEZE_MODEL”为先派劵后激活、“PAY_MODEL”为先激活后派劵
//                deployOrderPayUnfreezeRefundAo.setOutOrderNo(orderInfo.getAuthOrderNo());
//                deployOrderPayUnfreezeRefundAo.setViolateHandleType(cdisSpecLv2.getKeyStr());
//                deployOrderPayUnfreezeRefundAo.setViolateReceivables(new BigDecimal(cdisSpecLv2.getValueStr()));
//                deployAmount = cdisSpecLv2.getValueStr();
//              }
//
//            }
//          }
//        }
//      }
//
//      if(deployOrderPayUnfreezeRefundAo == null || StringUtils.isEmpty(deployAmount)){
//        outBizResponse.setSubCode("30009");
//        outBizResponse.setSubMsg("订单退款返销政策配置不正确,请校验政策的正确性");
//        return outBizResponse;
//      }
//
//      ApiInBizResponseMsg apiInBizResponseMsg = inInterfaceService.deployOrderPayAndUnfreezeRefund(deployOrderPayUnfreezeRefundAo);
//      if(!"10001".equals(apiInBizResponseMsg.getSubCode())){
//        outBizResponse.setSubCode("30010");
//        outBizResponse.setSubMsg("订单退款返销失败：" + apiInBizResponseMsg.getSubMsg());
//        return outBizResponse;
//      }
//
//      //返销成功的订单进行订单的信息更新
//      Date currDate = new Date();
//      orderInfo.setUpdateTime(currDate);
//      //更新订单信息
//      orderInfoService.updateById(orderInfo);
//
//      //计算解冻
//      Double totalUnfreezeAmount = orderInfo.getTotalFee().doubleValue() - Double.valueOf(deployAmount);
//      //更新pot的订单信息
//      CdisContOrder cdisContOrder = new CdisContOrder();
//      cdisContOrder.setOutOrderNo(orderInfo.getAuthOrderNo());
//      cdisContOrder.setOutRequestNo(orderInfo.getAuthRequestNo());
//      cdisContOrder.setOrderStatus(9);
//      cdisContOrder.setTotalUnfreezeAmount(totalUnfreezeAmount);
//      cdisContOrder.setTotalPayAmount(Double.valueOf(deployAmount));
//      cdisContOrder.setRestAmount(Double.valueOf("0.00"));
//      cdisContOrder.setOrderStatus(9);
//      cdisContOrder.setUpdtDate(currDate);
//      cdisContOrder.setReturnDate(currDate);
//      cdisContOrder.setApprove("违约退单返销");
//      cdisContOrder.setOperationStatus("SUCCESS");
//      MpsClientUtil.updateByPrimaryKeySelective(CdisContOrder.class ,cdisContOrder);
//
//      CdisContFreeze cdisContFreeze = new CdisContFreeze();
//      cdisContFreeze.setOutOrderNo(orderInfo.getAuthOrderNo());
//      cdisContFreeze.setOutRequestNo(orderInfo.getAuthRequestNo());
//      cdisContFreeze.setTotalUnfreezeAmount(totalUnfreezeAmount);
//      cdisContFreeze.setTotalPayAmount(Double.valueOf(deployAmount));
//      cdisContFreeze.setRestAmount(Double.valueOf(0.00));
//      cdisContFreeze.setUpdtDate(currDate);
//      cdisContFreeze.setApprove("违约退单返销");
//      cdisContFreeze.setOperationStatus("SUCCESS");
//      //更新子订单信息
//      MpsClientUtil.updateByPrimaryKeySelective(CdisContFreeze.class ,cdisContFreeze);
//
//      //TODO:推送订单违约通知给苏宁易购系统
//      TreeMap<String, Object> paramMap = new TreeMap<>();
//      paramMap.put("outTradeNo", IdGeneratorSnowflake.generateId());//必填、请求流水号
//      paramMap.put("outOrderNo", "");//非必填、订单号(cb订单号、业务系统的校验依据[存在cb订单号不一致的情况])
//      paramMap.put("orderNo", orderInfo.getOutTradeNo());//必填、订单号(乐芃订单号cdis_cont_order主键)
//      paramMap.put("unfrzType", "PAY");//UNFREEZE-解冻、PAY-转支付
//      paramMap.put("authConfirmMode", "NOT_COMPLETE");//非必填、在转支付类型的必填:1.COMPLETE-支付宝自动解冻剩余金额、2.NOT_COMPLETE-接入系统自行调用接口解冻
//      paramMap.put("month", "ALL");//必填、期数：1.实际期数、2.若全部则填ALL(建议忽略大小写判断)
//      paramMap.put("foundAmount", "-1");//必填、金额：1.实际金额、2.期数为ALL的时候剩余金额一定为-1
//      paramMap.put("phoneNo", orderInfo.getPhone());//必填、手机号码(业务系统的校验依据)
//
//      //2020-07-16通知苏宁订单毁约 tangxiong
//      SuningOrderKeepresultParam suningOrderKeepresultParam = TmallOrderKeepresultServiceImpl.assemblySuningOrder(paramMap);
//      if (suningOrderKeepresultParam == null) {
//        log.info("不支持的订单操作类型,无法通知苏宁系统：" + orderInfo.getOutTradeNo());
//        outBizResponse.setSubCode("30001");
//        outBizResponse.setSubMsg("不支持的订单操作类型,无法通知苏宁系统");
//        return outBizResponse;
//      }
//      suningOrderKeepresultParam.setOut_trade_no(orderInfo.getOutTradeNo());
//      log.info("苏宁履约、毁约接口请求参数：{}", JSONObject.toJSONString(suningOrderKeepresultParam));
//      OutApiResponse outApiResponse = tmallOrderKeepresultService.suningOrderKeepresultDispose(suningOrderKeepresultParam);
//      log.info("苏宁履约、毁约接口响应参数：{}", JSONObject.toJSONString(outApiResponse));
//      String retCode = outApiResponse == null ? "40000" : outApiResponse.getRetCode();
//      //通知苏宁之后，响应参数进行入库，记录流水状态
//      TmllOrderOprationLogParam tmllOrderOprationLogParam = TmallOrderKeepresultServiceImpl.assemblyTmallOpreationParam(paramMap);
//      tmllOrderOprationLogParam.setTmallOrderNo(orderInfo.getOutTradeNo());
//      tmllOrderOprationLogParam.setTmallStatus(retCode);
//      tmllOrderOprationLogParam.setTmallRequest(JSONObject.toJSONString(suningOrderKeepresultParam));
//      tmllOrderOprationLogParam.setTmallResponse(JSONObject.toJSONString(outApiResponse));
//      tmllOrderOprationLogService.add(tmllOrderOprationLogParam);
//      log.info("苏宁订单操作日志入库成功：" + orderInfo.getOutTradeNo());
//      if ("0000".equals(retCode)) {
//        outBizResponse.setSubCode("10000");
//        outBizResponse.setSubMsg("订单返销成功");
//      }else{
//        outBizResponse.setSubCode("30006");
//        outBizResponse.setSubMsg("苏宁订单毁约通知失败");
//      }
//      return outBizResponse;
//    }else

    if(wopayFtp != null){
      //注释同步ftp，只推送wsn能力
      //推送ftp信息到wsn系统 2020-10-12取消推送ftp信息到wsn系统
      //tmllOrderDisposeService.orderSyncWsnForftp(wopayFtp);
      //订单完成时间
      Date finishDate = new Date();
      //推送订单信息到wsn系统
      WsnOrderInfoVo wsnOrderInfoVo = new WsnOrderInfoVo();
      // 复制属性相同的信息
      BeanUtil.copyProperties(orderInfo, wsnOrderInfoVo);
      wsnOrderInfoVo.setField1("{}");
      wsnOrderInfoVo.setOrderFrom("suninggift");
      wsnOrderInfoVo.setOperatorCode("unicom");
      wsnOrderInfoVo.setStatus(2);
      CdisArea provinceArea = MpsClientUtil.selectByPrimaryKey(CdisArea.class ,orderInfo.getProvinceCode());
      //联通省编码
      wsnOrderInfoVo.setProvinceCode(provinceArea.getWopayCode());
      wsnOrderInfoVo.setProvince(provinceArea.getAreaName());
      CdisArea cityArea = MpsClientUtil.selectByPrimaryKey(CdisArea.class ,orderInfo.getCityCode());
      //联通市编码
      wsnOrderInfoVo.setCityCode(cityArea.getWopayCode());
      wsnOrderInfoVo.setCity(cityArea.getAreaName());
      wsnOrderInfoVo.setContractId(orderInfo.getProductId());
      wsnOrderInfoVo.setOrderVersion("02");//版本号
      wsnOrderInfoVo.setProvOrderId(orderInfo.getOrderLineId());//cbss订单号
      wsnOrderInfoVo.setFinishDate(finishDate);
      tmllOrderDisposeService.orderSyncWsnForOrderInfo(wsnOrderInfoVo);
//      JSONObject resultObject = JSONObject.parseObject(responeResult);
//      if(!"10000".equals(resultObject.getString("code"))){
//
//        outBizResponse.setSubCode("30006");
//        outBizResponse.setSubMsg("订单同步wsn系统失败");
//        return outBizResponse;
//      }

      orderInfo.setGoWsn("1");
      orderInfo.setUpdateTime(new Date());
      orderInfo.setProvince(provinceArea.getAreaName());
      orderInfo.setCity(cityArea.getAreaName());
      orderInfo.setFinishDate(finishDate);
      orderInfo.setContractTime(finishDate);
      //更新订单信息
      orderInfoService.updateById(orderInfo);
      //更新pot的订单信息
      CdisContOrder cdisContOrder = new CdisContOrder();
      cdisContOrder.setOutOrderNo(orderInfo.getAuthOrderNo());
      cdisContOrder.setOutRequestNo(orderInfo.getAuthRequestNo());
      cdisContOrder.setOrderStatus(12);
      cdisContOrder.setContractOrderId(dataObject.getString("openId"));
      cdisContOrder.setMerchantOrderNo(dataObject.getString("openId"));
      cdisContOrder.setUpdtDate(new Date());

      MpsClientUtil.updateByPrimaryKeySelective(CdisContOrder.class ,cdisContOrder);

      outBizResponse.setSubCode("10000");
      outBizResponse.setSubMsg("订单同步wsn系统成功");
      return outBizResponse;
    }

    outBizResponse.setSubCode("30007");
    outBizResponse.setSubMsg("订单处理失败,请校验订单实情");
    return outBizResponse;
  }

  /**
   * 查询苏宁新用户入网订单在12天都没有激活的数据进行返销操作
   * @param jsonBody
   * @return
   */
  public OutBizResponse taskSuNingNewUnicomOrderRefund(String jsonBody ,OrderInfo orderInfo) {
    OutBizResponse outBizResponse = new OutBizResponse();

    JSONObject jsonBodyObject = JSONObject.parseObject(jsonBody);
    // 校验必填参数是否为空
    String unicomPhoneNo = jsonBodyObject.getString("unicomPhoneNo");
    if(CommonUtil.isEmpty(unicomPhoneNo)) {
      outBizResponse.setSubCode("30001");
      outBizResponse.setSubMsg("联通电话号码为空");
      return outBizResponse;
    }

    JSONObject actJSON = new JSONObject();
    actJSON.put("outSysNo", outSysNo);
    actJSON.put("method", "card.query.by.mobile");
    actJSON.put("format", format);
    actJSON.put("charset", charset);
    actJSON.put("timestamp", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
    actJSON.put("version", version);
    actJSON.put("signType", signType);

    JSONObject bizContentJSON = new JSONObject();
    JSONObject outContentJSON = new JSONObject();
    bizContentJSON.put("unicomPhoneNo", unicomPhoneNo);

    //封装加签内容
    actJSON.put("bizContent", bizContentJSON);
    actJSON.put("outContent", outContentJSON);
    String signContent = SignContentUtil.getSignContent(0 ,bizContentJSON , outContentJSON);
    //加签
    String rsaSign = RSAEncrypt.sign(signContent , signType , privateKey, charset);

    actJSON.put("sign", rsaSign);
    log.info("<请求数据>" + JSON.toJSONString(actJSON));
    String result = null;
    try {
      String requestUrl = sysConfigService.getByCode("snCardQueryMobileUrl") + "/haoka/card.entrance";
      result = HttpUtil.sendPostBody(requestUrl, JSON.toJSONString(actJSON));
    } catch (IOException e) {
      e.printStackTrace();
      outBizResponse.setSubCode("30002");
      outBizResponse.setSubMsg("获取号卡消息异常");
      return outBizResponse;
    }

    if(CommonUtil.isEmpty(result)){
      outBizResponse.setSubCode("30003");
      outBizResponse.setSubMsg("获取号卡消息为空");
      return outBizResponse;
    }

    /**
     * 超时扣罚状态查询及判断规则
     *   A、下单成功后，启动定时器，触发12天激活期。如10日订单，于23日凌晨为超时。
     *   B、定时器到点后，查看订单号码状态：
     *   ①【激活】状态，订单不触发任何操作。
     *   ②【退卡】状态，启动业务扣罚返销流程，扣罚订单权益金额（根据政策配置），解冻剩余冻结款。
     */
    //转化响应json
    JSONObject responeObject = JSONObject.parseObject(result);
    if("10000".equals(responeObject.getString("subCode")) && "10000".equals(responeObject.getString("code"))){
      JSONObject dataObject = responeObject.getJSONObject("data");
      if("1".equals(dataObject.getString("state"))){
        outBizResponse.setSubCode("30004");
        outBizResponse.setSubMsg("订单号卡已经激活:state=" + dataObject.getString("state"));
        return outBizResponse;
      }
//      else if("2".equals(dataObject.getString("state"))){
//        outBizResponse.setSubCode("30005");
//        outBizResponse.setSubMsg("订单号卡已经退单:state=" + dataObject.getString("state"));
//        return outBizResponse;
//      }
    }
//    else if("30001".equals(responeObject.getString("subCode")) && "30001".equals(responeObject.getString("code"))){
//      outBizResponse.setSubCode("30005");
//      outBizResponse.setSubMsg("订单号卡不存在激活状态,不做业务返销处理");
//      return outBizResponse;
//    }

    String businessType = orderInfo.getBusinessType();
    if(!"xyhrw".equals(businessType)){
      outBizResponse.setSubCode("30006");
      outBizResponse.setSubMsg("该订单不是苏宁充送新入网订单");
      return outBizResponse;
    }

    String custNo = orderInfo.getCustNo();
    CustomerInfo customerInfo = customerInfoService.getById(custNo);
    if(customerInfo == null){
      outBizResponse.setSubCode("30007");
      outBizResponse.setSubMsg("订单用户信息为空");
      return outBizResponse;
    }

    //更新pot的订单信息
    CdisContOrder cdisContOrderNew = new CdisContOrder();
    cdisContOrderNew.setOutOrderNo(orderInfo.getAuthOrderNo());
    cdisContOrderNew.setOutRequestNo(orderInfo.getAuthRequestNo());
    //根据订单编码查询pot主订单信息
    CdisContOrder cdisContOrderOld = MpsClientUtil.selectOne(CdisContOrder.class ,cdisContOrderNew);
    if(cdisContOrderOld != null){
      //如果订单状态为“返销成功”状态，把MySQL库的订单状态更新为退单状态，并且同步信息给苏宁系统
      Integer orderStatus = cdisContOrderOld.getOrderStatus();
      if(orderStatus == 9){

        //退卡
        orderInfo.setProcessStates(ProcessStatesEnum.UNFREEZE_REFUND_ORDER.getCode());
        orderInfo.setStatus(3);
        orderInfo.setUpdateTime(new Date());
        //更新订单信息
        orderInfoService.updateById(orderInfo);

        //推送订单违约通知给苏宁易购系统
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("outTradeNo", IdGeneratorSnowflake.generateId());//必填、请求流水号
        paramMap.put("outOrderNo", "");//非必填、订单号(cb订单号、业务系统的校验依据[存在cb订单号不一致的情况])
        paramMap.put("orderNo", orderInfo.getOutTradeNo());//必填、订单号(乐芃订单号cdis_cont_order主键)
        paramMap.put("unfrzType", "PAY");//UNFREEZE-解冻、PAY-转支付
        paramMap.put("authConfirmMode", "NOT_COMPLETE");//非必填、在转支付类型的必填:1.COMPLETE-支付宝自动解冻剩余金额、2.NOT_COMPLETE-接入系统自行调用接口解冻
        paramMap.put("month", "all");//必填、期数：1.实际期数、2.若全部则填ALL(建议忽略大小写判断)
        paramMap.put("foundAmount", "-1");//必填、金额：1.实际金额、2.期数为ALL的时候剩余金额一定为-1
        paramMap.put("phoneNo", orderInfo.getPhone());//必填、手机号码(业务系统的校验依据)

        //2020-07-16通知苏宁订单毁约 tangxiong
        SuningOrderKeepresultParam suningOrderKeepresultParam = TmallOrderKeepresultServiceImpl.assemblySuningOrder(paramMap);
        if (suningOrderKeepresultParam == null) {
          log.info("不支持的订单操作类型,无法通知苏宁系统：" + orderInfo.getOutTradeNo());
          outBizResponse.setSubCode("30012");
          outBizResponse.setSubMsg("不支持的订单操作类型,无法通知苏宁系统");
          return outBizResponse;
        }
        suningOrderKeepresultParam.setOut_trade_no(orderInfo.getOutTradeNo());
        log.info("苏宁履约、毁约接口请求参数：{}", JSONObject.toJSONString(suningOrderKeepresultParam));
        OutApiResponse outApiResponse = tmallOrderKeepresultService.suningOrderKeepresultDispose(suningOrderKeepresultParam);
        log.info("苏宁履约、毁约接口响应参数：{}", JSONObject.toJSONString(outApiResponse));
        String retCode = outApiResponse == null ? "40000" : outApiResponse.getRetCode();
        //通知苏宁之后，响应参数进行入库，记录流水状态
        TmllOrderOprationLogParam tmllOrderOprationLogParam = TmallOrderKeepresultServiceImpl.assemblyTmallOpreationParam(paramMap);
        tmllOrderOprationLogParam.setTmallOrderNo(orderInfo.getOutTradeNo());
        tmllOrderOprationLogParam.setTmallStatus(retCode);
        tmllOrderOprationLogParam.setTmallRequest(JSONObject.toJSONString(suningOrderKeepresultParam));
        tmllOrderOprationLogParam.setTmallResponse(JSONObject.toJSONString(outApiResponse));
        tmllOrderOprationLogService.add(tmllOrderOprationLogParam);
        log.info("苏宁订单操作日志入库成功：" + orderInfo.getOutTradeNo());
        if ("0000".equals(retCode)) {
          outBizResponse.setSubCode("10000");
          outBizResponse.setSubMsg("订单返销成功");
        }else{
          outBizResponse.setSubCode("30013");
          outBizResponse.setSubMsg("苏宁订单毁约通知失败");
        }

        return outBizResponse;
      }

    }


    //退卡
    orderInfo.setProcessStates(ProcessStatesEnum.UNFREEZE_REFUND_ORDER.getCode());
    orderInfo.setStatus(3);
    orderInfo.setUpdateTime(new Date());

    DeployOrderPayUnfreezeRefundAo deployOrderPayUnfreezeRefundAo = new DeployOrderPayUnfreezeRefundAo();

    //根据运营商编码获取政策信息
    String lpunicom = OperatorEnum.lpsuningunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpunicom);
    //调用pot获取苏宁新入网政策列表信息
    String outProtNo = cardCenterInterfInfo.getOutPortNo();
    ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo , outProtNo ,false);
    if(null == policyListResponse || !"10000".equals(policyListResponse.getCode())){
      outBizResponse.setSubCode("30008");
      outBizResponse.setSubMsg(policyListResponse.getMessage());
      return outBizResponse;
    }

    PolicyListResponseBizContent policyListResponseBizContent = policyListResponse.getBizContent();
    List<PolicyListResponseBizContentPolicy> childSpecList = policyListResponseBizContent.getSpecList();
    // 总政策信息
    PolicyListResponseBizContent productPolicyVo = new PolicyListResponseBizContent();

    // 获取0级、
    List<PolicyListResponseBizContentPolicy> zeroList = new ArrayList<PolicyListResponseBizContentPolicy>();
    // 获取1级
    List<PolicyListResponseBizContentPolicy> firstList = new ArrayList<PolicyListResponseBizContentPolicy>();
    // 获取2级
    List<PolicyListResponseBizContentPolicy> twoList = new ArrayList<PolicyListResponseBizContentPolicy>();
    // 获取3级
    List<PolicyListResponseBizContentPolicy> thirdList = new ArrayList<PolicyListResponseBizContentPolicy>();
    // 获取4级
    List<PolicyListResponseBizContentPolicy> fourthList = new ArrayList<PolicyListResponseBizContentPolicy>();

    // 遍历获取到的所有政策明细:把对应层级的政策添加到对应级别中
    for (PolicyListResponseBizContentPolicy cdisSpec : childSpecList) {
      // 把0级的设置到zeroList中
      if (cdisSpec.getPrnId().equals("0")) {
        zeroList.add(cdisSpec);
      }

      // 把1级的设置到firstList中
      if (cdisSpec.getLevel() == 1) {
        firstList.add(cdisSpec);
      }

      // 把2级的设置到twoList中
      if (cdisSpec.getLevel() == 2) {
        twoList.add(cdisSpec);
      }

      // 把3级的设置到thirdList中
      if (cdisSpec.getLevel() == 3) {
        thirdList.add(cdisSpec);
      }

      // 把4级的设置到fourthList中
      if (cdisSpec.getLevel() == 4) {
        fourthList.add(cdisSpec);
      }

    }

    // 把4级对应的政策设置到3级中
    if (thirdList.size() > 0 && fourthList.size() > 0) {
      for (PolicyListResponseBizContentPolicy thirdSpec : thirdList) {
        // 设置子政策集合
        List<PolicyListResponseBizContentPolicy> childList = new ArrayList<PolicyListResponseBizContentPolicy>();
        for (PolicyListResponseBizContentPolicy fourthSpec : fourthList) {
          if (thirdSpec.getPsnId().equalsIgnoreCase(fourthSpec.getPrnId())) {
            childList.add(fourthSpec);
          }
        }

        // 把子政策集合添加到对应的父级政策中
        thirdSpec.setChildList(childList);
      }
    }

    // 把3级对应的政策设置到2级中
    if (twoList.size() > 0 && thirdList.size() > 0) {
      for (PolicyListResponseBizContentPolicy twoSpec : twoList) {
        // 设置子政策集合
        List<PolicyListResponseBizContentPolicy> childList = new ArrayList<PolicyListResponseBizContentPolicy>();
        for (PolicyListResponseBizContentPolicy thirdSpec : thirdList) {
          if (twoSpec.getPsnId().equalsIgnoreCase(thirdSpec.getPrnId())) {
            childList.add(thirdSpec);
          }
        }

        // 把子政策集合添加到对应的父级政策中
        twoSpec.setChildList(childList);
      }
    }

    // 把2级对应的政策设置到1级中
    if (firstList.size() > 0 && twoList.size() > 0) {
      for (PolicyListResponseBizContentPolicy firstSpec : firstList) {
        // 设置子政策集合
        List<PolicyListResponseBizContentPolicy> childList = new ArrayList<PolicyListResponseBizContentPolicy>();
        for (PolicyListResponseBizContentPolicy twoSpec : twoList) {
          if (firstSpec.getPsnId().equalsIgnoreCase(twoSpec.getPrnId())) {
            childList.add(twoSpec);
          }
        }

        // 把子政策集合添加到对应的父级政策中
        firstSpec.setChildList(childList);
      }
    }

    // 把1级对应的政策设置到0级中
    if (firstList.size() > 0 && zeroList.size() > 0) {
      for (PolicyListResponseBizContentPolicy zeroSpec : zeroList) {
        // 设置子政策集合
        List<PolicyListResponseBizContentPolicy> childList = new ArrayList<PolicyListResponseBizContentPolicy>();
        for (PolicyListResponseBizContentPolicy firstSpec : firstList) {
          if (zeroSpec.getPsnId().equalsIgnoreCase(firstSpec.getPrnId())) {
            childList.add(firstSpec);
          }
        }

        // 把子政策集合添加到对应的父级政策中
        zeroSpec.setChildList(childList);
      }
    }

    //把整理的政策信息设置到实体中
    PolicyListResponseBizContentProductPolicy productPolicy =policyListResponseBizContent.getCdisProductPolicy();
    productPolicyVo.setCdisProductPolicy(productPolicy);
    productPolicyVo.setSpecList(zeroList);
    if (productPolicyVo == null) {
      outBizResponse.setSubCode("30009");
      outBizResponse.setSubMsg("产品政策详情为空");
      return outBizResponse;
    }

    //获取本订单对应的0级政策编码
    String zeroSpecPsnId = orderInfo.getSpecPsnId();

    //遍历子元素中所有的二级政策
    String deployAmount = "";
    for (PolicyListResponseBizContentPolicy cdisSpec : zeroList) {
      //获取0级
      //判断是否是对应套餐的权益政策
      if(zeroSpecPsnId.equals(cdisSpec.getPsnId())) {

        List<PolicyListResponseBizContentPolicy> childLv1List = cdisSpec.getChildList();
        //遍历一级政策
        for (PolicyListResponseBizContentPolicy cdisSpecLv1 : childLv1List) {
          List<PolicyListResponseBizContentPolicy> childListv2 = cdisSpecLv1.getChildList();
          //遍历二级子政策所有权益数据
          for (PolicyListResponseBizContentPolicy cdisSpecLv2 : childListv2) {

            //节点表识，明细类型：1-下单明细，2-垫支明细，3-趸交明细，4-分期明细,5-退款明细，6-权益明细
            if(5 == Integer.valueOf(cdisSpecLv2.getCheckFlag())) {
              //退款标识/派券权益标识：“PAY_UNFREEZE_MODEL”为先派劵后激活、“PAY_MODEL”为先激活后派劵
              deployOrderPayUnfreezeRefundAo.setOutOrderNo(orderInfo.getAuthOrderNo());
              deployOrderPayUnfreezeRefundAo.setViolateHandleType(cdisSpecLv2.getKeyStr());
              deployOrderPayUnfreezeRefundAo.setViolateReceivables(new BigDecimal(cdisSpecLv2.getValueStr()));
              deployAmount = cdisSpecLv2.getValueStr();
            }

          }
        }
      }
    }

    if(deployOrderPayUnfreezeRefundAo == null || StringUtils.isEmpty(deployAmount)){
      outBizResponse.setSubCode("30010");
      outBizResponse.setSubMsg("订单退款返销政策配置不正确,请校验政策的正确性");
      return outBizResponse;
    }

    ApiInBizResponseMsg apiInBizResponseMsg = inInterfaceService.deployOrderPayAndUnfreezeRefund(deployOrderPayUnfreezeRefundAo);
    if(!"10001".equals(apiInBizResponseMsg.getSubCode())){
      outBizResponse.setSubCode("30011");
      outBizResponse.setSubMsg("订单退款返销失败：" + apiInBizResponseMsg.getSubMsg());
      return outBizResponse;
    }

    //返销成功的订单进行订单的信息更新
    Date currDate = new Date();
    orderInfo.setUpdateTime(currDate);
    //更新订单信息
    orderInfoService.updateById(orderInfo);

    //计算解冻
    Double totalUnfreezeAmount = orderInfo.getTotalFee().doubleValue() - Double.valueOf(deployAmount);
    //更新pot的订单信息
    CdisContOrder cdisContOrder = new CdisContOrder();
    cdisContOrder.setOutOrderNo(orderInfo.getAuthOrderNo());
    cdisContOrder.setOutRequestNo(orderInfo.getAuthRequestNo());
    cdisContOrder.setOrderStatus(9);
    //cdisContOrder.setTotalUnfreezeAmount(totalUnfreezeAmount);
    cdisContOrder.setTotalPayAmount(Double.valueOf(deployAmount));
    //cdisContOrder.setRestAmount(Double.valueOf("0.00"));
    cdisContOrder.setOrderStatus(9);
    cdisContOrder.setUpdtDate(currDate);
    cdisContOrder.setReturnDate(currDate);
    cdisContOrder.setApprove("违约退单返销");
    cdisContOrder.setOperationStatus("SUCCESS");
    MpsClientUtil.updateByPrimaryKeySelective(CdisContOrder.class ,cdisContOrder);

    CdisContFreeze cdisContFreeze = new CdisContFreeze();
    cdisContFreeze.setOutOrderNo(orderInfo.getAuthOrderNo());
    cdisContFreeze.setOutRequestNo(orderInfo.getAuthRequestNo());
    //cdisContFreeze.setTotalUnfreezeAmount(totalUnfreezeAmount);
    cdisContFreeze.setTotalPayAmount(Double.valueOf(deployAmount));
    //cdisContFreeze.setRestAmount(Double.valueOf(0.00));
    cdisContFreeze.setUpdtDate(currDate);
    cdisContFreeze.setApprove("违约退单返销");
    cdisContFreeze.setOperationStatus("SUCCESS");
    //更新子订单信息
    MpsClientUtil.updateByPrimaryKeySelective(CdisContFreeze.class ,cdisContFreeze);

    //TODO:推送订单违约通知给苏宁易购系统
    TreeMap<String, Object> paramMap = new TreeMap<>();
    paramMap.put("outTradeNo", IdGeneratorSnowflake.generateId());//必填、请求流水号
    paramMap.put("outOrderNo", "");//非必填、订单号(cb订单号、业务系统的校验依据[存在cb订单号不一致的情况])
    paramMap.put("orderNo", orderInfo.getOutTradeNo());//必填、订单号(乐芃订单号cdis_cont_order主键)
    paramMap.put("unfrzType", "PAY");//UNFREEZE-解冻、PAY-转支付
    paramMap.put("authConfirmMode", "NOT_COMPLETE");//非必填、在转支付类型的必填:1.COMPLETE-支付宝自动解冻剩余金额、2.NOT_COMPLETE-接入系统自行调用接口解冻
    paramMap.put("month", "all");//必填、期数：1.实际期数、2.若全部则填ALL(建议忽略大小写判断)
    paramMap.put("foundAmount", "-1");//必填、金额：1.实际金额、2.期数为ALL的时候剩余金额一定为-1
    paramMap.put("phoneNo", orderInfo.getPhone());//必填、手机号码(业务系统的校验依据)

    //2020-07-16通知苏宁订单毁约 tangxiong
    SuningOrderKeepresultParam suningOrderKeepresultParam = TmallOrderKeepresultServiceImpl.assemblySuningOrder(paramMap);
    if (suningOrderKeepresultParam == null) {
      log.info("不支持的订单操作类型,无法通知苏宁系统：" + orderInfo.getOutTradeNo());
      outBizResponse.setSubCode("30012");
      outBizResponse.setSubMsg("不支持的订单操作类型,无法通知苏宁系统");
      return outBizResponse;
    }
    suningOrderKeepresultParam.setOut_trade_no(orderInfo.getOutTradeNo());
    log.info("苏宁履约、毁约接口请求参数：{}", JSONObject.toJSONString(suningOrderKeepresultParam));
    OutApiResponse outApiResponse = tmallOrderKeepresultService.suningOrderKeepresultDispose(suningOrderKeepresultParam);
    log.info("苏宁履约、毁约接口响应参数：{}", JSONObject.toJSONString(outApiResponse));
    String retCode = outApiResponse == null ? "40000" : outApiResponse.getRetCode();
    //通知苏宁之后，响应参数进行入库，记录流水状态
    TmllOrderOprationLogParam tmllOrderOprationLogParam = TmallOrderKeepresultServiceImpl.assemblyTmallOpreationParam(paramMap);
    tmllOrderOprationLogParam.setTmallOrderNo(orderInfo.getOutTradeNo());
    tmllOrderOprationLogParam.setTmallStatus(retCode);
    tmllOrderOprationLogParam.setTmallRequest(JSONObject.toJSONString(suningOrderKeepresultParam));
    tmllOrderOprationLogParam.setTmallResponse(JSONObject.toJSONString(outApiResponse));
    tmllOrderOprationLogService.add(tmllOrderOprationLogParam);
    log.info("苏宁订单操作日志入库成功：" + orderInfo.getOutTradeNo());
    if ("0000".equals(retCode)) {
      outBizResponse.setSubCode("10000");
      outBizResponse.setSubMsg("订单返销成功");
    }else{
      outBizResponse.setSubCode("30013");
      outBizResponse.setSubMsg("苏宁订单毁约通知失败");
    }

    return outBizResponse;
  }

}
