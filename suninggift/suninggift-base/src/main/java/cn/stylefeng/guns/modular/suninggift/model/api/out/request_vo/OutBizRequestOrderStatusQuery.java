package cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo;

import cn.stylefeng.guns.modular.suninggift.model.OutBizRequest;
import lombok.Data;

/**
 * （对接外部支付工程）
 * 内部接口请求参数封装类
 */
@Data
public class OutBizRequestOrderStatusQuery extends OutBizRequest {

	/**
	 * 订单号
	 */
	private String out_trade_no;

}
