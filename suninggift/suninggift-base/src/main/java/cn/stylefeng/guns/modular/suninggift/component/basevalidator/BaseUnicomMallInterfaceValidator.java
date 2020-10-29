package cn.stylefeng.guns.modular.suninggift.component.basevalidator;


import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.UnicomOutBizRequest;

public interface BaseUnicomMallInterfaceValidator {
	
	/**
	 * 返回该校验参数的版本号
	 * @return
	 */
	String signMethod();

	/**
	 * 校验参数：获取号码列表
	 * @param outBizRequest
	 * @return
	 */
	MessagerVo validateHaokaOpennewNumSelect(UnicomOutBizRequest outBizRequest);

	/**
	 * 校验参数：号码状态变更服务
	 * @param outBizRequest
	 * @return
	 */
	MessagerVo validateHaokaOpennewNumStateChange(UnicomOutBizRequest outBizRequest);

	/**
	 * 校验参数：客户资料校验和身份证认证
	 * @param outBizRequest
	 * @return
	 */
	MessagerVo validateHaokaOpennewCheckIdentityCust(UnicomOutBizRequest outBizRequest);

	/**
	 * 校验参数：新入网订单信息保存入库
	 * @param outBizRequest
	 * @return
	 */
	MessagerVo validateHaokaOpennewConserveNewUnicomOrder(UnicomOutBizRequest outBizRequest);

	/**
	 * 校验参数：新入网订单同步联通集团商城
	 * @param outBizRequest
	 * @return
	 */
	MessagerVo validateHaokaOpennewUnicomOrderSync(UnicomOutBizRequest outBizRequest);

	/**
	 * 校验参数：派发权益
	 * @param outBizRequest
	 * @return
	 */
	MessagerVo validateHaokaOpennewUnicomSendRights(UnicomOutBizRequest outBizRequest);

}
