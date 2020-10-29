package cn.stylefeng.guns.modular.suninggift.component.validatorhandler;


import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.UnicomOutBizRequest;

public interface UnicomMallValidatorHandler {
	
	MessagerVo execute(UnicomOutBizRequest outBizRequest);

}
