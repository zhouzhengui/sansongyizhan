package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
public class UserProductRecommendAo implements Serializable{

	private static final long serialVersionUID = -1630302002144211608L;
	
	/**
	 * 客户手机号码
	 */
	private String userPhone;

	/**
	 * 策略库编码
	 */
	private String strategyId;

	/**
	 * 码归属运营商号码归属运营商
	 * 广东移动：lpgdcmcc
	 * 联通：lpunicom
	 * 电信：lptelecom
	 */
	private String operator;

	/**
	 * 接入商户编码
	 */
	private String clientId;

	/**
	 * 业务类型 默认存量升档
	 * 新用户：NEWUSER
	 * 存量升档：STOCKUSER
	 */
	private String orderType = "STOCKUSER";

}
