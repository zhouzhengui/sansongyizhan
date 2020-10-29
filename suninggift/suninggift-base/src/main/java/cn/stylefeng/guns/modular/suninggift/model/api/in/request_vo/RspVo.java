package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import java.io.Serializable;

/**
 * 
  * ClassName: RspVo <br/>  
  * Function: 想要实体类. <br/>  
  * date: 2020年2月19日 上午5:14:36 <br/>  
  *  
  * @author 彭齐文  
  * @version   
  * @since JDK 1.8
 */
public class RspVo implements Serializable {

	private static final long serialVersionUID = -8871293166117341679L;
	
	/**
	 * 请求内容,加密
	 */
	private String rspData;
	
	/**
	 * 分配给合作方的授权ID
	 */
	private String clientId;

	/**
	 * 获取请求的内容 
	 */
	public String getRspData() {
		return rspData;
	}

	/**
	 * 设置请求的内容 
	 */
	public void setRspData(String rspData) {
		this.rspData = rspData;
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
