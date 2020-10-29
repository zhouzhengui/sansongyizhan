package cn.stylefeng.guns.modular.suninggift.tools;

import cn.stylefeng.guns.modular.suninggift.enums.InnerSystemInterfaceEnum;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.ApiInBizRequest;
import cn.stylefeng.guns.modular.suninggift.entity.WopayFtp;
import cn.stylefeng.guns.modular.suninggift.utils.DESUtil;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSONObject;
import com.gzlplink.cloud.messagesend.vo.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-05-10 16:51
 */
@Slf4j
@Component
public class InnerSystemServiceTool {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 验证插入的数据是否正常
     * @param wopayFtp
     * @return null 代表无异常
     */
    public String verifyAddData(WopayFtp wopayFtp){

        if(null == wopayFtp){
            return "wopayFtp非空";
        }

        if(StringUtils.isBlank(wopayFtp.getTradeNo())){
            return "tradeNo非空";
        }
        if(StringUtils.isBlank(wopayFtp.getTradeFlowNo())){
            return "tradeFlowNo非空";
        }
        if(StringUtils.isBlank(wopayFtp.getPhoneNo())){
            return "phoneNo非空";
        }
        if(StringUtils.isBlank(wopayFtp.getStagesCode())){
            return "stagesCode非空";
        }
        if(StringUtils.isBlank(wopayFtp.getProductId())){
            return "productId非空";
        }
        if(null == wopayFtp.getCreditDate()){
            return "creditDate非空";
        }
        if(null == wopayFtp.getFinishDate()){
            return "finishDate非空";
        }
        if(StringUtils.isBlank(wopayFtp.getTradeStatus())){
            return "tradeStatus非空";
        }
        if(null == wopayFtp.getRefundDate()){
            return "refundDate非空";
        }
        if(StringUtils.isBlank(wopayFtp.getProductName())){
            return "productName非空";
        }
        if(null ==wopayFtp.getFqNum()){
            return "fqNum非空";
        }
        if(null == wopayFtp.getOrderAmount()){
            return "orderAmount非空";
        }
        if(null == wopayFtp.getFqSellerPercent()){
            return "fqSellerPercent非空";
        }
        if(null == wopayFtp.getFqRate()){
            return "fqRate非空";
        }
        if(StringUtils.isBlank(wopayFtp.getPayUserId())){
            return "payUserId非空";
        }
        if(StringUtils.isBlank(wopayFtp.getProvince())){
            return "province非空";
        }
        if(StringUtils.isBlank(wopayFtp.getCity())){
            return "city非空";
        }
        if(StringUtils.isBlank(wopayFtp.getBusinessType())){
            return "businessType非空";
        }

        return null;
    }

    /**
     * 验证公共参数及签名
     * @param apiInBizRequest
     * @return null 代表无异常
     */
    public String verifyPublicParam(ApiInBizRequest apiInBizRequest){
        String flowNo = apiInBizRequest.getFlowNo();
        String method = apiInBizRequest.getMethod();
        String time = apiInBizRequest.getTime();
        String sign = apiInBizRequest.getSign();

        if(null == apiInBizRequest || "{}".equals(JSONObject.toJSONString(apiInBizRequest))){
            return "请求内容非空";
        }

        if(StringUtils.isBlank(flowNo)){
            return "flowNo非空";
        }
        if(StringUtils.isBlank(method)){
            return "method非空";
        }
        if(StringUtils.isBlank(time)){
            return "time非空";
        }
        if(StringUtils.isBlank(apiInBizRequest.getSign())){
            return "sign非空";
        }

        InnerSystemInterfaceEnum byCode = InnerSystemInterfaceEnum.getByCode(method);
        if(null == byCode){
            return ResponseStatusEnum.CONTROLLER_ERROR_METHOD.getMsg();
        }

        //验签 flowNo + method + time
        String desKey = sysConfigService.getByCode("systemDesKey");

        String mySign = DESUtil.encryptDES(flowNo + method + time, desKey);
        if(!sign.equals(mySign)){
            return ResponseStatusEnum.CONTROLLER_ERROR_CHECK_SIGN.getMsg();
        }
        return null;
    }
}
