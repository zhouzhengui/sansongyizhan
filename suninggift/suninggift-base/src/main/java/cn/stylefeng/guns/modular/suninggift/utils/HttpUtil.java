package cn.stylefeng.guns.modular.suninggift.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * HTTP操作工具
 * @author Hanley
 *
 */
@Slf4j
public class HttpUtil {
	

	/**
	 * 获取请求参数
	 * @param req HttpServletRequest请求
	 * @return Map<String, String>
	 */
	public static Map<String, String> getParams(HttpServletRequest req) {
		Map<String, String> temp = new HashMap<String, String>();
		
		Enumeration<?> names = req.getParameterNames();
		
		while(names.hasMoreElements()) {
			String key = (String) names.nextElement();
			temp.put(key, req.getParameter(key));
		}
		
		return temp;
	}
	
	/**
	 * 将MAP转换成GET提交URL形式
	 * @param params
	 * @return
	 */
	public static String mapParamsToUrl(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		
		
		for(String s : params.keySet()) {
			sb.append(s).append("=").append(params.get(s)).append("&");
		}
		
		if (params.size() > 1)
			sb.delete(sb.length() - 1, sb.length()); // 去掉最后一个字符

		return sb.toString();
	}
	
	/**
	 * map 转化为根据key 排序的 keyvaluekeyvalue
	 * @param params
	 * @return
	 */
	public static String mapParamsToSignStrOrderby(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		
		Map<String, String> sortMap = new TreeMap<String, String>();
		sortMap.putAll(params);
		
		for(String s : sortMap.keySet()) {
			sb.append(s).append(sortMap.get(s));
		}
		
		if (params.size() > 1)
			sb.delete(sb.length() - 1, sb.length()); // 去掉最后一个字符

		return sb.toString();
	}

	/**
	 * 发送POST请求
	 * @param url
	 * @param params
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String sendPost(String url, String params, String encoding) throws Exception {

		String result = "";

		URL sendUrl = null;
		HttpURLConnection Connection = null;
		OutputStreamWriter out = null;

		try {
			sendUrl = new URL(url);
			Connection = (HttpURLConnection) sendUrl.openConnection();
			Connection.setUseCaches(false);			//关闭用户缓存
			Connection.setDoOutput(true);			//开启输出
			Connection.setDoInput(true);			//开启输入
			Connection.setRequestMethod("POST");	//设置请求类型
			Connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			Connection.connect();
			out = new OutputStreamWriter(Connection.getOutputStream(), encoding);
			out.write(params);
			out.flush();

			result = IOUtil.inputStreamToString(Connection.getInputStream(), encoding);

		}	finally {
			if (null != Connection) Connection.disconnect();
			if (null != out) out.close();
		}

		return result;
	}

public static String sendPost(String url, Map<String,String> header, String params, String encoding) throws Exception {
		
		String result = "";
		
		URL sendUrl = null;
		HttpURLConnection Connection = null;
		OutputStreamWriter out = null;
		
		try {
			sendUrl = new URL(url);
			Connection = (HttpURLConnection) sendUrl.openConnection();
			Connection.setUseCaches(false);			//关闭用户缓存
			Connection.setDoOutput(true);			//开启输出
			Connection.setDoInput(true);			//开启输入
			Connection.setRequestMethod("POST");	//设置请求类型
			if(header != null) {
				for(String key : header.keySet()) {
					Connection.setRequestProperty(key, header.get(key));
				}
			}
			Connection.connect();
			out = new OutputStreamWriter(Connection.getOutputStream(), encoding);
			out.write(params);
			out.flush();
			
			result = IOUtil.inputStreamToString(Connection.getInputStream(), encoding);
			
		}	finally {
			if (null != Connection) Connection.disconnect();
			if (null != out) out.close();
		}
		
		return result;
	}
	
	public static String sendPost(String url,Map<String,String> param,String charSet){
		StringBuffer result = new StringBuffer();
		try {
			URL httpurl = new URL(url);
			HttpURLConnection httpConn = (HttpURLConnection) httpurl.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			PrintWriter out = new PrintWriter(httpConn.getOutputStream());
			int i = 0;
			Set<Map.Entry<String, String>> set = param.entrySet();
			
			for (Map.Entry<String, String> entry:set){
				out.print(entry.getKey());
				out.print("=");
				out.print(entry.getValue());
				if (i!=set.size()-1){
					out.print("&");
				}
				i++;
			}
			
			out.flush();
			out.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), charSet));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
			in.close();
		} catch (MalformedURLException e) {
			throw new RuntimeException("发送post请求出错,url:"+url,e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("不支持的字符集,charSet:"+charSet,e);
		} catch (IOException e) {
			throw new RuntimeException("发送post请求IO出错,url:"+url,e);
		}
		return result.toString();
	}
	
	public static String sendPost(String url,Map<String,String> header, Map<String,String> param,String charSet){
		StringBuffer result = new StringBuffer();
		try {
			URL httpurl = new URL(url);
			HttpURLConnection httpConn = (HttpURLConnection) httpurl.openConnection();
			
			if(header != null) {
				for(String key : header.keySet()) {
					httpConn.setRequestProperty(key, header.get(key));
				}
			}
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			PrintWriter out = new PrintWriter(httpConn.getOutputStream());
			int i = 0;
			Set<Map.Entry<String, String>> set = param.entrySet();
			
			for (Map.Entry<String, String> entry:set){
				out.print(entry.getKey());
				out.print("=");
				out.print(entry.getValue());
				if (i!=set.size()-1){
					out.print("&");
				}
				i++;
			}
			
			out.flush();
			out.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), charSet));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
			in.close();
		} catch (MalformedURLException e) {
			throw new RuntimeException("发送post请求出错,url:"+url,e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("不支持的字符集,charSet:"+charSet,e);
		} catch (IOException e) {
			throw new RuntimeException("发送post请求IO出错,url:"+url,e);
		}
		return result.toString();
	}
	
	/**
	 * 发送 GET 请求
	 * @param url 请求的 URL 地址
	 * @param encode 请求的编码方式，null为默认编码
	 * @return
	 * @throws IOException 
	 */
	public static String sendGet(String url, String encode) throws IOException
	{
		String result = "";

		// 打开连接，设置参数
		URL httpUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
		conn.setDoInput(true); // 有返回参数
		conn.setRequestMethod("GET"); // 请求 GET 方式
		conn.setUseCaches(false); // 不使用缓存
		conn.connect();
		
		// 指定缓存 1024，编码方式为 HTTP_ENCODE
		result = IOUtil.read(conn.getInputStream(), 1024, encode);
		
		// 关闭连接
		conn.getInputStream().close();
		conn.disconnect();
		
		return result;
	}

	public static String sendPostBody(String urlPath, String json) throws IOException {
		String info = null;
		HttpClient httpclient = new HttpClient();

		PostMethod post = new PostMethod(urlPath);
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		post.setRequestBody(json);
		httpclient.executeMethod(post);
		info = new String(post.getResponseBody(), "utf-8");

		return info;
	}
	
	/**
	 * 发送二进制 body
	 * @param urlPath
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static String sendPostBodyAndTimeOut(String urlPath, String json,int time) throws IOException {
		String info = null;
		HttpClient httpclient = new HttpClient();
		httpclient.getHttpConnectionManager().getParams()
	    .setConnectionTimeout(time);
		PostMethod post = new PostMethod(urlPath);
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		post.setRequestBody(json);
		httpclient.executeMethod(post);
		info = new String(post.getResponseBody(), "utf-8");

		return info;
	}
	
	/**
	 * 获取请求参数
	 * @return String result 状态"success"
	 */
	public static String hostPost(String json, String address, String encode, int timeOut) {
        String result = "404";  
        URL url = null;  
        HttpURLConnection urlConn = null;  
        try {  
            /*得到url地址的URL类*/  
            url = new URL(address);  
            /*获得打开需要发送的url连接*/  
            urlConn = (HttpURLConnection) url.openConnection();  
            /*设置连接超时时间*/  
            urlConn.setConnectTimeout(timeOut);  
            /*设置读取响应超时时间*/  
            urlConn.setReadTimeout(timeOut);  
            /*设置post发送方式*/  
            urlConn.setRequestMethod("POST");  
            /*发送commString*/  
            urlConn.setDoOutput(true);  
            urlConn.setDoInput(true);  
            /*接收类型设置*/
            //urlConn.setRequestProperty("Accept", "application/json");
            /*发送类型设置*/
            urlConn.setRequestProperty("Content-Type", "application/json");
//            urlConn.addRequestProperty("Content-Type", "multipart/form-data");
            OutputStreamWriter out;  
            out = new OutputStreamWriter(urlConn.getOutputStream(), encode);  
            out.write(json);  
            out.flush();  
            out.close();  
            /*发送完毕 获取返回流，解析流数据*/  
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), encode));  
            StringBuffer buffer = new StringBuffer();  
            int ch;  
            while ((ch = reader.read()) > -1) {  
            	buffer.append((char) ch);  
            }  
            result = buffer.toString().trim();
//			log.info(result+"--111-");
            /*解析完毕关闭输入流*/
			reader.close();
		} catch (Exception e) {
            /*异常处理*/  
        	result = "404";  
            log.error("错误=====：",e);
            log.info("错误=====：",e);
        } finally {
            if (urlConn != null) {  
                /*关闭URL连接*/  
                urlConn.disconnect();  
            }  
        }  
        /*返回响应内容*/  
        return result;  
    }  
	
	/**
	 * 把 key1=value1 格式的 url 请求参数转换成  TreeMap
	 * @param params
	 * @return
	 */
	public static TreeMap<String, String> paramsUrlToTreeMap(String params) {
		String[] strs = params.split("&");
		TreeMap<String, String> m = new TreeMap<String, String>();
		for (String s : strs) {
			String[] ms = s.split("=");
			m.put(ms[0], ms[1]);
		}
		return m;
	}
	
	/**
	 * 将MAP转换成GET提交URL形式（按key字母升序排序)  key1=value1&key2=value2...
	 * 
	 * mapParamsToUrlOrderBy(map, "=", "|") = key1=value1|key2=value2|...
	 * mapParamsToUrlOrderBy(map, "&", "|") = key1&value1|key2&value2|...
	 * 
	 * @param params
	 * @return
	 */
	public static String mapParamsToUrlOrderBy(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		
		Map<String, String> sortMap = new TreeMap<String, String>();
		sortMap.putAll(params);
		
		for(String s : sortMap.keySet()) {
			sb.append(s).append("=").append(sortMap.get(s)).append("&");
		}
		
		if (params.size() > 1)
			sb.delete(sb.length() - 1, sb.length()); // 去掉最后一个字符

		return sb.toString();
	}
	
	/**
	 * 将MAP转换成GET提交URL形式（按key字母升序排序)  key1=value1&key2=value2...
	 * @param params
	 * @return
	 */
	public static String mapParamsToUrlOrderBy(Map<String, String> params, String appendStr1,  String appendStr2) {
		StringBuilder sb = new StringBuilder();
		
		Map<String, String> sortMap = new TreeMap<String, String>();
		sortMap.putAll(params);
		
		for(String s : sortMap.keySet()) {
			sb.append(s).append(appendStr1).append(sortMap.get(s)).append(appendStr2);
		}
		
		if (params.size() > 1)
			sb.delete(sb.length() - 1, sb.length()); // 去掉最后一个字符

		return sb.toString();
	}
	
//	public static void main(String[] args) throws Exception {
//		String name = "黄国栋";
//
//		String charset = System.getProperty("file.encoding");
//		System.out.println("==jvm 字符集 =="+charset);
//		System.out.println("原字符串:"+name);
//
//		String newCharset = "GBK".equals(charset.toUpperCase()) ? "utf-8" : "GBK";
//		String newStr = new String(name.getBytes(charset), newCharset);
//
//		System.out.println("原字符串:"+newStr);
//	}

	/**
	 * 描述:获取 post 请求内容
	 *
	 * <pre>
	 * 举例：
	 * </pre>
	 */
	public static String getRequestPostStr(HttpServletRequest request)
			throws IOException {
		byte buffer[] = getRequestPostBytes(request);
		String charEncoding = request.getCharacterEncoding();
		if (charEncoding == null) {
			charEncoding = "UTF-8";
		}
		return new String(buffer, charEncoding);
	}

	/**
	 * 描述:获取 post 请求的 byte[] 数组
	 *
	 * <pre>
	 * 举例：
	 * </pre>
	 */
	public static byte[] getRequestPostBytes(HttpServletRequest request)
			throws IOException {
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte buffer[] = new byte[contentLength];
		for (int i = 0; i < contentLength; ) {

			int readlen = request.getInputStream().read(buffer, i,
					contentLength - i);
			if (readlen == -1) {
				break;
			}
			i += readlen;
		}
		return buffer;
	}
}
