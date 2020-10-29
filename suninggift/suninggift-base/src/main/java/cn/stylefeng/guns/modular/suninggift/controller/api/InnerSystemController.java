package cn.stylefeng.guns.modular.suninggift.controller.api;

import cn.stylefeng.guns.modular.suninggift.entity.WopayFtp;
import cn.stylefeng.guns.modular.suninggift.enums.InnerSystemInterfaceEnum;
import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.ApiInBizRequest;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ApiInBizResponse;
import cn.stylefeng.guns.modular.suninggift.service.InnerSystemInterfaceService;
import cn.stylefeng.guns.modular.suninggift.tools.InnerSystemServiceTool;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
@Controller
@RequestMapping("/api/innerInterface")
@Api(tags = "InnerSystemController", description = "内部系统接口控制器")
public class InnerSystemController extends BaseController {

    @Autowired
    private InnerSystemInterfaceService innerSystemInterfaceService;

    @Autowired
    private InnerSystemServiceTool innerSystemServiceTool;

    /**
     * 插入数据到ftp表
     * @param apiInBizRequest
     * @return
     */
    @PostMapping("")
    @ResponseBody
    @ApiOperation(value = "接口总入口", notes = "接口总入口")
    public ApiInBizResponse index(ApiInBizRequest apiInBizRequest) {
        ApiInBizResponse apiInBizResponse = new ApiInBizResponse();
        apiInBizResponse.setCode(ResponseStatusEnum.EXCEPTION.getCode());
        apiInBizResponse.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
        long startTime = System.currentTimeMillis();
        log.info("内部系统接口请求：" + JSONObject.toJSONString(apiInBizRequest));
        //校验公共参数及验签
        String s = innerSystemServiceTool.verifyPublicParam(apiInBizRequest);
        if(StringUtils.isNotBlank(s)){
            apiInBizResponse.setCode(ResponseStatusEnum.CONTROLLER_ERROR_PARAMS.getCode());
            apiInBizResponse.setMessage(s);
            return apiInBizResponse;
        }

        //获取方法名
        InnerSystemInterfaceEnum byCode = InnerSystemInterfaceEnum.getByCode(apiInBizRequest.getMethod());
//        log
        if(byCode == InnerSystemInterfaceEnum.ADD_FTP){
//            policyListResponse = JSONObject.parseObject(s1, new TypeReference<ApiInBizResponse<PolicyListResponseBizContent>>(){});
            WopayFtp wopayFtp = JSONObject.parseObject(apiInBizRequest.getBizContent().toString(), WopayFtp.class);
            apiInBizResponse = innerSystemInterfaceService.addFtp(wopayFtp);
        }else {
            apiInBizResponse.setCode(ResponseStatusEnum.CONTROLLER_ERROR_METHOD.getCode());
            apiInBizResponse.setMessage(ResponseStatusEnum.CONTROLLER_ERROR_METHOD.getMsg());
        }

        log.info("内部系统接口响应：" + JSONObject.toJSONString(apiInBizResponse));
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("内部系统接口总耗时 {}", (endTime - startTime));
        return apiInBizResponse;
    }
}
