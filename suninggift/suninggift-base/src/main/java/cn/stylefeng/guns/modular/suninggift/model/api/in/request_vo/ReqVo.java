package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import java.io.Serializable;

/**
 * 
  * ClassName: ReqVo <br/>  
  * Function: 内部请求实体类. <br/>  
  * date: 2020年2月19日 上午5:21:38 <br/>  
  *  
  * @author cc  
  * @version   
  * @since JDK 1.8
 */
public class ReqVo implements Serializable {

	private static final long serialVersionUID = -8871293166117341679L;
	
	/**
	 * 请求的内容
	 */
	private String reqData;
	
	/**
	 * 分配给合作方的授权ID
	 */
	private String clientId;

	/**
	 * 获取请求的内容 
	 */
	public String getReqData() {
		return reqData;
	}

	/**
	 * 设置请求的内容 
	 */
	public void setReqData(String reqData) {
		this.reqData = reqData;
	}

	/**
	 * 获取分配给合作方的授权ID 
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * 设置分配给合作方的授权ID 
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
