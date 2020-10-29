import cn.hutool.core.util.RandomUtil;
import cn.stylefeng.guns.modular.suninggift.utils.DateUtil;
import cn.stylefeng.guns.modular.suninggift.utils.HttpUtil;
import cn.stylefeng.guns.modular.suninggift.utils.SignUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 新入网订单信息保存入库（预下单接口）
 */
public class HaokaOpennewConserveNewUnicomOrderTest {

  //private static final String serverUrl = "http://127.0.0.1:8086/haoka/opennew/gateway";
//  private static final String serverUrl = "http://localhost:8992/opennew/gateway";
//  private static final String serverUrl = "https://suninggift.gzlplink.com/opennew/gateway";
      private static final String serverUrl = "http://dsuninggift.gzlplink.com/opennew/gateway";
//  private static final String serverUrl = "https://dev.gzlplink.com/haoka/opennew/gateway";
  // 可替换为您的沙箱环境应用的appKey
  //private static final String appKey = "opennew-suning2020";
  //测试环境
  private static final String appKey = "20200422";
  //生产环境
//  private static final String appKey = "lp20200422";
  // 可替换为您的沙箱环境应用的appSecret
  //private static final String md5Key = "opennew-test";
  //测试环境
  private static final String md5Key = "a122112jjh1289218129129yyapo1nn81";
  //生产环境
//  private static final String md5Key = "Yy112j...!!jh128921@po1nn811009a";
  // 必须替换为沙箱账号授权得到的真实有效sessionKey
  private static final String signMethod = "md5";

  private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static void main(String[] args) throws IOException {
    //拼接请求参数
    Map<String, String> requestParams = new HashMap<String, String>();
    requestParams.put("app_key", appKey);
    requestParams.put("format", "json");
    requestParams.put("method", "haoka.opennew.conserve.new.unicom.order");
    requestParams.put("sign_method", "md5");
    requestParams.put("target_appkey", appKey);
    requestParams.put("timestamp", df.format(new Date()));

    JSONObject dataObject = new JSONObject();
    dataObject.put("contractPhone", "17600973908");
    dataObject.put("certNo", "44080319910721032X");
    dataObject.put("prokey", "9999920200927164611");
//    dataObject.put("policyNo", "PPID202007093864565597");
//    dataObject.put("orderDetailsPsnId", "PSID202007095487807381");
    dataObject.put("policyNo", "PPID202007015891736227");
    dataObject.put("orderDetailsPsnId", "PSID202007016275766480");
    dataObject.put("firstMonthId", "A000011V000003");
    dataObject.put("firstMonthName", "按量计费");
    dataObject.put("custName", "周珍贵");
    dataObject.put("custType", "1");
    dataObject.put("phoneBelong", "110000,110100");
    dataObject.put("freezePrice", "28.80");
    dataObject.put("postAddress", "北京北京市东城区测试专用地址");
    dataObject.put("postCustName", "蔡静茹");
    dataObject.put("postPhone", "15913390850");
    dataObject.put("postAreaCode", "110000,110100,110101");
    dataObject.put("certCheckCode", "0000");
    dataObject.put("certCheckMsg", "实名认证成功");
    //String outTradeNo = DateUtil.commontime + RandomUtil.randomNumbers(8);
    String outTradeNo = "2020092716474356291786";
    dataObject.put("outOrderNo", outTradeNo);
    dataObject.put("outRequestNo", outTradeNo);

    System.out.println("data=" + dataObject.toJSONString());
    requestParams.put("data", SignUtil.strConvertBase(dataObject.toJSONString()));
    //加签
    String sign = SignUtil.signTopRequest(requestParams, md5Key, signMethod);
    requestParams.put("sign", sign);

    //本地环境
    System.out.println("<请求入参==>" + JSON.toJSONString(requestParams));
    String result = HttpUtil.sendPostBody(serverUrl, JSON.toJSONString(requestParams));

    System.out.println("<请求结果==>" + JSONObject.parseObject(result));
  }

}
