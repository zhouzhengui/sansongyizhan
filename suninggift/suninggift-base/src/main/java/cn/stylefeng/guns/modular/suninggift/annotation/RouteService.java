package cn.stylefeng.guns.modular.suninggift.annotation;

import cn.stylefeng.guns.modular.suninggift.enums.OutApiMethodAndClassEnum;
import cn.stylefeng.guns.modular.suninggift.model.InBizRequest;
import cn.stylefeng.guns.modular.suninggift.model.InBizRespond;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.*;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.*;
import cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo.BaseofferGetResponseData;
import cn.stylefeng.guns.modular.suninggift.service.QimenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@MethodHandler
@Component
public class RouteService extends DetectMethodAnnotation {

    @Autowired
    private QimenService qimenService;

    /**
     * 奇门可升档套餐查询接口
     * @param request
     * @return
     */
    @Name({OutApiMethodAndClassEnum.QIMEN_ALIBABA_ALICOM_OPENTRADE_BASEOFFER_GET, OutApiMethodAndClassEnum.GIFT_GZLPLINK_OPENTRADE_BASEOFFER_GET})
    public InBizRespond<List<BaseofferGetResponseData>> packageQuery(InBizRequest<OutBizRequestBaseofferGetData> request) {
        return qimenService.baseofferGet(request.getT());
    }

    /**
     * 奇门存送升档套餐验证码处理接口
     * @param request
     * @return
     */
    @Name({OutApiMethodAndClassEnum.QIMEN_ALIBABA_ALICOM_OPENTRADE_IDENTIFY_DEAL, OutApiMethodAndClassEnum.GIFT_GZLPLINK_OPENTRADE_IDENTIFY_DEAL})
    public InBizRespond verifyCodeDispose(InBizRequest<OutBizRequestIdentifyDealData> request) {
        return qimenService.identifyDeal(request.getT());
    }

    /**
     * 创建订购订单
     * @param request
     * @return
     */
    @Name(OutApiMethodAndClassEnum.GIFT_GZLPLINK_OPENTRADE_CREATEORDER)
    public InBizRespond createOrderDispose(InBizRequest<OutBizRequestCreateOrder> request) {
        return qimenService.createOrder(request.getT());
    }

    /**
     * 订购状态查询
     * @param request
     * @return
     */
    @Name(OutApiMethodAndClassEnum.GIFT_GZLPLINK_OPENTRADE_ORDER_STATUS_QUERY)
    public InBizRespond orderStatusQueryDispose(InBizRequest<OutBizRequestOrderStatusQuery> request) {
        return qimenService.orderStatusQuery(request.getT());
    }

    /**
     * 三户校验
     * @param request
     * @return
     */
    @Name(OutApiMethodAndClassEnum.GIFT_GZLPLINK_THREE_ACCOUNT_VERIFY)
    public InBizRespond threeAccountVerifyDispose(InBizRequest<OutBizRequestThreeAccountVerify> request) {
        return qimenService.threeAccountVerify(request.getT());
    }




}