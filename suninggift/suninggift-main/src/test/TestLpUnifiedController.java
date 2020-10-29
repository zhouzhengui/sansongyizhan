
/**  
  * Project Name:gzlp_base  
  * File Name:TestLpUnifiedController.java  
  * Package Name:com.gzlplink.base.controller  
  * Date:2020年2月7日下午4:54:58  
  * Copyright (c) 2020, chenzhou1025@126.com All Rights Reserved.  
  *  
  */

import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.CheckUserPhoneAo;
import cn.stylefeng.guns.modular.suninggift.utils.CmpayUtil;
import cn.stylefeng.guns.modular.suninggift.utils.LinoHttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**  
  * ClassName:TestLpUnifiedController <br/>  
  * Function: 测试内部接口接入总入口. <br/>  
  * Date:     2020年2月19日 上午4:54:58 <br/>  
  * @author   cc  
  * @version    
  * @since    JDK 1.8  
  * @see        
  */

public class TestLpUnifiedController {


	/**
	 * 
	  * unicomPhoneTest: 三户校验. <br/>  
	  * @author 彭齐文
	  * @since JDK 1.8   
	  * @param 
	  * @return void
	  * @throws
	 */
	public static void main(String[] args) {
		String responseString = null;
		try {
			
			CheckUserPhoneAo checkUserPhone = new CheckUserPhoneAo();
			checkUserPhone.setUserPhone("13054266110");
			
			String reqJson = JSONObject.toJSONString(checkUserPhone);
			String json = CmpayUtil.encryptAgReqData("202002190702160141"
					, "muz32AD+fCRgGc/e/+H97u3wJEcEIu5I", reqJson);
//			String repString = "http://localhost:18791/uniline/lpuf/gzlp.unicom.check.phone";
			String repString = "http://127.0.0.1:18791/uniline/lpuf/gzlp.unicom.check.phone";
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
  
