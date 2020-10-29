package cn.stylefeng.guns.modular.suninggift.service;


import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.UnicomOutBizRequest;

public interface BaseSuNingUnicomMallInterfaceService {
	
	/**
	 * 返回该校验参数的加签方法
	 * @return
	 */
	String signMethod();

	/**
	 * 集团新用户入网业务处理：获取号码列表
	 * @param outBizRequest
	 * @return
	 */
	MessagerVo haokaOpennewNumSelect(UnicomOutBizRequest outBizRequest);

	/**
	 * 集团新用户入网业务处理：号码状态变更服务
	 * @param outBizRequest
	 * @return
	 */
	MessagerVo haokaOpennewNumStateChange(UnicomOutBizRequest outBizRequest);

	/**
	 * 集团新用户入网业务处理：客户资料校验和身份证认证
	 * @param outBizRequest
	 * @return
	 */
	MessagerVo haokaOpennewCheckIdentityCust(UnicomOutBizRequest outBizRequest);

	/**
	 * 集团新用户入网业务处理：新入网订单信息保存入库
	 * @param outBizRequest
	 * @return
	 */
	MessagerVo haokaOpennewConserveNewUnicomOrder(UnicomOutBizRequest outBizRequest);

	/**
	 * 集团新用户入网业务处理：新入网订单同步联通集团商城
	 * @param outBizRequest
	 * @return
	 */
	MessagerVo haokaOpennewUnicomOrderSync(UnicomOutBizRequest outBizRequest);

	/**
	 * 集团新用户入网业务处理：派发权益
	 * @param outBizRequest
	 * @return
	 */
	MessagerVo haokaOpennewUnicomSendRights(UnicomOutBizRequest outBizRequest);

}
