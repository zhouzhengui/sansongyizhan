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
 * 查询号码列表-选号接口
 */
public class HaokaOpennewNumSelectTest {

  //    private static final String serverUrl = "http://127.0.0.1:8086/haoka/opennew/gateway";
//	private static final String serverUrl = "https://dev.gzlplink.com/haoka/opennew/gateway";
//	private static final String serverUrl = "http://localhost:8992/opennew/gateway";
//  private static final String serverUrl = "https://dsuninggift.gzlplink.com/opennew/gateway";
  //	private static final String serverUrl = "http://localhost:8992/api/gateway/sn";
	private static final String serverUrl = "https://suninggift.gzlplink.com/opennew/gateway";
  // 可替换为您的沙箱环境应用的appKey
  //private static final String appKey = "opennew-suning2020";
  //测试环境
//  private static final String appKey = "20200422";
  //生产环境
    private static final String appKey = "lp20200422";
  // 可替换为您的沙箱环境应用的appSecret
  //private static final String md5Key = "opennew-test";
  //测试环境
//  private static final String md5Key = "a122112jjh1289218129129yyapo1nn81";
  //生产环境
  private static final String md5Key = "Yy112j...!!jh128921@po1nn811009a";
  // 必须替换为沙箱账号授权得到的真实有效sessionKey
  private static final String signMethod = "md5";

  private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static void main(String[] args) throws IOException {

    //拼接请求参数
    Map<String, String> requestParams = new HashMap<String, String>();
    requestParams.put("app_key", appKey);
    requestParams.put("format", "json");
    requestParams.put("method", "haoka.opennew.num.select");
    requestParams.put("sign_method", "md5");
    requestParams.put("target_appkey", appKey);
    requestParams.put("timestamp", df.format(new Date()));

    JSONObject dataObject = new JSONObject();
    dataObject.put("provinceCode", "110000");
    dataObject.put("cityCode", "110100");
    //测试
    dataObject.put("outSysNo", "suningAPPNew001");
    //生产
//		dataObject.put("outSysNo", "suningAPPNew001");
    dataObject.put("goodsId", "2007232925");
//    dataObject.put("goodsId", "2009220305");
    requestParams.put("data", SignUtil.strConvertBase(dataObject.toJSONString()));
    //加签
    String sign = SignUtil.signTopRequest(requestParams, md5Key, signMethod);
    requestParams.put("sign", sign);

    //本地环境
    System.out.println("<请求入参==>" + JSON.toJSONString(requestParams));
    String result = HttpUtil.sendPostBody(serverUrl, JSON.toJSONString(requestParams));

    System.out.println("<请求结果==>" + JSONObject.parseObject(result));

//    System.out.println(JSONObject.toJSONString("Yy112j...!!)jh128921@po1nn811009a"));
  }

}
