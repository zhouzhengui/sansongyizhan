package cn.stylefeng.guns.modular.suninggift.component.basevalidator;

import cn.stylefeng.guns.modular.suninggift.annotation.SpringContextService;
import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import cn.stylefeng.guns.modular.suninggift.enums.UnicomMallOutInterfaceMethodEnum;
import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.UnicomOutBizRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class UnicomMallValidatorFacade {
	
	private Lock unicomCardLock = new ReentrantLock();
	
	@Autowired
	private SpringContextService springContextService;
	
	private Map<String, BaseUnicomMallInterfaceValidator> unicomMallInterfaceValidatorMap;
	
	private BaseUnicomMallInterfaceValidator getBaseUnicomMallInterfaceValidator(String signMethod){
		if(unicomMallInterfaceValidatorMap == null){
			unicomCardLock.lock();
			try{
				if(unicomMallInterfaceValidatorMap == null){
					unicomMallInterfaceValidatorMap = springContextService.getAllImplementClassByInterfaceName(BaseUnicomMallInterfaceValidator.class);
				}
			}finally{
				unicomCardLock.unlock();
			}
		}
		
		if(unicomMallInterfaceValidatorMap != null){
			Set<Entry<String, BaseUnicomMallInterfaceValidator>> entrySet = unicomMallInterfaceValidatorMap.entrySet();
			for(Entry<String, BaseUnicomMallInterfaceValidator> entry : entrySet){
				BaseUnicomMallInterfaceValidator baseUnicomMallInterfaceValidator = entry.getValue();
				if(signMethod.equalsIgnoreCase(baseUnicomMallInterfaceValidator.signMethod())){
					return baseUnicomMallInterfaceValidator;
				}
			}
		}
		
		return null;
	}
	
	
	public MessagerVo validateUnicomCardHandle(UnicomOutBizRequest outBizRequest){
		BaseUnicomMallInterfaceValidator baseUnicomMallInterfaceValidator = getBaseUnicomMallInterfaceValidator(outBizRequest.getSign_method());
		if(baseUnicomMallInterfaceValidator == null){
			MessagerVo messagerVo = new MessagerVo();
			messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR.getCode());
			messagerVo.setSubMsg("验证规则没有找到，当前业务的加签方法:" + outBizRequest.getSign_method());
			return messagerVo;
		}

		UnicomMallOutInterfaceMethodEnum unicomMallInterfaceMethodEnum = UnicomMallOutInterfaceMethodEnum.getUnicomMallInterfaceMethod(outBizRequest.getMethod());
		MessagerVo messagerVo = null;
		switch(unicomMallInterfaceMethodEnum) {
			case HAOKA_OPENNEW_NUM_SELECT:
				messagerVo = baseUnicomMallInterfaceValidator.validateHaokaOpennewNumSelect(outBizRequest);
				break;
			case HAOKA_OPENNEW_NUM_STATE_CHANGE:
				messagerVo = baseUnicomMallInterfaceValidator.validateHaokaOpennewNumStateChange(outBizRequest);
				break;
			case HAOKA_OPENNEW_CHECK_IDENTITY_CUST:
				messagerVo = baseUnicomMallInterfaceValidator.validateHaokaOpennewCheckIdentityCust(outBizRequest);
				break;
			case HAOKA_OPENNEW_CONSERVE_NEW_UNICOM_ORDER:
				messagerVo = baseUnicomMallInterfaceValidator.validateHaokaOpennewConserveNewUnicomOrder(outBizRequest);
				break;
			case HAOKA_OPENNEW_UNICOM_ORDER_SYNC:
				messagerVo = baseUnicomMallInterfaceValidator.validateHaokaOpennewUnicomOrderSync(outBizRequest);
				break;
			case HAOKA_OPENNEW_UNICOM_SEND_RIGHTS:
				messagerVo = baseUnicomMallInterfaceValidator.validateHaokaOpennewUnicomSendRights(outBizRequest);
				break;
		}

		if(messagerVo == null){
			messagerVo = new MessagerVo();
			messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR.getCode());
			messagerVo.setSubMsg("参数校验过程出现问题");
		}

		return messagerVo;
	}

}
