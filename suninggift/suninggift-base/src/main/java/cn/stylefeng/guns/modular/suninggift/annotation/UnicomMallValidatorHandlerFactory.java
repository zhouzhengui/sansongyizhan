package cn.stylefeng.guns.modular.suninggift.annotation;

import cn.stylefeng.guns.modular.suninggift.component.validatorhandler.UnicomMallValidatorHandler;
import cn.stylefeng.guns.modular.suninggift.component.validatorhandler.UnicomMallValidatorTemplate;
import cn.stylefeng.guns.modular.suninggift.model.UnicomOutBizRequest;
import cn.stylefeng.guns.modular.suninggift.utils.CommonUtil;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

/**
 * 校验工厂
 * @author zhouz
 *
 */
@Component
public class UnicomMallValidatorHandlerFactory {
	
	//反射机制注入
	@Autowired
	private SpringContextService springContextService;
	
	private Map<String, UnicomMallValidatorTemplate> validatorHandlerMap;
	
	private synchronized Map<String, UnicomMallValidatorTemplate> getValidatorBean(){
		if(validatorHandlerMap == null){
			validatorHandlerMap = springContextService.getAllImplementClassByInterfaceName(UnicomMallValidatorTemplate.class);
		}
		
		return validatorHandlerMap;
	}
	
	public UnicomMallValidatorHandler getValidatorHandler(String jsonObj){
		if(CommonUtil.isEmpty(jsonObj)){
			return null;
		}
		
		UnicomOutBizRequest outBizRequest = JSON.parseObject(jsonObj, UnicomOutBizRequest.class);
		
		if(CommonUtil.isEmpty(outBizRequest.getSign_method())){
			return null;
		}
		
		Set<Entry<String, UnicomMallValidatorTemplate>> alipayHandlerSet = getValidatorBean().entrySet();
		for(Entry<String, UnicomMallValidatorTemplate> entry : alipayHandlerSet){
			UnicomMallValidatorTemplate validatorHander = entry.getValue();
			if(validatorHander.decryptType() != null && validatorHander.decryptType().equalsIgnoreCase(outBizRequest.getSign_method())){
				return validatorHander;
			}
		}
		
		return null;
	}

}
