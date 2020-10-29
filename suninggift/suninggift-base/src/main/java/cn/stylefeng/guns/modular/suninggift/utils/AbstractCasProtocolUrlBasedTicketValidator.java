/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/index.html
 */
package cn.stylefeng.guns.modular.suninggift.utils;

import org.jasig.cas.client.validation.AbstractUrlBasedTicketValidator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Abstract class that knows the protocol for validating a CAS ticket.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public abstract class AbstractCasProtocolUrlBasedTicketValidator extends
        AbstractUrlBasedTicketValidator {

	protected AbstractCasProtocolUrlBasedTicketValidator(
			final String casServerUrlPrefix) {
		super(casServerUrlPrefix);
	}

	static HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            System.out.println("Warning: URL Host: " + urlHostName + " vs. "
                               + session.getPeerHost());
            return true;
        }
    };

	/**
	 * Retrieves the response from the server by opening a connection and merely
	 * reading the response.
	 */
	public static final String retrieveResponseFromServer(String url
			,String param) {
		HttpURLConnection connection = null;

		try {
			URL validationUrl = new URL(url);
			trustAllHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(hv);

			connection = (HttpURLConnection) validationUrl.openConnection();
			OutputStream out = null; //写
//post方式请求
			connection.setRequestMethod("POST");
			//设置头部信息
			connection.setRequestProperty("headerdata", "ceshiyongde");
			//一定要设置 Content-Type 要不然服务端接收不到参数
			connection.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
			//指示应用程序要将数据写入URL连接,其值默认为false（是否传参）
			connection.setDoOutput(true);
			//httpURLConnection.setDoInput(true);

			connection.setUseCaches(false);
			connection.setConnectTimeout(30000); //30秒连接超时
			connection.setReadTimeout(30000);    //30秒读取超时
			//获取输出流
			out = connection.getOutputStream();
			//输出流里写入POST参数
			out.write(param.getBytes());
			out.flush();
			out.close();
			int responseCode = connection.getResponseCode();
			System.out.println(responseCode);

			final BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			String line;
			final StringBuffer stringBuffer = new StringBuffer(255);

			synchronized (stringBuffer) {
				while ((line = in.readLine()) != null) {
					stringBuffer.append(new String(line.getBytes(),"UTF-8"));
					stringBuffer.append("\n");
				}
				return stringBuffer.toString();
			}

		} catch (final IOException e) {
			e.printStackTrace();
//			log.error(e, e);
			return null;
		} catch (final Exception e1){
//			log.error(e1, e1);
			e1.printStackTrace();

			return null;
		}finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
				.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc
				.getSocketFactory());
	}

	static class miTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}

}
