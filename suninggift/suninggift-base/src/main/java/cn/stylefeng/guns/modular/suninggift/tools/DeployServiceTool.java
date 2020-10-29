package cn.stylefeng.guns.modular.suninggift.tools;

import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.utils.CommonUtil;
import cn.stylefeng.guns.modular.suninggift.utils.DateUtil;
import cn.stylefeng.guns.modular.suninggift.utils.HttpClientUtil;
import cn.stylefeng.guns.modular.suninggift.utils.MD5Util;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @ClassNameDeployServiceTool
 * @Description TODO
 * @Author tangxiong
 * @Date 2020/7/22 16:48
 **/
@Slf4j
@Service
public class DeployServiceTool {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 调用调拨系统进行对应订单的解冻/转支付操作
     *
     * @param modelObject
     * @return
     */
    public MessagerVo cardOrderPerformance(JSONObject modelObject) {
        MessagerVo messagerVo = new MessagerVo();
        String sendPost = null;

        //拼接请求参数
        //JSONObject modelObject = new JSONObject();
        //乐芃订单编码
        modelObject.put("outOrderNo", "");
        //解冻操作指令时,直接进行调拨系统的解冻操作
        BigDecimal restAmount = new BigDecimal("0.00");
        modelObject.put("unfrzType", "");
        modelObject.put("amount", restAmount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        // 调用调拨系统进行对应订单的解冻/转支付操作
        String outOrderNo = modelObject.getString("outOrderNo");
        try {
            String method = "/orderUmicom/unfrzBill";

            String methodPath = sysConfigService.getByCode("base_service_url") + "fund_deploy/orderUmicom/unfrzBill";
            //String methodPath = "http://192.168.2.93:8085" + "/fund_deploy/orderUmicom/unfrzBill";
            sendPost = execute(method, methodPath, modelObject);

            log.info("订单号[" + outOrderNo + "],<调用调拨系统进行对应订单的解冻/转支付操作响应参数>" + sendPost);
        } catch (Exception e) {
            e.printStackTrace();
            // 订单发券异常,保存到通知表中,进行重新推送
            messagerVo.setSubCode("30009");
            messagerVo.setSubMsg("订单号[" + outOrderNo + "]调用调拨系统进行对应订单的解冻/转支付操作异常");
            return messagerVo;
        }

        if (CommonUtil.isEmpty(sendPost)) {
            // 订单发券异常,保存到通知表中,进行重新推送
            messagerVo.setSubCode("30010");
            messagerVo.setSubMsg("订单号[" + outOrderNo + "]调用调拨系统进行对应订单的解冻/转支付操作异常");
            return messagerVo;
        }

        // 处理响应结果
        JSONObject parseObject = JSONObject.parseObject(sendPost);
        String code = parseObject.getString("code");
        String subCode = parseObject.getString("subCode");
        String subMsg = parseObject.getString("subMsg");
        if ("10000".equals(code) && "10001".equals(subCode)) {
            messagerVo.setSubCode("10000");
            messagerVo.setSubMsg("订单号[" + outOrderNo + "]:" + subMsg);
            JSONObject result = parseObject.getJSONObject("result");
            messagerVo.setData(result);
        } else {
            messagerVo.setSubCode("30011");
            messagerVo.setSubMsg("订单号[" + outOrderNo + "]:" + subMsg);
        }

        return messagerVo;

    }

    /**
     * 公共发送请求的方法
     *
     * @param method
     * @param methodPath
     * @param modelObject
     * @return
     */
    public String execute(String method, String methodPath, Map<String, Object> modelObject) {

        // 组装Json串
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("version", "");
        hashMap.put("method", method);
        hashMap.put("signType", "");
        hashMap.put("timestamp", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));

        String md5SignContent = getMD5SignContent(modelObject);
        // 加签
        String sign = MD5Util.encodeByMd5(md5SignContent + getInnerAuthSecret() + hashMap.get("timestamp"));
        hashMap.put("sign", sign);
        hashMap.put("model", modelObject);

        String str = JSONObject.toJSONString(hashMap);

        log.info("================================  测试情况 =========================================");
        log.info("<原加签内容>====>" + md5SignContent);
        log.info("<报文发送url>====>" + methodPath);
        log.info("<报文发送参数>====>" + str);

        String charset = "utf-8";
        try {
            return HttpClientUtil.doPostIgnoreCert(methodPath, str, charset);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("<请求调拨系统异常> ,{}", e);
        }

        return null;
    }

    /**
     * 拼接MD5加密内容
     */
    private String getMD5SignContent(Map<String, Object> map) {
        String json = "";
        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.putAll(map);

        if (map != null) {
            json += JSONObject.toJSONString(treeMap);
        }

        return json;
    }

    // 获取加密密钥
    private String getInnerAuthSecret() {
        return sysConfigService.getByCode("tmall_coupon_md5_key");
    }
}
