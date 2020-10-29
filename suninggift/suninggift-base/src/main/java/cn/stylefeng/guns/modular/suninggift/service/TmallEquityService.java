package cn.stylefeng.guns.modular.suninggift.service;


import cn.stylefeng.guns.modular.suninggift.entity.CustomerInfo;
import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.enums.OperatorEnum;
import cn.stylefeng.guns.modular.suninggift.enums.ProcessStatesEnum;
import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.DebitCreateAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.DebitSubscribeAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ApiInBizResponse;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ApiInBizResponseMsg;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.PolicyListResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.PolicyListResponseBizContentPolicy;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.PolicyListResponseBizContentProductPolicy;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.QueryAgencyOrStoreResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.QueryAgencyOrStoreResponseBizContentCdisAgency;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.QueryAgencyOrStoreResponseBizContentCdisAgencyGaths;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.QueryAgencyOrStoreResponseBizContentCdisAgencyTasks;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.QueryAgencyOrStoreResponseBizContentCdisStore;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.QueryCarrierAccountInfoResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.QueryProductPolicyDetailResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.EquitycodeVo;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.SendEquityVo;
import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.modular.suninggift.model.constant.DeployInterfInfo;
import cn.stylefeng.guns.modular.suninggift.tools.InInterfaceServiceTool;
import cn.stylefeng.guns.modular.suninggift.tools.SysConfigServiceTool;
import cn.stylefeng.guns.modular.suninggift.utils.CommonUtil;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSONObject;
import com.gzlplink.cloud.mps.client.model.access.entity.CdisAgency;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 权益派发服务类
 */
@Service
@Slf4j
public class TmallEquityService {

  @Autowired
  private SysConfigService sysConfigService;

  @Autowired
  private InInterfaceService inInterfaceService;

  @Autowired
  private SysConfigServiceTool sysConfigServiceTool;

  @Autowired
  private InInterfaceServiceTool inInterfaceServiceTool;

  @Autowired
  private OrderInfoService orderInfoService;

  /**
   * 权益派发
   * @param dataObject
   * @param orderInfo
   * @return
   */
  public MessagerVo sendEquity(JSONObject dataObject, OrderInfo orderInfo ,CustomerInfo customerInfo) {
    MessagerVo messagerVo = new MessagerVo();

    //根据运营商编码获取政策信息
    String lpunicom = OperatorEnum.lpsuningunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpunicom);
    //调用pot获取苏宁新入网政策列表信息
    String outProtNo = cardCenterInterfInfo.getOutPortNo();
    ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo , outProtNo ,false);
    if(null == policyListResponse || !"10000".equals(policyListResponse.getCode())){
      messagerVo.setSubCode("30007");
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
      messagerVo.setSubCode("30008");
      messagerVo.setSubMsg("产品政策详情为空");
      return messagerVo;
    }

    //获取本订单对应的0级政策编码
    String zeroSpecPsnId = orderInfo.getSpecPsnId();

    //遍历子元素中所有的二级政策
    String rightsFlag = "";
    //权益编码list
    List<EquitycodeVo> equitycodeList = new ArrayList<EquitycodeVo>();
    //权益名称
    String desc = null;
    //调拨放款商家外部编码
    String outAgencyNo = "";
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
              rightsFlag = cdisSpecLv2.getKeyStr();
            }

            //节点表识，明细类型：1-下单明细，2-垫支明细，3-趸交明细，4-分期明细,5-退款明细，6-权益明细
            if(6 == Integer.valueOf(cdisSpecLv2.getCheckFlag())) {
              EquitycodeVo equitycodeVo = new EquitycodeVo();
              equitycodeVo.setEquityCode(cdisSpecLv2.getKeyStr());
              equitycodeVo.setEquityAmount(cdisSpecLv2.getValueStr());
              equitycodeList.add(equitycodeVo);

              desc = cdisSpecLv2.getRemarkInfo();
              outAgencyNo = cdisSpecLv2.getName();
            }
          }
        }
      }
    }

    String outTradeNo = orderInfo.getOutTradeNo();
    //“PAY_MODEL”为先激活后派劵的情况不做派券处理
    if(!CommonUtil.isEmpty(rightsFlag) && "PAY_MODEL".equals(rightsFlag)) {
      messagerVo.setSubCode("30009");
      messagerVo.setSubMsg("该订单[" + outTradeNo + "]为先激活后派劵订单");
      log.info("该订单outOrderNo=[" + outTradeNo + "]为先激活后派劵订单");
      return messagerVo;
    }

    if(CommonUtil.isEmpty(outAgencyNo)) {
      messagerVo.setSubCode("30010");
      messagerVo.setSubMsg("权益政策中调拨放款商户外部编码为空");
      log.info(desc + " ,权益政策中调拨放款商户外部编码为空");
      return messagerVo;
    }

    //获取调拨创建相关参数
    cardCenterInterfInfo.setOutAgencyNo(outAgencyNo);
    ApiInBizResponse<QueryAgencyOrStoreResponseBizContent> queryAgencyOrStoreResp = inInterfaceService.queryAgencyOrStore(cardCenterInterfInfo , false);
    if (null == queryAgencyOrStoreResp || !queryAgencyOrStoreResp.getCode().equals("10000")) {
      messagerVo.setSubCode("30011");
      messagerVo.setSubMsg("商户外部编码[" + outAgencyNo + "]对应调拨商户、门店信息异常, " + queryAgencyOrStoreResp.getMessage());
      log.info("商户外部编码["+outAgencyNo+"]对应调拨商户、门店信息异常 ：" + queryAgencyOrStoreResp.getMessage());
      return messagerVo;
    }

    //获取商户冻结业务信息
    QueryAgencyOrStoreResponseBizContentCdisAgencyTasks freezeAgencyTasks = null;
    List<QueryAgencyOrStoreResponseBizContentCdisAgencyTasks> agencyTasksList = queryAgencyOrStoreResp.getBizContent().getCdisAgencyTasks();
    for (QueryAgencyOrStoreResponseBizContentCdisAgencyTasks agencyTasks : agencyTasksList) {
      if(agencyTasks.getTasksType().intValue() == 20102){
        freezeAgencyTasks = agencyTasks;
      }
    }

    String ataskNo = freezeAgencyTasks.getAtaskNo();
    //遍历判断收款账号信息
    // 查询商家收款账号(网商)
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths wSAgencyGath = null;
    // 查询商家空白收款账号信息-判断银行卡或者支付宝收款
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths blankAgencyGath = null;
    // 查询商家-支付宝账号
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths alipayAgencyGath = null;
    //查询商家-银行卡账号
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths bankAgencyGath = null;
    List<QueryAgencyOrStoreResponseBizContentCdisAgencyGaths> cdisAgencyGaths = queryAgencyOrStoreResp.getBizContent().getCdisAgencyGaths();
    for (QueryAgencyOrStoreResponseBizContentCdisAgencyGaths agencyGath : cdisAgencyGaths) {
      //gathType收款模式[0-销售营收,1-违约追收,2-主业追收,3-商户分润,4-垫资垫付,5-空白模式,6-话费趸交]
      //authType授信类:1-支付宝,2-微信,3-QQ,4-新浪,5-农业银行,6-网上银行
      if(agencyGath.getGathType() == 0 && ataskNo.equals(agencyGath.getAtaskNo())){
        //销售营收账号
        blankAgencyGath = agencyGath;
      }else if(agencyGath.getGathType() == 4 && agencyGath.getAuthType() == 6 && ataskNo.equals(agencyGath.getAtaskNo())){
        //垫资垫付账号
        wSAgencyGath = agencyGath;
      }else if(agencyGath.getGathType() == 5 && agencyGath.getAuthType() == 1 && ataskNo.equals(agencyGath.getAtaskNo())){
        //支付宝账号
        alipayAgencyGath = agencyGath;
      }else if(agencyGath.getGathType() == 5 && agencyGath.getAuthType() == 0 && ataskNo.equals(agencyGath.getAtaskNo())){
        //银行账号
        bankAgencyGath = agencyGath;
      }
    }

    // 推送调拨系统的收款账户
    String fundCarrierAccount = "";
    // 供应商账户类型 （ 收款账号-收款模式类型(0)-对应的授信类型）
    String fundCarrierAccountType = "";
    // 冻结的渠道编号
    String freezeExecChannelType = "";
    // 区分支付宝收款和银行卡收款
    if(blankAgencyGath != null && 1 == blankAgencyGath.getAuthType()){
      // 支付宝收款
      fundCarrierAccount = alipayAgencyGath.getAccountNo();
      fundCarrierAccountType = "alipay";
      freezeExecChannelType = "alipay";
    }else if(blankAgencyGath != null && 0 == blankAgencyGath.getAuthType()){
      // 银行收款
      fundCarrierAccount = bankAgencyGath.getAccountNo();
      fundCarrierAccountType = "outBank";
      freezeExecChannelType = "alipay";

    }

    // 根据账户编码获取对应的账号信息
    cardCenterInterfInfo.setAccountNo(productPolicy.getAccountNo());
    ApiInBizResponse<QueryCarrierAccountInfoResponseBizContent> apiInBizResponse = inInterfaceService.queryCarrierAccountInfo(cardCenterInterfInfo ,false);
    if(null == apiInBizResponse || !apiInBizResponse.getCode().equals("10000")){
      messagerVo.setSubCode("30012");
      messagerVo.setSubMsg("账号编码[" + cardCenterInterfInfo.getAccountNo() + "]对应的商户账号信息失败, " + apiInBizResponse.getMessage());
      return messagerVo;
    }
    QueryCarrierAccountInfoResponseBizContent carrierAccount = apiInBizResponse.getBizContent();

    //查询政策拆分明细信息
    String policyNo = productPolicy.getPolicyNo();
    ApiInBizResponse<QueryProductPolicyDetailResponseBizContent> policyDetailResp = inInterfaceService.queryProductPolicyDetail(policyNo, zeroSpecPsnId, cardCenterInterfInfo, false);
    if(policyDetailResp == null){
      messagerVo.setSubCode("30013");
      messagerVo.setSubMsg("查询政策拆分明细信息为空");
      return messagerVo;
    }

    // 垫资方编号
    String fundInvertNo = policyDetailResp.getBizContent().getFundInvertNo();

    // 调拨类型
    String allocationType = policyDetailResp.getBizContent().getAllocationType();

    // 违约处理类型
    String violateHandleType = policyDetailResp.getBizContent().getViolateHandleType();

    // 违约应收款
    BigDecimal violateReceivables = new BigDecimal(0);
    if (!StringUtils.isEmpty(policyDetailResp.getBizContent().getViolateReceivables())) {
      violateReceivables = new BigDecimal(policyDetailResp.getBizContent().getViolateReceivables());
    }
    // 垫资总金额
    BigDecimal fundTotalMoney = new BigDecimal(0);
    if (!StringUtils.isEmpty(policyDetailResp.getBizContent().getFundTotalMoney())) {
      fundTotalMoney = new BigDecimal(policyDetailResp.getBizContent().getFundTotalMoney());
    }

    DebitCreateAo debitCreateAo = new DebitCreateAo();
    debitCreateAo.setOutOrderNo(orderInfo.getAuthOrderNo());
    debitCreateAo.setOutTradeNo(orderInfo.getAuthRequestNo());
    JSONObject bodyObject = JSONObject.parseObject(orderInfo.getField1());
    debitCreateAo.setAgencyNum(bodyObject.getString("agencyNo"));
    debitCreateAo.setOrderTitle(orderInfo.getName());
    debitCreateAo.setOrderPriceAmount(orderInfo.getTotalFee());
    //订单首付金额
    BigDecimal orderPayAmount = new BigDecimal(0);
    debitCreateAo.setOrderPayAmount(orderPayAmount);
    debitCreateAo.setOrderTradeAmount(orderInfo.getTotalFee());
    debitCreateAo.setOrderTradeFqNum(Integer.valueOf(orderInfo.getFreezeMonth()));//订单分期期数
    debitCreateAo.setCustName(customerInfo.getCustName());
    debitCreateAo.setCarrierNo(productPolicy.getCarrierNo());
    debitCreateAo.setFundInvertNo(fundInvertNo);//垫资方编号
    debitCreateAo.setFundTotalMoney(fundTotalMoney);//垫资总金额
    debitCreateAo.setCustPhone(orderInfo.getPhone());
    //调拨状态变更接口
    String deployNotifyUrl = sysConfigService.getByCode("suninggiftUrl") + "/api/debit/callBack";
    debitCreateAo.setNotifyUrl(deployNotifyUrl);//异步通知地址
    debitCreateAo.setCreateUserName(productPolicy.getCarrierOrgNo());//创建者
    debitCreateAo.setFreezeDetails(JSONObject.toJSONString(policyDetailResp.getBizContent().getFreezeDetails()));//冻结明细
    debitCreateAo.setSettleDetails(JSONObject.toJSONString(policyDetailResp.getBizContent().getSettleDetails()));//分润明细
    debitCreateAo.setIsAdvanceFund(0);//此处统一填0，0需要垫资,1不需要垫资
    debitCreateAo.setAllocationType(allocationType);
    debitCreateAo.setFundCarrierNo(wSAgencyGath.getAccountId());
    debitCreateAo.setFundCarrierName(alipayAgencyGath.getAccountName());
    debitCreateAo.setFundCarrierAccount(fundCarrierAccount);
    debitCreateAo.setFundCarrierAccountType(fundCarrierAccountType);
    debitCreateAo.setViolateHandleType(violateHandleType);
    if(violateReceivables.compareTo(new BigDecimal(0)) > 0){
      debitCreateAo.setViolateReceivables(violateReceivables);
    }
    debitCreateAo.setPolicyNo(productPolicy.getPolicyNo());
    debitCreateAo.setPhoneBelong(customerInfo.getCustProvince());
    debitCreateAo.setGoodsId(productPolicy.getProductId()); //内部产品编码[product表的product_id]外键关联
    debitCreateAo.setOrderTradeStatus("WAIT_BUYER_PAY");//交易状态
    debitCreateAo.setOrderStatus(0);//订单状态//默认初始0
    debitCreateAo.setCarrierOrg(productPolicy.getCarrierOrgNo());//机构编号，订单透传，没有填0//业务接入商户[非技术接入商户]的关联组织的ID-具体问齐文
    debitCreateAo.setFreezeExecAuthCode("0");
    debitCreateAo.setFundOrderTotalMoney(orderInfo.getTotalFee());//订单总金额
    debitCreateAo.setFreezeExecAccount(carrierAccount.getPayeeLogonId());
    debitCreateAo.setFreezeExecPid(carrierAccount.getPayeeUserId());
    debitCreateAo.setOrderTradeType(1001);//1001-线上支付宝实时支付
    debitCreateAo.setFreezeExecChannelType(freezeExecChannelType);
    debitCreateAo.setStoreNum(bodyObject.getString("storeNo"));
    debitCreateAo.setCarrierDept("0");//商户部门
    debitCreateAo.setContractFund(orderInfo.getTotalFee());//签约金额
    debitCreateAo.setNeedPayPerMonth(new BigDecimal(0));
    debitCreateAo.setCityName(customerInfo.getCustCity());
    debitCreateAo.setProvinceName(customerInfo.getCustProvince());

    //获取调拨订阅相关参数
    DebitSubscribeAo debitSubscribeAo = inInterfaceServiceTool.debitSubscribeDispose(orderInfo);

    //设置淘淘票券权益请求参数
    SendEquityVo sendEquityVo = new SendEquityVo();

    sendEquityVo.setOutOrderNo(orderInfo.getAuthOrderNo());
    sendEquityVo.setOutTradeNo(orderInfo.getAuthRequestNo());
    sendEquityVo.setOutOrderStatus("TRADE_SUCCESS");

    //派券机构 unicom_pay 联通支付   suning 苏宁易购  walmart 沃尔 、 TaoTicket 淘票票
    sendEquityVo.setSendOrg("TaoTicket");
    //派券方式 phone 手机号   alipay 支付宝
    sendEquityVo.setSendWay("alipay");
    //付款支付宝userid
    sendEquityVo.setBuyerUserId(orderInfo.getUserId());
    //外部订单金额
    sendEquityVo.setOrderAmount(String.valueOf(orderInfo.getTotalFee()));
    //组合类型 单个--SINGLE，组合--COMBINATION
    sendEquityVo.setCombineType("COMBINATION");
    sendEquityVo.setOutOrderTitle(orderInfo.getName());
    sendEquityVo.setSendOrgValue(dataObject.getString("sendOrgValue"));
    if(!CommonUtil.isEmpty(dataObject.getString("merOrderNo"))){
      sendEquityVo.setMemberNo(dataObject.getString("merOrderNo"));
    }
    //设置异步通知地址
    String notifyUrl = sysConfigService.getByCode("suninggiftUrl") + "/api/equity/rights/";
    sendEquityVo.setNotifyUrl(notifyUrl);
    //门店商家编码
    sendEquityVo.setMerchantId(bodyObject.getString("agencyNo"));
    //商家名称
    sendEquityVo.setMerchantName(bodyObject.getString("agencyName"));
    //门店名称
    sendEquityVo.setStoreName(bodyObject.getString("storeName"));
    //门店编码
    sendEquityVo.setStoreCode(bodyObject.getString("storeNo"));
    //设置调拨创建参数
    sendEquityVo.setDeployCreateString(JSONObject.toJSONString(debitCreateAo));
    //设置调拨订阅参数
    sendEquityVo.setDeploySynchronieString(JSONObject.toJSONString(debitSubscribeAo));
    //添加到权益集合
    sendEquityVo.setEquityEntityList(equitycodeList);

    //进行权益派发能力调用
    DeployInterfInfo deployInfo = JSONObject.parseObject(sysConfigService.getByCode("deployInfo"), DeployInterfInfo.class);
    MessagerVo messagerEquityVo = inInterfaceService.sendEquity(sendEquityVo ,deployInfo);
    if("10000".equals(messagerEquityVo.getSubCode())){
      ApiInBizResponseMsg apiInBizResponseMsg = (ApiInBizResponseMsg) messagerEquityVo.getData();
      if("10009".equals(apiInBizResponseMsg.getCode())){
        messagerVo.setSubCode("10000");
        messagerVo.setSubMsg(desc + "权益派发成功");
      }else{
        messagerVo.setSubCode("30014");
        messagerVo.setSubMsg(desc + "权益派发失败");
      }
    }

    return messagerVo;
  }


  /**
   * 新用户入网：调拨创建相关参数
   * @param orderInfo
   * @return
   */
  public Map<String,Object> freezeOrderCreate(OrderInfo orderInfo ,CustomerInfo customerInfo) {
    Map<String,Object> resultMap = new HashMap<String,Object>();

    //根据运营商编码获取政策信息
    String lpunicom = OperatorEnum.lpsuningunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpunicom);
    //调用pot获取苏宁新入网政策列表信息
    String outProtNo = cardCenterInterfInfo.getOutPortNo();
    ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo , outProtNo ,false);
    if(null == policyListResponse || !"10000".equals(policyListResponse.getCode())){
      resultMap.put("code" , "40001");
      resultMap.put("msg" , policyListResponse.getMessage());
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

    //把整理的政策信息设置到实体中
    PolicyListResponseBizContentProductPolicy productPolicy =policyListResponseBizContent.getCdisProductPolicy();
    productPolicyVo.setCdisProductPolicy(productPolicy);
    productPolicyVo.setSpecList(zeroList);
    if (productPolicyVo == null) {
      resultMap.put("code" , "30016");
      resultMap.put("msg" , "产品政策详情为空");
      return resultMap;
    }

    //获取本订单对应的0级政策编码
    String zeroSpecPsnId = orderInfo.getSpecPsnId();

    //权益名称
    String desc = null;
    //调拨放款商家外部编码
    String outAgencyNo = "";
    //遍历子元素中所有的二级政策
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
            if(6 == Integer.valueOf(cdisSpecLv2.getCheckFlag())) {
              desc = cdisSpecLv2.getRemarkInfo();
              outAgencyNo = cdisSpecLv2.getName();
            }
          }
        }
      }
    }

    if(CommonUtil.isEmpty(outAgencyNo)) {
      log.info(desc + " ,权益政策中调拨放款商户外部编码为空");
      resultMap.put("code" , "30004");
      resultMap.put("msg" , "权益政策中调拨放款商户外部编码为空");
      return resultMap;
    }

    //获取调拨创建相关参数
    cardCenterInterfInfo.setOutAgencyNo(outAgencyNo);
    ApiInBizResponse<QueryAgencyOrStoreResponseBizContent> queryAgencyOrStoreResp = inInterfaceService.queryAgencyOrStore(cardCenterInterfInfo , false);
    if (null == queryAgencyOrStoreResp || !queryAgencyOrStoreResp.getCode().equals("10000")) {
      log.info("商户外部编码["+outAgencyNo+"]对应调拨商户、门店信息异常 ：" + queryAgencyOrStoreResp.getMessage());
      resultMap.put("code" , "30023");
      resultMap.put("msg" , "商户外部编码[" + outAgencyNo + "]对应调拨商户、门店信息异常, " + queryAgencyOrStoreResp.getMessage());
      return resultMap;
    }

    //获取商户冻结业务信息
    QueryAgencyOrStoreResponseBizContentCdisAgencyTasks freezeAgencyTasks = null;
    List<QueryAgencyOrStoreResponseBizContentCdisAgencyTasks> agencyTasksList = queryAgencyOrStoreResp.getBizContent().getCdisAgencyTasks();
    for (QueryAgencyOrStoreResponseBizContentCdisAgencyTasks agencyTasks : agencyTasksList) {
      if(agencyTasks.getTasksType().intValue() == 20102){
        freezeAgencyTasks = agencyTasks;
      }
    }

    String ataskNo = freezeAgencyTasks.getAtaskNo();
    //遍历判断收款账号信息
    // 查询商家收款账号(网商)
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths wSAgencyGath = null;
    // 查询商家空白收款账号信息-判断银行卡或者支付宝收款
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths blankAgencyGath = null;
    // 查询商家-支付宝账号
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths alipayAgencyGath = null;
    //查询商家-银行卡账号
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths bankAgencyGath = null;
    List<QueryAgencyOrStoreResponseBizContentCdisAgencyGaths> cdisAgencyGaths = queryAgencyOrStoreResp.getBizContent().getCdisAgencyGaths();
    for (QueryAgencyOrStoreResponseBizContentCdisAgencyGaths agencyGath : cdisAgencyGaths) {
      if(agencyGath.getGathType() == 0 && ataskNo.equals(agencyGath.getAtaskNo())){
        blankAgencyGath = agencyGath;
      }else if(agencyGath.getGathType() == 4 && agencyGath.getAuthType() == 6 && ataskNo.equals(agencyGath.getAtaskNo())){
        wSAgencyGath = agencyGath;
      }else if(agencyGath.getGathType() == 1 && agencyGath.getAuthType() == 5 && ataskNo.equals(agencyGath.getAtaskNo())){
        alipayAgencyGath = agencyGath;
      }else if(agencyGath.getGathType() == 5 && agencyGath.getAuthType() == 0 && ataskNo.equals(agencyGath.getAtaskNo())){
        bankAgencyGath = agencyGath;
      }
    }

    // 推送调拨系统的收款账户
    String fundCarrierAccount = "";
    // 供应商账户类型 （ 收款账号-收款模式类型(0)-对应的授信类型）
    String fundCarrierAccountType = "";
    // 冻结的渠道编号
    String freezeExecChannelType = "";
    // 区分支付宝收款和银行卡收款
    if(blankAgencyGath != null && 1 == blankAgencyGath.getAuthType()){
      // 支付宝收款
      fundCarrierAccount = alipayAgencyGath.getAccountNo();
      fundCarrierAccountType = "alipay";
      freezeExecChannelType = "alipay";
    }else if(blankAgencyGath != null && 0 == blankAgencyGath.getAuthType()){
      // 银行收款
      fundCarrierAccount = bankAgencyGath.getAccountNo();
      fundCarrierAccountType = "outBank";
      freezeExecChannelType = "alipay";

    }

    // 根据账户编码获取对应的账号信息
    cardCenterInterfInfo.setAccountNo(productPolicy.getAccountNo());
    ApiInBizResponse<QueryCarrierAccountInfoResponseBizContent> apiInBizResponse = inInterfaceService.queryCarrierAccountInfo(cardCenterInterfInfo ,false);
    if(null == apiInBizResponse || !apiInBizResponse.getCode().equals("10000")){
      resultMap.put("code" , "30022");
      resultMap.put("msg" , "账号编码[" + cardCenterInterfInfo.getAccountNo() + "]对应的商户账号信息失败, " + apiInBizResponse.getMessage());
      return resultMap;
    }
    QueryCarrierAccountInfoResponseBizContent carrierAccount = apiInBizResponse.getBizContent();

    //查询政策拆分明细信息
    String policyNo = productPolicy.getPolicyNo();
    ApiInBizResponse<QueryProductPolicyDetailResponseBizContent> policyDetailResp = inInterfaceService.queryProductPolicyDetail(policyNo, zeroSpecPsnId, cardCenterInterfInfo, false);
    if(policyDetailResp == null){
      resultMap.put("code" , "30022");
      resultMap.put("msg" , "查询政策拆分明细信息为空");
      return resultMap;
    }

    // 垫资方编号
    String fundInvertNo = policyDetailResp.getBizContent().getFundInvertNo();

    // 调拨类型
    String allocationType = policyDetailResp.getBizContent().getAllocationType();

    // 违约处理类型
    String violateHandleType = policyDetailResp.getBizContent().getViolateHandleType();

    // 违约应收款
    BigDecimal violateReceivables = new BigDecimal(0);
    if (!StringUtils.isEmpty(policyDetailResp.getBizContent().getViolateReceivables())) {
      violateReceivables = new BigDecimal(policyDetailResp.getBizContent().getViolateReceivables());
    }
    // 垫资总金额
    BigDecimal fundTotalMoney = new BigDecimal(0);
    if (!StringUtils.isEmpty(policyDetailResp.getBizContent().getFundTotalMoney())) {
      fundTotalMoney = new BigDecimal(policyDetailResp.getBizContent().getFundTotalMoney());
    }

    DebitCreateAo debitCreateAo = new DebitCreateAo();
    debitCreateAo.setOutOrderNo(orderInfo.getAuthOrderNo());
    debitCreateAo.setOutTradeNo(orderInfo.getAuthRequestNo());
    JSONObject bodyObject = JSONObject.parseObject(orderInfo.getField1());
    debitCreateAo.setAgencyNum(bodyObject.getString("agencyNo"));
    debitCreateAo.setOrderTitle(orderInfo.getName());
    debitCreateAo.setOrderPriceAmount(orderInfo.getTotalFee());
    //订单首付金额
    BigDecimal orderPayAmount = new BigDecimal(0);
    debitCreateAo.setOrderPayAmount(orderPayAmount);
    debitCreateAo.setOrderTradeAmount(orderInfo.getTotalFee());
    debitCreateAo.setOrderTradeFqNum(Integer.valueOf(orderInfo.getFreezeMonth()));//订单分期期数
    debitCreateAo.setCustName(customerInfo.getCustName());
    debitCreateAo.setCarrierNo(productPolicy.getCarrierNo());
    debitCreateAo.setFundInvertNo(fundInvertNo);//垫资方编号
    debitCreateAo.setFundTotalMoney(fundTotalMoney);//垫资总金额
    debitCreateAo.setCustPhone(orderInfo.getPhone());
    //调拨状态变更接口
    String deployNotifyUrl = sysConfigService.getByCode("suninggiftUrl") + "/api/debit/callBack";
    debitCreateAo.setNotifyUrl(deployNotifyUrl);//异步通知地址
    debitCreateAo.setCreateUserName(productPolicy.getCarrierOrgNo());//创建者
    debitCreateAo.setFreezeDetails(JSONObject.toJSONString(policyDetailResp.getBizContent().getFreezeDetails()));//冻结明细
    debitCreateAo.setSettleDetails(JSONObject.toJSONString(policyDetailResp.getBizContent().getSettleDetails()));//分润明细
    debitCreateAo.setIsAdvanceFund(0);//此处统一填0，0需要垫资,1不需要垫资
    debitCreateAo.setAllocationType(allocationType);
    debitCreateAo.setFundCarrierNo(wSAgencyGath.getAccountId());
    debitCreateAo.setFundCarrierName(alipayAgencyGath.getAccountName());
    debitCreateAo.setFundCarrierAccount(fundCarrierAccount);
    debitCreateAo.setFundCarrierAccountType(fundCarrierAccountType);
    debitCreateAo.setViolateHandleType(violateHandleType);
    if(violateReceivables.compareTo(new BigDecimal(0)) > 0){
      debitCreateAo.setViolateReceivables(violateReceivables);
    }
    debitCreateAo.setPolicyNo(productPolicy.getPolicyNo());
    debitCreateAo.setPhoneBelong(customerInfo.getCustProvince());
    debitCreateAo.setGoodsId(productPolicy.getProductId()); //内部产品编码[product表的product_id]外键关联
    debitCreateAo.setOrderTradeStatus("WAIT_BUYER_PAY");//交易状态
    debitCreateAo.setOrderStatus(0);//订单状态//默认初始0
    debitCreateAo.setCarrierOrg(productPolicy.getCarrierOrgNo());//机构编号，订单透传，没有填0//业务接入商户[非技术接入商户]的关联组织的ID-具体问齐文
    debitCreateAo.setFreezeExecAuthCode("0");
    debitCreateAo.setFundOrderTotalMoney(orderInfo.getTotalFee());//订单总金额
    debitCreateAo.setFreezeExecAccount(carrierAccount.getPayeeLogonId());
    debitCreateAo.setFreezeExecPid(carrierAccount.getPayeeUserId());
    debitCreateAo.setOrderTradeType(1001);//1001-线上支付宝实时支付
    debitCreateAo.setFreezeExecChannelType(freezeExecChannelType);
    debitCreateAo.setStoreNum(bodyObject.getString("storeNo"));
    debitCreateAo.setCarrierDept("0");//商户部门
    debitCreateAo.setContractFund(orderInfo.getTotalFee());//签约金额
    debitCreateAo.setNeedPayPerMonth(new BigDecimal(0));
    debitCreateAo.setCityName(customerInfo.getCustCity());
    debitCreateAo.setProvinceName(customerInfo.getCustProvince());

    ApiInBizResponseMsg apiInBizResponseMsg = inInterfaceService.debitCreate(debitCreateAo);
    log.info("苏宁新用户入网调拨创建响应结果," + JSONObject.toJSONString(apiInBizResponseMsg));
    if ("10001".equals(apiInBizResponseMsg.getSubCode())||"50001".equals(apiInBizResponseMsg.getSubCode())){
      orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_CREATE_SUCCESS.getCode());
      orderInfo.setUpdateTime(new Date());

    }else {
      orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_CREATE_FAILED.getCode());
      orderInfo.setUpdateTime(new Date());
    }

    //更新订单状态
    orderInfoService.updateById(orderInfo);

    resultMap.put("code", apiInBizResponseMsg.getSubCode());
    resultMap.put("msg", apiInBizResponseMsg.getSubMsg());
    return resultMap;
  }

  /**
   * 存量业务订单：调拨创建相关参数
   * @param orderInfo
   * @return
   */
  public Map<String,Object> freezeOrderCreate(OrderInfo orderInfo) {
    Map<String,Object> resultMap = new HashMap<String,Object>();

    //根据运营商编码获取政策信息
    String lpunicom = OperatorEnum.lpunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpunicom);
    //调用pot获取苏宁新入网政策列表信息
    String outProtNo = cardCenterInterfInfo.getOutPortNo();
    ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo , outProtNo ,false);
    if(null == policyListResponse || !"10000".equals(policyListResponse.getCode())){
      resultMap.put("code" , "40001");
      resultMap.put("msg" , policyListResponse.getMessage());
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

    //把整理的政策信息设置到实体中
    PolicyListResponseBizContentProductPolicy productPolicy =policyListResponseBizContent.getCdisProductPolicy();
    productPolicyVo.setCdisProductPolicy(productPolicy);
    productPolicyVo.setSpecList(zeroList);
    if (productPolicyVo == null) {
      resultMap.put("code" , "30016");
      resultMap.put("msg" , "产品政策详情为空");
      return resultMap;
    }

    //获取本订单对应的0级政策编码
    String zeroSpecPsnId = orderInfo.getSpecPsnId();

    //权益名称
    String desc = null;
    //调拨放款商家外部编码
    String outAgencyNo = "";
    //遍历子元素中所有的二级政策
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
            if(6 == Integer.valueOf(cdisSpecLv2.getCheckFlag())) {
              desc = cdisSpecLv2.getRemarkInfo();
              outAgencyNo = cdisSpecLv2.getName();
            }
          }
        }
      }
    }

    if(CommonUtil.isEmpty(outAgencyNo)) {
      log.info(desc + " ,权益政策中调拨放款商户外部编码为空");
      resultMap.put("code" , "30004");
      resultMap.put("msg" , "权益政策中调拨放款商户外部编码为空");
      return resultMap;
    }

    //获取调拨创建相关参数
    cardCenterInterfInfo.setOutAgencyNo(outAgencyNo);
    ApiInBizResponse<QueryAgencyOrStoreResponseBizContent> queryAgencyOrStoreResp = inInterfaceService.queryAgencyOrStore(cardCenterInterfInfo , false);
    if (null == queryAgencyOrStoreResp || !queryAgencyOrStoreResp.getCode().equals("10000")) {
      log.info("商户外部编码["+outAgencyNo+"]对应调拨商户、门店信息异常 ：" + queryAgencyOrStoreResp.getMessage());
      resultMap.put("code" , "30023");
      resultMap.put("msg" , "商户外部编码[" + outAgencyNo + "]对应调拨商户、门店信息异常, " + queryAgencyOrStoreResp.getMessage());
      return resultMap;
    }

    //获取商家信息
    QueryAgencyOrStoreResponseBizContentCdisAgency cdisAgency = queryAgencyOrStoreResp.getBizContent().getCdisAgency();
    //获取门店信息
    QueryAgencyOrStoreResponseBizContentCdisStore cdisStore = queryAgencyOrStoreResp.getBizContent().getCdisStore();

    //获取商户冻结业务信息
    QueryAgencyOrStoreResponseBizContentCdisAgencyTasks freezeAgencyTasks = null;
    List<QueryAgencyOrStoreResponseBizContentCdisAgencyTasks> agencyTasksList = queryAgencyOrStoreResp.getBizContent().getCdisAgencyTasks();
    for (QueryAgencyOrStoreResponseBizContentCdisAgencyTasks agencyTasks : agencyTasksList) {
      if(agencyTasks.getTasksType().intValue() == 20102){
        freezeAgencyTasks = agencyTasks;
      }
    }

    String ataskNo = freezeAgencyTasks.getAtaskNo();
    //遍历判断收款账号信息
    // 查询商家收款账号(网商)
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths wSAgencyGath = null;
    // 查询商家空白收款账号信息-判断银行卡或者支付宝收款
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths blankAgencyGath = null;
    // 查询商家-支付宝账号
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths alipayAgencyGath = null;
    //查询商家-银行卡账号
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths bankAgencyGath = null;
    List<QueryAgencyOrStoreResponseBizContentCdisAgencyGaths> cdisAgencyGaths = queryAgencyOrStoreResp.getBizContent().getCdisAgencyGaths();
    for (QueryAgencyOrStoreResponseBizContentCdisAgencyGaths agencyGath : cdisAgencyGaths) {
      if(agencyGath.getGathType() == 0 && ataskNo.equals(agencyGath.getAtaskNo())){
        blankAgencyGath = agencyGath;
      }else if(agencyGath.getGathType() == 4 && agencyGath.getAuthType() == 6 && ataskNo.equals(agencyGath.getAtaskNo())){
        wSAgencyGath = agencyGath;
      }else if(agencyGath.getGathType() == 1 && agencyGath.getAuthType() == 5 && ataskNo.equals(agencyGath.getAtaskNo())){
        alipayAgencyGath = agencyGath;
      }else if(agencyGath.getGathType() == 5 && agencyGath.getAuthType() == 0 && ataskNo.equals(agencyGath.getAtaskNo())){
        bankAgencyGath = agencyGath;
      }
    }

    // 推送调拨系统的收款账户
    String fundCarrierAccount = "";
    // 供应商账户类型 （ 收款账号-收款模式类型(0)-对应的授信类型）
    String fundCarrierAccountType = "";
    // 冻结的渠道编号
    String freezeExecChannelType = "";
    // 区分支付宝收款和银行卡收款
    if(blankAgencyGath != null && 1 == blankAgencyGath.getAuthType()){
      // 支付宝收款
      fundCarrierAccount = alipayAgencyGath.getAccountNo();
      fundCarrierAccountType = "alipay";
      freezeExecChannelType = "alipay";
    }else if(blankAgencyGath != null && 0 == blankAgencyGath.getAuthType()){
      // 银行收款
      fundCarrierAccount = bankAgencyGath.getAccountNo();
      fundCarrierAccountType = "outBank";
      freezeExecChannelType = "alipay";

    }

    // 根据账户编码获取对应的账号信息
    cardCenterInterfInfo.setAccountNo(productPolicy.getAccountNo());
    ApiInBizResponse<QueryCarrierAccountInfoResponseBizContent> apiInBizResponse = inInterfaceService.queryCarrierAccountInfo(cardCenterInterfInfo ,false);
    if(null == apiInBizResponse || !apiInBizResponse.getCode().equals("10000")){
      resultMap.put("code" , "30022");
      resultMap.put("msg" , "账号编码[" + cardCenterInterfInfo.getAccountNo() + "]对应的商户账号信息失败, " + apiInBizResponse.getMessage());
      return resultMap;
    }
    QueryCarrierAccountInfoResponseBizContent carrierAccount = apiInBizResponse.getBizContent();

    //查询政策拆分明细信息
    String policyNo = productPolicy.getPolicyNo();
    ApiInBizResponse<QueryProductPolicyDetailResponseBizContent> policyDetailResp = inInterfaceService.queryProductPolicyDetail(policyNo, zeroSpecPsnId, cardCenterInterfInfo, false);
    if(policyDetailResp == null){
      resultMap.put("code" , "30022");
      resultMap.put("msg" , "查询政策拆分明细信息为空");
      return resultMap;
    }

    // 垫资方编号
    String fundInvertNo = policyDetailResp.getBizContent().getFundInvertNo();

    // 调拨类型
    String allocationType = policyDetailResp.getBizContent().getAllocationType();

    // 违约处理类型
    String violateHandleType = policyDetailResp.getBizContent().getViolateHandleType();

    // 违约应收款
    BigDecimal violateReceivables = new BigDecimal(0);
    if (!StringUtils.isEmpty(policyDetailResp.getBizContent().getViolateReceivables())) {
      violateReceivables = new BigDecimal(policyDetailResp.getBizContent().getViolateReceivables());
    }
    // 垫资总金额
    BigDecimal fundTotalMoney = new BigDecimal(0);
    if (!StringUtils.isEmpty(policyDetailResp.getBizContent().getFundTotalMoney())) {
      fundTotalMoney = new BigDecimal(policyDetailResp.getBizContent().getFundTotalMoney());
    }

    DebitCreateAo debitCreateAo = new DebitCreateAo();
    debitCreateAo.setOutOrderNo(orderInfo.getAuthOrderNo());
    debitCreateAo.setOutTradeNo(orderInfo.getAuthRequestNo());
    debitCreateAo.setAgencyNum(cdisAgency.getAgencyNo());
    debitCreateAo.setOrderTitle(orderInfo.getName());
    debitCreateAo.setOrderPriceAmount(orderInfo.getTotalFee());
    //订单首付金额
    BigDecimal orderPayAmount = new BigDecimal(0);
    debitCreateAo.setOrderPayAmount(orderPayAmount);
    debitCreateAo.setOrderTradeAmount(orderInfo.getTotalFee());
    debitCreateAo.setOrderTradeFqNum(Integer.valueOf(orderInfo.getFreezeMonth()));//订单分期期数
    debitCreateAo.setCustName(orderInfo.getUserName());
    debitCreateAo.setCarrierNo(productPolicy.getCarrierNo());
    debitCreateAo.setFundInvertNo(fundInvertNo);//垫资方编号
    debitCreateAo.setFundTotalMoney(fundTotalMoney);//垫资总金额
    debitCreateAo.setCustPhone(orderInfo.getPhone());
    //调拨状态变更接口
    String deployNotifyUrl = sysConfigService.getByCode("suninggiftUrl") + "/api/debit/callBack";
    debitCreateAo.setNotifyUrl(deployNotifyUrl);//异步通知地址
    debitCreateAo.setCreateUserName(productPolicy.getCarrierOrgNo());//创建者
    debitCreateAo.setFreezeDetails(JSONObject.toJSONString(policyDetailResp.getBizContent().getFreezeDetails()));//冻结明细
    debitCreateAo.setSettleDetails(JSONObject.toJSONString(policyDetailResp.getBizContent().getSettleDetails()));//分润明细
    debitCreateAo.setIsAdvanceFund(0);//此处统一填0，0需要垫资,1不需要垫资
    debitCreateAo.setAllocationType(allocationType);
    debitCreateAo.setFundCarrierNo(wSAgencyGath.getAccountId());
    debitCreateAo.setFundCarrierName(alipayAgencyGath.getAccountName());
    debitCreateAo.setFundCarrierAccount(fundCarrierAccount);
    debitCreateAo.setFundCarrierAccountType(fundCarrierAccountType);
    debitCreateAo.setViolateHandleType(violateHandleType);
    if(violateReceivables.compareTo(new BigDecimal(0)) > 0){
      debitCreateAo.setViolateReceivables(violateReceivables);
    }
    debitCreateAo.setPolicyNo(productPolicy.getPolicyNo());
    debitCreateAo.setPhoneBelong(orderInfo.getProvinceCode());
    debitCreateAo.setGoodsId(productPolicy.getProductId()); //内部产品编码[product表的product_id]外键关联
    debitCreateAo.setOrderTradeStatus("WAIT_BUYER_PAY");//交易状态
    debitCreateAo.setOrderStatus(0);//订单状态//默认初始0
    debitCreateAo.setCarrierOrg(productPolicy.getCarrierOrgNo());//机构编号，订单透传，没有填0//业务接入商户[非技术接入商户]的关联组织的ID-具体问齐文
    debitCreateAo.setFreezeExecAuthCode("0");
    debitCreateAo.setFundOrderTotalMoney(orderInfo.getTotalFee());//订单总金额
    debitCreateAo.setFreezeExecAccount(carrierAccount.getPayeeLogonId());
    debitCreateAo.setFreezeExecPid(carrierAccount.getPayeeUserId());
    debitCreateAo.setOrderTradeType(1001);//1001-线上支付宝实时支付
    debitCreateAo.setFreezeExecChannelType(freezeExecChannelType);
    debitCreateAo.setStoreNum(cdisStore.getStoreNo());
    debitCreateAo.setCarrierDept("0");//商户部门
    debitCreateAo.setContractFund(orderInfo.getTotalFee());//签约金额
    debitCreateAo.setNeedPayPerMonth(new BigDecimal(0));
    debitCreateAo.setCityName(orderInfo.getCityCode());
    debitCreateAo.setProvinceName(orderInfo.getProvinceCode());

    ApiInBizResponseMsg apiInBizResponseMsg = inInterfaceService.debitCreate(debitCreateAo);
    log.info("苏宁新用户入网调拨创建响应结果," + JSONObject.toJSONString(apiInBizResponseMsg));
    if ("10001".equals(apiInBizResponseMsg.getSubCode())||"50001".equals(apiInBizResponseMsg.getSubCode())){
      orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_CREATE_SUCCESS.getCode());
      orderInfo.setUpdateTime(new Date());

    }else {
      orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_CREATE_FAILED.getCode());
      orderInfo.setUpdateTime(new Date());
    }

    //更新订单状态
    orderInfoService.updateById(orderInfo);

    resultMap.put("code", apiInBizResponseMsg.getSubCode());
    resultMap.put("msg", apiInBizResponseMsg.getSubMsg());
    return resultMap;
  }

  /**
   * 权益派发-联通存量业务订单
   * @param dataObject
   * @param orderInfo
   * @return
   */
  public MessagerVo sendEquity(JSONObject dataObject, OrderInfo orderInfo) {
    MessagerVo messagerVo = new MessagerVo();

    //根据运营商编码获取政策信息
    String lpunicom = OperatorEnum.lpunicom.getCode();
    CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpunicom);
    //调用pot获取联通存量业务政策列表信息
    String outProtNo = cardCenterInterfInfo.getOutPortNo();
    ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo , outProtNo ,false);
    if(null == policyListResponse || !"10000".equals(policyListResponse.getCode())){
      messagerVo.setSubCode("30007");
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
      messagerVo.setSubCode("30008");
      messagerVo.setSubMsg("产品政策详情为空");
      return messagerVo;
    }

    //获取本订单对应的0级政策编码
    String zeroSpecPsnId = orderInfo.getSpecPsnId();

    //遍历子元素中所有的二级政策
    String rightsFlag = "";
    //权益编码list
    List<EquitycodeVo> equitycodeList = new ArrayList<EquitycodeVo>();
    //权益名称
    String desc = null;
    //调拨放款商家外部编码
    String outAgencyNo = "";
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
              rightsFlag = cdisSpecLv2.getKeyStr();
            }

            //节点表识，明细类型：1-下单明细，2-垫支明细，3-趸交明细，4-分期明细,5-退款明细，6-权益明细
            if(6 == Integer.valueOf(cdisSpecLv2.getCheckFlag())) {
              EquitycodeVo equitycodeVo = new EquitycodeVo();
              equitycodeVo.setEquityCode(cdisSpecLv2.getKeyStr());
              equitycodeVo.setEquityAmount(cdisSpecLv2.getValueStr());
              equitycodeList.add(equitycodeVo);

              desc = cdisSpecLv2.getRemarkInfo();
              outAgencyNo = cdisSpecLv2.getName();
            }
          }
        }
      }
    }

    String outTradeNo = orderInfo.getOutTradeNo();
    //“PAY_MODEL”为先激活后派劵的情况不做派券处理
    if(!CommonUtil.isEmpty(rightsFlag) && "PAY_MODEL".equals(rightsFlag)) {
      messagerVo.setSubCode("30009");
      messagerVo.setSubMsg("该订单[" + outTradeNo + "]为先激活后派劵订单");
      log.info("该订单outOrderNo=[" + outTradeNo + "]为先激活后派劵订单");
      return messagerVo;
    }

    if(CommonUtil.isEmpty(outAgencyNo)) {
      messagerVo.setSubCode("30010");
      messagerVo.setSubMsg("权益政策中调拨放款商户外部编码为空");
      log.info(desc + " ,权益政策中调拨放款商户外部编码为空");
      return messagerVo;
    }

    //获取调拨创建相关参数
    cardCenterInterfInfo.setOutAgencyNo(outAgencyNo);
    ApiInBizResponse<QueryAgencyOrStoreResponseBizContent> queryAgencyOrStoreResp = inInterfaceService.queryAgencyOrStore(cardCenterInterfInfo , false);
    if (null == queryAgencyOrStoreResp || !queryAgencyOrStoreResp.getCode().equals("10000")) {
      messagerVo.setSubCode("30011");
      messagerVo.setSubMsg("商户外部编码[" + outAgencyNo + "]对应调拨商户、门店信息异常, " + queryAgencyOrStoreResp.getMessage());
      log.info("商户外部编码["+outAgencyNo+"]对应调拨商户、门店信息异常 ：" + queryAgencyOrStoreResp.getMessage());
      return messagerVo;
    }

    //获取商家信息
    QueryAgencyOrStoreResponseBizContentCdisAgency cdisAgency = queryAgencyOrStoreResp.getBizContent().getCdisAgency();

    //获取门店信息
    QueryAgencyOrStoreResponseBizContentCdisStore cdisStore = queryAgencyOrStoreResp.getBizContent().getCdisStore();

    //获取商户冻结业务信息
    QueryAgencyOrStoreResponseBizContentCdisAgencyTasks freezeAgencyTasks = null;
    List<QueryAgencyOrStoreResponseBizContentCdisAgencyTasks> agencyTasksList = queryAgencyOrStoreResp.getBizContent().getCdisAgencyTasks();
    for (QueryAgencyOrStoreResponseBizContentCdisAgencyTasks agencyTasks : agencyTasksList) {
      if(agencyTasks.getTasksType().intValue() == 20102){
        freezeAgencyTasks = agencyTasks;
      }
    }

    String ataskNo = freezeAgencyTasks.getAtaskNo();
    //遍历判断收款账号信息
    // 查询商家收款账号(网商)
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths wSAgencyGath = null;
    // 查询商家空白收款账号信息-判断银行卡或者支付宝收款
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths blankAgencyGath = null;
    // 查询商家-支付宝账号
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths alipayAgencyGath = null;
    //查询商家-银行卡账号
    QueryAgencyOrStoreResponseBizContentCdisAgencyGaths bankAgencyGath = null;
    List<QueryAgencyOrStoreResponseBizContentCdisAgencyGaths> cdisAgencyGaths = queryAgencyOrStoreResp.getBizContent().getCdisAgencyGaths();
    for (QueryAgencyOrStoreResponseBizContentCdisAgencyGaths agencyGath : cdisAgencyGaths) {
      //gathType收款模式[0-销售营收,1-违约追收,2-主业追收,3-商户分润,4-垫资垫付,5-空白模式,6-话费趸交]
      //authType授信类:1-支付宝,2-微信,3-QQ,4-新浪,5-农业银行,6-网上银行
      if(agencyGath.getGathType() == 0 && ataskNo.equals(agencyGath.getAtaskNo())){
        //销售营收账号
        blankAgencyGath = agencyGath;
      }else if(agencyGath.getGathType() == 4 && agencyGath.getAuthType() == 6 && ataskNo.equals(agencyGath.getAtaskNo())){
        //垫资垫付账号
        wSAgencyGath = agencyGath;
      }else if(agencyGath.getGathType() == 5 && agencyGath.getAuthType() == 1 && ataskNo.equals(agencyGath.getAtaskNo())){
        //支付宝账号
        alipayAgencyGath = agencyGath;
      }else if(agencyGath.getGathType() == 5 && agencyGath.getAuthType() == 0 && ataskNo.equals(agencyGath.getAtaskNo())){
        //银行账号
        bankAgencyGath = agencyGath;
      }
    }

    // 推送调拨系统的收款账户
    String fundCarrierAccount = "";
    // 供应商账户类型 （ 收款账号-收款模式类型(0)-对应的授信类型）
    String fundCarrierAccountType = "";
    // 冻结的渠道编号
    String freezeExecChannelType = "";
    // 区分支付宝收款和银行卡收款
    if(blankAgencyGath != null && 1 == blankAgencyGath.getAuthType()){
      // 支付宝收款
      fundCarrierAccount = alipayAgencyGath.getAccountNo();
      fundCarrierAccountType = "alipay";
      freezeExecChannelType = "alipay";
    }else if(blankAgencyGath != null && 0 == blankAgencyGath.getAuthType()){
      // 银行收款
      fundCarrierAccount = bankAgencyGath.getAccountNo();
      fundCarrierAccountType = "outBank";
      freezeExecChannelType = "alipay";

    }

    // 根据账户编码获取对应的账号信息
    cardCenterInterfInfo.setAccountNo(productPolicy.getAccountNo());
    ApiInBizResponse<QueryCarrierAccountInfoResponseBizContent> apiInBizResponse = inInterfaceService.queryCarrierAccountInfo(cardCenterInterfInfo ,false);
    if(null == apiInBizResponse || !apiInBizResponse.getCode().equals("10000")){
      messagerVo.setSubCode("30012");
      messagerVo.setSubMsg("账号编码[" + cardCenterInterfInfo.getAccountNo() + "]对应的商户账号信息失败, " + apiInBizResponse.getMessage());
      return messagerVo;
    }
    QueryCarrierAccountInfoResponseBizContent carrierAccount = apiInBizResponse.getBizContent();

    //查询政策拆分明细信息
    String policyNo = productPolicy.getPolicyNo();
    ApiInBizResponse<QueryProductPolicyDetailResponseBizContent> policyDetailResp = inInterfaceService.queryProductPolicyDetail(policyNo, zeroSpecPsnId, cardCenterInterfInfo, false);
    if(policyDetailResp == null){
      messagerVo.setSubCode("30013");
      messagerVo.setSubMsg("查询政策拆分明细信息为空");
      return messagerVo;
    }

    // 垫资方编号
    String fundInvertNo = policyDetailResp.getBizContent().getFundInvertNo();

    // 调拨类型
    String allocationType = policyDetailResp.getBizContent().getAllocationType();

    // 违约处理类型
    String violateHandleType = policyDetailResp.getBizContent().getViolateHandleType();

    // 违约应收款
    BigDecimal violateReceivables = new BigDecimal(0);
    if (!StringUtils.isEmpty(policyDetailResp.getBizContent().getViolateReceivables())) {
      violateReceivables = new BigDecimal(policyDetailResp.getBizContent().getViolateReceivables());
    }
    // 垫资总金额
    BigDecimal fundTotalMoney = new BigDecimal(0);
    if (!StringUtils.isEmpty(policyDetailResp.getBizContent().getFundTotalMoney())) {
      fundTotalMoney = new BigDecimal(policyDetailResp.getBizContent().getFundTotalMoney());
    }

    DebitCreateAo debitCreateAo = new DebitCreateAo();
    debitCreateAo.setOutOrderNo(orderInfo.getAuthOrderNo());
    debitCreateAo.setOutTradeNo(orderInfo.getAuthRequestNo());
    JSONObject bodyObject = JSONObject.parseObject(orderInfo.getField1());
    debitCreateAo.setAgencyNum(bodyObject.getString("agencyNo"));
    debitCreateAo.setOrderTitle(orderInfo.getName());
    debitCreateAo.setOrderPriceAmount(orderInfo.getTotalFee());
    //订单首付金额
    BigDecimal orderPayAmount = new BigDecimal(0);
    debitCreateAo.setOrderPayAmount(orderPayAmount);
    debitCreateAo.setOrderTradeAmount(orderInfo.getTotalFee());
    debitCreateAo.setOrderTradeFqNum(Integer.valueOf(orderInfo.getFreezeMonth()));//订单分期期数
    debitCreateAo.setCustName(orderInfo.getUserName());
    debitCreateAo.setCarrierNo(productPolicy.getCarrierNo());
    debitCreateAo.setFundInvertNo(fundInvertNo);//垫资方编号
    debitCreateAo.setFundTotalMoney(fundTotalMoney);//垫资总金额
    debitCreateAo.setCustPhone(orderInfo.getPhone());
    //调拨状态变更接口
    String deployNotifyUrl = sysConfigService.getByCode("suninggiftUrl") + "/api/debit/callBack";
    debitCreateAo.setNotifyUrl(deployNotifyUrl);//异步通知地址
    debitCreateAo.setCreateUserName(productPolicy.getCarrierOrgNo());//创建者
    debitCreateAo.setFreezeDetails(JSONObject.toJSONString(policyDetailResp.getBizContent().getFreezeDetails()));//冻结明细
    debitCreateAo.setSettleDetails(JSONObject.toJSONString(policyDetailResp.getBizContent().getSettleDetails()));//分润明细
    debitCreateAo.setIsAdvanceFund(0);//此处统一填0，0需要垫资,1不需要垫资
    debitCreateAo.setAllocationType(allocationType);
    debitCreateAo.setFundCarrierNo(wSAgencyGath.getAccountId());
    debitCreateAo.setFundCarrierName(alipayAgencyGath.getAccountName());
    debitCreateAo.setFundCarrierAccount(fundCarrierAccount);
    debitCreateAo.setFundCarrierAccountType(fundCarrierAccountType);
    debitCreateAo.setViolateHandleType(violateHandleType);
    if(violateReceivables.compareTo(new BigDecimal(0)) > 0){
      debitCreateAo.setViolateReceivables(violateReceivables);
    }
    debitCreateAo.setPolicyNo(productPolicy.getPolicyNo());
    debitCreateAo.setPhoneBelong(orderInfo.getProvinceCode());
    debitCreateAo.setGoodsId(productPolicy.getProductId()); //内部产品编码[product表的product_id]外键关联
    debitCreateAo.setOrderTradeStatus("WAIT_BUYER_PAY");//交易状态
    debitCreateAo.setOrderStatus(0);//订单状态//默认初始0
    debitCreateAo.setCarrierOrg(productPolicy.getCarrierOrgNo());//机构编号，订单透传，没有填0//业务接入商户[非技术接入商户]的关联组织的ID-具体问齐文
    debitCreateAo.setFreezeExecAuthCode("0");
    debitCreateAo.setFundOrderTotalMoney(orderInfo.getTotalFee());//订单总金额
    debitCreateAo.setFreezeExecAccount(carrierAccount.getPayeeLogonId());
    debitCreateAo.setFreezeExecPid(carrierAccount.getPayeeUserId());
    debitCreateAo.setOrderTradeType(1001);//1001-线上支付宝实时支付
    debitCreateAo.setFreezeExecChannelType(freezeExecChannelType);
    debitCreateAo.setStoreNum(cdisStore.getStoreNo());
    debitCreateAo.setCarrierDept("0");//商户部门
    debitCreateAo.setContractFund(orderInfo.getTotalFee());//签约金额
    debitCreateAo.setNeedPayPerMonth(new BigDecimal(0));
    debitCreateAo.setCityName(orderInfo.getCityCode());
    debitCreateAo.setProvinceName(orderInfo.getProvinceCode());

    //获取调拨订阅相关参数
    DebitSubscribeAo debitSubscribeAo = inInterfaceServiceTool.debitSubscribeDispose(orderInfo);

    //设置淘淘票券权益请求参数
    SendEquityVo sendEquityVo = new SendEquityVo();

    sendEquityVo.setOutOrderNo(orderInfo.getAuthOrderNo());
    sendEquityVo.setOutTradeNo(orderInfo.getAuthRequestNo());
    sendEquityVo.setOutOrderStatus("TRADE_SUCCESS");

    //派券机构 unicom_pay 联通支付   suning 苏宁易购  walmart 沃尔 、 TaoTicket 淘票票
    sendEquityVo.setSendOrg("TaoTicket");
    //派券方式 phone 手机号   alipay 支付宝
    sendEquityVo.setSendWay("alipay");
    //付款支付宝userid
    sendEquityVo.setBuyerUserId(orderInfo.getUserId());
    //外部订单金额
    sendEquityVo.setOrderAmount(String.valueOf(orderInfo.getTotalFee()));
    //组合类型 单个--SINGLE，组合--COMBINATION
    sendEquityVo.setCombineType("COMBINATION");
    sendEquityVo.setOutOrderTitle(orderInfo.getName());
    sendEquityVo.setSendOrgValue(dataObject.getString("sendOrgValue"));
    if(!CommonUtil.isEmpty(dataObject.getString("merOrderNo"))){
      sendEquityVo.setMemberNo(dataObject.getString("merOrderNo"));
    }
    //设置异步通知地址
    String notifyUrl = sysConfigService.getByCode("suninggiftUrl") + "/api/equity/rights/";
    sendEquityVo.setNotifyUrl(notifyUrl);
    //门店商家编码
    sendEquityVo.setMerchantId(cdisAgency.getAgencyNo());
    //商家名称
    sendEquityVo.setMerchantName(cdisAgency.getAgencyName());
    //门店名称
    sendEquityVo.setStoreName(cdisStore.getStoreName());
    //门店编码
    sendEquityVo.setStoreCode(cdisStore.getStoreNo());
    //设置调拨创建参数
    sendEquityVo.setDeployCreateString(JSONObject.toJSONString(debitCreateAo));
    //设置调拨订阅参数
    sendEquityVo.setDeploySynchronieString(JSONObject.toJSONString(debitSubscribeAo));
    //添加到权益集合
    sendEquityVo.setEquityEntityList(equitycodeList);

    //进行权益派发能力调用
    DeployInterfInfo deployInfo = JSONObject.parseObject(sysConfigService.getByCode("deployInfo"), DeployInterfInfo.class);
    MessagerVo messagerEquityVo = inInterfaceService.sendEquity(sendEquityVo ,deployInfo);
    if("10000".equals(messagerEquityVo.getSubCode())){
      ApiInBizResponseMsg apiInBizResponseMsg = (ApiInBizResponseMsg) messagerEquityVo.getData();
      if("10009".equals(apiInBizResponseMsg.getCode())){
        messagerVo.setSubCode("10000");
        messagerVo.setSubMsg(desc + "权益派发成功");
      }else{
        messagerVo.setSubCode("30014");
        messagerVo.setSubMsg(desc + "权益派发失败");
      }
    }

    return messagerVo;
  }

  /**
   * 调拨订阅相关参数
   * @param orderInfo
   * @return
   */
  public Map<String,Object> deployOrderStatus(OrderInfo orderInfo) {
    Map<String,Object> resultMap = new HashMap<String,Object>();

    //获取调拨订阅相关参数
    DebitSubscribeAo debitSubscribeAo = inInterfaceServiceTool.debitSubscribeDispose(orderInfo);

    //进行调拨订阅操作
    ApiInBizResponseMsg apiInBizResponseMsg = inInterfaceService.debitSubscribe(debitSubscribeAo);
    if ("10001".equals(apiInBizResponseMsg.getSubCode())) {
      orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_SUBSCRIBE_SUCCESS.getCode());
      orderInfo.setUpdateTime(new Date());
    }else {
      orderInfo.setProcessStates(ProcessStatesEnum.DEBIT_SUBSCRIBE_FAILED.getCode());
      orderInfo.setUpdateTime(new Date());
    }

    //更新订单状态
    orderInfoService.updateById(orderInfo);

    //10001-请求成功， 50001-更新不成功，调拨订单状态不对
    resultMap.put("code", apiInBizResponseMsg.getSubCode());
    resultMap.put("msg", apiInBizResponseMsg.getSubMsg());
    return  resultMap;
  }

}
