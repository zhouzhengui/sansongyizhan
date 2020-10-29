package cn.stylefeng.guns.modular.suninggift.service;

import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
import cn.stylefeng.guns.modular.suninggift.model.constant.SysConstant;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundAuthOperationDetailQueryRequest;
import com.alipay.api.request.AlipayFundAuthOrderUnfreezeRequest;
import com.alipay.api.response.AlipayFundAuthOperationDetailQueryResponse;
import com.alipay.api.response.AlipayFundAuthOrderUnfreezeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 奇门网关无服务类
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-02-18 16:08
 */
@Slf4j
@Service
public class AliService {



    /**
     * 查询授权状态接口
     */
    public AlipayFundAuthOperationDetailQueryResponse fundAuthOperationDetailQuery(PromotionAccountInfo promotionAccountInfo, String out_order_no, String out_request_no){
        long startTime = System.currentTimeMillis();
        AlipayClient alipayClient = new DefaultAlipayClient(
                SysConstant.ALI_URL,
                promotionAccountInfo.getAppId(),
                promotionAccountInfo.getPrivateKey(),
                promotionAccountInfo.getFormat(),
                promotionAccountInfo.getCharset(),
                promotionAccountInfo.getPlatformPublicKey(),
                promotionAccountInfo.getSignType());

        AlipayFundAuthOperationDetailQueryRequest request = new AlipayFundAuthOperationDetailQueryRequest();
        request.setBizContent("{" +
            "\"out_order_no\":\"" + out_order_no + "\"," +
            "\"out_request_no\":\"" + out_request_no + "\"" +
                "  }");
        AlipayFundAuthOperationDetailQueryResponse response = null;
        try {
            log.info("查询授权状态接口请求："+ JSONObject.toJSONString(request));
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        log.info("查询授权状态接口响应："+ JSONObject.toJSONString(response));
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("查询授权状态接口耗时 {}",(endTime-startTime));
        return response;
    }

    /**
     * 支付宝预授权苏宁订单解冻
     */
    public AlipayFundAuthOrderUnfreezeResponse fundAuthOrderUnfreeze(PromotionAccountInfo promotionAccountInfo, String auth_no, String out_request_no ,String amount ,String remark){
        long startTime = System.currentTimeMillis();
        AlipayClient alipayClient = new DefaultAlipayClient(
            SysConstant.ALI_URL,
            promotionAccountInfo.getAppId(),
            promotionAccountInfo.getPrivateKey(),
            promotionAccountInfo.getFormat(),
            promotionAccountInfo.getCharset(),
            promotionAccountInfo.getPlatformPublicKey(),
            promotionAccountInfo.getSignType());

        AlipayFundAuthOrderUnfreezeRequest request = new AlipayFundAuthOrderUnfreezeRequest();
        request.setBizContent("{" +
            "\"auth_no\":\"" + auth_no + "\"," +
            "\"out_request_no\":\"" + out_request_no + "\"," +
            "\"amount\":\"" + amount + "\"," +
            "\"remark\":\"" + remark + "\"" +
            "  }");
        AlipayFundAuthOrderUnfreezeResponse response = null;
        try {
            log.info("支付宝预授权苏宁订单解冻接口请求："+ JSONObject.toJSONString(request));
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        log.info("支付宝预授权苏宁订单解冻接口响应："+ JSONObject.toJSONString(response));
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("支付宝预授权苏宁订单解冻接口耗时 {}",(endTime-startTime));
        return response;
    }

}
