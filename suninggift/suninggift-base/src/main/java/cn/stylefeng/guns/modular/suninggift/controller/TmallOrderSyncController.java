package cn.stylefeng.guns.modular.suninggift.controller;

import cn.stylefeng.guns.modular.suninggift.service.TmllOrderDisposeService;
import cn.stylefeng.guns.modular.suninggift.utils.DesUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassNameTmallOrderSyncController
 * @Description TODO
 * @Author tangxiong
 * @Date 2020/5/26 17:48
 **/
@Slf4j
@RestController
@RequestMapping("ordersync/")
public class TmallOrderSyncController {

    @Autowired
    private TmllOrderDisposeService tmllOrderDisposeService;

    @ApiOperation(value = "通知系统订单采集", notes = "通知系统订单采集")
    @PostMapping("queryOrderList")
    public Map<String, Object> queryOrderList(@RequestBody Map<String, Object> paramMap) {
        Map<String, Object> resutMap = new HashMap<>();
        log.info("通知系统订单采集入参：" + JSONObject.toJSONString(paramMap));
        String bizContent = (String) paramMap.get("bizContent");
        String token = (String) paramMap.get("token");
        if (StringUtils.isEmpty(bizContent) || StringUtils.isEmpty(token)) {
            resutMap.put("code", 20000);
            resutMap.put("msg", "参数无效");
            return resutMap;
        }
        String sign = DesUtils.encryptHex(bizContent);
        if (!token.equals(sign)) {
            resutMap.put("code", 20000);
            resutMap.put("msg", "验签失败");
            return resutMap;
        }
        Map<String, Object> map = JSONObject.parseObject(bizContent, Map.class);
        resutMap = tmllOrderDisposeService.queryOrderListInfo(map);
        return resutMap;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        JSONObject json = new JSONObject();
        json.put("startTime", "2020-05-01 00:00:00");
        json.put("endTime", "2020-05-01 10:00:00");
        String token = DesUtils.encryptHex(json.toJSONString());
        map.put("bizContent", json.toJSONString());
        map.put("token", token);
        System.out.println(JSONObject.toJSONString(map));
    }

}
