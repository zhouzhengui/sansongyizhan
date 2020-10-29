package cn.stylefeng.guns.modular.suninggift.utils;

import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.ReqVo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.RspVo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @ClassName: 中移和包加解密加解签
 * @Description: 
 * @author lv_hl
 * @date 2015年11月29日下午7:43:03
 *
 */
public class CmpayUtil {

	/**
     * 服务商请求中移和包
     * @param clientId  和包贷分配给合作方的授权ID
     * @param clientKey 和包贷分配给合作方的授权KEY
     * @param clientUserId 合作方用户ID(唯一)
     * @param reqJson   请求和包贷的JSON字符串
     * @return
     * @throws Exception
     */
	public static String encryptCmReqData(String clientId, String clientKey,
			String clientUserId, String reqJson) throws Exception {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap = getJosn(reqJson);
        String codes = requestDataSort(paramMap);
        String sign = SHA_1(codes);
        String reqData = reqJson + sign;
		String encryptValue = XXTEAUtil.encryptWithBase64(
				reqData.getBytes("UTF-8"), clientKey.getBytes("UTF-8"));
        ReqVo reqVo = new ReqVo();
        reqVo.setReqData(encryptValue);
        reqVo.setClientId(clientId);
        reqData = JSONObject.toJSONString(reqVo);
    	return reqData;
    }
	
	/**
     * 中移和包请求服务商
     * @param clientId  和包贷分配给合作方的授权ID
     * @param clientKey 和包贷分配给合作方的授权KEY
     * @param reqJson   请求和包贷的JSON字符串
     * @return
     * @throws Exception
     */
	public static String encryptAgReqData(String clientId, String clientKey, String reqJson) throws Exception {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap = getJosn(reqJson);
        String codes = requestDataSort(paramMap);
        codes = URLEncoder.encode(codes, "UTF-8");  // 中移和包请求需要encode
        String sign = SHA_1(codes);
        String reqData = reqJson + sign;
		String encryptValue = XXTEAUtil.encryptWithBase64(
				reqData.getBytes("UTF-8"), clientKey.getBytes("UTF-8"));
        ReqVo reqVo = new ReqVo();
        reqVo.setReqData(encryptValue);
        reqVo.setClientId(clientId);
        reqData = JSONObject.toJSONString(reqVo);
    	return reqData;
    }
	
	/**
     * 服务商响应中移和包
     * @param clientId  和包贷分配给合作方的授权ID
     * @param clientKey 和包贷分配给合作方的授权KEY
     * @param clientUserId 合作方用户ID(唯一)
     * @param respJson  响应和包贷的JSON字符串
     * @return
     * @throws Exception
     */
	public static String encryptCmRespData(String clientId, String clientKey,
			String clientUserId, String respJson) throws Exception {

		respJson = URLEncoder.encode(respJson, "UTF-8");  // 响应中移和包需要encode
		String encryptValue = XXTEAUtil.encryptWithBase64(
				respJson.getBytes("UTF-8"), clientKey.getBytes("UTF-8"));
        RspVo rsqVo = new RspVo();
        rsqVo.setRspData(encryptValue);
        //cmpayReqVo.setClientId(clientId);
        String respData = JSONObject.toJSONString(rsqVo);
    	return respData;
    }
	
	/**
     * 中移和包响应服务商
     * @param clientId  和包贷分配给合作方的授权ID
     * @param clientKey 和包贷分配给合作方的授权KEY
     * @param clientUserId 合作方用户ID(唯一)
     * @param respJson  响应和包贷的JSON字符串
     * @return
     * @throws Exception
     */
	public static String encryptAgRespData(String clientId, String clientKey,
			String clientUserId, String respJson) throws Exception {

		String encryptValue = XXTEAUtil.encryptWithBase64(
				respJson.getBytes("UTF-8"), clientKey.getBytes("UTF-8"));
		RspVo rsqVo = new RspVo();
		rsqVo.setRspData(encryptValue);
        //cmpayReqVo.setClientId(clientId);
        String respData = JSONObject.toJSONString(rsqVo);
    	return respData;
    }
	
	/**
     * 将中移请求报文解密-验签返回JSON字符串
     * @param clientKey 和包贷分配给合作方的授权KEY
     * @param reqData   和包贷的请求密文字符串
     * @return
     * @throws Exception
     */
	public static String decryptCmReqData(String clientKey, String reqData) throws Exception {

		/*1.通过TEA解密获取明文*/
		byte[] decryptBytes = XXTEAUtil.decryptWithBase64(reqData,
				clientKey.getBytes("UTF-8"), "UTF-8");
		String decryptValue = new String(decryptBytes, "UTF-8");
		decryptValue = URLDecoder.decode(decryptValue, "UTF-8"); // 解析中移和包需要decode
		//System.out.println("解密后请求报文===" + decryptValue);
		/*2.报文转化为JSON字符串*/
		if(StringUtils.isBlank(decryptValue) || decryptValue.length() < 40)
			return null;
		String reqJson = decryptValue.substring(0, decryptValue.lastIndexOf("}") + 1);
		//System.out.println("解密后JSON报文===" + respJson);
		String reqSign = decryptValue.substring(decryptValue.lastIndexOf("}") + 1, decryptValue.length());
		/*3.执行签名验证确定来源*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap = getJosn(reqJson);
        String codes = requestDataSort(paramMap);
        codes = URLEncoder.encode(codes, "UTF-8");  // 中移和包请求需要encode
        String sign = SHA_1(codes);
        if(sign.equals(reqSign)){
        	return reqJson;
        }else {
			return null;
		}
    }
	
	/**
     * 将服务商响应报文解密-验签返回JSON字符串
     * @param clientKey 和包贷分配给合作方的授权KEY
     * @param respData  和包贷的响应密文字符串
     * @return
     * @throws Exception
     */
	public static String decryptAgRespData(String clientKey, String respData) throws Exception {

		/*1.通过TEA解密获取明文*/
		byte[] decryptBytes = XXTEAUtil.decryptWithBase64(respData,
				clientKey.getBytes("UTF-8"), "UTF-8");
		String decryptValue = new String(decryptBytes, "UTF-8");
		decryptValue = URLDecoder.decode(decryptValue, "UTF-8");   // 服务商响应中移和包的数据
		//System.out.println("解密后请求报文===" + decryptValue);
		/*2.报文转化为JSON字符串*/
		if(StringUtils.isBlank(decryptValue))
			return null;
		String respJson = decryptValue.substring(0, decryptValue.lastIndexOf("}") + 1);
		//System.out.println("解密后JSON报文===" + respJson);
        if(StringUtils.isNoneBlank(respJson)){
        	return respJson;
        }else {
			return null;
		}
    }
	
	/**
     * 将中移响应报文解密-验签返回JSON字符串
     * @param clientKey 和包贷分配给合作方的授权KEY
     * @param respData  和包贷的响应密文字符串
     * @return
     * @throws Exception
     */
	public static String decryptCmRespData(String clientKey, String respData) throws Exception {

		/*1.通过TEA解密获取明文*/
		byte[] decryptBytes = XXTEAUtil.decryptWithBase64(respData,
				clientKey.getBytes("UTF-8"), "UTF-8");
		String decryptValue = new String(decryptBytes, "UTF-8");
		//System.out.println("解密后请求报文===" + decryptValue);
		/*2.报文转化为JSON字符串*/
		if(StringUtils.isBlank(decryptValue))
			return null;
		String respJson = decryptValue.substring(0, decryptValue.lastIndexOf("}") + 1);
		//System.out.println("解密后JSON报文===" + respJson);
        if(StringUtils.isNoneBlank(respJson)){
        	return respJson;
        }else {
			return null;
		}
    }
	
	/**
     * 将JSON字符串解析-签名-加密-封装请求[RSA签名]
     * @param signType   签名类型[RSA/RSA2]
     * @param publicKey 签名私钥
     * @param charset    签名字符集
     * @param sign       签名字符串
     * @param jsonData   待验的JSON字符串
     * @return RSA签名结果
     * @throws Exception
     */
	public static boolean signRsaReqData(String signType, String publicKey,
			String charset, String sign, String jsonData) throws Exception {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap = getJosn(jsonData);
        String codes = requestDataSort(paramMap);
        Boolean checkBoolean = RSAUtil.checkSign(codes, sign, signType, publicKey, charset);
		return checkBoolean;
    }
	
	/**
     * 将JSON字符串解析-签名-加密-封装请求[RSA签名]
     * @param signType   签名类型[RSA/RSA2]
     * @param privateKey 签名私钥
     * @param charset    签名字符集
     * @param jsonData   待验的JSON字符串
     * @return RSA签名结果
     * @throws Exception
     */
	public static String signRsaReqData(String signType, String privateKey,
			String charset, String jsonData) throws Exception {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap = getJosn(jsonData);
        String codes = requestDataSort(paramMap);
        codes = RSAUtil.sign(codes, signType, privateKey, charset);
    	return codes;
    }
		 
	/**
     * 将HASHMAP拼装为中移和包固定格式字符串
     * @param map  JSON转化而成的MAP
     * @return
     * @throws Exception
     */
	private static String requestDataSort(Map<String, Object> map)
			throws Exception {

		String[] keyArray = (String[]) map.keySet().toArray(new String[0]);
		Arrays.sort(keyArray);
		StringBuilder stringBuilder = new StringBuilder();
		String[] arrayOfString;
		int j = (arrayOfString = keyArray).length;
		for (int i = 0; i < j; i++) {
			String key = arrayOfString[i];
			String mapValue = map.get(key).toString();
			if (mapValue.startsWith("[{") && mapValue.endsWith("}]")) {
				mapValue = mapValue.substring(1, mapValue.length());
				mapValue = mapValue.substring(0, mapValue.length() - 1);
				if (StringUtils.isBlank(mapValue)) {
					stringBuilder.append(key).append("{}");
				} else {
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap = convertStringToMap(mapValue);
					String requestData = requestDataSort(tempMap);
					stringBuilder.append(key).append("{").append(requestData).append("}");
				}
			} else {
				stringBuilder.append(key).append(mapValue);
			}
		}
		return stringBuilder.toString();
	}

	/**
     * 将MAP字符串重新封装为HASHMAP
     * @param request MAP的字符串
     * @return
     * @throws Exception
     */
	public static Map<String, Object> convertStringToMap(String request) {
		if (request.startsWith("{")) {
			request = request.substring(1, request.length());
		}
		if (request.endsWith("}")) {
			request = request.substring(0, request.length() - 1);
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String[] out = request.split(",");
		for (String anOut : out) {
			String key = anOut.substring(0, anOut.indexOf("="));
			String value = anOut.substring(anOut.indexOf("=") + 1,
					anOut.length());
			paramMap.put(key.trim(), value.trim());
		}
		return paramMap;
	}
	
    /**
     * 将字符串执行SHA1摘要生成固定长度40的字符串
     * @param decript
     * @return
     * @throws Exception 
     */
    public static String SHA_1(String decript) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes("UTF-8"));
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将单个json数组字符串解析放在map中
     * @param jsonStr
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getJosn(String jsonStr) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(jsonStr)) {
			JSONObject json = JSONObject.parseObject(jsonStr);
			for (String key : json.keySet()) {
				if (key.equals("baseInfo")) {
					JSONObject val = json.getJSONObject(key);
					for (String valKey : val.keySet()) {
						String value = val.getString(valKey);
						if (value.indexOf("{") == 0) {
							map.put(valKey.trim(), getJosn(value));
						} else if (value.indexOf("[") == 0) {
							map.put(valKey.trim(), getList(value));
						} else {
							map.put(valKey.trim(), value.trim());
						}

					}
				} else {
					String value = json.getString(key);
					if (value.indexOf("{") == 0) {
						map.put(key.trim(), getJosn(value));
					} else if (value.indexOf("[") == 0) {
						map.put(key.trim(), getList(value));
					} else {
						map.put(key.trim(), value.trim());
					}
				}
			}
		}
		return map;
	}

    /**
     * 将单个json数组字符串解析放在list中
     * @param jsonStr
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> getList(String jsonStr) throws Exception {
    	
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        for (Object object : jsonArray) {
        	String json = JSONObject.toJSONString(object);
        	if (json.indexOf("{") == 0) {
                Map<String, Object> map = getJosn(json);
                list.add(map);
            }
		}
        return list;
    }

}
