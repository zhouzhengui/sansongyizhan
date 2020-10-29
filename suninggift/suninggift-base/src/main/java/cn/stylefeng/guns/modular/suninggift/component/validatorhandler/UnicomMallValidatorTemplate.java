package cn.stylefeng.guns.modular.suninggift.component.validatorhandler;


import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.UnicomOutBizRequest;

public abstract class UnicomMallValidatorTemplate implements UnicomMallValidatorHandler{

	/**
	 * 是否需要进行验签
	 * 
	 * @return
	 */
	public abstract boolean isValidateSign();

	/**
	 * 对参数进行验签
	 * 
	 * @param outBizRequest
	 * @return
	 */
	public abstract MessagerVo validateSign(UnicomOutBizRequest outBizRequest);

	/**
	 * 是否需要进行解密
	 * 
	 * @return
	 */
	public abstract boolean isDecrypt();

	/**
	 * 对请求参数进行解密
	 * 
	 * @param outBizRequest
	 * @return
	 */
	public abstract MessagerVo decrypt(UnicomOutBizRequest outBizRequest);

	/**
	 * 解密类型
	 * 
	 * @return
	 */
	public abstract String decryptType();

	/**
	 * 真正的校验器
	 * 
	 * @param outBizRequest
	 * @return
	 */
	public abstract MessagerVo validator(UnicomOutBizRequest outBizRequest);
	
	
	@Override
	public MessagerVo execute(UnicomOutBizRequest outBizRequest) {
		// 是否需要进行解密
		if (isDecrypt()) {
			MessagerVo messagerVo = decrypt(outBizRequest);
			if (!messagerVo.isSuccess()) {
				return messagerVo;
			}
		}

		// 是否需要进行验签
		if (isValidateSign()) {
			MessagerVo messagerVo = validateSign(outBizRequest);
			if (!messagerVo.isSuccess()) {
				return messagerVo;
			}
		}

		// 进行参数校验
		MessagerVo messagerVo = validator(outBizRequest);
		return messagerVo;
	}
	
	

}
