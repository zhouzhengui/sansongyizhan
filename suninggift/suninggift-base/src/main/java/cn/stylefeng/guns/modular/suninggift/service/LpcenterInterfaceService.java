package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.modular.suninggift.entity.CustomerInfo;
import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
import cn.stylefeng.guns.modular.suninggift.enums.OperatorEnum;
import cn.stylefeng.guns.modular.suninggift.enums.ProcessStatesEnum;
import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.NewOrderSyncAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.NewOrderSyncNumberListInfoAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.NewOrderSyncProductListInfoAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.OrderSyncAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.*;
import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.modular.suninggift.tools.PotOrderInfoServiceTool;
import cn.stylefeng.guns.modular.suninggift.tools.SysConfigServiceTool;
import cn.stylefeng.guns.modular.suninggift.utils.CmpayUtil;
import cn.stylefeng.guns.modular.suninggift.utils.CommonUtil;
import cn.stylefeng.guns.modular.suninggift.utils.DateUtil;
import cn.stylefeng.guns.modular.suninggift.utils.LinoHttpUtil;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import cn.stylefeng.guns.sys.modular.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayFundAuthOperationDetailQueryResponse;
import com.gzlplink.cloud.mps.client.model.order.entity.CdisContFreeze;
import com.gzlplink.cloud.mps.client.model.order.entity.CdisContOrder;
import com.gzlplink.cloud.mps.client.model.order.entity.CdisFreezeLog;
import com.gzlplink.cloud.mps.client.util.MpsClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * 苏宁新入网调用乐芃中台接口服务
 */
@Slf4j
@Service
public class LpcenterInterfaceService {

  @Autowired
  private SysConfigService sysConfigService;

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private SysConfigServiceTool sysConfigServiceTool;

  @Autowired
  private InInterfaceService inInterfaceService;

  @Autowired
  private CustomerInfoService customerInfoService;

  @Autowired
  private OrderInfoService orderInfoService;

  @Autowired
  private TmallEquityService tmallEquityService;

  @Autowired
  private PotOrderInfoServiceTool potOrderInfoServiceTool;

  @Autowired
  private AliService aliService;

  @Autowired
  private PromotionAccountInfoService promotionAccountInfoService;

  @Value("${profile}")
  private String profile;

  /**
   * 获取号码列表
   *
   * @param jsonBody
   * @return
   */
  public MessagerVo unicomNumSelect(String jsonBody) {
    MessagerVo inBizRespond = new MessagerVo();

    //获取中台配置信息
    //根据运营商编码获取clientId
    String lpsuningunicom = OperatorEnum.lpsuningunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpsuningunicom);

    log.info("获取号码列表加密前请求报文:" + jsonBody);
    try {
      // 加密处理
      String clientId = cardCenterInterfInfo.getClientId();
      String clientKey = cardCenterInterfInfo.getClientKey();
      String json = CmpayUtil.encryptAgReqData(clientId, clientKey, jsonBody);
      // "http://127.0.0.1:18791/uniline/lpum/gzlp.unicom.num.select";
      String repString = cardCenterInterfInfo.getHaokaUrl() + "/lpcenter/sn.unicom.num.select";
      Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", 10000);
      log.info("获取号码列表加密响应报文:" + JSON.toJSONString(httpPostRsp));

      if (null != httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
        // 解析响应参数
        String responseString = String.valueOf(httpPostRsp.get("msg"));
        JSONObject parseObject = JSON.parseObject(responseString);
        String rspData = parseObject.getString("rspData");

        // 解密响应数据
        String decryptAgRespData = CmpayUtil.decryptAgRespData(clientKey, rspData);
        log.info("获取号码列表解密接口响应报文:" + decryptAgRespData);

        JSONObject decryptAgRespObject = JSONObject.parseObject(decryptAgRespData);
        if (!"10000".equals(decryptAgRespObject.getString("code"))) {
          inBizRespond.setSubCode("40003");
          inBizRespond.setSubMsg(decryptAgRespObject.getString("msg"));
        } else {
          inBizRespond.setSubCode("10000");
          inBizRespond.setSubMsg(decryptAgRespObject.getString("msg"));
          inBizRespond.setData(decryptAgRespObject.getJSONArray("data"));
        }

      } else {
        log.info("获取号码列表请求异常");
        inBizRespond.setSubCode("40004");
        inBizRespond.setSubMsg("获取号码列表请求异常");
      }

    } catch (Exception e) {
      e.printStackTrace();

      inBizRespond.setSubCode("40005");
      inBizRespond.setSubMsg("获取号码列表请求异常");
      log.error(JSON.toJSONString(inBizRespond) + "获取号码列表请求异常:" + e);
    }

    return inBizRespond;
  }

  /**
   * 号码状态变更
   *
   * @param jsonBody
   * @return
   */
  public MessagerVo unicomNumStateChange(String jsonBody) {
    MessagerVo inBizRespond = new MessagerVo();

    //获取中台配置信息
    //根据运营商编码获取clientId
    String lpsuningunicom = OperatorEnum.lpsuningunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpsuningunicom);

    JSONObject bodyObject = JSONObject.parseObject(jsonBody);
    String prokey = bodyObject.getString("prokey");
    if(!StringUtils.isEmpty(prokey)){
      bodyObject.put("proKey" ,prokey);
      bodyObject.remove("prokey");
    }

    log.info("号码状态变更加密前请求报文:" + bodyObject.toJSONString());
    try {
      // 加密处理
      String clientId = cardCenterInterfInfo.getClientId();
      String clientKey = cardCenterInterfInfo.getClientKey();
      String json = CmpayUtil.encryptAgReqData(clientId, clientKey, bodyObject.toJSONString());

      String repString = cardCenterInterfInfo.getHaokaUrl() + "/lpcenter/sn.unicom.num.state.change";
      Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", 10000);
      log.info("号码状态变更加密响应报文:" + JSON.toJSONString(httpPostRsp));

      if (null != httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
        // 解析响应参数
        String responseString = String.valueOf(httpPostRsp.get("msg"));
        JSONObject parseObject = JSON.parseObject(responseString);
        String rspData = parseObject.getString("rspData");

        // 解密响应数据
        String decryptAgRespData = CmpayUtil.decryptAgRespData(clientKey, rspData);
        log.info("号码状态变更解密接口响应报文:" + decryptAgRespData);

        JSONObject decryptAgRespObject = JSONObject.parseObject(decryptAgRespData);
        if (!"10000".equals(decryptAgRespObject.getString("code"))) {
          inBizRespond.setSubCode(decryptAgRespObject.getString("code"));
          inBizRespond.setSubMsg("号码状态变更请求异常," + decryptAgRespObject.getString("msg"));
        } else {
          inBizRespond.setSubCode("10000");
          inBizRespond.setSubMsg(decryptAgRespObject.getString("msg"));
          // 把请求参数返回给前端(包括预占关键字)
          JSONObject jsonBodyObject = decryptAgRespObject.getJSONObject("data");
          jsonBodyObject.remove("certNo");
          jsonBodyObject.remove("operator");
          jsonBodyObject.remove("apiMethod");
          inBizRespond.setData(jsonBodyObject);
        }

      } else {
        log.info("号码状态变更请求异常");
        inBizRespond.setSubCode("40003");
        inBizRespond.setSubMsg("号码状态变更请求失败");

      }

    } catch (Exception e) {
      e.printStackTrace();

      inBizRespond.setSubCode("40004");
      inBizRespond.setSubMsg("号码状态变更请求异常");
      log.error(JSON.toJSONString(inBizRespond) + "号码状态变更请求异常:" + e);
    }

    return inBizRespond;
  }

  /**
   * 客户资料校验和身份证认证
   *
   * @param jsonBody
   * @return
   */
  public MessagerVo unicomCheckIdentityCust(String jsonBody) {
    MessagerVo inBizRespond = new MessagerVo();

    //获取中台配置信息
    //根据运营商编码获取clientId
    String lpsuningunicom = OperatorEnum.lpsuningunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpsuningunicom);

    JSONObject jsonBodyObject = JSONObject.parseObject(jsonBody);
    if(!CommonUtil.isEmpty(jsonBodyObject.getString("certNo"))){
      jsonBodyObject.put("certNo" ,jsonBodyObject.getString("certNo").toUpperCase());
    }

    log.info("客户资料校验和身份证认证加密前请求报文:" + jsonBodyObject.toJSONString());
    try {
      // 加密处理
      String clientId = cardCenterInterfInfo.getClientId();
      String clientKey = cardCenterInterfInfo.getClientKey();
      String json = CmpayUtil.encryptAgReqData(clientId, clientKey, jsonBodyObject.toJSONString());

      String repString = cardCenterInterfInfo.getHaokaUrl() + "/lpcenter/sn.unicom.check.identity.cust";
      Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", 10000);
      log.info("客户资料校验和身份证认证加密响应报文:" + JSON.toJSONString(httpPostRsp));

      if (null != httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
        // 解析响应参数
        String responseString = String.valueOf(httpPostRsp.get("msg"));
        JSONObject parseObject = JSON.parseObject(responseString);
        String rspData = parseObject.getString("rspData");

        // 解密响应数据
        String decryptAgRespData = CmpayUtil.decryptAgRespData("muz32AD+fCRgGc/e/+H97u3wJEcEIu5I", rspData);
        log.info("客户资料校验和身份证认证解密接口响应报文:" + decryptAgRespData);

        JSONObject decryptAgRespObject = JSONObject.parseObject(decryptAgRespData);
        if (!"10000".equals(decryptAgRespObject.getString("code"))) {
          inBizRespond.setSubCode(decryptAgRespObject.getString("code"));
          inBizRespond.setSubMsg(decryptAgRespObject.getString("msg"));
          inBizRespond.setData(decryptAgRespObject.getJSONObject("data"));
        } else {
          inBizRespond.setSubCode("10000");
          inBizRespond.setSubMsg(decryptAgRespObject.getString("msg"));
          // 把请求参数返回给前端(包括预占关键字)
          inBizRespond.setData(decryptAgRespObject.getJSONObject("data"));
        }

      } else {
        log.info("客户资料校验和身份证认证请求异常");
        inBizRespond.setSubCode("40001");
        inBizRespond.setSubMsg("客户资料校验和身份证认证请求异常");

      }

    } catch (Exception e) {
      e.printStackTrace();

      inBizRespond.setSubCode("40001");
      inBizRespond.setSubMsg("客户资料校验和身份证认证请求异常");
      log.error(JSON.toJSONString(inBizRespond) + "客户资料校验和身份证认证请求异常:" + e);
    }

    return inBizRespond;
  }

  /**
   * 新入网订单信息保存入库
   *
   * @param jsonBody
   * @return
   */
  public MessagerVo conserveUnicomOrder(String jsonBody) {
    MessagerVo messagerVo = new MessagerVo();

    JSONObject jsonBodyObject = JSONObject.parseObject(jsonBody);

    //根据商户授权资金订单号、商户本次资金操作的请求流水号查询订单是否存在
    String outOrderNoOld = jsonBodyObject.getString("outOrderNo");
    String outRequestNoOld = jsonBodyObject.getString("outRequestNo");
    //OrderInfo orderInfoOld = orderInfoService.getByAuthOrderNoAndAuthRequestNo(outOrderNoOld , outRequestNoOld);
    OrderInfo orderInfoOld = orderInfoService.getById(outOrderNoOld);
    if(orderInfoOld != null){
      //天猫订单状态 0-代付款 1-支付成功 2-合约办理成功 3-合约办理失败 4-订单已同步联通集团商城
      Integer status = orderInfoOld.getStatus();
      if(status == 0 || status == 1){
        messagerVo.setSubCode("10000");
        messagerVo.setSubMsg("新入网订单保存成功");
        messagerVo.setData(jsonBodyObject);
      }else if(status == 3){
        messagerVo.setSubCode("40001");
        messagerVo.setSubMsg("订单outOrderNo=" + outOrderNoOld + " ,已经同步且为办理失败的订单");
      }else if(status == 2 || status == 4){
        messagerVo.setSubCode("40002");
        messagerVo.setSubMsg("订单outOrderNo=" + outOrderNoOld + " ,订单已同步联通集团商城成功,不需要重新同步订单信息");
      }

      return messagerVo;
    }

    // 根据政策编码获取对应的所有政策信息
    String policyNo = jsonBodyObject.getString("policyNo");

    //根据运营商编码获取政策信息
    String lpunicom = OperatorEnum.lpsuningunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpunicom);
    //调用pot获取苏宁新入网政策列表信息
    String outProtNo = cardCenterInterfInfo.getOutPortNo();
    ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo , outProtNo ,false);
    if(null == policyListResponse || !"10000".equals(policyListResponse.getCode())){
      messagerVo.setSubCode("40003");
      messagerVo.setSubMsg(policyListResponse.getMessage());
      return messagerVo;
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
            childList.add(thirdSpec);
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
      messagerVo.setSubCode("40004");
      messagerVo.setSubMsg("政策编码[" + policyNo + "]对应的产品政策详情为空");
      return messagerVo;
    }

    // 获取下单明细0级政策
    String orderDetailsPsnId = jsonBodyObject.getString("orderDetailsPsnId");
    //遍历0级政策匹配对应苏宁传值0级政策
    PolicyListResponseBizContentPolicy orderDetailsSpec = null;
    for (PolicyListResponseBizContentPolicy cdisSpec : zeroList) {
      if(orderDetailsPsnId.equals(cdisSpec.getPsnId())){
        orderDetailsSpec = cdisSpec;
      }
    }
    if (orderDetailsSpec == null) {
      messagerVo.setSubCode("40005");
      messagerVo.setSubMsg("0级政策编码[" + orderDetailsPsnId + "]对应的产品政策详情为空");
      return messagerVo;
    }

    // 获取所有的权益政策
    // 遍历子元素中所有的二级政策
    // 垫资打款商家外部编码
    String outAgencyNo = "";
    // 一级下单明细政策编码
    String specPsnIdFirst = "";
    // 垫资打款金额/派券金额
    String discountAmount = "";
    // 冻结本金
    String freezeAmount = "";
    // 产品和合约编码
    String productIdAndContactId = "";

    //保存调拨垫资打款政策规则以便提供给订单履约使用
    JSONObject field3Object = new JSONObject();

    for (PolicyListResponseBizContentPolicy cdisSpec : zeroList) {
      // 获取一级
      // 判断是否是对应套餐的权益政策
      if (cdisSpec.getName().equals(orderDetailsSpec.getName())) {// productVo.getSpecIdAStr()
        List<PolicyListResponseBizContentPolicy> childLv1List = cdisSpec.getChildList();

        // 遍历一级政策
        for (PolicyListResponseBizContentPolicy cdisSpecLv1 : childLv1List) {
          List<PolicyListResponseBizContentPolicy> childListv2 = cdisSpecLv1.getChildList();
          specPsnIdFirst = cdisSpecLv1.getPsnId();

          // 节点表识，明细类型：1-下单明细，2-垫支明细，3-趸交明细，4-分期明细,5-退款明细，6-权益明细
          if (1 == Integer.valueOf(cdisSpecLv1.getCheckFlag())) {
            freezeAmount = cdisSpecLv1.getValueStr();
            // 一级子政策编码
            jsonBodyObject.put("goodsId", cdisSpecLv1.getPsnId());
            // 期数
            jsonBodyObject.put("orderTradeFqNum", cdisSpecLv1.getName());
            jsonBodyObject.put("freezeMonth", cdisSpecLv1.getName());

          }

          // 遍历二级子政策所有权益数据
          for (PolicyListResponseBizContentPolicy cdisSpecLv2 : childListv2) {

            // 节点表识，明细类型：1-下单明细，2-垫支明细，3-趸交明细，4-分期明细,5-退款明细，6-权益明细
            if (6 == Integer.valueOf(cdisSpecLv2.getCheckFlag())) {
              outAgencyNo = cdisSpecLv2.getName();
              // 二级子政策编码：权益明细
              jsonBodyObject.put("goodsSa", cdisSpecLv2.getPsnId());
            }

            // 节点表识，明细类型：1-下单明细，2-垫支明细，3-趸交明细，4-分期明细,5-退款明细，6-权益明细
            if (2 == Integer.valueOf(cdisSpecLv2.getCheckFlag())) {
              discountAmount = cdisSpecLv2.getValueStr();
              // 一级子政策编码：垫支明细
              jsonBodyObject.put("goodsSb", cdisSpecLv2.getPsnId());
            }

            // 节点表识，明细类型：1-下单明细，2-垫支明细，3-趸交明细，4-分期明细,5-退款明细，6-权益明细
            if (1 == Integer.valueOf(cdisSpecLv2.getCheckFlag())) {
              productIdAndContactId = cdisSpecLv2.getRemarkInfo();
              // 一级子政策编码：下单明细
              jsonBodyObject.put("goodsSc", cdisSpecLv2.getPsnId());
            }

            // 节点表识，明细类型：1-下单明细，2-垫支明细，3-趸交明细，4-分期明细,5-退款明细，6-权益明细
            if(2 == Integer.valueOf(cdisSpecLv2.getCheckFlag())){
              List<PolicyListResponseBizContentPolicy> childListv3 = cdisSpecLv2.getChildList();
              for (PolicyListResponseBizContentPolicy cdisSpecLv3 : childListv3) {
                field3Object.put(cdisSpecLv3.getName(), cdisSpecLv3.getKeyStr());
              }
            }
          }
        }
      }
    }

    // 根据商家编码获取对应的商家信息、门店信息
    if (outAgencyNo == null) {
      messagerVo.setSubCode("40006");
      messagerVo.setSubMsg("政策编码[" + policyNo + "]对应的商户编码[outAgencyNo]为空");
      log.info("==outAgencyNo==商户编码为空");
      return messagerVo;
    }
    // 调拨商户编码
    jsonBodyObject.put("outAgencyNo", outAgencyNo);

    //判空
    if(field3Object != null && !field3Object.isEmpty()){
      jsonBodyObject.put("field3", field3Object.toJSONString());
    }

    // 根据账户编码获取对应的账号信息
    cardCenterInterfInfo.setAccountNo(productPolicy.getAccountNo());
    ApiInBizResponse<QueryCarrierAccountInfoResponseBizContent> apiInBizResponse = inInterfaceService.queryCarrierAccountInfo(cardCenterInterfInfo ,false);
    if(null == apiInBizResponse || !apiInBizResponse.getCode().equals("10000")){
      messagerVo.setSubCode("40007");
      messagerVo.setSubMsg("账号编码[" + cardCenterInterfInfo.getAccountNo() + "]对应的商户账号信息失败, " + apiInBizResponse.getMessage());
      return messagerVo;
    }
    QueryCarrierAccountInfoResponseBizContent carrierAccount = apiInBizResponse.getBizContent();

    //根据商户外部编码获取对应的权益商城调拨商户、门店信息
    cardCenterInterfInfo.setOutAgencyNo(outAgencyNo);
    ApiInBizResponse<QueryAgencyOrStoreResponseBizContent> queryAgencyOrStoreResp = inInterfaceService.queryAgencyOrStore(cardCenterInterfInfo , false);
    if (null == queryAgencyOrStoreResp || !queryAgencyOrStoreResp.getCode().equals("10000")) {
      messagerVo.setSubCode("40008");
      messagerVo.setSubMsg("商户外部编码[" + outAgencyNo + "]对应调拨商户、门店信息异常, " + queryAgencyOrStoreResp.getMessage());
      log.info("商户外部编码["+outAgencyNo+"]对应调拨商户、门店信息异常 ：" + queryAgencyOrStoreResp.getMessage());
      return messagerVo;
    }

    //获取商家、门店信息
    QueryAgencyOrStoreResponseBizContentCdisAgency cdisAgency = queryAgencyOrStoreResp.getBizContent().getCdisAgency();
    QueryAgencyOrStoreResponseBizContentCdisStore cdisStore =queryAgencyOrStoreResp.getBizContent().getCdisStore();

    //调拨商家名称
    jsonBodyObject.put("agencyNo", cdisAgency.getAgencyNo());
    //调拨商家名称
    jsonBodyObject.put("agencyName", cdisAgency.getAgencyName());
    //调拨门店名称
    jsonBodyObject.put("storeNo", cdisStore.getStoreNo());
    //调拨门店编码
    jsonBodyObject.put("storeName", cdisStore.getStoreName());

    // 根据省编码获取对应的cdis_area表信息中的联通省编码
    String phoneBelong = jsonBodyObject.getString("phoneBelong");
    String[] splitBelong = phoneBelong.split(",");

    // 处理产品编码、合约编码
    String[] productIdAndContactIdArrays = productIdAndContactId.split(",");
    if (productIdAndContactIdArrays.length > 0) {
      // 设置外部产品编码
      jsonBodyObject.put("outProtNo", productIdAndContactIdArrays[0]);
    }

    // 商户部门
    jsonBodyObject.put("carrierDept","-1");
    // 订单标题
    jsonBodyObject.put("orderTitle", orderDetailsSpec.getName());
    // 垫资打款商家外部编码
    jsonBodyObject.put("outAgencyNo", outAgencyNo);
    // isv发起账号PID
    jsonBodyObject.put("payeeLogonId", carrierAccount.getPayeeLogonId());
    // isv发起账号APPID
    jsonBodyObject.put("payeeUserId", carrierAccount.getPayeeUserId());
    // 发起冻结账号编码
    String accountNo = productPolicy.getAccountNo();
    jsonBodyObject.put("accountNo", accountNo);
    String appId = carrierAccount.getAccountAppId();
    jsonBodyObject.put("acountAppId" ,appId);
    // 商户编码
    String carrierNo = productPolicy.getCarrierNo();
    jsonBodyObject.put("carrierNo", carrierNo);
    // 机构名称
    jsonBodyObject.put("carrierOrg", "-1");
    // 机构编码
    String carrierOrgNo = productPolicy.getCarrierOrgNo() == null ? "-1" : productPolicy.getCarrierOrgNo();
    jsonBodyObject.put("carrierOrgNo", carrierOrgNo);
    // 冻结方式
    Integer freezeWay = productPolicy.getFreezeWay();
    jsonBodyObject.put("freezeWay", freezeWay);
    // 业务商城编码
    String merchantId = productPolicy.getMerchantId();
    jsonBodyObject.put("merchantId", merchantId);

    // 垫资打款金额/派券金额
    jsonBodyObject.put("discountAmount", discountAmount);
    // 冻结本金
    jsonBodyObject.put("freezeAmount", freezeAmount);
    // 一级下单明细政策编码
    jsonBodyObject.put("specPsnId", specPsnIdFirst);

    if(!CommonUtil.isEmpty(outOrderNoOld) && !CommonUtil.isEmpty(outRequestNoOld)) {
      jsonBodyObject.put("outOrderNo", outOrderNoOld);
      // 请求编码
      jsonBodyObject.put("outRequestNo", outRequestNoOld);
    }

    //校验接口传送金额是否与政策配置的冻结总金额匹配
    String freezeAmountDecimal =  jsonBodyObject.getString("freezeAmount");
    String freezePriceDecimal =  jsonBodyObject.getString("freezePrice");
    if(!freezeAmountDecimal.equals(freezePriceDecimal)){
      messagerVo.setSubCode("40011");
      messagerVo.setSubMsg("接口同步冻结总金额与政策配置冻结总金额不一致");
      return messagerVo;
    }

    // 主订单编码
    // 发起账号isvPid
    String payeeUserId = carrierAccount.getPayeeUserId();
    jsonBodyObject.put("isvPid", payeeUserId);

    try {
      // 保存客户信息到customer_info表
      Map<String, String> savecusterResult = customerInfoService.saveCustomerInfo(jsonBodyObject);
      if (!"10000".equals(savecusterResult.get("status"))) {
        messagerVo.setSubCode(savecusterResult.get("status"));
        messagerVo.setSubMsg(savecusterResult.get("msg"));
        return messagerVo;
      }

      // 客户信息主键
      String custNo = savecusterResult.get("custNo");
      // 请求编码
      jsonBodyObject.put("custNo", custNo);

      // 保存订单信息
      Map<String, String> saveResult = orderInfoService.saveSuNingOrderInfo(jsonBodyObject);
      if (!"10000".equals(saveResult.get("status"))) {
        messagerVo.setSubCode(saveResult.get("status"));
        messagerVo.setSubMsg(saveResult.get("msg"));
        return messagerVo;
      }

      //设置cdisContOrder订单信息
      CdisContOrder cdisContOrder = potOrderInfoServiceTool.setCdisContOrderInfo(jsonBodyObject);
      //保存订单信息到pot系统
      int result = MpsClientUtil.insertSelective(CdisContOrder.class ,cdisContOrder);
      if(result <= 0){
        messagerVo.setSubCode("40009");
        messagerVo.setSubMsg("新入网订单保存失败-保存订单到订单中心失败");
        return messagerVo;
      }
      log.info("订单outOrderNo = " + cdisContOrder.getOutOrderNo() + "信息到pot系统成功");

      CdisContFreeze cdisContFreeze = potOrderInfoServiceTool.setCdisContFreezeInfo(cdisContOrder);
      cdisContFreeze.setAccountNo(accountNo);
      int resultFreeze = MpsClientUtil.insertSelective(CdisContFreeze.class ,cdisContFreeze);
      if(resultFreeze <= 0){
        messagerVo.setSubCode("40010");
        messagerVo.setSubMsg("新入网订单保存失败-保存订单到订单中心冻结子单失败");
        return messagerVo;
      }

      messagerVo.setSubCode("10000");
      messagerVo.setSubMsg("新入网订单保存成功");
      messagerVo.setData(jsonBodyObject);
      return messagerVo;

    } catch (Exception e) {
      e.printStackTrace();
      log.error("新入网订单保存异常 , " + e);
      messagerVo.setSubCode("40011");
      messagerVo.setSubMsg("新入网订单保存异常");
    }

    return messagerVo;
  }

  /**
   * 更新订单支付状态、userId、authNo到主订单和冻结相关订单信息中
   * @param dataObject
   * @return
   */
  public MessagerVo unicomMallInterfaceUpdateOrderInfo(String dataObject) {
    MessagerVo messagerVo = new MessagerVo();
    JSONObject jsonBodyObject = JSONObject.parseObject(dataObject);

    // 根据订单编码获取对应的订单信息
    String outOrderNo = jsonBodyObject.getString("outOrderNo");
    //OrderInfo orderInfo = orderInfoService.getByAuthOrderNo(outOrderNo);
    OrderInfo orderInfo = orderInfoService.getById(outOrderNo);

    if (orderInfo == null) {
      messagerVo.setSubCode("40001");
      messagerVo.setSubMsg("订单号[" + outOrderNo + "]对应订单信息为空");
      return messagerVo;
    }

    //判断订单状态是否已经是冻结支付状态3
    String outRequestNo = orderInfo.getAuthRequestNo();
    Integer orderStatus = orderInfo.getStatus();
    if(orderStatus == 4) {
      messagerVo.setSubCode("40002");
      messagerVo.setSubMsg("订单已经同步联通集团商城成功,不需要重复进行同步推送");
      return messagerVo;
    }

    if(orderStatus == 3) {
      messagerVo.setSubCode("40003");
      messagerVo.setSubMsg("订单已经进行解冻操作,不允许进行同步推送");
      return messagerVo;
    }

    if(orderStatus == 2) {
      messagerVo.setSubCode("40004");
      messagerVo.setSubMsg("订单已经签约成功,不需要重复进行同步推送");
      return messagerVo;
    }

    //到支付宝查询确定订单是否已经冻结完成
    String freezeAppId = sysConfigService.getByCode("freezeAppId");
    PromotionAccountInfo byAppId = promotionAccountInfoService.getByAppId(freezeAppId);
    AlipayFundAuthOperationDetailQueryResponse alipayFundAuthOperationDetailQueryResponse = aliService.fundAuthOperationDetailQuery(byAppId, outOrderNo, outRequestNo);
    if(!alipayFundAuthOperationDetailQueryResponse.isSuccess()){
      messagerVo.setSubCode("40005");
      messagerVo.setSubMsg("outOrderNo=" + outOrderNo + " , outRequestNo=" + outRequestNo + " ,在支付宝中没存在对应的冻结订单");
      return messagerVo;
    }else if(alipayFundAuthOperationDetailQueryResponse.isSuccess()){
      //判断订单是否已经解冻
      String restAmount = alipayFundAuthOperationDetailQueryResponse.getRestAmount();
      String totalFee = orderInfo.getTotalFee().toString();
      //校验冻结金额是否一致
      if(!restAmount.equals(totalFee)){
        messagerVo.setSubCode("40005");
        messagerVo.setSubMsg("outOrderNo=" + outOrderNo + " , outRequestNo=" + outRequestNo + " ,订单同步创建冻结金额与支付宝订单实际冻结金额不一致");
        return messagerVo;
      }

      //支付宝冻结授信编码
      jsonBodyObject.put("authNo" ,alipayFundAuthOperationDetailQueryResponse.getAuthNo());
      //支付宝的授权资金操作流水号
      jsonBodyObject.put("operationId" ,alipayFundAuthOperationDetailQueryResponse.getOperationId());
    }

    //获取用户信息
    String custNo = orderInfo.getCustNo();
    CustomerInfo customerInfo = customerInfoService.getById(custNo);
    if(customerInfo == null) {
      messagerVo.setSubCode("40005");
      messagerVo.setSubMsg("订单号[" + outOrderNo + "]对应客户信息为空");
      return messagerVo;
    }

    //支付成功的订单直接返回
//    if(orderStatus == 1) {
//
//      //把获取的订单信息,客户信息返回
//      Map<String , Object> dataMap = new HashMap<String , Object>();
//      dataMap.put("orderInfo" , orderInfo);
//      dataMap.put("customerInfo" , customerInfo);
//
//      messagerVo.setSubCode("10000");
//      messagerVo.setSubMsg("订单已经冻结更新成功");
//      messagerVo.setData(dataMap);
//      return messagerVo;
//    }

    //更新主订单信息
    //客户userId
    String payerUserId = jsonBodyObject.getString("userId");
    orderInfo.setUserId(payerUserId);
    //支付宝冻结授信编码
    String authNo = jsonBodyObject.getString("authNo");
    orderInfo.setAuthNo(authNo);
    //支付宝的授权资金操作流水号
    String operationId = jsonBodyObject.getString("operationId");
    orderInfo.setOperationId(operationId);
    // 订单状态：支付/冻结成功
    orderInfo.setStatus(1);
    orderInfo.setIsPaySuccess("1");
    //业务办理中
    orderInfo.setProcessStates(ProcessStatesEnum.BUSINESS_HANDLING.getCode());
    orderInfo.setUpdateTime(new Date());

    String orderSuccessTime = jsonBodyObject.getString("orderSuccessTime");
    try {
      orderInfo.setContractTime(DateUtil.parseTime(orderSuccessTime, "yyyy-MM-dd HH:mm:ss"));
    } catch (Exception e) {
      e.printStackTrace();
      log.error("时间转化异常,{}" ,e);
    }

    // 更新主订单数据
    boolean updateResult = orderInfoService.updateById(orderInfo);
    if(!updateResult){
      messagerVo.setSubCode("40006");
      messagerVo.setSubMsg("订单信息更新失败,outTradeNo ={"+orderInfo.getOutTradeNo()+"} , authOrderNo={"+orderInfo.getAuthOrderNo()+"}");
      return messagerVo;
    }

    //更新客户信息
    customerInfo.setCustUserid(payerUserId);
    customerInfo.setUpdateTime(new Date());
    customerInfoService.updateById(customerInfo);

    //更新pot订单表数据
    //更新主订单信息
    //根据订单编码获取pot主订单信息
    CdisContOrder cdisContOrder = MpsClientUtil.selectByPrimaryKey(CdisContOrder.class , orderInfo.getAuthOrderNo());
    if(cdisContOrder == null){
      messagerVo.setSubCode("40007");
      messagerVo.setSubMsg("订单信息更新失败,outOrderNo={"+orderInfo.getAuthOrderNo()+"} 订单信息为空");
      return messagerVo;
    }

    cdisContOrder.setPayerUserId(payerUserId);
    //支付宝冻结授信编码
    cdisContOrder.setAuthNo(authNo);
    //支付宝的授权资金操作流水号
    cdisContOrder.setTradeNo(operationId);
    // 订单冻结金额
    cdisContOrder.setOrderFreezeAmount(Double.valueOf(orderInfo.getTotalFee().toString()));
    cdisContOrder.setRestAmount(Double.valueOf(orderInfo.getTotalFee().toString()));
    cdisContOrder.setContractFund(Double.valueOf(orderInfo.getTotalFee().toString()));
    cdisContOrder.setOrderAmount(Double.valueOf(orderInfo.getTotalFee().toString()));
    // 订单冻结金额
    cdisContOrder.setTotalFreezeAmount(Double.valueOf(orderInfo.getTotalFee().toString()));
    cdisContOrder.setContractMonth(String.valueOf(orderInfo.getFreezeMonth()));
    cdisContOrder.setContractStatus(1);
    // 订单状态：支付/冻结成功
    Integer orderStatusNew = 3;
    cdisContOrder.setOrderStatus(orderStatusNew);
    cdisContOrder.setUpdtDate(new Date());
    int result = MpsClientUtil.updateByPrimaryKeySelective(CdisContOrder.class ,cdisContOrder);
    if(result <= 0){
      messagerVo.setSubCode("40008");
      messagerVo.setSubMsg("订单冻结信息更新主订单失败");
      return messagerVo;
    }

    //子订单信息
    CdisContFreeze cdisContFreeze = new CdisContFreeze();
    cdisContFreeze.setOutOrderNo(orderInfo.getAuthOrderNo());
    cdisContFreeze.setOutRequestNo(orderInfo.getAuthRequestNo());
    // 支付宝授权编码
    cdisContFreeze.setAuthNo(authNo);
    cdisContFreeze.setContractTime(new Date());
    cdisContFreeze.setTradeNo(operationId);
    cdisContFreeze.setFreezeDate(new Date());
    cdisContFreeze.setAccountPid(orderInfo.getPayeeUserId());
    cdisContFreeze.setAccountAppid(orderInfo.getAppId());
    cdisContFreeze.setPayerUserId(payerUserId);
    // 更新冻结金额
    cdisContFreeze.setTotalFreezeAmount(Double.valueOf(orderInfo.getTotalFee().toString()));
    cdisContFreeze.setRestAmount(Double.valueOf(orderInfo.getTotalFee().toString()));
    cdisContFreeze.setContractAmount(Double.valueOf(orderInfo.getTotalFee().toString()));
    cdisContFreeze.setOrderAmount(Double.valueOf(orderInfo.getTotalFee().toString()));
    cdisContFreeze.setOrderFreezeAmount(Double.valueOf(orderInfo.getTotalFee().toString()));
    cdisContFreeze.setOperationStatus("SUCCESS");
    cdisContFreeze.setOrderPriceAmount(Double.valueOf(orderInfo.getTotalFee().toString()));
    try {
      cdisContFreeze.setOrderFreezeDate(DateUtil.parseTime(orderSuccessTime, "yyyy-MM-dd HH:mm:ss"));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    cdisContFreeze.setPayerUserId(payerUserId);
    cdisContFreeze.setUpdtDate(new Date());

    int resultFreeze = MpsClientUtil.updateByPrimaryKeySelective(CdisContFreeze.class ,cdisContFreeze);
    if(resultFreeze <= 0){
      messagerVo.setSubCode("40009");
      messagerVo.setSubMsg("订单冻结信息更新子订单失败");
      return messagerVo;
    }

    //保存入库订单日志流水信息
    CdisFreezeLog cdisFreezeLog = potOrderInfoServiceTool.setCdisFreezeLogInfo(cdisContOrder);
    CdisFreezeLog cdisFreezeLogOld = MpsClientUtil.selectByPrimaryKey(CdisFreezeLog.class ,cdisFreezeLog.getOutRequestNo());
    int resultLog = 0;
    if(cdisFreezeLogOld != null){
      resultLog = MpsClientUtil.updateByPrimaryKeySelective(CdisFreezeLog.class ,cdisFreezeLog);
    }else{
      resultLog = MpsClientUtil.insertSelective(CdisFreezeLog.class ,cdisFreezeLog);
    }

    if(resultLog <= 0){
      messagerVo.setSubCode("40010");
      messagerVo.setSubMsg("订单冻结信息保存冻结日志流水订单失败");
      return messagerVo;
    }

    //把获取的订单信息,客户信息返回
    Map<String , Object> dataMap = new HashMap<String , Object>();
    dataMap.put("orderInfo" , orderInfo);
    dataMap.put("customerInfo" , customerInfo);

    messagerVo.setSubCode("10000");
    messagerVo.setSubMsg("订单冻结信息更新成功");
    messagerVo.setData(dataMap);
    return messagerVo;
  }

  /**
   * 新入网订单同步联通集团商城:外放调用能力
   *
   * @param jsonBody
   * @return
   */
  public MessagerVo unicomMallInterfaceOrderSync(String jsonBody ,Map<String ,Object> dataMap) {
    MessagerVo inBizRespond = new MessagerVo();

    JSONObject jsonBodyObject = JSONObject.parseObject(jsonBody);
    //订单信息
    OrderInfo orderInfo = (OrderInfo) dataMap.get("orderInfo");
    //客户信息
    CustomerInfo customerInfo = (CustomerInfo) dataMap.get("customerInfo");

    // 获取订单信息
    String outOrderNo = jsonBodyObject.getString("outOrderNo");
    if (orderInfo == null) {
      inBizRespond.setSubCode("40012");
      inBizRespond.setSubMsg("订单号[" + outOrderNo + "]对应订单信息为空");
      return inBizRespond;
    }

    if(customerInfo == null) {
      inBizRespond.setSubCode("40013");
      inBizRespond.setSubMsg("订单号[" + outOrderNo + "]对应客户信息为空");
      return inBizRespond;
    }

    // 拼装请求参数
    Map<String, Object> reqMap = createUnicomMallInterfaceOrderSyncParam(orderInfo , customerInfo);
    if (!"10000".equals(reqMap.get("status").toString())) {
      inBizRespond.setSubCode(reqMap.get("status").toString());
      inBizRespond.setSubMsg(reqMap.get("msg").toString());
      return inBizRespond;
    }

    OrderSyncAo orderSyncAo = (OrderSyncAo) reqMap.get("orderSyncAo");
    String reqJson = JSONObject.toJSONString(orderSyncAo);

    log.info("新入网订单同步联通集团商城加密前请求报文:" + reqJson);

    //获取中台配置信息
    //根据运营商编码获取clientId
    String lpsuningunicom = OperatorEnum.lpsuningunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpsuningunicom);
    try {
      // 加密处理
      String clientId = cardCenterInterfInfo.getClientId();
      String clientKey = cardCenterInterfInfo.getClientKey();
      String json = CmpayUtil.encryptAgReqData(clientId, clientKey, reqJson);

      String repString = cardCenterInterfInfo.getHaokaUrl() + "/lpcenter/sn.unicom.order.sync";
      Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", 10000);
      log.info("新入网订单同步联通集团商城加密响应报文:" + JSON.toJSONString(httpPostRsp));

      if (null != httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
        // 解析响应参数
        String responseString = String.valueOf(httpPostRsp.get("msg"));
        JSONObject parseObject = JSON.parseObject(responseString);
        String rspData = parseObject.getString("rspData");

        // 解密响应数据
        String decryptAgRespData = CmpayUtil.decryptAgRespData("muz32AD+fCRgGc/e/+H97u3wJEcEIu5I", rspData);
        log.info("新入网订单同步联通集团商城解密接口响应报文,{}" , decryptAgRespData);

        JSONObject decryptAgRespObject = JSONObject.parseObject(decryptAgRespData);
        if (!"10000".equals(decryptAgRespObject.getString("code"))) {
          // 失败原因
          String msg = decryptAgRespObject.getString("msg");
          inBizRespond.setSubCode("40017");
          inBizRespond.setSubMsg("新入网订单同步联通集团商城失败：[" + msg + "]");

          //业务办理失败
          orderInfo.setProcessStates(ProcessStatesEnum.BUSINESS_HANDLING_FAILED.getCode());
          // 合约办理失败
          orderInfo.setStatus(3);
          orderInfo.setUpdateTime(new Date());
          // 更新主订单数据
          boolean updateResult = orderInfoService.updateById(orderInfo);

          log.info("outOrderNo=" + outOrderNo + " ,{}" ,JSONObject.toJSONString(inBizRespond));
          return inBizRespond;
        }

        // 订单同步成功后,把主订单信息的状态更新,并且入库对应的集团商城订单编码
        String data = decryptAgRespObject.getString("data");
        // 获取集团商城订单号
        JSONObject dataObject = JSONObject.parseObject(data);
        String unicomOrderId = dataObject.getString("orderId");

        orderInfo.setOperatorOrderId(unicomOrderId);
        orderInfo.setProcessStates(ProcessStatesEnum.BUSINESS_HANDLING_SUCCESS.getCode());
        // 等待发货
        orderInfo.setStatus(4);
        orderInfo.setUpdateTime(new Date());
        // 更新主订单数据
        boolean updateResult = orderInfoService.updateById(orderInfo);
        if(!updateResult){
          inBizRespond.setSubCode("40018");
          inBizRespond.setSubMsg("订单同步集团商城成功,更新主订单数据失败");
          inBizRespond.setData(decryptAgRespObject.getString("data"));
          return inBizRespond;
        }

        //更新pot的订单状态
        CdisContOrder cdisContOrder = new CdisContOrder();
        cdisContOrder.setOutOrderNo(orderInfo.getAuthOrderNo());
        //集团商城订单号
        cdisContOrder.setUnicomOrderId(unicomOrderId);
        // 等待发货
        cdisContOrder.setOrderStatus(13);
        cdisContOrder.setUpdtDate(new Date());
        int result = MpsClientUtil.updateByPrimaryKeySelective(CdisContOrder.class ,cdisContOrder);
        if(result <= 0){
          inBizRespond.setSubCode("40019");
          inBizRespond.setSubMsg("订单冻结信息更新主订单失败");
          return inBizRespond;
        }

        // 返回成功
        inBizRespond.setSubCode("10000");
        inBizRespond.setSubMsg(decryptAgRespObject.getString("msg"));
        // 把请求参数返回给前端(包括预占关键字)
        inBizRespond.setData(decryptAgRespObject.getJSONObject("data"));
      } else {
        inBizRespond.setSubCode("40020");
        inBizRespond.setSubMsg("新入网订单同步联通集团商城请求异常");
        log.info("新入网订单同步联通集团商城请求异常");
      }

    } catch (Exception e) {
      e.printStackTrace();

      inBizRespond.setSubCode("40021");
      inBizRespond.setSubMsg("新入网订单同步联通集团商城请求异常");
      log.error(JSON.toJSONString(inBizRespond) + "新入网订单同步联通集团商城请求异常:" + e);
    }

    log.info("新入网订单同步联通集团商城响应前端报文:" + JSON.toJSONString(inBizRespond));
    return inBizRespond;
  }

  /**
   * 新入网订单同步联通集团商城:外放调用能力(新)
   * @param jsonBody
   * @return
   */
  public MessagerVo unicomMallInterfaceNewOrderSync(String jsonBody ,Map<String ,Object> dataMap) {
    MessagerVo inBizRespond = new MessagerVo();

    JSONObject jsonBodyObject = JSONObject.parseObject(jsonBody);
    //订单信息
    OrderInfo orderInfo = (OrderInfo) dataMap.get("orderInfo");
    //客户信息
    CustomerInfo customerInfo = (CustomerInfo) dataMap.get("customerInfo");

    // 获取订单信息
    String outOrderNo = jsonBodyObject.getString("outOrderNo");

    if (orderInfo == null) {
      inBizRespond.setSubCode("40012");
      inBizRespond.setSubMsg("订单号[" + outOrderNo + "]对应订单信息为空");
      return inBizRespond;
    }

    if(customerInfo == null) {
      inBizRespond.setSubCode("40013");
      inBizRespond.setSubMsg("订单号[" + outOrderNo + "]对应客户信息为空");
      return inBizRespond;
    }

    // 拼装请求参数
    Map<String, Object> reqMap = createUnicomMallInterfaceNewOrderSyncParam(orderInfo , customerInfo);
    if (!"10000".equals(reqMap.get("status").toString())) {
      inBizRespond.setSubCode(reqMap.get("status").toString());
      inBizRespond.setSubMsg(reqMap.get("msg").toString());
      return inBizRespond;
    }

    NewOrderSyncAo newOrderSyncAo = (NewOrderSyncAo) reqMap.get("newOrderSyncAo");
    // 获取金融渠道编码
    //String financeChannelCode = sysConfigService.getByCode("financeChannelCode");
    String financeChannelCode = jsonBodyObject.getString("financeChannelCode");
    newOrderSyncAo.setFinanceChannelCode(financeChannelCode);
    String reqJson = JSONObject.toJSONString(newOrderSyncAo);

    log.info("新入网订单同步联通集团商城加密前请求报文:" + reqJson);

    //获取中台配置信息
    //根据运营商编码获取clientId
    String lpsuningunicom = OperatorEnum.lpsuningunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpsuningunicom);
    try {
      // 加密处理
      String clientId = cardCenterInterfInfo.getClientId();
      String clientKey = cardCenterInterfInfo.getClientKey();
      String json = CmpayUtil.encryptAgReqData(clientId, clientKey, reqJson);

      String repString = cardCenterInterfInfo.getHaokaUrl() + "/lpcenter/sn.unicom.new.order.sync";
//      String repString = "http://127.0.0.1:48100/communication/lpcenter/sn.unicom.new.order.sync";
      Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", 10000);
      log.info("新入网订单同步联通集团商城加密响应报文:" + JSON.toJSONString(httpPostRsp));

      if (null != httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
        // 解析响应参数
        String responseString = String.valueOf(httpPostRsp.get("msg"));
        JSONObject parseObject = JSON.parseObject(responseString);
        String rspData = parseObject.getString("rspData");

        // 解密响应数据
        String decryptAgRespData = CmpayUtil.decryptAgRespData("muz32AD+fCRgGc/e/+H97u3wJEcEIu5I", rspData);
        log.info("新入网订单同步联通集团商城解密接口响应报文,{}" , decryptAgRespData);

        JSONObject decryptAgRespObject = JSONObject.parseObject(decryptAgRespData);
        if (!"10000".equals(decryptAgRespObject.getString("code"))) {
          // 失败原因
          String msg = decryptAgRespObject.getString("msg");
          inBizRespond.setSubCode("40017");
          inBizRespond.setSubMsg("新入网订单同步联通集团商城失败：[" + msg + "]");

          //业务办理失败
          orderInfo.setProcessStates(ProcessStatesEnum.BUSINESS_HANDLING_FAILED.getCode());
          // 合约办理失败
          orderInfo.setStatus(3);
          orderInfo.setUpdateTime(new Date());
          // 更新主订单数据
          boolean updateResult = orderInfoService.updateById(orderInfo);

          log.info("outOrderNo=" + outOrderNo + " ,{}" ,JSONObject.toJSONString(inBizRespond));
          return inBizRespond;
        }

        // 订单同步成功后,把主订单信息的状态更新,并且入库对应的集团商城订单编码
        String data = decryptAgRespObject.getString("data");
        // 获取集团商城订单号
        JSONObject dataObject = JSONObject.parseObject(data);
        String unicomOrderId = dataObject.getString("orderId");

        orderInfo.setOperatorOrderId(unicomOrderId);
        orderInfo.setProcessStates(ProcessStatesEnum.BUSINESS_HANDLING_SUCCESS.getCode());
        // 等待发货
        orderInfo.setStatus(4);
        orderInfo.setUpdateTime(new Date());
        // 更新主订单数据
        boolean updateResult = orderInfoService.updateById(orderInfo);
        if(!updateResult){
          inBizRespond.setSubCode("40018");
          inBizRespond.setSubMsg("订单同步集团商城成功,更新主订单数据失败");
          inBizRespond.setData(decryptAgRespObject.getString("data"));
          return inBizRespond;
        }

        //更新pot的订单状态
        CdisContOrder cdisContOrder = new CdisContOrder();
        cdisContOrder.setOutOrderNo(orderInfo.getAuthOrderNo());
        //集团商城订单号
        cdisContOrder.setUnicomOrderId(unicomOrderId);
        // 等待发货
        cdisContOrder.setOrderStatus(13);
        cdisContOrder.setUpdtDate(new Date());
        int result = MpsClientUtil.updateByPrimaryKeySelective(CdisContOrder.class ,cdisContOrder);
        if(result <= 0){
          inBizRespond.setSubCode("40019");
          inBizRespond.setSubMsg("订单冻结信息更新主订单失败");
          return inBizRespond;
        }

        // 返回成功
        inBizRespond.setSubCode("10000");
        inBizRespond.setSubMsg(decryptAgRespObject.getString("msg"));
        // 把请求参数返回给前端(包括预占关键字)
        inBizRespond.setData(decryptAgRespObject.getJSONObject("data"));
      } else {
        inBizRespond.setSubCode("40020");
        inBizRespond.setSubMsg("新入网订单同步联通集团商城请求异常");
        log.info("新入网订单同步联通集团商城请求异常");
      }

    } catch (Exception e) {
      e.printStackTrace();

      inBizRespond.setSubCode("40021");
      inBizRespond.setSubMsg("新入网订单同步联通集团商城请求异常");
      log.error(JSON.toJSONString(inBizRespond) + "新入网订单同步联通集团商城请求异常:" + e);
    }

    log.info("新入网订单同步联通集团商城响应前端报文:" + JSON.toJSONString(inBizRespond));

    return inBizRespond;
  }

  /**
   * 拼装新用户入网请求参数
   * @param orderInfo
   * @param customerInfo
   * @return
   */
  private Map<String,Object> createUnicomMallInterfaceOrderSyncParam(OrderInfo orderInfo, CustomerInfo customerInfo) {
    Map<String,Object> resultMap = new HashMap<String ,Object>();
    // 订单同步实体
    OrderSyncAo orderSyncAo = new OrderSyncAo();

    // 获取省编码
    String phoneBelong = customerInfo.getPhoneBelong();
    String[] provincCode = phoneBelong.split(",");
    if (CommonUtil.isEmpty(provincCode[0])) {
      resultMap.put("status", "40014");
      resultMap.put("msg", "同步支付成功订单给联通商城端:没有省编码编码信息");
      log.info("==同步支付成功订单给联通商城端==没有省编码编码信息");
      return resultMap;
    }

    //根据运营商编码获取政策信息
    String lpunicom = OperatorEnum.lpsuningunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpunicom);
    //调用pot获取苏宁新入网政策列表信息
    String outProtNo = cardCenterInterfInfo.getOutPortNo();
    ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo , outProtNo ,false);
    if(null == policyListResponse || !"10000".equals(policyListResponse.getCode())){
      resultMap.put("status", "40015");
      resultMap.put("msg", policyListResponse.getMessage());
      log.info("策略查询失败,{}" ,policyListResponse.getMessage());
      return resultMap;
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
            childList.add(thirdSpec);
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

    // 获取所有的权益政策
    // 遍历子元素中所有的二级政策
    String baseProductName = null;
    // CB端的合约编码
    String contractId = null;
    // 期数
    String month = null;
    for (PolicyListResponseBizContentPolicy cdisSpec : zeroList) {
      // 获取一级
      // 判断是否是对应套餐的权益政策
      if (cdisSpec.getName().equals(orderInfo.getName())) {
        List<PolicyListResponseBizContentPolicy> childLv1List = cdisSpec.getChildList();

        // 遍历一级政策
        for (PolicyListResponseBizContentPolicy cdisSpecLv1 : childLv1List) {
          List<PolicyListResponseBizContentPolicy> childListv2 = cdisSpecLv1.getChildList();
          baseProductName = cdisSpecLv1.getRemarkInfo();
          month = cdisSpecLv1.getName();

          // 遍历二级子政策所有权益数据
          for (PolicyListResponseBizContentPolicy cdisSpecLv2 : childListv2) {
            // 刷选联通集团政策商品编码、政策编码、商品名称
            // check_flag:明细类型：1-下单明细，2-垫支明细，3-趸交明细，4-分期明细,5-退款明细
            if (1 == Integer.valueOf(cdisSpecLv2.getCheckFlag())) {// 下单明细二级政策

              // 商城生产环境商品id
              orderSyncAo.setGoodsId(cdisSpecLv2.getKeyStr());
              // 商城生产环境商品名称
              orderSyncAo.setGoodsName(cdisSpecLv2.getName());

              // 填写CB端的商品信息、CB端的合约编码
              String remarkInfo = cdisSpecLv2.getRemarkInfo();
              String[] split = remarkInfo.split(",");
              // CB端的商品编码
              orderSyncAo.setBaseProductId(split[0]);
              // CB端的商品名称
              orderSyncAo.setBaseProductName(baseProductName);

              // CB端的合约编码、合约名称、合约期
              if (split.length == 2) {
                contractId = split[1];
                orderSyncAo.setContractId(contractId);
                orderSyncAo.setContractName(baseProductName);
                orderSyncAo.setContractPeriod(month);
              }
            }
          }
        }
      }
    }

    //设置订单同步实体请求信息
    //邮递地址
    orderSyncAo.setAddress(customerInfo.getPostAddress());
    //校验身份证结果编码
    orderSyncAo.setCertCheckCode(customerInfo.getCertCheckCode());
    //校验身份证结果描述
    orderSyncAo.setCertCheckMsg(customerInfo.getCertCheckMsg());
    //身份证姓名
    orderSyncAo.setCertName(customerInfo.getCustName());
    //身份证号码
    orderSyncAo.setCertNo(customerInfo.getCertNo().toUpperCase());
    //省份编码（两位）
    orderSyncAo.setProvinceCode(customerInfo.getCustProvince());
    //地市编码(三位)
    orderSyncAo.setCityCode(customerInfo.getCustCity());
    //订单触点编码/渠道编码
    String channel = cardCenterInterfInfo.getChannel();
    orderSyncAo.setChannel(channel);
    //合约手机号
    orderSyncAo.setContractPhone(orderInfo.getPhone());
    //发展人编码
    orderSyncAo.setDevCode("0");

    //首月资费方式id A000011V000001 （全月）A000011V000002（半月）A000011V000003（套外）
    String firstMonthId = customerInfo.getFirstMonthId();
    if("A000011V000001".equals(firstMonthId)){
      //首月资费方式id
      orderSyncAo.setFirstMonthId(firstMonthId);
      //首月资费方式名称
      orderSyncAo.setFirstMonthName("全月");
    }else if("A000011V000002".equals(firstMonthId)){
      //首月资费方式id
      orderSyncAo.setFirstMonthId(firstMonthId);
      //首月资费方式名称
      orderSyncAo.setFirstMonthName("半月");
    }else if("A000011V000003".equals(firstMonthId)){
      //首月资费方式id
      orderSyncAo.setFirstMonthId(firstMonthId);
      //首月资费方式名称
      orderSyncAo.setFirstMonthName("套外");
    }

    //商品价格
    orderSyncAo.setGoodsPrice("0");
    //合约期
    orderSyncAo.setMonthLimit(month);
    //靓号标记 1靓号 0普号
    orderSyncAo.setNiceTag("0");
    //号码预占到期时间(例如：20591231235959)
    Date occupationDate = customerInfo.getOccupationDate();
    orderSyncAo.setOccupyTime(DateUtil.formatDate(occupationDate, "yyyyMMddHHmmss"));
    //邮递姓名
    orderSyncAo.setPostName(customerInfo.getPostCustName());
    //邮递联系电话
    orderSyncAo.setPostPhone(customerInfo.getPostCustPhone());
    //号码预占关键字（与号码状态变更接口的号码预占关键字proKey保持传值一致）
    orderSyncAo.setProKey(customerInfo.getProkey());
    //商户id
    orderSyncAo.setUnicomMerchantId(customerInfo.getCustProvince());

    String areaCode = customerInfo.getPostAreaCode();
    if(StringUtils.isEmpty(areaCode)) {
      resultMap.put("status", "40016");
      resultMap.put("msg", "邮递省、市、区编码为空");
      log.info("==同步支付成功订单给联通商城端==没有该客户编码[" + customerInfo.getCustNo() + "]对应的客户信息");
      return resultMap;
    }
    String[] codeArray = areaCode.split(",");
    //邮递省份编码（6位）
    orderSyncAo.setWebProvince(codeArray[0]);
    //邮递地市编码（6位）
    orderSyncAo.setWebCity(codeArray[1]);
    //邮递区县编码（6位）
    orderSyncAo.setWebCounty(codeArray[2]);

    //返回成功
    resultMap.put("status", "10000");
    resultMap.put("orderSyncAo", orderSyncAo);
    String outOrderNo = orderInfo.getAuthOrderNo();
    resultMap.put("msg", outOrderNo + "订单同步集团商城请求参数设置成功");
    log.info(outOrderNo + "订单同步集团商城请求参数设置成功");
    return resultMap;

  }

  /**
   * 拼装新用户入网请求参数(新)
   * @param orderInfo
   * @param customerInfo
   * @return
   */
  private Map<String,Object> createUnicomMallInterfaceNewOrderSyncParam(OrderInfo orderInfo, CustomerInfo customerInfo) {
    Map<String,Object> resultMap = new HashMap<String ,Object>();
    // 订单同步实体
    NewOrderSyncAo newOrderSyncAo = new NewOrderSyncAo();

    // 获取省编码
    String phoneBelong = customerInfo.getPhoneBelong();
    String[] provincCode = phoneBelong.split(",");
    if (CommonUtil.isEmpty(provincCode[0])) {
      resultMap.put("status", "40014");
      resultMap.put("msg", "同步支付成功订单给联通商城端:没有省编码编码信息");
      log.info("==同步支付成功订单给联通商城端==没有省编码编码信息");
      return resultMap;
    }

    //根据运营商编码获取政策信息
    String lpunicom = OperatorEnum.lpsuningunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpunicom);
    //调用pot获取苏宁新入网政策列表信息
    String outProtNo = cardCenterInterfInfo.getOutPortNo();
    ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo , outProtNo ,false);
    if(null == policyListResponse || !"10000".equals(policyListResponse.getCode())){
      resultMap.put("status", "40015");
      resultMap.put("msg", policyListResponse.getMessage());
      log.info("策略查询失败,{}" ,policyListResponse.getMessage());
      return resultMap;
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
            childList.add(thirdSpec);
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

    // 获取所有的权益政策
    // 遍历子元素中所有的二级政策
    String baseProductName = null;
    // CB端的合约编码
    String contractId = null;
    // 期数
    String month = null;
    for (PolicyListResponseBizContentPolicy cdisSpec : zeroList) {
      // 获取一级
      // 判断是否是对应套餐的权益政策
      if (cdisSpec.getName().equals(orderInfo.getName())) {
        List<PolicyListResponseBizContentPolicy> childLv1List = cdisSpec.getChildList();

        // 遍历一级政策
        for (PolicyListResponseBizContentPolicy cdisSpecLv1 : childLv1List) {
          List<PolicyListResponseBizContentPolicy> childListv2 = cdisSpecLv1.getChildList();
          baseProductName = cdisSpecLv1.getRemarkInfo();
          month = cdisSpecLv1.getName();

          // 遍历二级子政策所有权益数据
          for (PolicyListResponseBizContentPolicy cdisSpecLv2 : childListv2) {
            // 刷选联通集团政策商品编码、政策编码、商品名称
            // check_flag:明细类型：1-下单明细，2-垫支明细，3-趸交明细，4-分期明细,5-退款明细
            if (1 == Integer.valueOf(cdisSpecLv2.getCheckFlag())) {// 下单明细二级政策

              // 商城生产环境商品id
              newOrderSyncAo.setGoodsId(cdisSpecLv2.getKeyStr());
              // 填写CB端的商品信息、CB端的合约编码
              String remarkInfo = cdisSpecLv2.getRemarkInfo();
              String[] split = remarkInfo.split(",");
              // CB端的商品编码
              newOrderSyncAo.setBaseProductId(split[0]);
              // CB端的合约编码、合约名称、合约期
              if (split.length == 2) {
                contractId = split[1];
                newOrderSyncAo.setContractId(contractId);
              }
            }
          }
        }
      }
    }

    //设置订单同步实体请求信息
    //邮递地址
    newOrderSyncAo.setAddress(customerInfo.getPostAddress());
    //省份编码（两位）
    newOrderSyncAo.setProvinceCode(customerInfo.getCustProvince());
    //地市编码(三位)
    newOrderSyncAo.setCityCode(customerInfo.getCustCity());
    //订单触点编码/渠道编码
    String channel = cardCenterInterfInfo.getChannel();
    newOrderSyncAo.setChannel(channel);
    //发展人编码
    newOrderSyncAo.setDevCode("0");
    //订单总费用(单位：厘)
    newOrderSyncAo.setOrderTotalFee("0");

    //首月资费方式id A000011V000001 （全月）A000011V000002（半月）A000011V000003（套外）
    String firstMonthId = customerInfo.getFirstMonthId();
    if("A000011V000001".equals(firstMonthId)){
      //首月资费方式id
      newOrderSyncAo.setFirstMonthId(firstMonthId);
    }else if("A000011V000002".equals(firstMonthId)){
      //首月资费方式id
      newOrderSyncAo.setFirstMonthId(firstMonthId);
    }else if("A000011V000003".equals(firstMonthId)){
      //首月资费方式id
      newOrderSyncAo.setFirstMonthId(firstMonthId);
    }

    //邮递姓名
    newOrderSyncAo.setPostName(customerInfo.getPostCustName());
    //邮递联系电话
    newOrderSyncAo.setPostNumber(customerInfo.getPostCustPhone());

    String areaCode = customerInfo.getPostAreaCode();
    if(StringUtils.isEmpty(areaCode)) {
      resultMap.put("status", "40016");
      resultMap.put("msg", "邮递省、市、区编码为空");
      log.info("==同步支付成功订单给联通商城端==没有该客户编码[" + customerInfo.getCustNo() + "]对应的客户信息");
      return resultMap;
    }
    String[] codeArray = areaCode.split(",");
    //邮递省份编码（6位）
    newOrderSyncAo.setWebProvince(codeArray[0]);
    //邮递地市编码（6位）
    newOrderSyncAo.setWebCity(codeArray[1]);
    //邮递区县编码（6位）
    newOrderSyncAo.setWebCounty(codeArray[2]);

    //号码信息
    ArrayList<NewOrderSyncNumberListInfoAo> newOrderSyncNumberListInfoAos = new ArrayList<>();
    NewOrderSyncNumberListInfoAo newOrderSyncNumberListInfoAo = new NewOrderSyncNumberListInfoAo();
    newOrderSyncNumberListInfoAo.setCertId(customerInfo.getCertNo().toUpperCase());
    newOrderSyncNumberListInfoAo.setCertName(customerInfo.getCustName());
    newOrderSyncNumberListInfoAo.setProvinceCode(customerInfo.getCustProvince());
    newOrderSyncNumberListInfoAo.setCityCode(customerInfo.getCustCity());
    newOrderSyncNumberListInfoAo.setMainFlag("1");
    newOrderSyncNumberListInfoAo.setUserFlag("1");
    newOrderSyncNumberListInfoAo.setNumber(orderInfo.getPhone());
    newOrderSyncNumberListInfoAos.add(newOrderSyncNumberListInfoAo);
    newOrderSyncAo.setNumberListInfo(newOrderSyncNumberListInfoAos);

    //附加产品
    ArrayList<NewOrderSyncProductListInfoAo> newOrderSyncProductListInfoAos = new ArrayList<>();
    newOrderSyncAo.setProductListInfo(newOrderSyncProductListInfoAos);

    //返回成功
    resultMap.put("status", "10000");
    resultMap.put("newOrderSyncAo", newOrderSyncAo);
    String outOrderNo = orderInfo.getAuthOrderNo();
    resultMap.put("msg", outOrderNo + "订单同步集团商城请求参数设置成功");
    log.info(outOrderNo + "订单同步集团商城请求参数设置成功");
    return resultMap;

  }

  /**
   * 集团新用户入网业务处理：派发权益
   * @param dataObject
   * @return
   */
  public MessagerVo orderSendRights(JSONObject dataObject) {
    MessagerVo messagerVo = new MessagerVo();

    if(dataObject == null || dataObject.isEmpty()){
      messagerVo.setSubCode("40001");
      messagerVo.setSubMsg("请求体为空");
      return messagerVo;
    }

    // 校验必填参数是否为空
    String outOrderNo = dataObject.getString("outOrderNo");
    if (StringUtils.isEmpty(outOrderNo)) {
      messagerVo.setSubCode("40002");
      messagerVo.setSubMsg("订单编码为空");
      return messagerVo;
    }

    // 根据订单编码获取对应的订单信息
    OrderInfo orderInfo = orderInfoService.getById(outOrderNo);
    if (orderInfo == null) {
      messagerVo.setSubCode("40003");
      messagerVo.setSubMsg("订单号[" + outOrderNo + "]对应订单信息为空");
      return messagerVo;
    }

    //订单状态 0-代付款 1-支付成功 2-合约办理成功 3-合约办理失败 4-订单已同步联通集团商城
    Integer status = orderInfo.getStatus();
    String isPaySuccess = orderInfo.getIsPaySuccess();
    String processStates = orderInfo.getProcessStates();
    if(status == 0 || "0".equals(isPaySuccess)) {
      messagerVo.setSubCode("40004");
      messagerVo.setSubMsg("订单号[" + outOrderNo + "]还没有冻结支付成功,不允许进行派发操作");
      return messagerVo;
    } else if (status == 3 || ProcessStatesEnum.BUSINESS_HANDLING_FAILED.getCode().equals(processStates)){
      messagerVo.setSubCode("40005");
      messagerVo.setSubMsg("订单号[" + outOrderNo + "]订单办理业务失败,不允许进行派发操作");
      return messagerVo;
    }

    //判断订单是否已经派发权益,派发权益的订单直接返回派成功,并且把链接返回前端
    if(ProcessStatesEnum.SEND_EQUITY_SUCCESS.getCode().equals(processStates)) {
      messagerVo.setSubCode("10000");
      messagerVo.setSubMsg("订单[" + outOrderNo + "]已经派发权益成功,不需要重新派发");
      return messagerVo;
    }

    String businessType = orderInfo.getBusinessType();
    //存量订单进行权益派发
    if("clsd".equals(businessType) || "llb".equals(businessType)){
      // 派发权益
      MessagerVo messagerVoTicket = tmallEquityService.sendEquity(dataObject, orderInfo);
      log.info("存量业务权益派发响应报文=" + JSON.toJSONString(messagerVoTicket));
      if (!"10000".equals(messagerVoTicket.getSubCode())) {
        messagerVo.setSubCode("40009");
        messagerVo.setSubMsg(messagerVoTicket.getSubMsg());
      } else {
        messagerVo.setSubCode("10000");
        messagerVo.setSubMsg(messagerVoTicket.getSubMsg());
        messagerVo.setData(messagerVoTicket.getData());
      }

      return messagerVo;
    }

    //===========================新用户入网权益派发=======================
    //获取订单对应的用户信息
    String custNo = orderInfo.getCustNo();
    if(CommonUtil.isEmpty(custNo)){
      messagerVo.setSubCode("40006");
      messagerVo.setSubMsg("订单号[" + outOrderNo + "]对应订单的custNo为空");
      return messagerVo;
    }
    CustomerInfo customerInfo = customerInfoService.getById(custNo);
    if(customerInfo == null){
      messagerVo.setSubCode("40007");
      messagerVo.setSubMsg("订单号[" + outOrderNo + "]对应订单的用户信息为空");
      return messagerVo;
    }

    // 派发权益
    MessagerVo messagerVoTicket = tmallEquityService.sendEquity(dataObject, orderInfo ,customerInfo);
    log.info("新用户入网业务权益派发响应报文=" + JSON.toJSONString(messagerVoTicket));
    if (!"10000".equals(messagerVoTicket.getSubCode())) {
      messagerVo.setSubCode("40008");
      messagerVo.setSubMsg(messagerVoTicket.getSubMsg());
    } else {
      messagerVo.setSubCode("10000");
      messagerVo.setSubMsg(messagerVoTicket.getSubMsg());
      messagerVo.setData(messagerVoTicket.getData());
    }

    return messagerVo;
  }
}
