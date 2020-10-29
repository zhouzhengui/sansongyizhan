package cn.stylefeng.guns.modular.suninggift.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.DES;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 * @ClassNameDesUtils
 * @Description TODO
 * @Author tangxiong
 * @Date 2019/10/21 14:42
 **/
public class DesUtils {
    // BASE64格式密钥
    private static final String desKey = "kfejow7wef27we5f";

    public static String encryptHex(String data){
        String encryptToken = "";
        if(StringUtils.isEmpty(data)){
            return null;
        }
        try{
            byte[] rdw = Base64.decodeBase64(desKey);
            DES des = new DES(rdw);
            encryptToken = des.encryptHex(data, CharsetUtil.UTF_8);
        }catch (Exception e){
            encryptToken = null;
        }

        return encryptToken;

    }


}
