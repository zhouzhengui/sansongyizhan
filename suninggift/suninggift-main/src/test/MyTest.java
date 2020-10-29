import com.alibaba.fastjson.JSONObject;
import com.jd.tc.tsap.adapter.dto.HttpResult;
import com.jd.tc.tsap.adapter.exporter.dto.ApiResult;
import com.jd.tc.tsap.adapter.utils.JDHttpClient;
import com.jd.tc.tsap.adapter.utils.OKHttpClientUitl;
import org.springframework.jdbc.support.incrementer.HsqlMaxValueIncrementer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-04-22 16:49
 */
public class MyTest {

    public static void main(String[] args) {
        String url = "https://dsuninggift.gzlplink.com/api/gateway";
        url = "http://localhost:8996/api/gateway/jd?a=1";
//        url = "http://localhost:8080/cmstest/aa?a=1";
        Map<String, String> header = new HashMap();
        Map<String, Object> body = new HashMap();
        header.put("Token", "LszXn06P3f9HUqAOZ2YhW1Yr5FK3qezq");
        header.put("appName", "tsapadapter");
        header.put("systemName", "tsap");
        header.put("source", "jdos-release");
        String body2 = "{\"orderId\":\"1\", \"orderNum\":\"1\", \"orderStatus\":\"13005\", \"orderStatusDesc\":\"成功\", \"backTime\":\"20190930123030\"}";
//        HttpResult re = JDHttpClient.sendGet(url, header);
//        HttpResult re = JDHttpClient.sendGetWithCA(url, header,"");
//        HttpResult re = JDHttpClient.sendPost(url, body, header);
//        HttpResult re = JDHttpClient(url, body, header);
//        HttpResult re = OKHttpClientUitl.sendPost(url, body2, header);
//        HttpResult re = OKHttpClientUitl.sendGet(url, body2);
//        HttpResult re = OKHttpClientUitl.sendPost(url, body, header);
        HttpResult re = JDHttpClient.sendGetIgnoreCa(url, header);
        String s = JSONObject.toJSONString(re);
//        s = unicodeToString(s);
        System.out.println(s);

    }

    /**
     * Unicode转 汉字字符串
     *
     * @param str \u6728
     * @return '木' 26408
     */
    public static String unicodeToString(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            //group 6728
            String group = matcher.group(2);
            //ch:'木' 26408
            ch = (char) Integer.parseInt(group, 16);
            //group1 \u6728
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;
    }


}
