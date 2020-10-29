import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.ApiInBizRequest;
import cn.stylefeng.guns.modular.suninggift.utils.DESUtil;
import cn.stylefeng.guns.modular.suninggift.utils.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.TreeMap;

public abstract class QueryProductPolicyDetailTest {
	
	//查询政策明细
    public static void main(String[] args) {
        String key = "kfejow7wef27we5f"; //

        TreeMap<String, String> bizContent = new TreeMap<String, String>();
        bizContent.put("policyNo", "PPID202003096261238546");//政策编码
        bizContent.put("psnId", "PSID202003094101119625");//0级子政策编码

        ApiInBizRequest inBizRequest = new ApiInBizRequest();
        inBizRequest.setFlowNo(DateUtil.format(new Date(), "yyyyMMddHHmmss")+ RandomUtil.randomNumbers(10));
        inBizRequest.setTime(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        inBizRequest.setMethod("pcredit.query.product.policy.detail");
        String jsonString = JSONObject.toJSONString(bizContent);
        inBizRequest.setBizContent(jsonString);
        System.out.println("请求前报文===》"+jsonString);
        String sign = null;
        try {
//            sign =Md5Encrypt.Md5Encrypt(inBizRequest.getFlowNo()+inBizRequest.getMethod()+inBizRequest.getTime(), key);
            sign = DESUtil.encryptDES(inBizRequest.getFlowNo()+inBizRequest.getMethod()+inBizRequest.getTime(), key);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("签名错误=============");
        }
        System.out.println("返回的密文："+ JSONObject.toJSONString(sign));
        inBizRequest.setSign(sign);
        System.out.println(JSONObject.toJSON(inBizRequest));
        System.out.println("请求报文===" + JSON.toJSONString(inBizRequest));
        // post请求
		String result = null;
		try {
			String url = "http://dev.gzlplink.com/fmapi-equity/gzlp/unicom/payment";
//			String url = "https://www.gzlplink.com/fmapi-equity/gzlp/unicom/payment";
//			String url = "http://127.0.0.1:8086/fmapi-equity/gzlp/unicom/payment";
			result = HttpClientUtil.doPostIgnoreCert(url,JSON.toJSONString(inBizRequest), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<请求结果=================>" + result);
		//响应验签
		JSONObject json = JSONObject.parseObject(result);
		String signStr = json.getString("sign");
		String flowNo = json.getString("flowNo");
		String time = json.getString("time");
		String sourceData = flowNo+time;
		String decryptDES = DESUtil.decryptDES(signStr, key);
        if(!decryptDES.equals(sourceData)) {
        	System.out.println("验签不过");
        }else{
        	System.out.println("验签通过");
        }
    }

}
