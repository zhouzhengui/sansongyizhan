package cn.stylefeng.guns.modular.suninggift.model;

import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import java.io.Serializable;
import lombok.Data;


/**
 * @Description   前端业务交互模型
 * @author        linjz
 * @date          2019年3月28日
 * @version       v1.0
 **/
@Data
public class MessagerVo implements Serializable{
	
	private static final long serialVersionUID = 3063478820902783897L;
	
	private Integer status; //交互体的状态编码

	private String code;    //交互体的错误编码
	
	private String msg;     //交互体的提示信息
	
    private String subCode; //交互体的明细编码
	
	private String subMsg;  //交互体的明细信息
	
	private Object data;    //交互体的数据对象

	public MessagerVo(Integer status, String code, String msg, String subCode, String subMsg, Object data) {
		super();
		this.status = status;
		this.code = code;
		this.msg = msg;
		this.subCode = subCode;
		this.subMsg = subMsg;
		this.data = data;
	}

	public MessagerVo() {
		super();
	}

	
	public boolean isSuccess(){
		ResponseStatusEnum responseStatusEnum = ResponseStatusEnum.byCode(subCode);
		return responseStatusEnum == ResponseStatusEnum.SUCCESS;
	}
}
