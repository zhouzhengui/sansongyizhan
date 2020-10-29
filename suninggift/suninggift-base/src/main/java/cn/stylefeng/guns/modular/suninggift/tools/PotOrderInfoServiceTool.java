package cn.stylefeng.guns.modular.suninggift.tools;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.stylefeng.guns.modular.suninggift.utils.BeanSetdefaultUtil;
import cn.stylefeng.guns.modular.suninggift.utils.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.gzlplink.cloud.mps.client.model.order.entity.CdisContFreeze;
import com.gzlplink.cloud.mps.client.model.order.entity.CdisContOrder;
import com.gzlplink.cloud.mps.client.model.order.entity.CdisFreezeLog;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * 操作pot数据库工具类
 */
@Slf4j
@Component
public class PotOrderInfoServiceTool {


  /**
   * 设置cdisContOrder订单信息
   * @param jsonBodyObject
   * @return
   */
  public CdisContOrder setCdisContOrderInfo(JSONObject jsonBodyObject) {
    // 创建订单对象
    CdisContOrder cdisContOrder = new CdisContOrder();
    // 复制属性相同的信息
    BeanUtil.copyProperties(jsonBodyObject, cdisContOrder);

    // 交易流水号
    String outTradeNo = DateUtil.commontime + RandomUtil.randomNumbers(8);
    cdisContOrder.setOutTradeNo(outTradeNo);
    // 区分存量用户还是新入网用户订单 0:尚未定义，1:存量用户，2：新增开户
    String stockType = "2";
    cdisContOrder.setStockType(stockType);
    // 订单总价[商品冻结金额]
    String orderPriceAmount = jsonBodyObject.getString("freezeAmount");
    cdisContOrder.setOrderPriceAmount(Double.valueOf(orderPriceAmount));
    // 订单首付金额[例如:非冻结部分]
    cdisContOrder.setOrderPayAmount(Double.valueOf(0));
    // 订单交易金额[例如:送花呗金额]
    String orderTradeAmount = jsonBodyObject.getString("freezeAmount");
    cdisContOrder.setOrderTradeAmount(Double.valueOf(orderTradeAmount));
    // 分期数
    // cdisContOrder.setOrderTradeFqNum(orderTradeFqNum);
    // 费率
    cdisContOrder.setOrderTradeFqSellerPercent(0);
    // 支付宝支付:预授权线上冻结
    Integer orderTradeType = Integer.parseInt("1000");
    cdisContOrder.setOrderTradeType(orderTradeType);
    // 订单交易状态
    cdisContOrder.setOrderTradeStatus("WAIT_BUYER_PAY");
    // 线上权益冻结orderType：12024
    String orderType = "12024";
    cdisContOrder.setOrderType(orderType);
    // 订单状态[-3-订单异常,-2-全额退款,-1-部分退款,0-初始状态,1-交易关闭,2-部分成功,3-交易成功]
    Integer orderStatus = 0;
    cdisContOrder.setOrderStatus(orderStatus);
    // 线上产品
    String productCode = "PRE_AUTH_ONLINE";
    cdisContOrder.setProductCode(productCode);
    // 产品名称
    String productName = jsonBodyObject.getString("orderTitle");
    cdisContOrder.setProductName(productName);
    // 签约号码
    String contractPhone = jsonBodyObject.getString("contractPhone");
    cdisContOrder.setCustPhone(contractPhone);
    // 首次冻结金额
    String contractFund = jsonBodyObject.getString("freezeAmount");
    cdisContOrder.setContractFund(Double.valueOf(contractFund));
    // 签约状态:-1-解约,0-暂停,1-正常
    Integer contractStatus = -1;
    cdisContOrder.setContractStatus(contractStatus);
    // 触发类型
    Integer triggerType = -1;
    cdisContOrder.setTriggerType(triggerType);
    // 触发金额
    cdisContOrder.setTriggerFund(Double.valueOf("0.00"));
    // 实际总冻结金额
    cdisContOrder.setTotalFreezeAmount(Double.valueOf("0.00"));
    // 冻结金额
    cdisContOrder.setOrderFreezeAmount(Double.valueOf("0.00"));
    // 累计解冻金额
    cdisContOrder.setTotalUnfreezeAmount(Double.valueOf("0.00"));
    // 累计支付金额
    cdisContOrder.setTotalPayAmount(Double.valueOf("0.00"));
    // 剩余冻结金额
    cdisContOrder.setRestAmount(Double.valueOf("0.00"));
    // 本次交易金额
    String fundAmount = jsonBodyObject.getString("freezeAmount");
    cdisContOrder.setFundAmount(Double.valueOf(fundAmount));
    // 操作类型
    cdisContOrder.setOperateType("订单入库，等待用户确认支付");
    // 支付宝订单交易流水
    cdisContOrder.setTradeNo("0");
    // 支付宝授信冻结流水
    cdisContOrder.setAuthNo("0");
    //操作状态
    cdisContOrder.setOperationStatus("INIT");
    //店员编码
    cdisContOrder.setClerkNo("0");
    // 托收类型:1-正向代扣,2-反向代扣
    Integer freezeType = 1;
    cdisContOrder.setFreezeType(freezeType);
    // 执行频率
    Integer freezeFreq = -1;
    cdisContOrder.setFreezeFreq(freezeFreq);
    //操作类型
    cdisContOrder.setOperateType("FREEZE");
    //产品明细
    JSONObject goodsDetailObject = new JSONObject();
    //1级下单明细政策编码
    goodsDetailObject.put("goodsId", cdisContOrder.getGoodsId());
    //2级权益明细政策编码
    goodsDetailObject.put("goodsSa", cdisContOrder.getGoodsSa());
    //2级垫支明细政策编码
    goodsDetailObject.put("goodsSb", cdisContOrder.getGoodsSb());
    //2级下单明细政策编码
    goodsDetailObject.put("goodsSc", cdisContOrder.getGoodsSc());
    cdisContOrder.setGoodsDetail(goodsDetailObject.toJSONString());
    //把超长字段值设置为0
    cdisContOrder.setGoodsId("0");
    cdisContOrder.setGoodsSa("0");
    cdisContOrder.setGoodsSb("0");
    cdisContOrder.setGoodsSc("0");
    //证件类型：1-身份证
    cdisContOrder.setCertType(1);
    //处理phoneBelong
    String phoneBelongs = jsonBodyObject.getString("phoneBelong");
    String[] belongs = phoneBelongs.split(",");
    cdisContOrder.setPhoneBelong(belongs[0]);


    // 添加首月计费标准到orderBody中
    JSONObject js = new JSONObject();
    js.put("firstMonthId", jsonBodyObject.getString("firstMonthId"));
    js.put("firstMonthName", jsonBodyObject.getString("firstMonthName"));
    js.put("prokey", jsonBodyObject.getString("prokey"));
    js.put("provinceWopayCode", jsonBodyObject.getString("provinceWopayCode"));
    js.put("cityWopayCode", jsonBodyObject.getString("cityWopayCode"));
    js.put("phoneBelong", phoneBelongs);
    js.put("agencyName", jsonBodyObject.getString("agencyName"));
    js.put("storeName", jsonBodyObject.getString("storeName"));
    cdisContOrder.setOrderBody(js.toJSONString());

    // 垫资垫付外部商家编码
    String outAgencyNo = jsonBodyObject.getString("outAgencyNo");
    cdisContOrder.setAgencyId(outAgencyNo);
    //冻结顾客的userId
    String userId = jsonBodyObject.getString("userId");
    cdisContOrder.setPayerUserId(userId);
    // 设置入库时间
    cdisContOrder.setInstDate(new Date());
    // 签约月数
    cdisContOrder.setContractMonth(jsonBodyObject.getString("freezeMonth"));
    //期数
    cdisContOrder.setOrderTradeFqNum(Integer.valueOf(jsonBodyObject.getString("freezeMonth")));
    //乐芃垫资1002
    cdisContOrder.setChannelCode("1002");
    // 操作者
    cdisContOrder.setApprove("system");
    // 签约时间
    cdisContOrder.setContractDate(new Date());
    // 调拨状态
    Integer deployStatus = 0;
    cdisContOrder.setDeployStatus(deployStatus);
    // 发起isv账号
//		cdisContOrder.setIsvPid();
    cdisContOrder.setOrderTradeFqRate(Double.valueOf(0.00));

    // 额外字段
    cdisContOrder.setExtString("[]");
    cdisContOrder.setOutString("[]");

    //设置默认值
    BeanSetdefaultUtil.setDefaultValue(cdisContOrder);

    return cdisContOrder;
  }

  /**
   * 设置cdisContOrder订单信息
   * @param cdisContOrder
   * @return
   */
  public CdisContFreeze setCdisContFreezeInfo(CdisContOrder cdisContOrder) {
    CdisContFreeze cdisContFreeze = new CdisContFreeze();
    // 复制属性相同的信息
    BeanUtil.copyProperties(cdisContOrder, cdisContFreeze);

    // 有效时长,单位分钟[m]默认30分
    cdisContFreeze.setValidityTime("30");
    // 支付方式[例如:1201-线上花呗冻结\1202-线上余额宝冻结\2201-线下二维码花呗冻结\2202-线下二维码余额宝冻结\2211-线下APP扫码花呗冻结\2212-线下APP扫码余额宝冻结\2221-线下POST扫码花呗冻结\2222-线下POST扫码余额宝冻结]
    cdisContFreeze.setOrderFreezeType(1202);
    // 资金操作流水的状态。目前支持：| INIT：初始 | PROCESSING：处理中 | SUCCESS：成功 | FAIL：失败 | CLOSED：关闭
    cdisContFreeze.setOperationStatus("INIT");
    // 资金渠道编码1001-华盛垫资1002-乐芃垫资1003-暂无垫资1004-利卡垫资
    cdisContFreeze.setChannelCode("1003");
    // 资金渠道名称[冗余字段]
    cdisContFreeze.setChannelName("广州乐芃");
    // 冻结资金类型：18 -- MONEY_FUND --余额宝，17--PCREDIT_PAY -- 花呗 ，0--MONEY-FUND &
    // PCREDIT_PAY -- 花呗和余额宝并存
    cdisContFreeze.setFreezePayChannels(18);
    // 订单创建时间
    cdisContFreeze.setInstDate(new Date());
    cdisContFreeze.setCreateTime(new Date());
    cdisContFreeze.setContractTime(new Date());
    // 操作状态
    cdisContFreeze.setOperationStatus("INIT");
    //调拨状态
    //cdisContFreeze.setDeployStatus("0");
    //订单费率
    //cdisContFreeze.setOrderFreezeFqRate("0");
    //账号编码
    //cdisContFreeze.setAccountNo(cdisContOrder.get);
    //账号appId
    //cdisContFreeze.setAccountAppid(accountAppid);
    //账号pid
    cdisContFreeze.setAccountPid(cdisContOrder.getPayeeUserId());
    //设置冻结订单金额
    cdisContFreeze.setOrderAmount(cdisContOrder.getFundAmount());

    //设置默认值
    BeanSetdefaultUtil.setDefaultValue(cdisContFreeze);

    return cdisContFreeze;
  }

  /**
   * 设置CdisFreezeLog订单流水日志信息
   * @param cdisContOrder
   * @return
   */
  public CdisFreezeLog setCdisFreezeLogInfo(CdisContOrder cdisContOrder) {
    //新建冻结日志
    CdisFreezeLog freezeLog = new CdisFreezeLog();
    //赋值订单信息属性
    BeanUtil.copyProperties(cdisContOrder, freezeLog);
    //冻结流水号
    String outRequestNo = cdisContOrder.getOutRequestNo();
    freezeLog.setOutRequestNo(outRequestNo);
    //外部订单号
    freezeLog.setMerchantOrderNo(cdisContOrder.getMerchantOrderNo());
    //归属机构
    String carrierOrg = cdisContOrder.getCarrierOrg();
    freezeLog.setCustOrg(carrierOrg);
    //资金操作类型[含自定义]FREEZE：冻结 	UNFREEZE：解冻 	FUNDPAY：支付	REFUND：退款 CANCEL：撤销
    String operationType = "FREEZE" ;
    freezeLog.setOperationType(operationType);
    //资金操作流水的状态。目前支持：l  INIT：初始 l  PROCESSING：处理中 l  SUCCESS：成功 l  FAIL：失败 l  CLOSED：关闭
    String operationStatus = "SUCCESS";
    freezeLog.setOperationStatus(operationStatus);

    //正式使用
    String localUrl = "-1";
    freezeLog.setLocalUrl(localUrl);
    //备注
    String remark = "资金冻结";
    freezeLog.setRemark(remark);
    //超时时间
    freezeLog.setTimeoutExpress("30m");
    //回调地址
    String returnUrl = "-1";
    freezeLog.setReturnUrl(returnUrl);
    //设置操作时间
    freezeLog.setOperationTime(new Date());

    return freezeLog;
  }
}
