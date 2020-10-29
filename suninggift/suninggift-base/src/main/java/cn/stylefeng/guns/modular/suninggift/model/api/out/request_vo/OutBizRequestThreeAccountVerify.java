package cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo;

import cn.stylefeng.guns.modular.suninggift.model.OutBizRequest;
import lombok.Data;

/**
 * （对接外部支付工程）
 * 内部接口请求参数封装类
 */
@Data
public class OutBizRequestThreeAccountVerify extends OutBizRequest {

	/**
	 * 手机号
	 */
	private String userPhone;

}
