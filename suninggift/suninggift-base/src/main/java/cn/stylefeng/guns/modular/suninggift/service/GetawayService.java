package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.modular.suninggift.annotation.RouteService;
import cn.stylefeng.guns.modular.suninggift.enums.OutApiMethodAndClassEnum;
import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import cn.stylefeng.guns.modular.suninggift.model.InBizRequest;
import cn.stylefeng.guns.modular.suninggift.model.InBizRespond;
import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
import cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo.OutApiResponse;
import cn.stylefeng.guns.modular.suninggift.utils.AliNotifyUtil;
import cn.stylefeng.guns.modular.suninggift.utils.ChatbotSendUtil;
import cn.stylefeng.guns.modular.suninggift.utils.SignUtil;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 奇门网关无服务类
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-02-18 16:08
 */
@Slf4j
@Service
public class GetawayService {

    @Autowired
    PromotionAccountInfoService promotionAccountInfoService;

    @Autowired
    RouteService routeService;

    @Autowired
    ChatbotSendUtil chatbotSendUtil;

    @Autowired
    SysConfigService sysConfigService;

    /**
     * 外部接口网关操作
     * @return
     */
    public OutApiResponse getawayDispose(Map<String,String> map){
        OutApiResponse outApiResponse = new OutApiResponse(false, ResponseStatusEnum.EXCEPTION.getCode(),ResponseStatusEnum.EXCEPTION.getMsg());

        String target_appkey = map.get("target_appkey");
        if(StringUtils.isBlank(target_appkey)){
            log.info("app_key为空");
            return new OutApiResponse(false,ResponseStatusEnum.CONTROLLER_ERROR_PARAMS.getCode(),ResponseStatusEnum.CONTROLLER_ERROR_PARAMS.getMsg());
        }
        PromotionAccountInfo promotionAccountInfo = promotionAccountInfoService.getByAppId(target_appkey);
        //验签
        Boolean aBoolean = AliNotifyUtil.verifyTopRequestSign(map, promotionAccountInfo);
        if(!aBoolean){
            log.info("验签不合法");
            return new OutApiResponse(false,ResponseStatusEnum.CONTROLLER_ERROR_CHECK_SIGN.getCode(),ResponseStatusEnum.CONTROLLER_ERROR_CHECK_SIGN.getMsg());
        }

        InBizRespond execute = null;
        try{
            //方法路由处理
            String method = map.get("method");
            String data = map.get("data");//业务参数
            String app_key = map.get("app_key");//业务参数
            InBizRequest inBizRequest = new InBizRequest();
            inBizRequest.setMethod(method);
            String classPathByMethod = OutApiMethodAndClassEnum.getClassPathByMethod(inBizRequest.getMethod());
            if(null == classPathByMethod){
                log.info("非法请求方法");
                return new OutApiResponse(false,ResponseStatusEnum.CONTROLLER_ERROR_METHOD.getCode(),ResponseStatusEnum.CONTROLLER_ERROR_METHOD.getMsg());
            }
            if(StringUtils.isEmpty(data)){
                return new OutApiResponse(false,ResponseStatusEnum.CONTROLLER_ERROR_PARAMS.getCode(),ResponseStatusEnum.CONTROLLER_ERROR_PARAMS.getMsg());
            }
            //业务参数方法与类的映射.
            //将业务参数Base64解密
            String jsonStr = SignUtil.baseConvertStr(data);
            log.info("业务参数明文：" +jsonStr);

           JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            jsonObject.put("app_key", app_key);
           // String s = JSONObject.toJSONString(map);
            Object o = JSONObject.parseObject(jsonObject.toJSONString(), Class.forName(classPathByMethod));
            inBizRequest.setT(o);
            execute = routeService.execute(inBizRequest);
        }catch (Exception e){
            log.error("系统异常,{}",e);
            chatbotSendUtil.sendMsg(e.getMessage());
        }
        outApiResponse.setRetMessage(execute.getMessage());
        if(execute.getCode().equals("10000")){
            outApiResponse.setRetCode("0000");
            outApiResponse.setIsSuccess(true);
        }else{
            outApiResponse.setIsSuccess(false);
            outApiResponse.setRetCode(execute.getCode());
        }
        outApiResponse.setData(execute.getT());

        return outApiResponse;
    }

}
