package cn.stylefeng.guns.modular.suninggift.utils;

import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-02-21 10:10
 */
@Slf4j
public class AliNotifyUtil {

    /**
     * 获取支付宝请求参数
     * @param request
     * @return 返回map
     */
    public static Map<String, String> getRequestData(HttpServletRequest request){
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 验证签名是否合法
     * @param params
     * @param promotionAccountInfo
     * @return
     */
    public static Boolean verifyRequestSign(Map<String, String> params, PromotionAccountInfo promotionAccountInfo){
        boolean verify_result = false;
        try{
            verify_result = AlipaySignature.rsaCheckV1(params, promotionAccountInfo.getPlatformPublicKey(), promotionAccountInfo.getCharset(), promotionAccountInfo.getSignType());
        }catch (Exception e){
            log.error("验签异常",e);
        }

        return verify_result;
    }

    /**
     * 验证签名是否合法
     * @param params
     * @param promotionAccountInfo
     * @return
     */
    public static Boolean verifyTopRequestSign(Map<String, String> params, PromotionAccountInfo promotionAccountInfo){
        boolean verify_result = false;
        try{
            //获取签名与加密方式
            String sign = params.get("sign");
            String sign_method = params.get("sign_method");
//            String app_key = params.get("target_appkey");
//            if("23558957".equals(app_key)){
//                //前期对接用 后面注释掉 cms 2020-0226
//                return true;
//            }
            params.remove("sign");
            //去掉签名重新生成签名比对;
            String mySign = SignUtil.signTopRequest(params,promotionAccountInfo.getPlatformPublicKey(),sign_method);
            if(sign.equals(mySign)){
                verify_result = true;
            }
        }catch (Exception e){
            log.error("验签异常",e);
        }

        return verify_result;
    }


}
