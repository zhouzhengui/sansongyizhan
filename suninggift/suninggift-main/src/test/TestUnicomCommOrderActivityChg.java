
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.CommOrderActivityChgAo;
import cn.stylefeng.guns.modular.suninggift.utils.CmpayUtil;
import cn.stylefeng.guns.modular.suninggift.utils.LinoHttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class TestUnicomCommOrderActivityChg {
	
	/**
	 * 
	  * TestUnicomCommOrderActivityChg: 订单同步接口. <br/>  
	  * @author zzg
	  * @since JDK 1.8   
	  * @param 
	  * @return void
	  * @throws
	 */
	public static void main(String[] args) {
		String responseString = null;
		try {
			
			CommOrderActivityChgAo commOrderActivityChgAo = new CommOrderActivityChgAo();
			commOrderActivityChgAo.setUserPhone("13125266110");
			commOrderActivityChgAo.setContractId("90757266");
			commOrderActivityChgAo.setCustId("9118071103762069");
			commOrderActivityChgAo.setOutOrderNo("2020022113125266110");
			commOrderActivityChgAo.setPayDate("2020-02-21 17:16:44");
			commOrderActivityChgAo.setProductNo("90657927");
			commOrderActivityChgAo.setProductPrice("350.40");
			commOrderActivityChgAo.setRealPayMoney("0.00");
			
			String reqJson = JSONObject.toJSONString(commOrderActivityChgAo);
			String json = CmpayUtil.encryptAgReqData("202002190702160141"
					, "muz32AD+fCRgGc/e/+H97u3wJEcEIu5I", reqJson);
//			String repString = "http://localhost:18791/uniline/lpuf/gzlp.unicom.comm.order.activity.chg";
			String repString = "http://127.0.0.1:18791/uniline/lpuf/gzlp.unicom.comm.order.activity.chg";
			Map<String, String> httpPostRsp = LinoHttpUtil.httpPost(json, repString, "UTF-8", 10000);
			if(null!= httpPostRsp && "200".equals(String.valueOf(httpPostRsp.get("code")))) {
				responseString = String.valueOf(httpPostRsp.get("msg"));
				JSONObject parseObject = JSON.parseObject(responseString);
				String rspData = parseObject.getString("rspData");
				System.out.println(responseString);
				String decryptAgRespData = CmpayUtil.decryptAgRespData("muz32AD+fCRgGc/e/+H97u3wJEcEIu5I", rspData);
				System.out.println("接口响应>>>" + decryptAgRespData);
			}else {
				System.out.println("请求异常");
			}
			
		} catch (UnsupportedEncodingException e) {
			    // TODO Auto-generated catch block  
			    e.printStackTrace();  
		} catch (Exception e) {
			    // TODO Auto-generated catch block  
			    e.printStackTrace();  
			    
		} //将相应的数据转换为字符
	}

}
