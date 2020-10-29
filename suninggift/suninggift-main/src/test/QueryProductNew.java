import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.ApiInBizRequest;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ApiInBizResponse;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.PolicyListResponseBizContent;
import cn.stylefeng.guns.modular.suninggift.utils.DESUtil;
import cn.stylefeng.guns.modular.suninggift.utils.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.Date;
import java.util.TreeMap;


/**
 * @author Xie JianBin
 * @date  2019年5月11日 
 */
public class QueryProductNew {
	//政策查询
    public static void main(String[] args) {

        String key = "kfejow7wef27we5f"; //

        TreeMap<String, String> bizContent = new TreeMap<String, String>();
        bizContent.put("outPortNo", "20200225");//政策外部产品编码

        ApiInBizRequest inBizRequest = new ApiInBizRequest();
        inBizRequest.setFlowNo(DateUtil.format(new Date(), "yyyyMMddHHmmss")+ RandomUtil.randomNumbers(10));
        inBizRequest.setTime(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        inBizRequest.setMethod("query.policyInfo");
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
            String url = "https://dev.gzlplink.com/fmapi-equity/gzlp/unicom/payment";
			result = HttpClientUtil.doPostIgnoreCert(url,
					JSON.toJSONString(inBizRequest), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("<请求结果=================>" + result);

        ApiInBizResponse<PolicyListResponseBizContent> policyListResponseBizContentApiInBizResponse = JSONObject.parseObject(result, new TypeReference<ApiInBizResponse<PolicyListResponseBizContent>>() {
        });
        System.out.println(JSONObject.toJSONString(policyListResponseBizContentApiInBizResponse));

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
