package cn.stylefeng.guns.modular.suninggift.utils;

/*
 --------------------------------------------**********--------------------------------------------

 该算法于1977年由美国麻省理工学院MIT(Massachusetts Institute of Technology)的Ronal Rivest，Adi Shamir和Len Adleman三位年轻教授提出，并以三人的姓氏Rivest，Shamir和Adlernan命名为RSA算法，是一个支持变长密钥的公共密钥算法，需要加密的文件快的长度也是可变的!

 所谓RSA加密算法，是世界上第一个非对称加密算法，也是数论的第一个实际应用。它的算法如下：

 1.找两个非常大的质数p和q（通常p和q都有155十进制位或都有512十进制位）并计算n=pq，k=(p-1)(q-1)。

 2.将明文编码成整数M，保证M不小于0但是小于n。

 3.任取一个整数e，保证e和k互质，而且e不小于0但是小于k。加密钥匙（称作公钥）是(e, n)。

 4.找到一个整数d，使得ed除以k的余数是1（只要e和n满足上面条件，d肯定存在）。解密钥匙（称作密钥）是(d, n)。

 加密过程： 加密后的编码C等于M的e次方除以n所得的余数。

 解密过程： 解密后的编码N等于C的d次方除以n所得的余数。

 只要e、d和n满足上面给定的条件。M等于N。

 --------------------------------------------**********--------------------------------------------
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAEncrypt {
    private static final Logger log = LogManager.getLogger(RSAEncrypt.class);
	/** 指定RSA_KEY的大小 */
	private static final int RSA_KEYSIZE = 1024;
	/** 指定RSA2_KEY的大小 */
	private static final int RSA2_KEYSIZE = 2048;
    
    /**
     * 生成RSA密钥对
     * @param  encryptType 加密类型
     * @return base64格式的密钥对
     */
	public static Map<String, String> generateKeyPair(String encryptType, String charEncoding) 
			throws Exception {
		/** RSA算法要求有一个可信任的随机数源 */
		SecureRandom sr = new SecureRandom();
		/** 为RSA算法创建一个KeyPairGenerator对象 */
		KeyPairGenerator kpg = null;
		int keySize = 0;
		if(Configure.ENCRYPT_TYPE_RSA2.equals(encryptType)){
			kpg = KeyPairGenerator.getInstance(Configure.ENCRYPT_TYPE_RSA2);
			keySize = RSA2_KEYSIZE;
		}else{ /** 默认生成RSA */
			kpg = KeyPairGenerator.getInstance(Configure.ENCRYPT_TYPE_RSA);
			keySize = RSA_KEYSIZE;
		}
		/** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
		kpg.initialize(keySize, sr);
		/** 生成密匙对 */
		KeyPair kp = kpg.generateKeyPair();
		/** 得到公钥 */
		Key publicKey = kp.getPublic();
		byte[] publicKeyBytes = publicKey.getEncoded();
		/** 得到私钥 */
		Key privateKey = kp.getPrivate();
		byte[] privateKeyBytes = privateKey.getEncoded();
		String pub = null;
		String pri = null;
		if(charEncoding == null){
			new String(Base64.encodeBase64(publicKeyBytes), Configure.CHAR_ENCODING);
			new String(Base64.encodeBase64(privateKeyBytes), Configure.CHAR_ENCODING);
		}else {
			new String(Base64.encodeBase64(publicKeyBytes), charEncoding);
			new String(Base64.encodeBase64(privateKeyBytes), charEncoding);
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("publicKey", pub);
		map.put("privateKey", pri);
		RSAPublicKey rsp = (RSAPublicKey) kp.getPublic();
		BigInteger bint = rsp.getModulus();
		byte[] b = bint.toByteArray();
		byte[] deBase64Value = Base64.encodeBase64(b);
		String retValue = new String(deBase64Value);
		map.put("modulus", retValue);
		return map;
	}

	/**
     * 使用公钥加密
     * @param  clearString  待加密字符串
     * @param  publicKey    公钥base64串
     * @param  charEncoding 待加密字符集
     * @return base64格式的密文
     */
	public static String encrypt(String clearString, String publicKey, String charEncoding)
			throws Exception {
		Key key = getPublicKey(publicKey);
		/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher = Cipher.getInstance(Configure.RSA_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptBytes = clearString.getBytes();
		/** 执行加密操作 */
		byte[] cipherBytes = cipher.doFinal(encryptBytes);
		if(charEncoding == null){
			return new String(Base64.encodeBase64(cipherBytes), Configure.CHAR_ENCODING);
		}else {
			return new String(Base64.encodeBase64(cipherBytes), charEncoding);
		}
	}

	/**
     * 使用私钥解密
     * @param  cipherString 待解密字符串
     * @param  privateKey    私人钥base64串
     * @param  charEncoding 待解密字符集
     * @return 加密前明文
     */
	public static String decrypt(String cipherString, String privateKey, String charEncoding)
			throws Exception {
		Key key = getPrivateKey(privateKey);
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher = Cipher.getInstance(Configure.RSA_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		/** Base64解码 */
		byte[] encryptBytes = Base64.decodeBase64(cipherString.getBytes());
		/** 执行解密操作 */
		byte[] cipherBytes = cipher.doFinal(encryptBytes);
		if(charEncoding == null){
			return new String(cipherBytes, Configure.CHAR_ENCODING);
		}else {
			return new String(cipherBytes, charEncoding);
		}
	}

	/**
	 * 得到公钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
				Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 得到私钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
				Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
     * 使用RSA签名
     * @param  content      待加签名符串
     * @param  signType     签名使用类型
     * @param  privateKey   私钥base64串
     * @param  charEncoding 待加签字符集
     * @return 加签结果BASE64
     */
	public static String sign(String content, String signType, String privateKey, String charEncoding) {
		String charset = null;
		if(charEncoding == null){
			charset = Configure.CHAR_ENCODING;
		}else {
			charset = charEncoding;
		}
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decodeBase64(privateKey.getBytes()));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			
			Signature signature = null;
			if(Configure.ENCRYPT_TYPE_RSA2.equals(signType)){
				signature = Signature.getInstance(Configure.SIGN_SHA256RSA_ALGORITHMS);
			}else {
				signature = Signature.getInstance(Configure.SIGN_ALGORITHMS);
			}

			signature.initSign(priKey);
			signature.update(content.getBytes(charset));

			byte[] signed = signature.sign();

			return new String(Base64.encodeBase64(signed));
		} catch (Exception e) {

		}

		return null;
	}
	
	/**
     * 使用RSA验签
     * @param  content      待加签名符串
     * @param  sign         签名base64串
     * @param  signType     签名使用类型
     * @param  publicKey   私钥base64串
     * @param  charEncoding 待加签字符集
     * @return 布尔值
     */
	public static boolean checkSign(String content, String sign, String signType, String publicKey, String charEncoding)
	{
		String charset = null;
		if(charEncoding == null){
			charset = Configure.CHAR_ENCODING;
		}else {
			charset = charEncoding;
		}
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        byte[] encodedKey = Base64.decodeBase64(publicKey.getBytes(charset));
	        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			Signature signature = null;
			if(Configure.ENCRYPT_TYPE_RSA2.equals(signType)){
				signature = Signature.getInstance(Configure.SIGN_SHA256RSA_ALGORITHMS);
			}else {
				signature = Signature.getInstance(Configure.SIGN_ALGORITHMS);
			}
			signature.initVerify(pubKey);
			signature.update( content.getBytes(charset));
		
			boolean bverify = signature.verify(Base64.decodeBase64(sign.getBytes(charset)));;
			return bverify;
		} 
		catch (Exception e) 
		{
			log.error(e.getMessage(), e);
		}
		
		return false;
	}	

	
}