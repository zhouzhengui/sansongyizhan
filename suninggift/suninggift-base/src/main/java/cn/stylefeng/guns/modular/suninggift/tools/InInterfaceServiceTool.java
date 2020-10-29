package cn.stylefeng.guns.modular.suninggift.tools;

import cn.stylefeng.guns.modular.suninggift.entity.CustomerInfo;
import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.entity.WopayFtp;
import cn.stylefeng.guns.modular.suninggift.enums.BusinessTypeEnum;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.DebitCreateAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.DebitSubscribeAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.DebitSubscribeAoFreezeDetail;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.*;
import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.modular.suninggift.service.InInterfaceService;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSONObject;
import com.gzlplink.cloud.mps.client.model.access.entity.CdisArea;
import com.gzlplink.cloud.mps.client.util.MpsClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-04-24 15:49
 */
@Slf4j
@Component
public class InInterfaceServiceTool {

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private InInterfaceService inInterfaceService;

    /**
     * 拼接推送WSN系统
     *
     * @param orderInfo
     * @return
     */
    public static WopayFtp orderSyncWsnForftpAo(OrderInfo orderInfo, CustomerInfo customerInfo) {
        WopayFtp wopayFtp = new WopayFtp();

        wopayFtp.setTradeNo(orderInfo.getOutTradeNo());
        wopayFtp.setTradeFlowNo(orderInfo.getOrderLineId());
        wopayFtp.setPhoneNo(orderInfo.getPhone());
        wopayFtp.setProductId(orderInfo.getProductId());
        wopayFtp.setCreateTime(orderInfo.getCreateTime());
        wopayFtp.setUpdateTime(orderInfo.getUpdateTime());
        wopayFtp.setCreditDate(orderInfo.getCreateTime());
        wopayFtp.setFinishDate(orderInfo.getUpdateTime());
        wopayFtp.setRefundDate(orderInfo.getUpdateTime());
        wopayFtp.setProductDescription(orderInfo.getName());
        wopayFtp.setTradeStatus("1");
        wopayFtp.setUserName(orderInfo.getUserName());
        wopayFtp.setIdCard(customerInfo.getCertNo());
        wopayFtp.setProductName(orderInfo.getName());
        wopayFtp.setFqNum(orderInfo.getFreezeMonth());
        wopayFtp.setOrderAmount(orderInfo.getTotalFee());
        wopayFtp.setFqSellerPercent(new BigDecimal(0));
        wopayFtp.setFqRate(new BigDecimal(12.50));
        wopayFtp.setPayUserId(orderInfo.getUserId());
        CdisArea provinceArea = MpsClientUtil.selectByPrimaryKey(CdisArea.class ,orderInfo.getProvinceCode());
        wopayFtp.setProvince(provinceArea.getWopayCode());
        CdisArea cityArea = MpsClientUtil.selectByPrimaryKey(CdisArea.class ,orderInfo.getCityCode());
        wopayFtp.setCity(cityArea.getWopayCode());
        wopayFtp.setBusinessType("1");
        wopayFtp.setOrderVersion("02");

        return wopayFtp;
    }

    /**
     * 传入政策返回调拨下单参数
     *
     * @param
     * @return
     */
    public DebitCreateAo debitCreateDispose(OrderInfo orderInfo, PolicyListResponseBizContent policyListResponseBizContent,
                                            QueryAgencyOrStoreResponseBizContent queryAgencyOrStoreResponseBizContent,
                                            QueryAgencygathResponseBizContent queryAgencygathResponseBizContent,
                                            QueryCarrierAccountInfoResponseBizContent queryCarrierAccountInfoResponseBizContent,
                                            QueryCarrierInfoResponseBizContent queryCarrierInfoResponseBizContent,
                                            CardCenterInterfInfo cardCenterInterfInfo) {
        String deployNotifyUrl = sysConfigService.getByCode("suninggiftUrl") + "/api/debit/callBack";//调拨状态变更接口

        //主政策信息
        PolicyListResponseBizContentProductPolicy cdisPolicyListResponseBizContentProductPolicy = policyListResponseBizContent.getCdisProductPolicy();

        //商家信息
        QueryAgencyOrStoreResponseBizContentCdisAgency cdisAgency = queryAgencyOrStoreResponseBizContent.getCdisAgency();
        //门店信息
        QueryAgencyOrStoreResponseBizContentCdisStore cdisStore = queryAgencyOrStoreResponseBizContent.getCdisStore();
        List<QueryAgencyOrStoreResponseBizContentCdisAgencyTasks> cdisAgencyTasks = queryAgencyOrStoreResponseBizContent.getCdisAgencyTasks();
        String ataskNo = null;
        for (QueryAgencyOrStoreResponseBizContentCdisAgencyTasks queryAgencyOrStoreResponseBizContentCdisAgencyTasks : cdisAgencyTasks) {
            if (queryAgencyOrStoreResponseBizContentCdisAgencyTasks.getTasksType().intValue() == 20102) {
                ataskNo = queryAgencyOrStoreResponseBizContentCdisAgencyTasks.getAtaskNo();
                break;
            }
        }

        if (StringUtils.isBlank(ataskNo)) {
            log.info("ataskNo为空");
            return null;
        }
        String bankAccountNo = null;
        List<QueryAgencyOrStoreResponseBizContentCdisAgencyGaths> cdisAgencyGaths = queryAgencyOrStoreResponseBizContent.getCdisAgencyGaths();
        Integer authType = null;
        for (QueryAgencyOrStoreResponseBizContentCdisAgencyGaths queryAgencyOrStoreResponseBizContentCdisAgencyGaths : cdisAgencyGaths) {
            if (queryAgencyOrStoreResponseBizContentCdisAgencyGaths.getGathType().intValue() == 0 &&
                    queryAgencyOrStoreResponseBizContentCdisAgencyGaths.getAtaskNo().equals(ataskNo)) {
                authType = queryAgencyOrStoreResponseBizContentCdisAgencyGaths.getAuthType();
                bankAccountNo = queryAgencyOrStoreResponseBizContentCdisAgencyGaths.getAccountNo();
                break;
            }
        }

        if (null == authType) {
            log.info("authType");
            return null;
        }

        //订单首付金额
        BigDecimal orderPayAmount = new BigDecimal(0);
        if (null != orderInfo.getOrderPayAmount()) {
            orderPayAmount = orderInfo.getOrderPayAmount();
        }

        //获取政策拆分
        ApiInBizResponse<QueryProductPolicyDetailResponseBizContent> queryProductPolicyDetailResponseBizContentApiInBizResponse = inInterfaceService.queryProductPolicyDetail(cdisPolicyListResponseBizContentProductPolicy.getPolicyNo(), orderInfo.getSpecPsnId(), cardCenterInterfInfo, false);
        QueryProductPolicyDetailResponseBizContent queryProductPolicyDetailResponseBizContent = queryProductPolicyDetailResponseBizContentApiInBizResponse.getBizContent();

        //查询政策明细
        Map<String, Object> param = new HashMap<>();
        param.put("policyNo", cdisPolicyListResponseBizContentProductPolicy.getPolicyNo());
        if (queryProductPolicyDetailResponseBizContent == null) {
            log.info("根据【policyNo】：" + cdisPolicyListResponseBizContentProductPolicy.getPolicyNo() + "，查询产品政策明细【cdisProductPolicyDetail】为空");
            return null;
        }

        //垫资方编号
        String fundInvertNo = queryProductPolicyDetailResponseBizContent.getFundInvertNo();

        //调拨类型
        String allocationType = queryProductPolicyDetailResponseBizContent.getAllocationType();

        //违约处理类型
        String violateHandleType = queryProductPolicyDetailResponseBizContent.getViolateHandleType();

        //违约应收款
        BigDecimal violateReceivables = new BigDecimal(0);
        if (!StringUtils.isEmpty(queryProductPolicyDetailResponseBizContent.getViolateReceivables())) {
            violateReceivables = new BigDecimal(queryProductPolicyDetailResponseBizContent.getViolateReceivables());
        }

        //垫资总金额
        BigDecimal fundTotalMoney = new BigDecimal(0);
        if (!StringUtils.isEmpty(queryProductPolicyDetailResponseBizContent.getFundTotalMoney())) {
            fundTotalMoney = new BigDecimal(queryProductPolicyDetailResponseBizContent.getFundTotalMoney());
        }

        DebitCreateAo debitCreateAo = new DebitCreateAo();
        debitCreateAo.setOutOrderNo(orderInfo.getAuthOrderNo());
        debitCreateAo.setOutTradeNo(orderInfo.getAuthRequestNo());
        debitCreateAo.setAgencyNum(cdisAgency.getAgencyNo());
        debitCreateAo.setOrderTitle(orderInfo.getName());
        debitCreateAo.setOrderPriceAmount(orderInfo.getTotalFee());
        debitCreateAo.setOrderPayAmount(orderPayAmount);
        debitCreateAo.setOrderTradeAmount(orderInfo.getTotalFee());
        debitCreateAo.setOrderTradeFqNum(Integer.valueOf(orderInfo.getFreezeMonth()));//订单分期期数
        debitCreateAo.setCustName("0");
        debitCreateAo.setCarrierNo(cdisPolicyListResponseBizContentProductPolicy.getCarrierNo());
        debitCreateAo.setFundInvertNo(fundInvertNo);//垫资方编号
        debitCreateAo.setFundTotalMoney(fundTotalMoney);//垫资总金额
        debitCreateAo.setCustPhone(orderInfo.getPhone());
        debitCreateAo.setNotifyUrl(deployNotifyUrl);//异步通知地址
        debitCreateAo.setCreateUserName(cdisPolicyListResponseBizContentProductPolicy.getCarrierOrgNo());//创建者
        debitCreateAo.setFreezeDetails(JSONObject.toJSONString(queryProductPolicyDetailResponseBizContent.getFreezeDetails()));//冻结明细
        debitCreateAo.setSettleDetails(JSONObject.toJSONString(queryProductPolicyDetailResponseBizContent.getSettleDetails()));//分润明细
        debitCreateAo.setIsAdvanceFund(0);//此处统一填0，0需要垫资,1不需要垫资
        debitCreateAo.setAllocationType(allocationType); //
        debitCreateAo.setFundCarrierNo(queryAgencygathResponseBizContent.getAccountId());
        debitCreateAo.setFundCarrierName(queryAgencygathResponseBizContent.getAccountName());
        debitCreateAo.setFundCarrierAccount(queryAgencygathResponseBizContent.getAccountNo());
        //供应商账户类型 （ 收款账号-收款模式类型(0)-对应的授信类型）
        String fundCarrierAccountType = null;
        //冻结的渠道编号
        String freezeExecChannelType = null;
        if (authType.intValue() == 0) {
            //outBank
            fundCarrierAccountType = "outBank";
//            freezeExecChannelType = "bankcard";
            freezeExecChannelType = "alipay";
            debitCreateAo.setFundCarrierAccount(bankAccountNo);
        } else if (authType.intValue() == 1) {
            //aliPay
            fundCarrierAccountType = "alipay";
            freezeExecChannelType = "alipay";
        }


        debitCreateAo.setFundCarrierAccountType(fundCarrierAccountType);

        debitCreateAo.setViolateHandleType(violateHandleType);
        if (violateReceivables.compareTo(new BigDecimal(0)) > 0) {
            debitCreateAo.setViolateReceivables(violateReceivables);
        }
        debitCreateAo.setPolicyNo(cdisPolicyListResponseBizContentProductPolicy.getPolicyNo());
        debitCreateAo.setPhoneBelong("");
        debitCreateAo.setGoodsId(cdisPolicyListResponseBizContentProductPolicy.getProductId()); //内部产品编码[product表的product_id]外键关联
        debitCreateAo.setOrderTradeStatus("WAIT_BUYER_PAY");//交易状态
        debitCreateAo.setOrderStatus(0);//订单状态//默认初始0
        debitCreateAo.setCarrierOrg(cdisPolicyListResponseBizContentProductPolicy.getCarrierOrgNo());//机构编号，订单透传，没有填0//业务接入商户[非技术接入商户]的关联组织的ID-具体问齐文
        debitCreateAo.setFreezeExecAuthCode("0");
        debitCreateAo.setFundOrderTotalMoney(orderInfo.getTotalFee());//订单总金额
        debitCreateAo.setFreezeExecAccount(queryCarrierAccountInfoResponseBizContent.getPayeeLogonId());
        debitCreateAo.setFreezeExecPid(queryCarrierAccountInfoResponseBizContent.getPayeeUserId());
        debitCreateAo.setOrderTradeType(1001);//1001-线上支付宝实时支付
        debitCreateAo.setFreezeExecChannelType(freezeExecChannelType);
        debitCreateAo.setStoreNum(cdisStore.getStoreNo());
        debitCreateAo.setCarrierDept(queryCarrierInfoResponseBizContent.getCarrierDept() == null ? "" : queryCarrierInfoResponseBizContent.getCarrierDept());//商户部门
        debitCreateAo.setContractFund(orderInfo.getTotalFee());//签约金额
        debitCreateAo.setNeedPayPerMonth(new BigDecimal(0));
        debitCreateAo.setCityName("0");
        debitCreateAo.setProvinceName("0");

        return debitCreateAo;
    }

    /**
     * 传入政策返回订阅下单参数
     *
     * @param
     * @return
     */
    public DebitSubscribeAo debitSubscribeDispose(OrderInfo orderInfo) {
        String orderStatus = "SUCCESS";//SUCCESS-交易成功  FAIL-交易失败 CLOSE-关闭
        DebitSubscribeAo debitSubscribeAo = new DebitSubscribeAo();
        List<DebitSubscribeAoFreezeDetail> freezeDetails = new ArrayList<>();
        DebitSubscribeAoFreezeDetail cdisFreezeDetail1 = new DebitSubscribeAoFreezeDetail();
        cdisFreezeDetail1.setBizNo("0");
        cdisFreezeDetail1.setBizNo2("0");
        cdisFreezeDetail1.setAccountType("alipay");
        cdisFreezeDetail1.setSettleTypeEnum("repay_per_month");
        cdisFreezeDetail1.setAuthNo(orderInfo.getAuthNo());
        cdisFreezeDetail1.setContractAmount(orderInfo.getTotalFee());
        cdisFreezeDetail1.setOutTradeNo(orderInfo.getAuthRequestNo());
        cdisFreezeDetail1.setPayeeLogonId(orderInfo.getPayeeLogonId());
        cdisFreezeDetail1.setPayeeUseridId(orderInfo.getPayeeUserId());
        freezeDetails.add(cdisFreezeDetail1);
        debitSubscribeAo.setOutOrderNo(orderInfo.getAuthOrderNo());//订单号
        debitSubscribeAo.setOperationStatus(orderStatus);//订单状态
        debitSubscribeAo.setFreezeDetails(JSONObject.toJSONString(freezeDetails));//订单授权详细信息数据

        return debitSubscribeAo;
    }


    /**
     * 返回政策外部编码及所属业务
     *
     * @param operatorCode         所属运营商
     * @param mobile               手机号
     * @param cardCenterInterfInfo 接口信息
     * @param productId            阿里侧虚拟id
     * @return outPortNo  businessType
     */
    public JSONObject getOutProtNoByMobile(String operatorCode, String mobile, CardCenterInterfInfo cardCenterInterfInfo, String productId) {
        String outPortNo = null;
        String businessType = BusinessTypeEnum.clsd.getCode();
        JSONObject jsonObject = new JSONObject();
        //判断是存量升档还是流量包的业务
        //调用pot获取政策列表
        String[] outPortNoList = cardCenterInterfInfo.getOutPortNo().split(",");
        outFor:
        for (String aa : outPortNoList) {
            ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo, aa, false);
            if (policyListResponse != null && policyListResponse.getBizContent() != null
                    && policyListResponse.getBizContent().getSpecList() != null
                    && policyListResponse.getBizContent().getCdisProductPolicy() != null) {
                List<PolicyListResponseBizContentPolicy> specList = policyListResponse.getBizContent().getSpecList();
                PolicyListResponseBizContentProductPolicy cdisProductPolicy = policyListResponse.getBizContent().getCdisProductPolicy();
                for (PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy : specList) {
                    //获取运营商城套餐id
                    if (policyListResponseBizContentPolicy.getPolicySpecStatus().intValue() == 1 &&
                            policyListResponseBizContentPolicy.getCheckFlag().equals("1") &&
                            policyListResponseBizContentPolicy.getLevel().intValue() == 3 &&
                            policyListResponseBizContentPolicy.getKeyStr().equals(productId)) {
                        outPortNo = cdisProductPolicy.getOutProtNo();
                        break outFor;
                    }
                }
            }
        }

        if (StringUtils.isBlank(outPortNo)) {
            //如果还没找到 则查流量包的政策
            String flowOutPortNoStr = cardCenterInterfInfo.getFlowOutPortNo() + "," + cardCenterInterfInfo.getFlowPlusOutPortNo();
            String[] flowOutPortNos = flowOutPortNoStr.split(",");
            outFor:
            for (String flowOutPortNo : flowOutPortNos) {
                ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo, flowOutPortNo, false);
                if (policyListResponse != null && policyListResponse.getBizContent() != null
                        && policyListResponse.getBizContent().getSpecList() != null
                        && policyListResponse.getBizContent().getCdisProductPolicy() != null) {
                    List<PolicyListResponseBizContentPolicy> specList2 = policyListResponse.getBizContent().getSpecList();
                    PolicyListResponseBizContentProductPolicy cdisProductPolicy = policyListResponse.getBizContent().getCdisProductPolicy();
                    for (PolicyListResponseBizContentPolicy policyListResponseBizContentPolicy : specList2) {
                        //获取运营商城套餐id
                        if (policyListResponseBizContentPolicy.getPolicySpecStatus().intValue() == 1 &&
                                policyListResponseBizContentPolicy.getCheckFlag().equals("1") &&
                                policyListResponseBizContentPolicy.getLevel().intValue() == 3 &&
                                policyListResponseBizContentPolicy.getKeyStr().equals(productId)) {
                            outPortNo = cdisProductPolicy.getOutProtNo();
                            businessType = BusinessTypeEnum.llb.getCode();
                            break;
                        }
                    }
                }
            }
        }

        jsonObject.put("outPortNo", outPortNo);
        jsonObject.put("businessType", businessType);

        return jsonObject;
    }

    /**
     * 转换成ftp
     *
     * @param orderInfo
     * @return
     */
    public WopayFtp changeToWoapFtp(OrderInfo orderInfo) {
        WopayFtp wopayFtp = new WopayFtp();
        wopayFtp.setTradeNo(orderInfo.getOutTradeNo());
        wopayFtp.setTradeFlowNo(orderInfo.getOrderLineId());
        wopayFtp.setPhoneNo(orderInfo.getPhone());
//        1400：天猫商城
        wopayFtp.setStagesCode("1400");
        wopayFtp.setProductId(orderInfo.getContractId());
        wopayFtp.setCreditDate(orderInfo.getCreateTime());
        wopayFtp.setFinishDate(orderInfo.getCreateTime());
//        1.支付成功
//        2.退款成功
//        3.提前结清
        wopayFtp.setTradeStatus("1");
        wopayFtp.setUserName(orderInfo.getUserName());//老用户不传
//        wopayFtp.setIdCard();//选填
        wopayFtp.setProductName(orderInfo.getName());
//        wopayFtp.setProductDescription();//选填
        wopayFtp.setFqNum(orderInfo.getFreezeMonth());
        wopayFtp.setOrderAmount(orderInfo.getTotalFee());
        wopayFtp.setFqSellerPercent(new BigDecimal(0));
        wopayFtp.setFqRate(new BigDecimal("12.5"));//暂时固定12.5
        wopayFtp.setPayUserId(orderInfo.getUserId());
        wopayFtp.setProvince(orderInfo.getProvinceCode());
        wopayFtp.setCity(orderInfo.getCityCode());
//        业务类型
//        1.新用户入网
//        2.老用户提档
        wopayFtp.setBusinessType("2");
        wopayFtp.setCreateTime(new Date());
        //ftp订单版本暂时写死02
        wopayFtp.setOrderVersion("02");
        return wopayFtp;
    }
}
