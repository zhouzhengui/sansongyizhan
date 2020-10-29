import cn.stylefeng.guns.modular.suninggift.utils.DesUtils;
import cn.stylefeng.guns.modular.suninggift.utils.IdGeneratorSnowflake;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

public class OrderKeeperTest {

    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = "";
        //String url = "http://localhost:8996/suninggift/tmall/order/keepresult";
       // String url = "https://dsuninggift.gzlplink.com/tmall/cmcc/order/keepresult";
        //String url = "https://suninggift.gzlplink.com/tmall/cmcc/order/keepresult";
        String url = "https://suninggift.gzlplink.com/tmall/order/keepresult";
        //String url = "http://localhost:8996/tmall/cmcc/order/keepresult";
        // String url = "http://localhost:8996/tmall/order/keepresult";
        JSONObject jsonObject = new JSONObject();
        //JSONObject biz_content = new JSONObject();//
        jsonObject.put("charset", "UTF-8");
        jsonObject.put("format", "JSON");
        jsonObject.put("method", "order.keepresult");
        jsonObject.put("version", "1.0");

        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("outTradeNo", IdGeneratorSnowflake.generateId());//必填、请求流水号
        paramMap.put("outOrderNo", "");//非必填、订单号(cb订单号、业务系统的校验依据[存在cb订单号不一致的情况])
        paramMap.put("orderNo", "2020081118095436807809452");//必填、订单号(乐芃订单号cdis_cont_order主键)
        paramMap.put("unfrzType", "UNFREEZE");//UNFREEZE-解冻、PAY-转支付
        paramMap.put("authConfirmMode", "NOT_COMPLETE");//非必填、在转支付类型的必填:1.COMPLETE-支付宝自动解冻剩余金额、2.NOT_COMPLETE-接入系统自行调用接口解冻
        paramMap.put("month", "7");//必填、期数：1.实际期数、2.若全部则填ALL(建议忽略大小写判断)
        paramMap.put("foundAmount", "39.00");//必填、金额：1.实际金额、2.期数为ALL的时候剩余金额一定为-1
        paramMap.put("phoneNo", "18552391916");//必填、手机号码(业务系统的校验依据)
        String data = JSONObject.toJSONString(paramMap);
        String encryData = DesUtils.encryptHex(data);
        jsonObject.put("token", encryData);
        jsonObject.put("biz_content", data);
        String jsonString = JSONObject.toJSONString(jsonObject);
        System.out.println("请求参数：" + jsonString);

        httpClient = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(50000).setConnectTimeout(50000).build();
        httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        httpPost.setEntity(new StringEntity(jsonString, Charset.forName("UTF-8")));
        httpPost.setHeader("token", "token");
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setHeader("Accept", "application/json");
        long startDate = System. ();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity httpEntity = response.getEntity();
        result = EntityUtils.toString(httpEntity, "UTF-8");
        JSONObject json = JSONObject.parseObject(result);
        long endDat = System.currentTimeMillis();
        System.out.println("请求耗时：" + (endDat - startDate));
        System.out.println("响应参数：" + json);

    }

}
