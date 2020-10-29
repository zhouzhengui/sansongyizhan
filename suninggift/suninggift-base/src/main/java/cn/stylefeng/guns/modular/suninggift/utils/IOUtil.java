package cn.stylefeng.guns.modular.suninggift.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 获取IO流数据
 * @author Hanley
 *
 */
public class IOUtil {
	
	/**
	 * 一次读取缓存大小
	 */
	private static final int CACHE_SIZE = 1024;
	/**
	 * 默认的编码方式
	 */
	public static final String ENCODE = "UTF-8";
	
	
	/**
	 * 读取字符串操作
	 * @param in
	 * 		输入流
	 * @param length
	 * 		一次读取长度，单位byte，默认为 CACHE_LENGTH
	 * @return
	 * 		返回输入流的字符串 
	 * @throws IOException
	 */
	public static String read(InputStream in, int length) throws IOException
	{
		return read(in, length, ENCODE);
	}
	
	/**
	 * 指定缓存大小，编码方式读取输入流
	 * @param in
	 * 			输入流
	 * @param length
	 * 			缓存大小
	 * @param encode
	 * 			编码方式
	 * @return
	 * @throws IOException
	 */
	public static String read(InputStream in, int length, String encode) throws IOException
	{
		StringBuilder result = new StringBuilder();
		
		byte[] b = new byte[length];
		int len = -1;
		while (-1 != (len = in.read(b)))
			result.append(new String(b, 0, len, encode));
		
		return result.toString();
	}
	
	/**
	 * 指定输入流和编码方式输入指定信息
	 * @param out
	 * @param msg
	 * @param encode
	 * @throws IOException 
	 */
	public static void write(OutputStream out, String msg, String encode) throws IOException
	{
		byte[] b = msg.getBytes(encode);
		out.write(b);
		out.flush();
	}
	
	/**
	 * 将input流转换为String类型
	 * @param encode	编码
	 * @return
	 */
	public static String inputStreamToString(InputStream in, String encode){
		StringBuilder result = new StringBuilder();
		
		byte[] b = new byte[CACHE_SIZE];
		int len = -1;
		
		try {
			
			while( -1 != (len = in.read(b))) {
				result.append( new String(b, 0, len, encode) );
			}
			
		} catch (Exception e) {
			return "";
		}
		
		return result.toString();
	}
}
