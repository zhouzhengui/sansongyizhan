import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestCreateOrder;
import cn.stylefeng.guns.modular.suninggift.utils.HttpUtil;
import cn.stylefeng.guns.modular.suninggift.utils.IdGeneratorSnowflake;
import cn.stylefeng.guns.modular.suninggift.utils.SignUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassNameTestoldUserTest
 * @Description TODO
 * @Author tangxiong
 * @Date 2020/7/3 16:48
 **/
public class TestoldUserTest {
    //公共参数
    String sign_method = "md5";
    String key = "a122112jjh1289218129129yyapo1nn81";

    //可升档套餐查询
    @Test
    public void baseofferGet() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TreeMap<String, String> params = new TreeMap<>();
        //公共参数
        params.put("app_key", "20200422");
        params.put("target_appkey", "20200422");
        params.put("timestamp", sdf.format(new Date()));
        params.put("format", "json");
        params.put("sign_method", "md5");

        //业务参数
        params.put("method", "gift.gzlplink.opentrade.baseoffer.get");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile", "13078857192");
        String jsonStr = SignUtil.strConvertBase(jsonObject.toJSONString());
        params.put("data", jsonStr);
        String mySign = null;
        try {
            mySign = SignUtil.signTopRequest(params, key, sign_method);
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("sign", mySign);
        System.out.println("请求参数：" + JSONObject.toJSONString(params));
        System.out.println("请求参数：" + params);
    }

    //发送短信验证码
    @Test
    public void identifyDealSendCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TreeMap<String, String> params = new TreeMap<>();
        //公共参数
        params.put("app_key", "20200422");
        params.put("target_appkey", "20200422");
        params.put("timestamp", sdf.format(new Date()));
        params.put("format", "json");
        params.put("sign_method", "md5");
        params.put("method", "gift.gzlplink.opentrade.identify.deal");
        //业务参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile", "13268271901");
        jsonObject.put("biz_type", "SEND_VERIFICATION_CODE");
        String jsonStr = SignUtil.strConvertBase(jsonObject.toJSONString());
        params.put("data", jsonStr);
        String mySign = null;
        try {
            mySign = SignUtil.signTopRequest(params, key, sign_method);
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("sign", mySign);
        System.out.println("请求参数：" + JSONObject.toJSONString(params));
        System.out.println("请求参数：" + params);
    }

    //订单创建
    @Test
    public void orderCreate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TreeMap<String, String> params = new TreeMap<>();
        //公共参数
        params.put("app_key", "20200422");
        params.put("target_appkey", "20200422");
        params.put("timestamp", sdf.format(new Date()));
        params.put("format", "json");
        params.put("sign_method", "md5");
        JSONObject jsonObject = new JSONObject();

        //业务参数
        String outTradeNo = IdGeneratorSnowflake.generateId();
        params.put("method", "gift.gzlplink.opentrade.createorder");
        jsonObject.put("contract_id", IdGeneratorSnowflake.generateId());
        jsonObject.put("name", "张三");
        jsonObject.put("operator", "中国联通");
        jsonObject.put("user_id", "2088702731485603");
        jsonObject.put("phone", "17682308882");
        jsonObject.put("product_id", "22010000603037");//22010000603037
        jsonObject.put("status", "1");
        jsonObject.put("total_fee", "72000");
        jsonObject.put("out_trade_no", outTradeNo);
        jsonObject.put("transfer_id", IdGeneratorSnowflake.generateId());
        jsonObject.put("auth_no", IdGeneratorSnowflake.generateId());
        jsonObject.put("operation_id", IdGeneratorSnowflake.generateId());
        jsonObject.put("out_order_no", outTradeNo);
        jsonObject.put("out_request_no", IdGeneratorSnowflake.generateId());
        String mySign = null;
        String jsonStr = SignUtil.strConvertBase(jsonObject.toJSONString());
        params.put("data", jsonStr);

        try {
            mySign = SignUtil.signTopRequest(params, key, sign_method);
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("sign", mySign);
        System.out.println("请求参数：" + JSONObject.toJSONString(params));
        System.out.println("请求参数：" + params);

    }

    /**
     * @Author
     * @Description //苏宁履约信息通知
     * @Date
     * @Param ${param}
     * @return ${return}
     **/
    @Test
    public void orderKeepresult() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TreeMap<String, String> params = new TreeMap<>();
        //公共参数
        params.put("app_key", "20200422");
        params.put("target_appkey", "20200422");
        params.put("timestamp", sdf.format(new Date()));
        params.put("format", "json");
        params.put("sign_method", "md5");

        //业务参数
      /*        RESULTCODE_0000("0000","用户正常缴费单期履约解冻"),
                RESULTCODE_1111("1111","用户缴费异常单期扣罚转支付"),
                RESULTCODE_9999("9999","用户毁约剩余全额扣罚转支付"),*/
        /*2020081200322232510587112
        2020081117033490024519477
        2020081116112476327259733
        2020081013041308040042873
        2020081011465556733464996
        2020080919020363117003575
        2020080713215116994448568
        2020080607304722151217883
        2020080100453057704499099*/
        params.put("method", "gift.gzlplink.opentrade.order.keepresult");
       // JSONObject jsonObject = new JSONObject();
        params.put("result_desc", "用户缴费异常单期扣罚转支付");//结果描述
        params.put("bill_month", "-1");
        params.put("bill_money", "all");
        params.put("out_trade_no", "2020081719190542900652792");
        params.put("result_code", "1111");
        /*String jsonStr = SignUtil.strConvertBase(JSONObject.toJSONString(params));
        params.put("data", jsonStr);*/
        String mySign = null;
        try {
            mySign = SignUtil.signTopRequest(params, key, sign_method);
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("sign", mySign);
        System.out.println("请求参数：" + JSONObject.toJSONString(params));
        System.out.println("请求参数：" + params);

        //String url = "http://mtoappre.cnsuning.com/mtoap/getJsmResultExpose";//测试
        String url = "http://mtoappre.cnsuning.com/mtoap/getGzlpPerformanceResult";//测试
        //String url = "http://toap.suning.com/mtoap/getGzlpPerformanceResult";//生产
        //Map<String, String> head = new HashMap<>();
        //head.put("Content-Type", "application/x-www-form-urlencoded");
        //head.put("Accept", "application/json");
        System.out.println("苏宁履约通知请求参数:" + JSONObject.toJSONString(params));
        //String result = HttpUtil.sendPost(url, head, JSONObject.toJSONString(params), "utf-8");
         String result = HttpUtil.hostPost(JSONObject.toJSONString(params), url, "utf-8", 60*1000);
        System.out.println("苏宁履约通知响应" + result);
    }

    /**
     * @Author
     * @Description //苏宁订购信息通知
     * @Date
     * @Param ${param}
     * @return ${return}
     **/
    @Test
    public void suningNotify() throws Exception {
        String suningNotifyUrl = "http://mtoappre.cnsuning.com/mtoap/getGzlpShiftUpOrderNotify";//测试
        //String suningNotifyUrl = "http://toap.suning.com/mtoap/getGzlpShiftUpOrderNotify";//生产
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TreeMap<String, String> params = new TreeMap<>();
        //公共参数
        params.put("app_key", "20200422");
        params.put("target_appkey", "20200422");
        params.put("timestamp", sdf.format(new Date()));
        params.put("format", "json");
        params.put("sign_method", "md5");
        //params.put("method", "gift.gzlplink.opentrade.order.status.notify");

        //业务参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result_code", "0000");
        jsonObject.put("method", "gift.gzlplink.opentrade.order.status.notify");
        jsonObject.put("desc", ResponseStatusEnum.SUCCESS.getMsg());
        jsonObject.put("out_trade_no", "2020081917104457609251900");
        String jsonStr = SignUtil.strConvertBase(jsonObject.toJSONString());
        params.put("data", jsonStr);
        String mySign = null;
        try {
            mySign = SignUtil.signTopRequest(params, key, sign_method);
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("sign", mySign);
        System.out.println("请求参数：" + JSONObject.toJSONString(params));
        System.out.println("请求参数：" + params);
        ///String resutl = HttpUtil.sendPostBody(suningNotifyUrl, JSONObject.toJSONString(params));

        String resutl = HttpUtil.hostPost(JSONObject.toJSONString(params), suningNotifyUrl, "utf-8", 60 * 1000);
      /*  Calendar cal = Calendar.getInstance();
        int curryMonth = cal.get(Calendar.MONTH);
        int curryMonth1 = cal.get(Calendar.MONTH) + 1;
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM");*/
        System.out.println("响应参数：" + resutl);
        System.out.println("响应参数curryMonth1：" + resutl);
       // System.out.println(sdf1.format(new Date()));
    }

    public static void main(String[] args) {
        String str = "2020081916353917243736140";
        String str2 = str.substring(str.length()-20, 20);
        String str3 = str.substring(str.length()-20+1, 20);
        String str1 = "20081916353917243736140";
        System.out.println(str2);
        System.out.println(str2.length());
        System.out.println(str3);
        System.out.println(str3.length());
    }

}