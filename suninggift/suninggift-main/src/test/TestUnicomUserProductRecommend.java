import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.UserProductRecommendAo;
import cn.stylefeng.guns.modular.suninggift.utils.CmpayUtil;
import cn.stylefeng.guns.modular.suninggift.utils.LinoHttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class TestUnicomUserProductRecommend {



	/**
	 * 
	  * TestUnicomUserProductRecommend: 策略查询. <br/>  
	  * @author zzg
	  * @since JDK 1.8   
	  * @param 
	  * @return void
	  * @throws
	 */
	public static void main(String[] args) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar instance = Calendar.getInstance();
		String format = simpleDateFormat.format(instance.getTime());
		System.out.println(format);
		instance.add(Calendar.SECOND, 3);
		System.out.println(simpleDateFormat.format(instance.getTime()));



//		String responseString = null;
//		try {
//
//			UserProductRecommendAo checkUserPhone = new UserProductRecommendAo();
//			checkUserPhone.setUserPhone("13125266110");
//
//			String reqJson = JSONObject.toJSONString(checkUserPhone);
//			String json = CmpayUtil.encryptAgReqData("202002190702160141"
//					, "muz32AD+fCRgGc/e/+H97u3wJEcEIu5I", "", reqJson);
////			String repString = "http://localhost:18791/uniline/lpuf/gzlp.unicom.user.product.recommend";
//			String repString = "http://127.0.0.1:18791/uniline/lpuf/gzlp.unicom.user.product.recommend";
//			Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", 10000);
//			if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
//				responseString = String.valueOf(httpPostRsp.get("msg"));
//				JSONObject parseObject = JSON.parseObject(responseString);
//				String rspData = parseObject.getString("rspData");
//				System.out.println(responseString);
//				String decryptAgRespData = CmpayUtil.decryptAgRespData("muz32AD+fCRgGc/e/+H97u3wJEcEIu5I", rspData);
//				System.out.println("接口响应>>>" + decryptAgRespData);
//			}else {
//				System.out.println("请求异常");
//			}
//
//		} catch (UnsupportedEncodingException e) {
//			    // TODO Auto-generated catch block
//			    e.printStackTrace();
//		} catch (Exception e) {
//			    // TODO Auto-generated catch block
//			    e.printStackTrace();
//
//		} //将相应的数据转换为字符
	}

}
