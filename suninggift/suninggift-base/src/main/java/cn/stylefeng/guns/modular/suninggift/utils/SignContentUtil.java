package cn.stylefeng.guns.modular.suninggift.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SignContentUtil {
	
	private static String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCy0cgUKDdboQhtitvO4UhNoeZyyXaviU818TQy3nJ3m7g1d29PAwc5YK+bAVJ5agp0/+EOlkqYztLbpcRZqlp8gfNAMeAHat0R0Bli4xvrBLqG4e6ZcdTOHfRfxMO5vL3hOg7kV0J128Dpij7Fb1rHIM/0AvooMPghhXPa5tZvpBS2CovjOe05CgshUWjhwiP0x10M0X3/Q2Ckue3GucmWkb56jsvHWKTna99lpriuW6vnc4CFFaYza7CqiZ4Xy1cttaor86QwD/ZNl48OkOtAl1B2wfRvbfHLzCToYsXZumOOTHjU0v+/i/rHzfCkdNCEvFrtp6KpJdCrpHSn0qQRAgMBAAECggEAFaMgrJ+5IxM3K9oTgtLrpskiU11hSRg6R3KqOEWGYEatYXrUMyFQrHawiPb9/ccBAOs9VkCFQjw0c3N5cMgWLt6iim/+hac+nLsrdzW32RY3Y77O2i5V66LgQP5UGJNn/mrCBoswdYZjr16U/drdhx7HSuVZU6epPpUhUamA5P0ny3PYpe5wyx989orQxiGRsURS0+p2WDYxfWuT+D/dwXngTRZ8trbWzWR5w3fLnkkcfCGHar+Z+ijj1XnLNImCDItSJ/ZdnBfTTxU6Y0svzv7U2ujkeXt+MC2d7KYGmNHDc8g5DLgMDudbDl+YsUAp0MGjiBEN0HsAbw6hx8dDYQKBgQDjrgefAw3HlWQuoV4t9XM6BSRJS6iw3fbtZbHE54Qe/+5SVWtLcDPPYyNEcWrPagh3sfw636HRmADR5yead58kJkpRzPf6Vex4yKJn1ciBoHgzLpDT8nO6H5giJz27ZWWJoy9YrxMniZ3i6dEVG56aMJDMwFGhhaV6tgduFPZqZQKBgQDJD+ZiZBuo2WvhFTs9/dvdYiy6AGQxUCfJc/DOjRacRrgGniwo0O2EevHr/CAMc8G22SM5HJtBmbX1m35ml9IcwJymdAOu8BvkFW/HdIToGjF0pY+a9K6O2JDJipbDmXHVBrGIlUg+xg7UNjoYZFR3gnsXEHTezh6/pnymz4SCPQKBgQCCM3V8Q5zPs+j0madoFONuHJ6ho7i2XFjnba55cgQmTimVv+951pJqWMFEVK/GKVbqTEh3yoeZepmNb+k/9oR1gcgie1qcOCLg96uYQbNGHeRfMJP2AMKZf5PahCJTVir4yO88gfIsPBCkQR4mOgM7HebQkHKeMfVt9UbOdwJr2QKBgQCBHpPeJdnZcl8kOgVEh011+4Erpeqx/H7AkZNYoiSeEZBGt7c2Mw4FYQsReGWAeaoyaXmESMmvtC21X7kp21/UlSFEiAaBhrLyo42YMkzT3VZbSaNSaMXc+FjrBPRTAqymmFjUlMHDhl+pinV+ipfv87xmkGvtVcNLYnh0i5nWJQKBgQCY9phkPmijPMiIkH0bYuix/HNYLIhucqxDX3Zu0JWl2krI8ogp9u1jLPvCat2K/PbJPqdx2Yqz6Lm4HVpnPNztfK4NnnU8V19INYZuU/oLqGZB7FOxEmYFCVLQ+BmOrK6gcilcft0gREUKhR6un7/Zt+dBdIFp6RbjJQJj91rC3g==";
	private static String charset = "UTF-8";
	private static String signType = "RSA2";
	
	/**
	 * 封装解签内容
	 * @param falg falg = 0//表示通用接口 falg = 1 //表示为联通调用
	 * @return
	 */
	public static String getSignContent(int falg,JSONObject bizContent ,JSONObject outContent) {
		
		//封装bizContent
		//返回加签内容
		String signContent = null;
		String firstHead = "bizContent=";
		String secondHead = "&outContent=";
		
		if(0 != falg) {
		    firstHead = "BIZ_CONTENT=";
		    secondHead = "&OUT_CONTENT=";
		}
		
		if(outContent != null && !outContent.isEmpty()) {
            signContent = firstHead + JSON.toJSONString(bizContent ,SerializerFeature.MapSortField) + secondHead + JSON.toJSONString(outContent ,SerializerFeature.MapSortField);
        }else {
            signContent = firstHead + JSON.toJSONString(bizContent ,SerializerFeature.MapSortField);
        }
		
		return signContent;
	}
	
	/**
	 * 加签
	 * @return
	 */
	public String getSign(String signContent) {
		
        //加签
        String rsaSign = RSAEncrypt.sign(signContent , signType , privateKey, charset);
		
		return rsaSign;
	}
	
}
