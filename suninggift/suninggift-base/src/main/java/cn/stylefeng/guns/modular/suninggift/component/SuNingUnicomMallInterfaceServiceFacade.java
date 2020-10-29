package cn.stylefeng.guns.modular.suninggift.component;

import cn.stylefeng.guns.modular.suninggift.annotation.SpringContextService;
import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import cn.stylefeng.guns.modular.suninggift.enums.UnicomMallOutInterfaceMethodEnum;
import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.UnicomOutBizRequest;
import cn.stylefeng.guns.modular.suninggift.service.BaseSuNingUnicomMallInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 集团新入网接口业务处理
 * @author 
 *
 */
@Service
public class SuNingUnicomMallInterfaceServiceFacade {
	
	private Lock unicomMallInterfaceLock = new ReentrantLock();
	
    @Autowired
    private SpringContextService springContextService;
    
    private Map<String, BaseSuNingUnicomMallInterfaceService> baseUnicomMallInterfaceServiceMap;
    
    private BaseSuNingUnicomMallInterfaceService getBaseUnicomMallInterfaceService(String signMethod) {
        if (baseUnicomMallInterfaceServiceMap == null) {
        	unicomMallInterfaceLock.lock();
            try {
            	baseUnicomMallInterfaceServiceMap = springContextService.getAllImplementClassByInterfaceName(BaseSuNingUnicomMallInterfaceService.class);
            } finally {
            	unicomMallInterfaceLock.unlock();
            }
        }

        if (baseUnicomMallInterfaceServiceMap != null) {
            Set<Entry<String, BaseSuNingUnicomMallInterfaceService>> entrySet = baseUnicomMallInterfaceServiceMap.entrySet();
            for (Entry<String, BaseSuNingUnicomMallInterfaceService> entry : entrySet) {
            	BaseSuNingUnicomMallInterfaceService baseUnicomMallInterfaceService = entry.getValue();
                if (signMethod.equalsIgnoreCase(baseUnicomMallInterfaceService.signMethod())) {
                    return baseUnicomMallInterfaceService;
                }
            }
        }

        return null;
    }
    
    private MessagerVo processUnicomMallInterface(UnicomOutBizRequest outBizRequest) {
    	BaseSuNingUnicomMallInterfaceService baseUnicomMallInterfaceService = getBaseUnicomMallInterfaceService(outBizRequest.getSign_method());
        if (baseUnicomMallInterfaceService == null) {
        	MessagerVo messagerVo = new MessagerVo();
            messagerVo.setSubCode(ResponseStatusEnum.SERVICE_ERROR.getCode());
            messagerVo.setSubMsg("集团外放接口服务类没有找到，当前业务的:" + outBizRequest.getSign_method());
            return messagerVo;
        }

        UnicomMallOutInterfaceMethodEnum unicomMallInterfaceMethodEnum = UnicomMallOutInterfaceMethodEnum
            .getUnicomMallInterfaceMethod(outBizRequest.getMethod());
        MessagerVo messagerVo = null;
        switch (unicomMallInterfaceMethodEnum) {
            case HAOKA_OPENNEW_NUM_SELECT:
            	messagerVo = baseUnicomMallInterfaceService.haokaOpennewNumSelect(outBizRequest);
                break;
            case HAOKA_OPENNEW_NUM_STATE_CHANGE:
            	messagerVo = baseUnicomMallInterfaceService.haokaOpennewNumStateChange(outBizRequest);
                break;
            case HAOKA_OPENNEW_CHECK_IDENTITY_CUST:
            	messagerVo = baseUnicomMallInterfaceService.haokaOpennewCheckIdentityCust(outBizRequest);
            	break;
            case HAOKA_OPENNEW_CONSERVE_NEW_UNICOM_ORDER:
            	messagerVo = baseUnicomMallInterfaceService.haokaOpennewConserveNewUnicomOrder(outBizRequest);
            	break;
            case HAOKA_OPENNEW_UNICOM_ORDER_SYNC:
            	messagerVo = baseUnicomMallInterfaceService.haokaOpennewUnicomOrderSync(outBizRequest);
            	break;
            case HAOKA_OPENNEW_UNICOM_SEND_RIGHTS:
            	messagerVo = baseUnicomMallInterfaceService.haokaOpennewUnicomSendRights(outBizRequest);
            	break;
            
        }

        if (messagerVo == null) {
        	messagerVo = new MessagerVo();
            messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR.getCode());
            messagerVo.setSubMsg("新入网业务处理操作过程出现问题");
        }

        return messagerVo;
    }
    
    /**
     * 统一服务入口类
     */
    public final MessagerVo unifyProcess(UnicomOutBizRequest outBizRequest) {
        String method = outBizRequest.getMethod();
        UnicomMallOutInterfaceMethodEnum unicomMallInterfaceMethodEnum = UnicomMallOutInterfaceMethodEnum.getUnicomMallInterfaceMethod(method);
        if (unicomMallInterfaceMethodEnum != null) {
            return processUnicomMallInterface(outBizRequest);
        }

        MessagerVo messagerVo = new MessagerVo();
        messagerVo.setSubCode(ResponseStatusEnum.SERVICE_ERROR.getCode());
        messagerVo.setMsg("需要处理的服务类不存在");
        return messagerVo;
    }

}
