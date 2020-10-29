package cn.stylefeng.guns.modular.suninggift.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonUtil {
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,20}$";
 
    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";
 
    /**
     * 正则表达式：验证手机号（已经过时，缺少号段）
     */
    @Deprecated
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
 
    
    /**
     * 正则表达式：验证手机号(只校验长度)
     */
    public static final String REGEX_MOBILE2 = "^\\d{11}$";
 
    
    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
 
    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
 
    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
 
    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
 
    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
 
    /**
     * 校验用户名
     * 
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }
 
    /**
     * 校验密码
     * 
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }
 
    /**
     * 校验手机号
     * 
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE2, mobile);
    }
 
    /**
     * 校验邮箱
     * 
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }
 
    /**
     * 校验汉字
     * 
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }
 
    /**
     * 校验身份证
     * 
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }
 
    /**
     * 校验URL
     * 
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }
 
    /**
     * 校验IP地址
     * 
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }
    
    /*
     * 非空校验
     */
    public static boolean isEmpty(String text){
    	if(text == null || text.trim().length() == 0){
    		return true;
    	}
    	return false;
    }
    
    /*
     * 判断是否是整数
     */
    public static boolean isDigit(String digit){
    	return Pattern.matches("(\\+|\\-)?\\d+", digit);
    }
    
    /**
	 * 判断是否是receiveId
	 * @param
	 * @return
	 */
	public static boolean isReceiveId(String receiveId){
		return Pattern.matches("^2088\\d{12}$", receiveId);
	}
    
    /**
     * 获取请求中的参数
     * @param request
     * @return
     * @throws IOException
     */
  	public static String parseRequest(HttpServletRequest request) throws IOException{
  		String json = null;
  		if("GET".equals(request.getMethod())) {
  			json = new String (request.getQueryString().getBytes("iso-8859-1"),"utf-8").replaceAll("%22", "\"");
  		}else {
  			json = getRequestPostStr(request);
  		}
  		
  		return json;
  	}
  	
  	private static String getRequestPostStr(HttpServletRequest request) throws IOException{
  		byte buffer[] = getRequestPostBytes(request);
  		String charEncoding = request.getCharacterEncoding();
  		if (charEncoding == null) {
  			charEncoding = "UTF-8";
  		}
  		return new String(buffer, charEncoding);
  	}
  	
  	private static byte[] getRequestPostBytes(HttpServletRequest request)
  			throws IOException {
  		int contentLength = request.getContentLength();
  		if (contentLength < 0) {
  			return null;
  		}
  		byte buffer[] = new byte[contentLength];
  		for (int i = 0; i < contentLength;) {

  			int readlen = request.getInputStream().read(buffer, i,
  					contentLength - i);
  			if (readlen == -1) {
  				break;
  			}
  			i += readlen;
  		}
  		return buffer;
  	}
  	
  	/**
     * 返回随机的8位 merchant_code
     * @return
     */
    public static String getRandomMerchantCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * 从request中获取所有参数数据信息
     * @param request
     * @return
     */
    public static Map<String, String> getAllParams(HttpServletRequest request){
        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        
        return params;
    }
    


		/**
	 * 验证手机号码
	 * @param
	 * @return
	 */
	public static boolean isPhone(String phone) {
		/**
		 * 手机号:目前全国有27种手机号段。
		 * 移动有16个号段：134、135、136、137、138、139、147、150、151、152、157、158、159、182、187、188。其中147、157、188是3G号段，其他都是2G号段。
		 * 联通有7种号段：130、131、132、155、156、185、186。其中186是3G（WCDMA）号段，其余为2G号段。
		 * 电信有4个号段：133、153、180、189。其中189是3G号段（CDMA2000），133号段主要用作无线网卡号。
		 * 150、151、152、153、155、156、157、158、159 九个;
		 * 130、131、132、133、134、135、136、137、138、139 十个;
		 * 180、182、185、186、187、188、189 七个;
		 * 13、15、18三个号段共30个号段，154、181、183、184暂时没有，加上147共27个。
		 */
		if(isEmpty(phone)){
			return false;
		}
		
		Pattern p = Pattern.compile("^((19[8-9]\\d{8}$)|(16[6]\\d{8}$)|(13\\d{9}$)|(15[0,1,2,3,4,5,6,7,8,9]\\d{8}$)|(18[0,1,2,3,4,5,6,7,8,9]\\d{8}$)|(147\\d{8})$)|(17[0,1,2,3,4,5,6,7,8,9]\\d{8}$)");
		Matcher m = p.matcher(phone);  
		return m.matches();  
	}
	
}