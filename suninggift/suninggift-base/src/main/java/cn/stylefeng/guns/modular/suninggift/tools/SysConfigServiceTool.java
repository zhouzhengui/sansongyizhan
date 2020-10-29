package cn.stylefeng.guns.modular.suninggift.tools;

import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-04-28 14:55
 */
@Slf4j
@Component
public class SysConfigServiceTool {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     *
     * @param clientId 对接client
     * @return 接口信息
     */
    public CardCenterInterfInfo getByClientId(String clientId){
        List<CardCenterInterfInfo> cardCenterInfo = JSONObject.parseArray(sysConfigService.getByCode("cardCenterInfo"), CardCenterInterfInfo.class);
        for (CardCenterInterfInfo cardCenterInterfInfo : cardCenterInfo){
            if (cardCenterInterfInfo.getClientId().equals(clientId)){
                return cardCenterInterfInfo;
            }
        }
        return null;
    }

    /**
     * 根据运营商编码返回配置信息
     * @param operatorCode 对接operatorCode
     * @return 接口配置信息
     */
    public CardCenterInterfInfo getByOperatorCode(String operatorCode){
        List<CardCenterInterfInfo> cardCenterInfo = JSONObject.parseArray(sysConfigService.getByCode("cardCenterInfo"), CardCenterInterfInfo.class);
        for (CardCenterInterfInfo cardCenterInterfInfo : cardCenterInfo){
            if (cardCenterInterfInfo.getOperatorCode().equals(operatorCode)){
                return cardCenterInterfInfo;
            }
        }
        return null;
    }
}
