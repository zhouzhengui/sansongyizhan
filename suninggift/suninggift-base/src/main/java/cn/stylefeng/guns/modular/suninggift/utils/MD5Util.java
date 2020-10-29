package cn.stylefeng.guns.modular.suninggift.utils;



import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

public class MD5Util {

	/**
	 * 利用MD5进行加密
	 * 
	 * @param str 待加密的字符串
	 * @return 加密后的字符串
	 * @throws NoSuchAlgorithmException 没有这种产生消息摘要的算法
	 * @throws UnsupportedEncodingException
	 */
    public static String encodeByMd5(String str){
        // 加密后的字符串
		String newstr="";
		try {
			// 确定计算方法
			MessageDigest md5=MessageDigest.getInstance("MD5");
			newstr = Base64Util.encode(md5.digest(str.getBytes("utf-8")));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return newstr;
    }

    /***
     * md5--16位加密
     * @param plainText
     * @return
     */
    public static String md5s(String plainText) {
    	String md5str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("utf-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			md5str = buf.toString().substring(8, 24);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return md5str;
	}
    
    /***
     * md5--32位加密
     * @param plainText
     * @return
     */
    public static String md5s_32(String plainText) {
    	String md5str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("utf-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			md5str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return md5str;
	}


	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}


	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String Bit32(String SourceString) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(SourceString.getBytes("UTF-8"));
			byte messageDigest[] = digest.digest();
			return toHexString(messageDigest).toLowerCase();
		}catch (Exception e){
			throw new RuntimeException("md5失败",e);
		}

	}

	public static String Bit32(String SourceString,String charsetName) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(SourceString.getBytes(charsetName));
			byte messageDigest[] = digest.digest();
			return toHexString(messageDigest).toLowerCase();
		}catch (Exception e){
			throw new RuntimeException("md5失败",e);
		}

	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 拼接MD5加密内容
	 */
	public static String getMD5SignContent(Map<String, Object> map) {
		TreeMap<String, Object> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		String json = "";
		if (treeMap != null) {
			json += JSON.toJSONString(treeMap);
		}

		return json;
	}
}
