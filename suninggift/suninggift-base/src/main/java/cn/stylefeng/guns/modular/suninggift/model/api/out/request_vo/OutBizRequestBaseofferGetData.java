package cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo;

import cn.stylefeng.guns.modular.suninggift.model.OutBizRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * （对接外部支付工程）
 * 内部接口请求参数封装类
 */
@Data
public class OutBizRequestBaseofferGetData extends OutBizRequest implements Serializable {

	private String mobile;//手机号


	/**
	 * 号码归属省份
	 */
	private String province;
	/**
	 * 号码归属市
	 */
	private String city;

	/**
	 * 号码归属运营商
	 */
	private String isp;

}
