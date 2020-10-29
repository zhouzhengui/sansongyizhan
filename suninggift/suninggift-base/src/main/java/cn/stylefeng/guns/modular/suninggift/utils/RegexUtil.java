package cn.stylefeng.guns.modular.suninggift.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-04-30 16:33
 */
public class RegexUtil {

//    134、135、136、137、138、139、150、151、152、157、158、159、178、182、183、184、187、188、198 2020-04-30 cms
//    134、135、136、137、138、139、147、150、151、152、157、158、159、172、178、182、183、184、187、188、195、198 2020年5月9日 zba
//    private static String gdcmccRegex = "^((13[4-9]|15[0-2,7-9]|178|18[23478]|198))\\d{8}$";
    private static String gdcmccRegex = "^((13[4-9]|147|15[0-2,7-9]|17[28]|18[23478]|19[58]))\\d{8}$";// 2020年5月9日 zba
    private static String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
    private static Pattern compile = Pattern.compile(gdcmccRegex);

    /**
     * 判断是否为广东移动的手机号
     * @param mobile
     * @return
     */
    public static boolean isGdCmcc(String mobile){
        Matcher matcher = compile.matcher(mobile);
        return matcher.matches();
    }

//    public static void main(String[] args) {
//        String a = "18668122056";
//        System.out.println(isGdCmcc(a));
//    }
}
