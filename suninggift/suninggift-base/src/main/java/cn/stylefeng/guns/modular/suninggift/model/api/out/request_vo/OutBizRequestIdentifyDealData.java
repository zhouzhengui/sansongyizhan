package cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo;

import cn.stylefeng.guns.modular.suninggift.model.OutBizRequest;
import lombok.Data;

/**
 * （对接外部支付工程）
 * 内部接口请求参数封装类
 */
@Data
public class OutBizRequestIdentifyDealData extends OutBizRequest {

	private String biz_type;//验证处理类型

	private String mobile;//手机号码

	private String verification_code;//验证码

}
