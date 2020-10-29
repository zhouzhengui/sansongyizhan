import cn.stylefeng.guns.modular.suninggift.utils.HttpUtil;
import cn.stylefeng.guns.modular.suninggift.utils.SignUtil;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

public class TestSuningOrderKeepresult {

  public static void main(String[] args) {

    //公共参数
    String sign_method = "md5";
    String key = "a122112jjh1289218129129yyapo1nn81";

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
    params.put("result_desc", "用户正常缴费单期履约解冻");//结果描述
    params.put("bill_month", "1");
    params.put("bill_money", "39.00");
    params.put("out_trade_no", "2020073023342693312960776");
    params.put("result_code", "0000");
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
//        String url = "http://mtoappre.cnsuning.com/mtoap/getGzlpPerformanceResult";//测试
    String url = "http://toap.suning.com/mtoap/getGzlpPerformanceResult";//生产
    //Map<String, String> head = new HashMap<>();
    //head.put("Content-Type", "application/x-www-form-urlencoded");
    //head.put("Accept", "application/json");
    System.out.println("苏宁履约通知请求参数:" + JSONObject.toJSONString(params));
    //String result = HttpUtil.sendPost(url, head, JSONObject.toJSONString(params), "utf-8");
    String result = HttpUtil.hostPost(JSONObject.toJSONString(params), url, "utf-8", 60*1000);
    System.out.println("苏宁履约通知响应" + result);
  }

}
