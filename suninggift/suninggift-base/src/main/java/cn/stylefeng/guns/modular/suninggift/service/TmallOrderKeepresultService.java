package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo.OutApiResponse;
import cn.stylefeng.guns.modular.suninggift.model.params.SuningOrderKeepresultParam;

import java.util.Map;

/**
 * @ClassNameTmallOrderKeepresultService
 * @Description TODO天猫存送业务履约、毁约通知接口
 * @Author tangxiong
 * @Date 2020/2/24 14:51
 **/
public interface TmallOrderKeepresultService {

    //存送业务履约、毁约通知接口==联通
    Map<String, Object> orderKeepresult(Map<String, Object> paramMap);

    //存送业务履约、毁约通知接口==移动
    Map<String, Object> cmccOrderKeepresult(Map<String, Object> paramMap);

    //业务办理结果查询
    Map<String, Object> orderQuery(Map<String, Object> paramMap);

    //苏宁履约通知接口
    OutApiResponse suningOrderKeepresultDispose(SuningOrderKeepresultParam param);
}
