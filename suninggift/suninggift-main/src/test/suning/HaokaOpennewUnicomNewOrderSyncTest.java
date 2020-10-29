package suning;

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
 * @ClassName: HaokaOpennewUnicomNewOrderSyncTest
 * @Author: yjh
 * @Description: 订单同步(新)
 * @Date: 2020/9/18 15:06
 * @Version: 1.0
 */
public class HaokaOpennewUnicomNewOrderSyncTest {

//    private static final String serverUrl = "http://localhost:8992/opennew/gateway";
private static final String serverUrl = "https://suninggift.gzlplink.com/opennew/gateway";
    //测试环境
//    private static final String appKey = "20200422";
    //生产环境
  private static final String appKey = "lp20200422";
    //测试环境
//    private static final String md5Key = "a122112jjh1289218129129yyapo1nn81";
    //生产环境
    private static final String md5Key = "Yy112j...!!jh128921@po1nn811009a";
    //必须替换为沙箱账号授权得到的真实有效sessionKey
    private static final String signMethod = "md5";
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws IOException {
        //拼接请求参数
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("app_key", appKey);
        requestParams.put("format", "json");
        requestParams.put("method", "haoka.opennew.unicom.order.sync");
        requestParams.put("sign_method", "md5");
        requestParams.put("target_appkey", appKey);
        requestParams.put("timestamp", df.format(new Date()));

        JSONObject dataObject = new JSONObject();
        dataObject.put("outOrderNo", "2020101301253457789440264");
        dataObject.put("userId", "2088602327527617");
        dataObject.put("authNo", "2020101310002001610580000764");
        dataObject.put("paymentType", "1");
        dataObject.put("operationId", "20201013281779766105");
        dataObject.put("orderSuccessTime", "2020-09-27 16:56:28");
        dataObject.put("financeChannelCode","LPSN0002");
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
