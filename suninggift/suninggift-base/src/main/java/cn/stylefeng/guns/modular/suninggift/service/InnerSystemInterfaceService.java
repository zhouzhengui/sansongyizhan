package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.ApiInBizResponse;
import cn.stylefeng.guns.modular.suninggift.tools.InnerSystemServiceTool;
import cn.stylefeng.guns.modular.suninggift.entity.WopayFtp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-05-10 16:49
 */
@Service
@Slf4j
public class InnerSystemInterfaceService {

    @Autowired
    private WopayFtpService wopayFtpService;

    @Autowired
    private InnerSystemServiceTool innerSystemServiceTool;

    /**
     * 新增ftp数据
     * @param wopayFtp
     * @return
     */
    public ApiInBizResponse addFtp(WopayFtp wopayFtp) {
        ApiInBizResponse apiInBizResponse = new ApiInBizResponse();
        apiInBizResponse.setCode(ResponseStatusEnum.EXCEPTION.getCode());
        apiInBizResponse.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());

        String s = innerSystemServiceTool.verifyAddData(wopayFtp);
        if(StringUtils.isNotBlank(s)){
            apiInBizResponse.setCode(ResponseStatusEnum.CONTROLLER_ERROR_PARAMS.getCode());
            apiInBizResponse.setMessage(s);
            return apiInBizResponse;
        }

        boolean add = wopayFtpService.add(wopayFtp);
        if(!add){
            log.info("插入wopayftp表错误");
            apiInBizResponse.setCode(ResponseStatusEnum.EXCEPTION.getCode());
            apiInBizResponse.setMessage(ResponseStatusEnum.EXCEPTION.getMsg());
            return apiInBizResponse;
        }

        apiInBizResponse.setCode(ResponseStatusEnum.SUCCESS.getCode());
        apiInBizResponse.setMessage(ResponseStatusEnum.SUCCESS.getMsg());
        return apiInBizResponse;
    }

}
