package cn.stylefeng.guns.modular.suninggift.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Slf4j
public class LinoHttpUtil {
	
	private static final String encode = "UTF-8";
	
	private static final String code = "404";
	
	/**
	 * 以JSON的格式POST请求
	 * @param json 需要发送的数据
	 * @param httpUrl    请求地址
	 * @param charset    编码格式
	 * @param timeOut 请求时间响应时间
	 * @return result Map<String, String>结果
	 */
	public static Map<String, String> httpPost(String json, String httpUrl, String charset, int timeOut) {  
		if(StringUtils.isBlank(charset))
			charset = encode;
        Map<String, String> result = new HashMap<String, String>();
        URL url = null;  
        HttpURLConnection conn = null;  
        try {  
            /*得到url地址的URL类*/  
            url = new URL(httpUrl);   
            /*获得打开需要发送的url连接*/  
            if ("https".equals(url.getProtocol())){
            	HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection(); 
                conn = connHttps;
            }else{
            	conn = (HttpURLConnection) url.openConnection(); 
            }
            /*设置连接超时时间[毫秒]*/  
            conn.setConnectTimeout(timeOut);  
            /*设置读取响应超时时间[毫秒]*/  
            conn.setReadTimeout(timeOut);  
            /*设置post发送方式*/  
            conn.setRequestMethod("POST");  
            /*发送commString*/  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            /*设置字符编码*/
            conn.setRequestProperty("Charset", charset);
            /*接收类型设置*/
            conn.setRequestProperty("Accept", "application/json");
            /*发送类型设置*/
            conn.setRequestProperty("Content-Type", "application/json"); //必须在前面
            conn.addRequestProperty("Content-Type", "multipart/form-data");
            OutputStreamWriter out;  
            out = new OutputStreamWriter(conn.getOutputStream(), charset);  
            out.write(json);  
            out.flush();  
            out.close(); 
            
            String encoding = getResponseCharset(conn.getContentType(), charset);
            result.put("code", String.valueOf(conn.getResponseCode()));
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            	result.put("msg", getStreamAsString(conn.getInputStream(), encoding));
            }else {
            	result.put("msg", conn.getResponseMessage());
            }
            
        } catch (Exception e) {  
            /*异常处理*/  
        	result.put("code", code);
        	result.put("msg", e.getMessage());
            e.printStackTrace();
            log.error("错误", e);
        } finally {  
            if (conn != null) {  
                /*关闭URL连接*/  
            	conn.disconnect();  
            }  
        }  
        /*返回响应内容*/  
        return result;  
    }

	/**
	 * 以JSON的格式POST请求
	 * @param json 需要发送的数据
	 * @param httpUrl    请求地址
	 * @param charset    编码格式
	 * @param timeOut 请求时间响应时间
	 * @return result Map<String, String>结果
	 */
	public static Map<String, String> httpPostDb(String json, String httpUrl, String charset, int timeOut) {
        Map<String, String> result = new HashMap<>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(httpUrl);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(json, charSet);
        httpPost.setEntity(entity);
        String resultse = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse != null) {
                HttpEntity resEntity = httpResponse.getEntity();
                if (resEntity != null) {
                    resultse = EntityUtils.toString(resEntity, charset);
                }
            }
            log.info("<返回参数>====>" + resultse);
            result.put("code", "200");
            result.put("msg", resultse);
        } catch (IOException e) {
            e.printStackTrace();
            /*异常处理*/
            result.put("code", code);
            result.put("msg", e.getMessage());
            log.error("错误",e);
        }
        /*返回响应内容*/
        return result;
    }

	/**
	 * 以JSON的格式POST请求
	 * @param json 需要发送的数据
	 * @param httpUrl  请求地址
	 * @param charset  编码格式
	 * @param timeOut  请求时间响应时间
	 * @param property 请求头的公共参数
	 * @return result Map<String, String>结果
	 */
	public static Map<String, String> httpPost(String json, String httpUrl, String charset, int timeOut
			, Map<String, String> property) {  
		if(StringUtils.isBlank(charset))
			charset = encode;
        Map<String, String> result = new HashMap<String, String>();
        URL url = null;  
        HttpURLConnection conn = null;  
        try {  
            /*得到url地址的URL类*/  
            url = new URL(httpUrl);   
            /*获得打开需要发送的url连接*/  
            if ("https".equals(url.getProtocol())){
            	HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection(); 
                conn = connHttps;
            }else{
            	conn = (HttpURLConnection) url.openConnection(); 
            }
            /*设置连接超时时间[毫秒]*/  
            conn.setConnectTimeout(timeOut);  
            /*设置读取响应超时时间[毫秒]*/  
            conn.setReadTimeout(timeOut);  
            /*设置post发送方式*/  
            conn.setRequestMethod("POST");  
            /*发送commString*/  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            /*设置字符编码*/
            conn.setRequestProperty("Charset", charset);
            /*接收类型设置*/
            //conn.setRequestProperty("Accept", "application/json");
            /*发送类型设置*/
            conn.setRequestProperty("Content-Type", "application/json"); //必须在前面
            conn.addRequestProperty("Content-Type", "multipart/form-data");
            for (Entry<String, String> entry : property.entrySet()) {
            	if("Content-Type".equals(entry.getKey()))
            		conn.addRequestProperty(entry.getKey(), entry.getValue());
            	else 
            		conn.setRequestProperty(entry.getKey(), entry.getValue());
            	}
            
            OutputStreamWriter out;  
            out = new OutputStreamWriter(conn.getOutputStream(), charset);  
            out.write(json);  
            out.flush();  
            out.close(); 
            
            String encoding = getResponseCharset(conn.getContentType(), charset);
            result.put("code", String.valueOf(conn.getResponseCode()));
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            	result.put("msg", getStreamAsString(conn.getInputStream(), encoding));
            }else {
            	result.put("msg", conn.getResponseMessage());
            }
        } catch (Exception e) {  
        	/*异常处理*/  
        	result.put("code", code);
        	result.put("msg", e.getMessage());
            e.printStackTrace();
        } finally {  
            if (conn != null) {  
                /*关闭URL连接*/  
            	conn.disconnect();  
            }  
        }  
        /*返回响应内容*/  
        return result;  
    } 
	
	/**
	 * 以MAP的格式POST请求
	 * @param map 需要发送的数据
	 * @param httpUrl  请求地址
	 * @param charset  编码格式
	 * @param timeOut  请求时间响应时间
	 * @return result Map<String, String>结果
	 */
	public static Map<String, String> httpPost(Map<String, String> map, String httpUrl, String charset, int timeOut) {  
		if(StringUtils.isBlank(charset))
			charset = encode;
        Map<String, String> result = new HashMap<String, String>();
        URL url = null;  
        HttpURLConnection conn = null;  
        try {  
            /*得到url地址的URL类*/  
            url = new URL(httpUrl);  
            /*获得打开需要发送的url连接*/  
            if ("https".equals(url.getProtocol())){
            	HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection(); 
                conn = connHttps;
            }else{
            	conn = (HttpURLConnection) url.openConnection(); 
            }
            /*设置连接超时时间[毫秒]*/  
            conn.setConnectTimeout(timeOut);  
            /*设置读取响应超时时间[毫秒]*/  
            conn.setReadTimeout(timeOut);  
            /*设置post发送方式*/  
            conn.setRequestMethod("POST");  
            /*发送commString*/  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            /*设置字符编码*/
            conn.setRequestProperty("Charset", charset);
            /*接收类型设置*/
            //conn.setRequestProperty("Accept", "application/json");
            /*发送类型设置*/
            /*数据转换成一个字串（name1=value1&name2=value2...），
             * 然后把这个字串追加到url后面，用?分割，加载这个新的url*/
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            
            OutputStreamWriter out;  
            out = new OutputStreamWriter(conn.getOutputStream(), charset);  
            out.write(buildQuery(map, charset));  
            out.flush();  
            out.close(); 
            
            String encoding = getResponseCharset(conn.getContentType(), charset);
            result.put("code", String.valueOf(conn.getResponseCode()));
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            	result.put("msg", getStreamAsString(conn.getInputStream(), encoding));
            }else {
            	result.put("msg", conn.getResponseMessage());
            }
        } catch (Exception e) {  
        	/*异常处理*/  
        	result.put("code", code);
        	result.put("msg", e.getMessage());
            e.printStackTrace();
        } finally {  
            if (conn != null) {  
                /*关闭URL连接*/  
            	conn.disconnect();  
            }  
        }  
        /*返回响应内容*/  
        return result;  
    } 
	
	/**
	 * 以FILE的格式PUT请求
	 * @param file 需要发送的文件
	 * @param httpUrl  请求地址
	 * @param charset  编码格式
	 * @param timeOut  请求时间响应时间
	 * @param property 请求头的公共参数
	 * @return result Map<String, String>结果
	 */
	public static Map<String, String> httpPut(File file, String httpUrl, String charset, int timeOut
			, Map<String, String> property) {  
		if(StringUtils.isBlank(charset))
			charset = encode;
        Map<String, String> result = new HashMap<String, String>();
        URL url = null;  
        HttpURLConnection conn = null;  
        try {  
            /*得到url地址的URL类*/  
            url = new URL(httpUrl);  
            /*获得打开需要发送的url连接*/  
            if ("https".equals(url.getProtocol())){
            	HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection(); 
                conn = connHttps;
            }else{
            	conn = (HttpURLConnection) url.openConnection(); 
            }
            /*设置连接超时时间[毫秒]*/  
            conn.setConnectTimeout(timeOut);  
            /*设置读取响应超时时间[毫秒]*/  
            conn.setReadTimeout(timeOut);  
            /*设置post发送方式*/  
            conn.setRequestMethod("PUT");  
            /*发送commString*/  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            /*设置字符编码*/
            conn.setRequestProperty("Charset", charset);
            /*接收类型设置*/
            //conn.setRequestProperty("Accept", "application/json");
            /*发送类型设置*/
            conn.setRequestProperty("Content-Type", "multipart/form-data");
            for (Entry<String, String> entry : property.entrySet()) {
            	if("Content-Type".equals(entry.getKey()))
            		conn.addRequestProperty(entry.getKey(), entry.getValue());
            	else 
            		conn.setRequestProperty(entry.getKey(), entry.getValue());
            	}
            conn.setRequestProperty("Connection", "keep-alive");  //设置连接的状态
            conn.connect(); 
            
			/*获取输出写入流*/
			OutputStream ops = conn.getOutputStream();  
			/*读取文件并写入*/
			FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            byte[] buffer = bos.toByteArray();
            
            ops.write(buffer);
            ops.flush();
            ops.close();
            
            /*文件上传完成*/
            String encoding = getResponseCharset(conn.getContentType(), charset);
            result.put("code", String.valueOf(conn.getResponseCode()));
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            	result.put("msg", getStreamAsString(conn.getInputStream(), encoding));
            }else {
            	result.put("msg", conn.getResponseMessage());
            }
        } catch (Exception e) {  
        	/*异常处理*/  
        	result.put("code", code);
        	result.put("msg", e.getMessage());
            e.printStackTrace();
        } finally {  
            if (conn != null) {  
                /*关闭URL连接*/  
            	conn.disconnect();  
            }  
        }  
        /*返回响应内容*/  
        return result;  
    } 
	
	/**
	 * 以byte[]的格式PUT请求
	 * @param httpUrl  请求地址
	 * @param charset  编码格式
	 * @param timeOut  请求时间响应时间
	 * @param property 请求头的公共参数
	 * @return result Map<String, String>结果
	 */
	public static Map<String, String> httpPut(byte[] buffer, String httpUrl, String charset, int timeOut
			, Map<String, String> property) {  
		if(StringUtils.isBlank(charset))
			charset = encode;
        Map<String, String> result = new HashMap<String, String>();
        URL url = null;  
        HttpURLConnection conn = null;  
        try {  
            /*得到url地址的URL类*/  
            url = new URL(httpUrl);  
            /*获得打开需要发送的url连接*/  
            if ("https".equals(url.getProtocol())){
            	HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection(); 
                conn = connHttps;
            }else{
            	conn = (HttpURLConnection) url.openConnection(); 
            }
            /*设置连接超时时间[毫秒]*/  
            conn.setConnectTimeout(timeOut);  
            /*设置读取响应超时时间[毫秒]*/  
            conn.setReadTimeout(timeOut);  
            /*设置post发送方式*/  
            conn.setRequestMethod("PUT");  
            /*发送commString*/  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            /*设置字符编码*/
            conn.setRequestProperty("Charset", charset);
            /*接收类型设置*/
            //conn.setRequestProperty("Accept", "application/json");
            /*发送类型设置*/
            conn.setRequestProperty("Content-Type", "multipart/form-data");
            for (Entry<String, String> entry : property.entrySet()) {
            	if("Content-Type".equals(entry.getKey()))
            		conn.addRequestProperty(entry.getKey(), entry.getValue());
            	else 
            		conn.setRequestProperty(entry.getKey(), entry.getValue());
            	}
            conn.setRequestProperty("Connection", "keep-alive");  //设置连接的状态
            conn.connect(); 
            
			/*获取输出写入流*/
			OutputStream ops = conn.getOutputStream();  
            ops.write(buffer);
            ops.flush();
            ops.close();
            
            /*文件上传完成*/
            String encoding = getResponseCharset(conn.getContentType(), charset);
            result.put("code", String.valueOf(conn.getResponseCode()));
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            	result.put("msg", getStreamAsString(conn.getInputStream(), encoding));
            }else {
            	result.put("msg", conn.getResponseMessage());
            }
        } catch (Exception e) {  
        	/*异常处理*/  
        	result.put("code", code);
        	result.put("msg", e.getMessage());
            e.printStackTrace();
        } finally {  
            if (conn != null) {  
                /*关闭URL连接*/  
            	conn.disconnect();  
            }  
        }  
        /*返回响应内容*/  
        return result;  
    } 
	
	/**
	 * 字符流转换为字符串
	 * @param stream   需要转换的字符流
	 * @param charset  转换使用的字符集
	 * @return result  转换后字符串内容
	 */
	private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();

            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
	
	/**
	 * 获取使用的字符集
	 * @param contentType 内容类型
	 * @param  charset  默认编码格式
	 * @return charset 获得的字符集
	 */
	private static String getResponseCharset(String contentType, String charset) {
        if (!StringUtils.isEmpty(contentType)) {
            String[] params = contentType.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (!StringUtils.isEmpty(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }
        return charset;
    }
	
	/**
	 * 以Map通过URL形式拼装字符串
	 * @param params   需要拼装的Map
	 * @param charset  编码格式
	 * @param timeOut  请求时间响应时间
	 * @param property 请求头的公共参数
	 * @return String  拼装结果
	 */
	public static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder query = new StringBuilder();
        Set<Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;

        for (Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            // 忽略参数名或参数值为空的参数
            if (StringUtils.isNoneBlank(name, value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }
                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }
        return query.toString();
    }

}
