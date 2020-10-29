package cn.stylefeng.guns.modular.suninggift.controller.api;

import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.DeployBizRequestVo;
import cn.stylefeng.guns.modular.suninggift.model.constant.DeployInterfInfo;
import cn.stylefeng.guns.modular.suninggift.service.InInterfaceService;
import cn.stylefeng.guns.modular.suninggift.utils.CommonUtil;
import cn.stylefeng.guns.modular.suninggift.utils.MD5Util;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 调拨系统回调地址
 *
 * @author zba
 * @Date 2020-02-27 12:38:15
 */
@Slf4j
@Controller
@RequestMapping("/api/debit")
public class DebitController extends BaseController {

    @Autowired
    private InInterfaceService inInterfaceService;

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 调拨系统回调地址，处理订单流程状态：1、调拨放款成功，2、调拨放款失败
     */
    @RequestMapping(value = "/callBack")
    public void callBack(HttpServletRequest request, HttpServletResponse response) {
        try {
            String jsonBody = CommonUtil.parseRequest(request);
            log.info("资金挑拨网商银行异步通知入参:" + jsonBody);
            DeployBizRequestVo deployBizRequestVo = JSON.parseObject(jsonBody, DeployBizRequestVo.class);
            response.setCharacterEncoding("UTF-8");
            PrintWriter pw = response.getWriter();
            DeployInterfInfo deployInterfInfo = JSONObject.parseObject(sysConfigService.getByCode("deployInfo"), DeployInterfInfo.class);
            String sign = MD5Util.encodeByMd5(deployInterfInfo.getInnerAuthSecretKey() + deployBizRequestVo.getTimestamp());
            if(sign.equals(deployBizRequestVo.getSign())) {
                //处理具体的方法
                String s = inInterfaceService.deployNotifyDispose(deployBizRequestVo);
                if(StringUtils.isNoneBlank(s)) {
                    Map<String, Object> responseMap = new HashMap<String, Object>();
                    responseMap.put("code", 20001);
                    responseMap.put("msg", "业务处理失败");
                    pw.write(JSON.toJSONString(responseMap));
                    pw.flush();
                    pw.close();
                    return;
                }
            }else {
                Map<String, Object> responseMap = new HashMap<String, Object>();
                responseMap.put("code", 20002);
                responseMap.put("msg", "验签不通过");
                pw.write(JSON.toJSONString(responseMap));
                pw.flush();
                pw.close();
                return;
            }
            Map<String, Object> responseMap = new HashMap<String, Object>();
            responseMap.put("code", 10000);
            responseMap.put("msg", "SUCCESS");
            pw.write(JSON.toJSONString(responseMap));
            pw.flush();
            pw.close();
        } catch (Exception e) {
            log.error("资金挑拨网商银行异步通知异常" , e);
        }
    }

}
