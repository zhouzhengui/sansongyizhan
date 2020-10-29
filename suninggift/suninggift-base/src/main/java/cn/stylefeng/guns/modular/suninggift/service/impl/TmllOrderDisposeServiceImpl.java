package cn.stylefeng.guns.modular.suninggift.service.impl;


import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.entity.WopayFtp;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.WsnOrderInfoVo;
import cn.stylefeng.guns.modular.suninggift.service.OrderInfoService;
import cn.stylefeng.guns.modular.suninggift.service.TmllOrderOprationLogService;
import cn.stylefeng.guns.modular.suninggift.entity.TmllOrderOprationLog;
import cn.stylefeng.guns.modular.suninggift.model.params.OrderDeployParam;
import cn.stylefeng.guns.modular.suninggift.model.params.OrderInfoParam;
import cn.stylefeng.guns.modular.suninggift.model.result.OrderInfoResult;
import cn.stylefeng.guns.modular.suninggift.service.TmllOrderDisposeService;
import cn.stylefeng.guns.modular.suninggift.utils.*;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * @ClassNameTmllOrderDisposeServiceImpl
 * @Description TODO天猫履约、毁约订单pot,调拨系统订单信息更新处理
 * @Author tangxiong
 * @Date 2020/2/27 14:38
 **/
@Service
@Slf4j
@Component
public class TmllOrderDisposeServiceImpl implements TmllOrderDisposeService {
    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private TmllOrderOprationLogService tmllOrderOprationLogService;

    @Autowired
    private ChatbotSendUtil chatbotSendUtil;

    private static final String inner_auth_secret_key = "fHKck@59c_vmn";

    //pot订单信息更新
    @Async
    @Override
    public void tmallOrderCenterDispose(OrderInfo orderInfo) {
        log.info("pot订单信息更新入参：{}", JSONObject.toJSONString(orderInfo));
        //组装接口传参信息
        String requestJson = "";


        log.info("pot订单信息更新请求参数：{}", requestJson);
        //获取请求pot订单更新接口url
        try {
            String url = sysConfigService.getByCode("potUrl");
            String responseStr = "";
            responseStr = HttpUtil.hostPost(requestJson, url, "utf-8", 3 * 60 * 1000);
            log.info("pot订单信息更新响应参数：{}", responseStr);
            if ("404".equals(responseStr)) {
                log.info("调用pot订单接口出错：{}", orderInfo.getOutTradeNo());
            }
            //如果更新成功
            log.info("pot订单信息更新成功：{}", orderInfo.getOutTradeNo());

        } catch (Exception e) {
            log.error("调用pot订单接口异常：{}", e);
        }
    }


    //调拨系统订单信息更新
    @Async
    @Override
    public void tmallOrderDeployDispose(OrderDeployParam orderDeployParam) {
        log.info("更新调拨系统订单信息入参：{}", JSONObject.toJSONString(orderDeployParam));
        //组装接口传参信息
        String dingMsg = "";
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> hashMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(sysConfigService.getByCode("deployInfo"));
        String fundDeployUrl = jsonObject.getString("url");
        String version = jsonObject.getString("version");
        String signType = jsonObject.getString("signType");
        String innerAuthSecretKey = jsonObject.getString("innerAuthSecretKey");

        hashMap.put("version", version);
        hashMap.put("method", "/orderUmicom/unfrzStatus");
        hashMap.put("signType", signType);
        hashMap.put("timestamp", DateUtil.getNowDate());
        fundDeployUrl = fundDeployUrl + "/orderUmicom/unfrzStatus";
        paramMap.put("outOrderNo", orderDeployParam.getOutOrderNo());
        paramMap.put("month", orderDeployParam.getMonth());
        paramMap.put("unfrzType", orderDeployParam.getUnfrzType());
        try {
            String joinStr = MD5Util.getMD5SignContent(paramMap);
            log.info("调拨系统订单更新加签信息：{}", joinStr);
            String str = joinStr + innerAuthSecretKey + hashMap.get("timestamp");//加签字符串
            String signStr = MD5Util.encodeByMd5(str);
            hashMap.put("sign", signStr);
            hashMap.put("model", paramMap);
            //获取请求pot订单更新接口url
            ///String url = sysConfigService.getByCode("deployUrl");
            String requestJson = JSONObject.toJSONString(hashMap);
            String responseStr = "";
            log.info("调拨系统订单信息更新请求参数：{}", hashMap);
            responseStr = HttpUtil.hostPost(requestJson, fundDeployUrl, "utf-8", 3 * 60 * 1000);
            if (StringUtils.isEmpty(responseStr) || "404".equals(responseStr)) {
                Map<String, Object> map = new HashMap<>();
                map.put("code", "404");
                map.put("msg", "调拨系统请求失败");
                responseStr = JSONObject.toJSONString(map);
            }

            log.info("调拨系统订单信息更新响应参数：{}", responseStr, ";订单号:{}", orderDeployParam.getOutOrderNo());
            JSONObject json = JSONObject.parseObject(responseStr);
            //完成之后执行操作日志如入库
            TmllOrderOprationLog tmllOrderOprationLog = new TmllOrderOprationLog();
            tmllOrderOprationLog.setId(orderDeployParam.getOprationId());
            tmllOrderOprationLog.setUpdateDate(new Date());
            tmllOrderOprationLog.setDeployStatus(json.getString("code"));
            tmllOrderOprationLog.setDeployRequest(requestJson);
            //判断长度，防止返回的信息过多，无法入库
            tmllOrderOprationLog.setDeployResponse(json.getString("msg"));
            tmllOrderOprationLogService.updateById(tmllOrderOprationLog);

            if ("404".equals(responseStr)) {
                dingMsg = "调拨冻结转支付调用失败:" + 404;
                chatbotSendUtil.sendMsg(dingMsg);
                log.info("调拨系统解冻转支付接口出错");
            } else {
                if ("10000".equals(json.getString("code"))) {
                    log.info("调拨系统解冻转支付通知成功：{}", orderDeployParam.getOutOrderNo());
                } else {
                    dingMsg = "调拨冻结转支付调用失败:" + responseStr;
                    chatbotSendUtil.sendMsg(dingMsg);
                    log.info("调拨系统解冻转支付失败：{}", orderDeployParam.getOutOrderNo());
                }
            }
        } catch (Exception e) {
            dingMsg = "调拨冻结转支付调用失败:" + e.getMessage();
            chatbotSendUtil.sendMsg(dingMsg);
            log.error("调用调拨系统解冻转支付接口异常：{}", e);
        }
    }

    //调拨系统订单信息更新结清
    @Async
    @Override
    public void tmallOrderDeploySettleRefundDispose(OrderDeployParam orderDeployParam) {
        log.info("更新调拨系统结清订单信息入参：{}", JSONObject.toJSONString(orderDeployParam));
        //组装接口传参信息
        String dingMsg = "";
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> hashMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(sysConfigService.getByCode("deployInfo"));
        String fundDeployUrl = jsonObject.getString("url");
        String version = jsonObject.getString("version");
        String signType = jsonObject.getString("signType");
        String innerAuthSecretKey = jsonObject.getString("innerAuthSecretKey");

        hashMap.put("version", version);
        hashMap.put("method", "/orderRefund/settleRefund");
        hashMap.put("signType", signType);
        hashMap.put("timestamp", DateUtil.getNowDate());
        fundDeployUrl = fundDeployUrl + "/orderRefund/settleRefund";
        paramMap.put("outOrderNo", orderDeployParam.getOutOrderNo());
        try {
            String joinStr = MD5Util.getMD5SignContent(paramMap);
            log.info("调拨系统订单结清更新加签信息：{}", joinStr);
            String str = joinStr + innerAuthSecretKey + hashMap.get("timestamp");//加签字符串
            String signStr = MD5Util.encodeByMd5(str);
            hashMap.put("sign", signStr);
            hashMap.put("model", paramMap);
            //获取请求pot订单更新接口url
            ///String url = sysConfigService.getByCode("deployUrl");
            String requestJson = JSONObject.toJSONString(hashMap);
            String responseStr = "";
            log.info("调拨系统订单结清信息更新请求参数：{}", hashMap);
            responseStr = HttpUtil.hostPost(requestJson, fundDeployUrl, "utf-8", 3 * 60 * 1000);
            log.info("调拨系统订单结清信息更新响应参数：{}", responseStr, ";订单号:{}", orderDeployParam.getOutOrderNo());
            if (StringUtils.isEmpty(responseStr) || "404".equals(responseStr)) {
                Map<String, Object> map = new HashMap<>();
                map.put("code", "404");
                map.put("msg", "调拨系统请求失败");
                responseStr = JSONObject.toJSONString(map);
            }

            JSONObject json = JSONObject.parseObject(responseStr);

            //完成之后执行操作日志如入库
            TmllOrderOprationLog tmllOrderOprationLog = new TmllOrderOprationLog();
            tmllOrderOprationLog.setId(orderDeployParam.getOprationId());
            tmllOrderOprationLog.setUpdateDate(new Date());
            tmllOrderOprationLog.setDeployStatus(json.getString("code"));
            tmllOrderOprationLog.setDeployRequest(requestJson);
            tmllOrderOprationLog.setDeployResponse(json.getString("msg"));
            tmllOrderOprationLogService.updateById(tmllOrderOprationLog);
            if ("404".equals(responseStr)) {
                dingMsg = "调拨系统结清接口调用失败:" + 404;
                chatbotSendUtil.sendMsg(dingMsg);
                log.info("调拨系统结清接口出错");
            } else {
                if ("10000".equals(json.getString("code"))) {
                    log.info("调拨系统结清通知成功：{}", orderDeployParam.getOutOrderNo());
                } else {
                    dingMsg = "调拨系统结清失败:" + responseStr;
                    chatbotSendUtil.sendMsg(dingMsg);
                    log.info("调拨系统结清失败：{}", orderDeployParam.getOutOrderNo());
                }
            }
        } catch (Exception e) {
            dingMsg = "调拨系统结清失败:" + e.getMessage();
            chatbotSendUtil.sendMsg(dingMsg);
            log.error("调用调拨系统结清接口异常：{}", e);
        }
    }

    @Override
    public LayuiPageInfo tmallQueryOrderList(Map<String, Object> paramMap) {
        //Map<String, Object> resultMap = new HashMap<>();
        log.info("pot订单查询入参：{}", JSONObject.toJSONString(paramMap));
        try {
            String outTradeNo = (String) paramMap.get("outTradeNo");
            String phone = (String) paramMap.get("phone");
            String status = (String) paramMap.get("status");
            String startTime = (String) paramMap.get("startTime");
            String endTime = (String) paramMap.get("endTime");
            int limit = (int) (paramMap.get("limit") == null ? "10" : paramMap.get("limit"));
            int page = (int) (paramMap.get("page") == null ? "1" : paramMap.get("page"));
            OrderInfoParam orderInfoParam = new OrderInfoParam();
            if (!(StringUtils.isEmpty(outTradeNo) || "null".equals(outTradeNo))) {
                orderInfoParam.setOutTradeNo(outTradeNo);
            }

            if (!(StringUtils.isEmpty(phone) || "null".equals(phone))) {
                orderInfoParam.setPhone(phone);
            }

            if (!StringUtils.isEmpty(status)) {
                orderInfoParam.setStatus(Integer.valueOf(status));
            }

            if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
                orderInfoParam.setStartTime(startTime);
                orderInfoParam.setEndTime(endTime);
            }
            orderInfoParam.setLimit(limit);
            orderInfoParam.setPage(page);
            LayuiPageInfo pageInfo = orderInfoService.findPageBySpec(orderInfoParam);
            return pageInfo;
        } catch (Exception e) {
            log.error("调用pot订单接口异常：{}", e);
        }
        return null;
    }

    @Override
    public List<OrderInfoResult> tmallQueryOrderListExport(Map<String, Object> paramMap) {
        log.info("pot订单查询入参：{}", JSONObject.toJSONString(paramMap));
        try {
            String outTradeNo = (String) paramMap.get("outTradeNo");
            String phone = (String) paramMap.get("phone");
            String status = (String) paramMap.get("status");
            String startTime = (String) paramMap.get("startTime");
            String endTime = (String) paramMap.get("endTime");
            OrderInfoParam orderInfoParam = new OrderInfoParam();
            if (!(StringUtils.isEmpty(outTradeNo) || "null".equals(outTradeNo))) {
                orderInfoParam.setOutTradeNo(outTradeNo);
            }

            if (!(StringUtils.isEmpty(phone) || "null".equals(phone))) {
                orderInfoParam.setPhone(phone);
            }

            if (!StringUtils.isEmpty(status)) {
                orderInfoParam.setStatus(Integer.valueOf(status));
            }

            if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
                orderInfoParam.setStartTime(startTime);
                orderInfoParam.setEndTime(endTime);
            }

            List<OrderInfoResult> orderInfoResultList = orderInfoService.queryOrderListExport(orderInfoParam);
            return orderInfoResultList;
        } catch (Exception e) {
            log.error("调用pot订单接口异常：{}", e);
        }
        return null;
    }

    /**
     * 调拨解冻、转支付
     *
     * @param method
     * @param methodPath
     * @param modelObject
     * @return
     */
    public String freezeOrPay(String method, String methodPath, Map<String, Object> modelObject) {
        Map<String, Object> map = new HashMap<>();
        //组装Json串
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("version", "1.0");
        hashMap.put("method", method);
        hashMap.put("signType", "md5");
        hashMap.put("timestamp", DateUtil.getNowDate());

        String url = "https://dev.gzlplink.com/fund_deploy/";
        String md5SignContent = MD5Util.getMD5SignContent(modelObject);
        //加签
        String sign = MD5Util.encodeByMd5(md5SignContent + inner_auth_secret_key + hashMap.get("timestamp"));
        hashMap.put("sign", sign);
        hashMap.put("model", modelObject);

        String str = JSON.toJSONString(hashMap);
        HttpClient httpClient = new DefaultHttpClient();

        System.out.println("================================  测试情况 =========================================");
        System.out.println("<原加签内容>====>" + md5SignContent);
        System.out.println("<sign>====>" + sign);
        System.out.println("<报文发送url>====>" + url + methodPath);
        System.out.println("<报文发送参数>====>" + str);

        HttpPost httpPost = new HttpPost(url + methodPath);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(str, charSet);
        httpPost.setEntity(entity);
        String resultse = null;
        String charset = "utf-8";

        try {

            HttpResponse httpResponse = httpClient.execute(httpPost);


//                HttpResponse httpResponse = null;
            if (httpResponse != null) {
                HttpEntity resEntity = httpResponse.getEntity();
                if (resEntity != null) {
                    resultse = EntityUtils.toString(resEntity, charset);
                }
            }

            System.out.println("<返回参数>====>" + resultse);
            JSONObject jsonObject = JSONObject.parseObject(resultse);

            map.put("code", jsonObject.get("code"));
            map.put("msg", jsonObject.get("msg"));
            map.put("subCode", jsonObject.get("subCode"));
            map.put("subMsg", jsonObject.get("subMsg"));

            return resultse;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param method
     * @param methodPath
     * @param modelObject
     * @return
     */
    public String setllRefund(String method, String methodPath, Map<String, Object> modelObject) {
        Map<String, Object> map = new HashMap<>();
        //组装Json串
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("version", "2.0");
        hashMap.put("method", method);
        hashMap.put("signType", "md5");
        hashMap.put("timestamp", DateUtil.getNowDate());

        String url = "https://www.gzlplink.com/fund_deploy/";
        String md5SignContent = MD5Util.getMD5SignContent(modelObject);
        //加签
        String sign = MD5Util.encodeByMd5(md5SignContent + inner_auth_secret_key + hashMap.get("timestamp"));
        hashMap.put("sign", sign);
        hashMap.put("model", modelObject);

        String str = JSON.toJSONString(hashMap);
        HttpClient httpClient = new DefaultHttpClient();

        System.out.println("================================  测试情况 =========================================");
        System.out.println("<原加签内容>====>" + md5SignContent);
        System.out.println("<sign>====>" + sign);
        System.out.println("<报文发送url>====>" + url + methodPath);
        System.out.println("<报文发送参数>====>" + str);

        HttpPost httpPost = new HttpPost(url + methodPath);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(str, charSet);
        httpPost.setEntity(entity);
        String resultse = null;
        String charset = "utf-8";

        try {

            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse != null) {
                HttpEntity resEntity = httpResponse.getEntity();
                if (resEntity != null) {
                    resultse = EntityUtils.toString(resEntity, charset);
                }
            }

            System.out.println("<返回参数>====>" + resultse);
            JSONObject jsonObject = JSONObject.parseObject(resultse);

            map.put("code", jsonObject.get("code"));
            map.put("msg", jsonObject.get("msg"));
            map.put("subCode", jsonObject.get("subCode"));
            map.put("subMsg", jsonObject.get("subMsg"));
            return resultse;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @return ${return}
     * @Author
     * @Description //通知系统订单采集
     * @Date
     * @Param ${param}
     **/
    @Override
    public Map<String, Object> queryOrderListInfo(Map<String, Object> paramMap) {
        Map<String, Object> resultMap = new HashMap<>();
        log.info("订单采集入参：{}", paramMap);
        try {
            String startTime = (String) paramMap.get("startTime");
            String endTime = (String) paramMap.get("endTime");
            OrderInfoParam orderInfoParam = new OrderInfoParam();
            if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
                orderInfoParam.setStartTime(startTime);
                orderInfoParam.setEndTime(endTime);
            }
            List<OrderInfo> orderInfoList = null;
            try {
                orderInfoList = orderInfoService.queryOrderListInfo(orderInfoParam);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (orderInfoList != null && orderInfoList.size() > 0) {
                resultMap.put("code", 10000);
                resultMap.put("msg", "查询成功");
                resultMap.put("orderInfoList", orderInfoList);
            } else {
                resultMap.put("code", 30000);
                resultMap.put("msg", "查无数据");
            }
            return resultMap;
        } catch (Exception e) {
            log.error("订单采集入参：", e);
            resultMap.put("code", 40000);
            resultMap.put("msg", "系统异常，请联系管理员");
        }
        return resultMap;

    }

    @Async
    @Override
    public String orderSyncWsnForftp(WopayFtp wopayFtp) {
        log.info("开始推送ftp账单信息到wsn系统");
        if (wopayFtp == null) {
            log.info("账单信息到wsn系统（参数无效）");
            return null;
        }

        //获取系统参数
        String desKey = sysConfigService.getByCode("systemDesKey");
        String wsnFtpUrl = sysConfigService.getByCode("wsnFtpUrl");

        JSONObject jsonObject = new JSONObject();
        String data = JSONObject.toJSONString(wopayFtp);
        String encryData = DESUtil.encryptDES(data, desKey);
        jsonObject.put("token", encryData);
        jsonObject.put("bizContent", data);

        try {
            log.info("wsnFtpUrl：{}", wsnFtpUrl);
            log.info("推送ftp账单信息到wsn系统请求参数：{}", jsonObject.toJSONString());
            String result = HttpUtil.hostPost(jsonObject.toJSONString(), wsnFtpUrl, "utf-8", 3 * 60 * 1000);
            log.info("推送ftp账单信息到wsn系统响应参数：{}", JSONObject.toJSONString(result));

            if (StringUtils.isEmpty(result) || "404".equals(result)) {
                log.info("推送ftp账单信息到wsn系统失败：{}", result);
                chatbotSendUtil.sendMsg("推送ftp账单信息到wsn系统失败====" + jsonObject.toJSONString());
                return result;
            }
            JSONObject resultJson = JSONObject.parseObject(result);
            String code = resultJson.getString("code");
            if ("10000".equals(code)) {
                log.info("推送ftp账单信息到wsn系统成功：{}", wopayFtp.getTradeNo());
                return result;
            } else {
                log.info("推送ftp账单信息到wsn系统失败：{}", result + ">>>>" + wopayFtp.getTradeNo());
                chatbotSendUtil.sendMsg("推送ftp账单信息到wsn系统失败====" + result + "<<<<<<<<" + jsonObject.toJSONString());
                return "推送ftp账单信息到wsn系统失败：{}";
            }
        } catch (Exception e) {
            log.error("推送ftp账单信息到wsn系统异常：{}", e);
            chatbotSendUtil.sendMsg("推送ftp账单信息到wsn系统异常====" + "<<<<<<<<" + jsonObject.toJSONString());
        }
        return null;
    }

    @Async
    @Override
    public String orderSyncWsnForOrderInfo(WsnOrderInfoVo wsnOrderInfoVo) {
        log.info("开始推送订单信息到wsn系统");
        if (wsnOrderInfoVo == null) {
            log.info("推送订单信息到wsn系统（参数为空）");
            return "推送订单信息到wsn系统（参数为空）";
        }

        //获取系统参数
        //String desKey = sysConfigService.getByCode("systemDesKey");
        String wsnOrderGatherCollectUrl = sysConfigService.getByCode("wsnOrderGatherCollectUrl");
        //String url  = "https://dwsn.gzlplink.com/orderGather/orderGatherCollect";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = sdf.format(new Date());
        String method = "order.gather.collect";
        String version = "1.0";
        List<WsnOrderInfoVo> orderInfoList = new ArrayList<WsnOrderInfoVo>();
        orderInfoList.add(wsnOrderInfoVo);
        String token =  DesUtils.encryptHex(time);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", time);
        jsonObject.put("method", method);
        jsonObject.put("version", version);
        jsonObject.put("token", token);
        jsonObject.put("orderInfoList", orderInfoList);

        try {
            log.info("wsnOrderGatherCollectUrl：{}", wsnOrderGatherCollectUrl);
            log.info("推送订单信息到wsn系统请求参数," + JSONObject.toJSONString(jsonObject));
            String result = HttpUtil.hostPost(jsonObject.toJSONString(), wsnOrderGatherCollectUrl, "utf-8", 60 * 1000);
            log.info("推送订单信息到wsn系统响应参数," + JSONObject.toJSONString(result));

            if (StringUtils.isEmpty(result) || "404".equals(result)) {
                log.info("推送订单信息到wsn系统失败：{}", result);
                chatbotSendUtil.sendMsg("推送订单信息到wsn系统失败====" + jsonObject.toJSONString());
                return result;
            }
            JSONObject resultJson = JSONObject.parseObject(result);
            String code = resultJson.getString("code");
            if ("10000".equals(code)) {
                log.info("推送订单信息到wsn系统成功：{}", wsnOrderInfoVo.getOutTradeNo());
                return result;
            } else {
                log.info("推送订单信息到wsn系统失败：{}", result + ">>>>" + wsnOrderInfoVo.getOutTradeNo());
                chatbotSendUtil.sendMsg("推送订单信息到wsn系统失败====" + result + "<<<<<<<<" + jsonObject.toJSONString());
                return "推送订单信息到wsn系统失败：{}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("推送订单信息到wsn系统异常：{}", e);
            chatbotSendUtil.sendMsg("推送订单信息到wsn系统异常====" + "<<<<<<<<" + jsonObject.toJSONString());
        }

        return null;
    }

}
