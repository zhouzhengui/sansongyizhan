package cn.stylefeng.guns.modular.suninggift.component.basevalidator.impl;

import cn.stylefeng.guns.modular.suninggift.component.basevalidator.BaseUnicomMallInterfaceValidator;
import cn.stylefeng.guns.modular.suninggift.enums.EncryptAlgorithmEnum;
import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.UnicomOutBizRequest;
import cn.stylefeng.guns.modular.suninggift.utils.CommonUtil;
import cn.stylefeng.guns.modular.suninggift.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Slf4j
@Service
public class SuNingUnicomMallValidatorV1Impl implements BaseUnicomMallInterfaceValidator {

	@Override
	public String signMethod() {
		return EncryptAlgorithmEnum.MD5.getAlgorithmName();
	}

	/**
	 * 校验参数：获取号码列表
	 */
	@Override
	public MessagerVo validateHaokaOpennewNumSelect(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		if(outBizRequest == null || outBizRequest.getData() == null) {
			messagerVo.setSubCode("30001");
			messagerVo.setSubMsg("获取号码列表请求参数为空");
			return messagerVo;
		}
		
		String datastr = (String) outBizRequest.getData();
		String baseConvertStr = SignUtil.baseConvertStr(datastr);
		JSONObject dataObject = JSONObject.parseObject(baseConvertStr);
		outBizRequest.setData(dataObject);
		//校验必填字段是否为空
		if(CommonUtil.isEmpty(dataObject.getString("provinceCode"))) {
			messagerVo.setSubCode("30002");
			messagerVo.setSubMsg("获取号码列表请求参数[provinceCode]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("cityCode"))) {
			messagerVo.setSubCode("30003");
			messagerVo.setSubMsg("获取号码列表请求参数[cityCode]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("outSysNo"))) {
			messagerVo.setSubCode("30004");
			messagerVo.setSubMsg("获取号码列表请求参数[outSysNo]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("goodsId"))) {
			messagerVo.setSubCode("30005");
			messagerVo.setSubMsg("获取号码列表请求参数[goodsId]为空");
			return messagerVo;
		}
		
		messagerVo.setSubCode("10000");
		messagerVo.setSubMsg("获取号码列表参数校验成功");
		messagerVo.setData(outBizRequest);
		return messagerVo;
	}

	/**
	 * 校验参数：号码状态变更服务
	 */
	@Override
	public MessagerVo validateHaokaOpennewNumStateChange(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		if(outBizRequest == null || outBizRequest.getData() == null) {
			messagerVo.setSubCode("30001");
			messagerVo.setSubMsg("号码状态变更服务请求参数为空");
			return messagerVo;
		}
		
		String datastr = (String) outBizRequest.getData();
		String baseConvertStr = SignUtil.baseConvertStr(datastr);
		JSONObject dataObject = JSONObject.parseObject(baseConvertStr);
		
		//校验必填字段是否为空
		String certNo = dataObject.getString("certNo");
		if(CommonUtil.isEmpty(certNo)) {
			dataObject.put("certNo", "0");
		}
		outBizRequest.setData(dataObject);
		
		if(CommonUtil.isEmpty(dataObject.getString("provinceCode"))) {
			messagerVo.setSubCode("30002");
			messagerVo.setSubMsg("号码状态变更服务请求参数[provinceCode]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("cityCode"))) {
			messagerVo.setSubCode("30003");
			messagerVo.setSubMsg("号码状态变更服务请求参数[cityCode]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("custPhone"))) {
			messagerVo.setSubCode("30004");
			messagerVo.setSubMsg("号码状态变更服务请求参数[custPhone]为空");
			return messagerVo;
		}
		
		String occupiedFlag = dataObject.getString("occupiedFlag");
		if(CommonUtil.isEmpty(occupiedFlag)) {
			messagerVo.setSubCode("30005");
			messagerVo.setSubMsg("号码状态变更服务请求参数[occupiedFlag]为空");
			return messagerVo;
		}
		
		String occupiedTimeTag = dataObject.getString("occupiedTimeTag");
		if(CommonUtil.isEmpty(occupiedTimeTag)) {
			messagerVo.setSubCode("30006");
			messagerVo.setSubMsg("号码状态变更服务请求参数[occupiedTimeTag]为空");
			return messagerVo;
		}
		
		String times = dataObject.getString("times");
		if(CommonUtil.isEmpty(times)) {
			messagerVo.setSubCode("30007");
			messagerVo.setSubMsg("号码状态变更服务请求参数[times]为空");
			return messagerVo;
		}
		
		if(!"1".equals(times) && !"2".equals(times)) {
			messagerVo.setSubCode("30008");
			messagerVo.setSubMsg("号码状态变更服务请求参数[times]只能为1或者2");
			return messagerVo;
		}
		
		if("1".equals(times)) {
			if(!"S".equalsIgnoreCase(occupiedFlag)) {
				messagerVo.setSubCode("30009");
				messagerVo.setSubMsg("号码状态变更服务:选占号码请求参数[occupiedFlag]标识为S");
				return messagerVo;
			}
			
			if(!"S2".equalsIgnoreCase(occupiedTimeTag)) {
				messagerVo.setSubCode("30010");
				messagerVo.setSubMsg("号码状态变更服务:选占号码请求参数[occupiedTimeTag]时间标识为S2");
				return messagerVo;
			}
			
		}
		
		if("2".equals(times)) {
			if(!"D".equalsIgnoreCase(occupiedFlag)) {
				messagerVo.setSubCode("30011");
				messagerVo.setSubMsg("号码状态变更服务:延时选占号码请求参数[occupiedFlag]标识为D");
				return messagerVo;
			}
			
			if(!"D8".equalsIgnoreCase(occupiedTimeTag)) {
				messagerVo.setSubCode("30012");
				messagerVo.setSubMsg("号码状态变更服务:延时选占号码请求参数[occupiedTimeTag]时间标识为D8");
				return messagerVo;
			}
			
			if(CommonUtil.isEmpty(dataObject.getString("prokey"))) {
				messagerVo.setSubCode("30013");
				messagerVo.setSubMsg("号码状态变更服务请求参数[prokey]为空");
				return messagerVo;
			}
			
		}
		
		messagerVo.setSubCode("10000");
		messagerVo.setSubMsg("号码状态变更服务参数校验成功");
		messagerVo.setData(outBizRequest);
		return messagerVo;
	}

	/**
	 * 校验参数：客户资料校验和身份证认证
	 */
	@Override
	public MessagerVo validateHaokaOpennewCheckIdentityCust(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		if(outBizRequest == null || outBizRequest.getData() == null) {
			messagerVo.setSubCode("30001");
			messagerVo.setSubMsg("客户资料校验和身份证认证请求参数为空");
			return messagerVo;
		}
		
		String datastr = (String) outBizRequest.getData();
		String baseConvertStr = SignUtil.baseConvertStr(datastr);
		JSONObject dataObject = JSONObject.parseObject(baseConvertStr);
		outBizRequest.setData(dataObject);
		//校验必填字段是否为空
		if(CommonUtil.isEmpty(dataObject.getString("provinceCode"))) {
			messagerVo.setSubCode("30002");
			messagerVo.setSubMsg("客户资料校验和身份证认证请求参数[provinceCode]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("cityCode"))) {
			messagerVo.setSubCode("30003");
			messagerVo.setSubMsg("客户资料校验和身份证认证请求参数[cityCode]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("certName"))) {
			messagerVo.setSubCode("30004");
			messagerVo.setSubMsg("客户资料校验和身份证认证请求参数[certName]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("certNo"))) {
			messagerVo.setSubCode("30005");
			messagerVo.setSubMsg("客户资料校验和身份证认证请求参数[certNo]为空");
			return messagerVo;
		}
		
		messagerVo.setSubCode("10000");
		messagerVo.setSubMsg("客户资料校验和身份证认证参数校验成功");
		messagerVo.setData(outBizRequest);
		return messagerVo;
	}

	/**
	 * 校验参数：新入网订单信息保存入库
	 */
	@Override
	public MessagerVo validateHaokaOpennewConserveNewUnicomOrder(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		if(outBizRequest == null || outBizRequest.getData() == null) {
			messagerVo.setSubCode("30001");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数为空");
			return messagerVo;
		}
		
		String datastr = (String) outBizRequest.getData();
		String baseConvertStr = SignUtil.baseConvertStr(datastr);
		JSONObject dataObject = JSONObject.parseObject(baseConvertStr);
		if(CommonUtil.isEmpty(dataObject.getString("userId"))) {
			dataObject.put("userId", "0");
		}
		outBizRequest.setData(dataObject);
		//校验必填字段是否为空
		if(CommonUtil.isEmpty(dataObject.getString("contractPhone"))) {
			messagerVo.setSubCode("30002");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[contractPhone]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("certNo"))) {
			messagerVo.setSubCode("30003");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[certNo]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("prokey"))) {
			messagerVo.setSubCode("30004");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[prokey]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("policyNo"))) {
			messagerVo.setSubCode("30005");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[policyNo]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("orderDetailsPsnId"))) {
			messagerVo.setSubCode("30006");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[orderDetailsPsnId]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("firstMonthId"))) {
			messagerVo.setSubCode("30007");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[firstMonthId]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("firstMonthName"))) {
			messagerVo.setSubCode("30008");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[firstMonthName]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("custName"))) {
			messagerVo.setSubCode("30009");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[custName]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("custType"))) {
			messagerVo.setSubCode("30010");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[custType]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("phoneBelong"))) {
			messagerVo.setSubCode("30011");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[phoneBelong]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("freezePrice"))) {
			messagerVo.setSubCode("30012");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[freezePrice]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("postAddress"))) {
			messagerVo.setSubCode("30013");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[postAddress]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("postCustName"))) {
			messagerVo.setSubCode("30014");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[postCustName]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("postPhone"))) {
			messagerVo.setSubCode("30015");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[postPhone]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("postAreaCode"))) {
			messagerVo.setSubCode("30016");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[postAreaCode]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("certCheckCode"))) {
			messagerVo.setSubCode("30017");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[certCheckCode]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("certCheckMsg"))) {
			messagerVo.setSubCode("30018");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[certCheckMsg]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("outOrderNo"))) {
			messagerVo.setSubCode("30019");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[outOrderNo]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("outRequestNo"))) {
			messagerVo.setSubCode("30020");
			messagerVo.setSubMsg("新入网订单信息保存入库请求参数[outRequestNo]为空");
			return messagerVo;
		}
		
		messagerVo.setSubCode("10000");
		messagerVo.setSubMsg("新入网订单信息保存入库请求参数校验成功");
		messagerVo.setData(outBizRequest);
		return messagerVo;
	}

	/**
	 * 校验参数：新入网订单同步联通集团商城
	 */
	@Override
	public MessagerVo validateHaokaOpennewUnicomOrderSync(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		if(outBizRequest == null || outBizRequest.getData() == null) {
			messagerVo.setSubCode("30001");
			messagerVo.setSubMsg("新入网订单同步联通集团商城请求参数为空");
			return messagerVo;
		}
		
		String datastr = (String) outBizRequest.getData();
		String baseConvertStr = SignUtil.baseConvertStr(datastr);
		JSONObject dataObject = JSONObject.parseObject(baseConvertStr);
		outBizRequest.setData(dataObject);
		//校验必填字段是否为空
		if(CommonUtil.isEmpty(dataObject.getString("outOrderNo"))) {
			messagerVo.setSubCode("30002");
			messagerVo.setSubMsg("新入网订单同步联通集团商城请求参数[outOrderNo]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("userId"))) {
			messagerVo.setSubCode("30003");
			messagerVo.setSubMsg("新入网订单同步联通集团商城请求参数[userId]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("authNo"))) {
			messagerVo.setSubCode("30004");
			messagerVo.setSubMsg("新入网订单同步联通集团商城请求参数[authNo]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("paymentType"))) {
			messagerVo.setSubCode("30005");
			messagerVo.setSubMsg("新入网订单同步联通集团商城请求参数[paymentType]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("orderSuccessTime"))) {
			messagerVo.setSubCode("30006");
			messagerVo.setSubMsg("新入网订单同步联通集团商城请求参数[orderSuccessTime]为空");
			return messagerVo;
		}
		
		if(CommonUtil.isEmpty(dataObject.getString("operationId"))) {
			messagerVo.setSubCode("30007");
			messagerVo.setSubMsg("新入网订单同步联通集团商城请求参数[operationId]为空");
			return messagerVo;
		}

		if(CommonUtil.isEmpty(dataObject.getString("financeChannelCode"))) {
			messagerVo.setSubCode("30008");
			messagerVo.setSubMsg("新入网订单同步联通集团商城请求参数[financeChannelCode]为空");
			return messagerVo;
		}
		
		messagerVo.setSubCode("10000");
		messagerVo.setSubMsg("新入网订单同步联通集团商城请求参数校验成功");
		messagerVo.setData(outBizRequest);
		return messagerVo;
	}

	/**
	 * 校验参数：派发权益
	 */
	@Override
	public MessagerVo validateHaokaOpennewUnicomSendRights(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		if(outBizRequest == null || outBizRequest.getData() == null) {
			messagerVo.setSubCode("30001");
			messagerVo.setSubMsg("新入网订单派发权益请求参数为空");
			return messagerVo;
		}
		
		String datastr = (String) outBizRequest.getData();
		String baseConvertStr = SignUtil.baseConvertStr(datastr);
		JSONObject dataObject = JSONObject.parseObject(baseConvertStr);
		outBizRequest.setData(dataObject);
		//校验必填字段是否为空
		if(CommonUtil.isEmpty(dataObject.getString("outOrderNo"))) {
			messagerVo.setSubCode("30002");
			messagerVo.setSubMsg("新入网订单派发权益请求参数[outOrderNo]为空");
			return messagerVo;
		}

		if(CommonUtil.isEmpty(dataObject.getString("sendWay"))) {
			messagerVo.setSubCode("30003");
			messagerVo.setSubMsg("新入网订单派发权益请求参数[sendWay]为空");
			return messagerVo;
		}

		String sendOrg = dataObject.getString("sendOrg");
		if(CommonUtil.isEmpty(sendOrg)) {
			messagerVo.setSubCode("30004");
			messagerVo.setSubMsg("新入网订单派发权益请求参数[sendOrg]为空");
			return messagerVo;
		}
		if(!"suning".equals(sendOrg)) {
			messagerVo.setSubCode("30005");
			messagerVo.setSubMsg("新入网订单派发权益请求参数[sendOrg]不正确");
			return messagerVo;
		}

		if(CommonUtil.isEmpty(dataObject.getString("orderAmount"))) {
			messagerVo.setSubCode("30006");
			messagerVo.setSubMsg("新入网订单派发权益请求参数[orderAmount]为空");
			return messagerVo;
		}

		if(CommonUtil.isEmpty(dataObject.getString("sendOrgValue"))) {
			messagerVo.setSubCode("30007");
			messagerVo.setSubMsg("新入网订单派发权益请求参数[sendOrgValue]为空");
			return messagerVo;
		}

		if(CommonUtil.isEmpty(dataObject.getString("transactionDate"))) {
			messagerVo.setSubCode("30008");
			messagerVo.setSubMsg("新入网订单派发权益请求参数[transactionDate]为空");
			return messagerVo;
		}
		
		messagerVo.setSubCode("10000");
		messagerVo.setSubMsg("新入网订单派发权益请求参数校验成功");
		messagerVo.setData(outBizRequest);
		return messagerVo;
	}

}
