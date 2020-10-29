package cn.stylefeng.guns.modular.suninggift.controller.api;

import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo.OutApiResponse;
import cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo.QimenInterResponse;
import cn.stylefeng.guns.modular.suninggift.service.GetawayService;
import cn.stylefeng.guns.modular.suninggift.utils.AliNotifyUtil;
import cn.stylefeng.guns.modular.suninggift.utils.CommonUtil;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 乐芃重置送网关
 *
 * @author cms
 * @Date 2020/2/17 23:39
 */
@Slf4j
@Controller
@RequestMapping("/api/gateway")
public class GetawayController extends BaseController {

    @Autowired
    private GetawayService getawayService;

    @ResponseBody
    @RequestMapping(value = "", produces = {"application/json;charset=UTF-8"})
    public QimenInterResponse index(HttpServletRequest request, HttpServletResponse response) {
        return rounte(request, response, null);
    }

    @ResponseBody
    @RequestMapping(value = "sn", produces = {"application/json;charset=UTF-8"})
    public QimenInterResponse sn(HttpServletRequest request, HttpServletResponse response) {
        return rounte(request, response, null);
    }

    @ResponseBody
    @RequestMapping(value = "/jd", produces = {"application/json;charset=UTF-8"})
    public QimenInterResponse jd(Object o) {
        return rounte(null, null, o);
    }

    @ResponseBody
    @RequestMapping(value = "/own", produces = {"application/json;charset=UTF-8"})
    public QimenInterResponse own(HttpServletRequest request, HttpServletResponse response) {
        return rounte(request, response, null);
    }

    private QimenInterResponse rounte(HttpServletRequest request, HttpServletResponse response, Object o){
        long startTime = System.currentTimeMillis();
        OutApiResponse outApiResponse = null;
        QimenInterResponse qimenInterResponse = new QimenInterResponse();
        //获取请求参数
        Map<String, String> requestData = null;
      /*  if(null != o){
            requestData = (Map) o;
        }else{
            requestData = AliNotifyUtil.getRequestData(request);
        }*/
        // 获取请求参数
        String jsonBody = "";
        try {
            if ("GET".equals(request.getMethod())) {
                jsonBody = new String(request.getQueryString().getBytes("iso-8859-1"), "utf-8").replaceAll("%22", "\"");
            } else {
                jsonBody = CommonUtil.parseRequest(request);
            }
            requestData = JSONObject.parseObject(jsonBody, Map.class);
        } catch (Exception e) {
            log.info(this.getClass() + "接口访问错误:" , e);
            qimenInterResponse.setResult(new OutApiResponse(false, ResponseStatusEnum.EXCEPTION.getCode(),ResponseStatusEnum.EXCEPTION.getMsg()));
            return qimenInterResponse;
        }

        log.info("奇门网关接收参数===>" + JSONObject.toJSONString(requestData));
        if(requestData.size() == 0){
            log.info("请求参数非空");
            qimenInterResponse.setResult(new OutApiResponse(false, ResponseStatusEnum.CONTROLLER_ERROR_REQUESTPARAM.getCode(),ResponseStatusEnum.CONTROLLER_ERROR_REQUESTPARAM.getMsg()));
            return qimenInterResponse;
        }

        //调用service处理支付宝通知参数
        outApiResponse = getawayService.getawayDispose(requestData);

        qimenInterResponse.setResult(outApiResponse);
        log.info("奇门网关响应参数===>" + JSONObject.toJSONString(outApiResponse));
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("奇门网关耗时 {} 毫秒",(endTime - startTime));
        return qimenInterResponse;
    }
}

