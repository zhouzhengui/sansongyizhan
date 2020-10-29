package cn.stylefeng.guns.modular.suninggift.service.impl;

import cn.stylefeng.guns.modular.suninggift.enums.EncryptAlgorithmEnum;
import cn.stylefeng.guns.modular.suninggift.enums.OperatorEnum;
import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.UnicomOutBizRequest;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ApiInBizResponse;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.PolicyListResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.PolicyListResponseBizContentPolicy;
import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.modular.suninggift.service.BaseSuNingUnicomMallInterfaceService;
import cn.stylefeng.guns.modular.suninggift.service.InInterfaceService;
import cn.stylefeng.guns.modular.suninggift.service.LpcenterInterfaceService;
import cn.stylefeng.guns.modular.suninggift.service.RightsNotifyService;
import cn.stylefeng.guns.modular.suninggift.tools.SysConfigServiceTool;
import cn.stylefeng.guns.modular.suninggift.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service 
public class SuNingUnicomMallInterfaceServiceV1Impl implements BaseSuNingUnicomMallInterfaceService {

	@Autowired
	private LpcenterInterfaceService lpcenterInterfaceService;

	@Autowired
	private SysConfigServiceTool sysConfigServiceTool;

	@Autowired
	private InInterfaceService inInterfaceService;

	@Autowired
	private RightsNotifyService rightsNotifyService;


	@Override
	public String signMethod() {
		return EncryptAlgorithmEnum.MD5.getAlgorithmName();
	}

	/**
	 * 集团新用户入网业务处理：获取号码列表
	 */
	@Override
	public MessagerVo haokaOpennewNumSelect(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(outBizRequest.getData()));
		//根据政策外部编码获取对应的政策信息
		String outProtNo = jsonObject.getString("outSysNo");
		//根据运营商编码获取clientId
		String lpunicom = OperatorEnum.lpsuningunicom.getCode();
		CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(lpunicom);

		//调用pot获取苏宁新入网政策列表信息
		ApiInBizResponse<PolicyListResponseBizContent> policyListResponse = inInterfaceService.queryPolicyList(cardCenterInterfInfo , outProtNo ,false);
		if(null == policyListResponse || !"10000".equals(policyListResponse.getCode())){
			messagerVo.setSubCode("40001");
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

		productPolicyVo.setCdisProductPolicy(policyListResponseBizContent.getCdisProductPolicy());
		productPolicyVo.setSpecList(zeroList);

		//匹配对应请求联通集团号库产品编码,获取0级的政策编码返回给接口调用方
		String goodsId = jsonObject.getString("goodsId");
		//0级子政策
		String psnIdZero = "";
		//2级子政策
		String psnIdTwo = "";
		//套餐级别
		String price = "";

		List<PolicyListResponseBizContentPolicy> childList = productPolicyVo.getSpecList();
		for (PolicyListResponseBizContentPolicy cdisSpec : childList) {

			List<PolicyListResponseBizContentPolicy> childList1 = cdisSpec.getChildList();
			for (PolicyListResponseBizContentPolicy cdisSpec1 : childList1) {
				List<PolicyListResponseBizContentPolicy> childList2 = cdisSpec1.getChildList();
				for (PolicyListResponseBizContentPolicy cdisSpec2 : childList2) {
					if(goodsId.equals(cdisSpec2.getKeyStr())) {
						psnIdTwo = cdisSpec2.getPsnId();
						psnIdZero = cdisSpec.getPsnId();
						price = cdisSpec.getKeyStr();
						 break ;
					}
				}
			}
		}

		if(CommonUtil.isEmpty(psnIdZero) || CommonUtil.isEmpty(price)) {
			messagerVo.setSubCode("40002");
			messagerVo.setSubMsg("0级政策编码/套餐金额为空");
			return messagerVo;
		}

		//拼接goodsData数据
		JSONObject goodsData = new JSONObject();
		goodsData.put("price", price);
		goodsData.put("policyNo", productPolicyVo.getCdisProductPolicy().getPolicyNo());
		goodsData.put("orderDetailsPsnId", psnIdZero);

		//请求联通集团选号接口能力
		jsonObject.remove("outSysNo");
		MessagerVo unicomNumSelectMessagerVo = lpcenterInterfaceService.unicomNumSelect(jsonObject.toJSONString());
		log.info("获取号码列表请求联通响应报文,{}" ,JSON.toJSONString(unicomNumSelectMessagerVo));
		if(!unicomNumSelectMessagerVo.isSuccess()) {
			return unicomNumSelectMessagerVo;
		}

		//获取号码信息
		JSONObject jsonObjectData = new JSONObject();
		jsonObjectData.put("phoneData", unicomNumSelectMessagerVo.getData());
		jsonObjectData.put("goodsData", goodsData);

		messagerVo.setSubCode("10000");
		messagerVo.setSubMsg("获取号码列表成功");
		messagerVo.setData(jsonObjectData);
		return messagerVo;
	}

	/**
	 * 集团新用户入网业务处理：号码状态变更服务
	 */
	@Override
	public MessagerVo haokaOpennewNumStateChange(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		String dataObject = JSON.toJSONString(outBizRequest.getData());
		
		//调用联通集团：号码状态变更能力
		MessagerVo inBizRespond = lpcenterInterfaceService.unicomNumStateChange(dataObject);
		log.info("调用联通集团:号码状态变更能力响应报文,{}" ,JSON.toJSONString(inBizRespond));
		if(!"10000".equals(inBizRespond.getSubCode())) {
			messagerVo.setSubCode("40001");
			messagerVo.setSubMsg(inBizRespond.getSubMsg());
			return messagerVo;
		}
		
		messagerVo.setSubCode("10000");
		messagerVo.setSubMsg(inBizRespond.getSubMsg());
		messagerVo.setData(inBizRespond.getData());
		return messagerVo;
	}

	/**
	 * 集团新用户入网业务处理：客户资料校验和身份证认证
	 */
	@Override
	public MessagerVo haokaOpennewCheckIdentityCust(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		String dataObject = JSON.toJSONString(outBizRequest.getData());
		
		//调用联通集团：客户资料校验和身份证认证
		MessagerVo inBizRespond = lpcenterInterfaceService.unicomCheckIdentityCust(dataObject);
		log.info("调用联通集团:客户资料校验和身份证认证响应报文,{}" ,JSON.toJSONString(inBizRespond));
		if(!"10000".equals(inBizRespond.getSubCode())) {
			messagerVo.setSubCode(inBizRespond.getSubCode());
			messagerVo.setSubMsg(inBizRespond.getSubMsg());
			return messagerVo;
		}

		JSONObject custObject = (JSONObject) inBizRespond.getData();
		custObject.remove("body");
		
		messagerVo.setSubCode("10000");
		messagerVo.setSubMsg(inBizRespond.getSubMsg());
		messagerVo.setData(custObject);
		return messagerVo;
	}

	/**
	 * 集团新用户入网业务处理：新入网订单信息保存入库
	 */
	@Override
	public MessagerVo haokaOpennewConserveNewUnicomOrder(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		String dataObject = JSON.toJSONString(outBizRequest.getData());
		
		//新入网订单信息保存入库
		MessagerVo unicomOrderMessagerVo = lpcenterInterfaceService.conserveUnicomOrder(dataObject);
		log.info("新入网订单信息保存入库底层响应报文,{}" ,JSON.toJSONString(unicomOrderMessagerVo));
		if(!unicomOrderMessagerVo.isSuccess()) {
			messagerVo.setSubCode(unicomOrderMessagerVo.getSubCode());
			messagerVo.setSubMsg(unicomOrderMessagerVo.getSubMsg());
			return messagerVo;
		}

		//把乐芃订单号返回给调用方
		JSONObject orderObject = (JSONObject) unicomOrderMessagerVo.getData();
		String outOrderNo = orderObject.getString("outOrderNo");
		JSONObject dataReqObject = (JSONObject) outBizRequest.getData();
		dataReqObject.put("outOrderNo", outOrderNo);
		dataReqObject.remove("userId");
		messagerVo.setSubCode("10000");
		messagerVo.setSubMsg(unicomOrderMessagerVo.getSubMsg());
		messagerVo.setData(dataReqObject);
		return messagerVo;
	}

	/**
	 * 集团新用户入网业务处理：新入网订单同步联通集团商城
	 */
	@Override
	public MessagerVo haokaOpennewUnicomOrderSync(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		String dataObject = JSON.toJSONString(outBizRequest.getData());
		
		//更新订单支付状态、userId、authNo到主订单和冻结相关订单信息中
		MessagerVo updateOrderResult = lpcenterInterfaceService.unicomMallInterfaceUpdateOrderInfo(dataObject);
		if(!updateOrderResult.isSuccess()) {
			messagerVo.setSubCode(updateOrderResult.getSubCode());
			messagerVo.setSubMsg(updateOrderResult.getSubMsg());
			return messagerVo;
		}

		//集团新用户入网业务处理：新入网订单同步联通集团商城
		Map<String ,Object> dataMap = (Map<String, Object>) updateOrderResult.getData();
		//老订单下单入口
		//MessagerVo inBizRespond = lpcenterInterfaceService.unicomMallInterfaceOrderSync(dataObject ,dataMap);

		//取消旧接口，启用新接口，新订单下单入口
		MessagerVo inBizRespond = lpcenterInterfaceService.unicomMallInterfaceNewOrderSync(dataObject ,dataMap);
		log.info("集团新用户入网业务处理：新入网订单同步联通集团商城底层响应报文,{}" ,JSON.toJSONString(inBizRespond));
		if(!"10000".equals(inBizRespond.getSubCode())) {
			messagerVo.setSubCode(inBizRespond.getSubCode());
			messagerVo.setSubMsg(inBizRespond.getSubMsg());
			messagerVo.setData(inBizRespond.getData());

			//如果订单同步给集团联通商城下单失败时,触发异步解冻返销订单信息
			rightsNotifyService.unicomMallOrderUnfreeze(dataObject , true);

			return messagerVo;
		}
		
		messagerVo.setSubCode("10000");
		messagerVo.setSubMsg(inBizRespond.getSubMsg());
		messagerVo.setData(inBizRespond.getData());
		return messagerVo;
	}

	/**
	 * 集团新用户入网业务处理：派发权益
	 */
	@Override
	public MessagerVo haokaOpennewUnicomSendRights(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		JSONObject dataObject = (JSONObject) outBizRequest.getData();
		
		//集团新用户入网业务处理：派发权益
		MessagerVo sendRightsMessagerVo = lpcenterInterfaceService.orderSendRights(dataObject);
		log.info("集团新用户入网业务处理：派发权益底层响应报文,{}" ,JSON.toJSONString(sendRightsMessagerVo));
		if(!sendRightsMessagerVo.isSuccess()) {
			return sendRightsMessagerVo;
		}
		
		messagerVo.setSubCode("10000");
		messagerVo.setSubMsg(sendRightsMessagerVo.getSubMsg());
		messagerVo.setData(sendRightsMessagerVo.getData());
		return messagerVo;
	}

}
