package service;

import cn.stylefeng.guns.GunsApplication;
import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
import cn.stylefeng.guns.modular.suninggift.entity.QueueSchedule;
import cn.stylefeng.guns.modular.suninggift.enums.OperatorEnum;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.OrderDetailsQueryAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo.UserProductRecommendAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.OrderDetailsQueryRspAo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.*;
import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.modular.suninggift.service.AliService;
import cn.stylefeng.guns.modular.suninggift.service.InInterfaceService;
import cn.stylefeng.guns.modular.suninggift.service.OrderInfoService;
import cn.stylefeng.guns.modular.suninggift.service.PromotionAccountInfoService;
import cn.stylefeng.guns.modular.suninggift.service.QueueScheduleService;
import cn.stylefeng.guns.modular.suninggift.service.WopayFtpService;
import cn.stylefeng.guns.modular.suninggift.tools.QimenServiceTool;
import cn.stylefeng.guns.modular.suninggift.tools.SysConfigServiceTool;
import cn.stylefeng.guns.modular.suninggift.utils.DateUtil;
import cn.stylefeng.guns.modular.suninggift.utils.IdGeneratorSnowflake;
import cn.stylefeng.guns.modular.suninggift.utils.KafkaProducer;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayFundAuthOperationDetailQueryResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-03-09 17:59
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GunsApplication.class)
public class InterfaceServiceTest {

    @Autowired
    private InInterfaceService inInterfaceService;

    @Autowired
    private KafkaProducer kafkaService;

    @Autowired
    private QueueScheduleService queueScheduleService;

    @Autowired
    private SysConfigServiceTool sysConfigServiceTool;

    @Autowired
    private QimenServiceTool qimenServiceTool;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private WopayFtpService wopayFtpService;
    @Autowired
    private AliService aliService;

    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private PromotionAccountInfoService promotionAccountInfoService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //号卡三户校验
    @Test
    public void threeAccountVerifyTest(){
        long l = System.currentTimeMillis();
        String userPhone = "15624584659";
        OperatorEnum operatorByMobile = qimenServiceTool.getOperatorByMobile(userPhone);
        CardCenterInterfInfo byOperatorCode = sysConfigServiceTool.getByOperatorCode(operatorByMobile.getCode());
        CardCenterResponseVo<ThreeAccountVerifyData> threeAccountVerifyDataCardCenterResponseVo = inInterfaceService.threeAccountVerify(userPhone, byOperatorCode);
        System.out.println(JSONObject.toJSONString(threeAccountVerifyDataCardCenterResponseVo));
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
    }

    //中台三户校验
    @Test
    public void threeAccountVerifyMiddleSystemTest(){
        long l = System.currentTimeMillis();
        String userPhone = "18819468832";
        OperatorEnum operatorByMobile = qimenServiceTool.getOperatorByMobile(userPhone);
        CardCenterInterfInfo byOperatorCode = sysConfigServiceTool.getByOperatorCode(operatorByMobile.getCode());

        CardCenterResponseVo<ThreeAccountVerifyMiddleSystemData> threeAccountVerifyMiddleSystemDataCardCenterResponseVo = inInterfaceService.threeAccountVerifyMiddleSystem(userPhone, byOperatorCode, operatorByMobile.getCode());
        System.out.println(JSONObject.toJSONString(threeAccountVerifyMiddleSystemDataCardCenterResponseVo));
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
    }

    //查询订购状态
    @Test
    public void queryOrderTest(){
        String outTradeNo = "";
        OrderInfo orderInfo = orderInfoService.getById(outTradeNo);
        CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(orderInfo.getOperatorCode());
        long l = System.currentTimeMillis();
        String operatorCode = OperatorEnum.lpunicom.getCode();
        OrderDetailsQueryAo orderDetailsQueryAo = new OrderDetailsQueryAo();
        orderDetailsQueryAo.setOutOrderNo(orderInfo.getOutTradeNo());
        orderDetailsQueryAo.setOperator(orderInfo.getOperatorCode());
        orderDetailsQueryAo.setClientId(cardCenterInterfInfo.getClientId());
        orderDetailsQueryAo.setContractOrderId(orderInfo.getOperatorOrderId());
        orderDetailsQueryAo.setUserPhone(orderInfo.getPhone());
        orderDetailsQueryAo.setAuthNo(orderInfo.getAuthNo());
        orderDetailsQueryAo.setRequestTime(sdf.format(new Date()));
        orderDetailsQueryAo.setAuthNo(orderInfo.getAuthNo());
        orderDetailsQueryAo.setMonth(orderInfo.getFreezeMonth() + "");
        orderDetailsQueryAo.setPayeeLogonId(orderInfo.getPayeeLogonId());
        orderDetailsQueryAo.setPayeeUserId(orderInfo.getPayeeUserId());
        orderDetailsQueryAo.setPayerUserId(orderInfo.getUserId());


        CardCenterResponseVo<OrderDetailsQueryRspAo> orderDetailsQueryRspAoRspData = inInterfaceService.queryOrder(orderDetailsQueryAo, cardCenterInterfInfo);//订单查询，包含轮询查询订单逻辑，业务办理中继续查询、
        System.out.println(JSONObject.toJSONString(orderDetailsQueryRspAoRspData));
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
    }

    //号卡可办单产品政策
    @Test
    public void getPackListTest(){
        long l = System.currentTimeMillis();
        String aa = "17586898689";
        String strategyId = "17586898689";
        OperatorEnum operatorByMobile = qimenServiceTool.getOperatorByMobile(aa);
        CardCenterInterfInfo byOperatorCode = sysConfigServiceTool.getByOperatorCode(operatorByMobile.getCode());

        UserProductRecommendAo userProductRecommendAo = new UserProductRecommendAo();
        userProductRecommendAo.setUserPhone(aa);
        userProductRecommendAo.setStrategyId(strategyId);
        CardCenterResponseVo<List<CardCenterResponseVoGetPackListData>> packList = inInterfaceService.getPackList(userProductRecommendAo, byOperatorCode);//订单查询，包含轮询查询订单逻辑，业务办理中继续查询
        System.out.println(JSONObject.toJSONString(packList));
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
    }

    //中台卡可办单产品政策
    @Test
    public void getPackListMiddleSystemTest(){
        long l = System.currentTimeMillis();
        String userPhone = "18819468832";
        OperatorEnum operatorByMobile = qimenServiceTool.getOperatorByMobile(userPhone);
        CardCenterInterfInfo byOperatorCode = sysConfigServiceTool.getByOperatorCode(operatorByMobile.getCode());

        CardCenterResponseVo<GetPackListMiddleSystemResponseData> middleSystemResponseVoGetPackListDataCardCenterResponseVo = inInterfaceService.getPackListMiddleSystem(userPhone, byOperatorCode, byOperatorCode.getOperatorCode());//订单查询，包含轮询查询订单逻辑，业务办理中继续查询
        System.out.println(JSONObject.toJSONString(middleSystemResponseVoGetPackListDataCardCenterResponseVo));
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
    }

    //插入队列
    @Test
    public void addToQueueTest(){
        //落库队列执行查询操作
        QueueSchedule queueSchedule = new QueueSchedule();
        queueSchedule.setFlowNo(IdGeneratorSnowflake.generateLongId()+"");
        queueSchedule.setCreateTime(new Date());
        queueSchedule.setNotifyCount(0);
        queueSchedule.setOutTradeNo("121221211212");
        queueSchedule.setStatus(0);
        queueSchedule.setNextNotifyTime(queueSchedule.getCreateTime());
        queueSchedule.setType("tmall_qimen");
        //插入数据库
        queueScheduleService.myAdd(queueSchedule);
        //插入kafka队列
        boolean aBoolean = kafkaService.addToQueue(queueSchedule);
    }

    //插入队列
    @Test
    public void wopayFtpServiceTest(){
        //获取前一天的时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, -1);
        Date startTime = DateUtil.getStartTimeByDate(instance.getTime());
        Date endTime = DateUtil.getEndTimeByDate(instance.getTime());

        wopayFtpService.uploadFtp(startTime, endTime);
    }

    //查询订购状态
    @Test
    public void aliServiceTest(){
        String freezeAppId = sysConfigService.getByCode("freezeAppId");
        PromotionAccountInfo byAppId = promotionAccountInfoService.getByAppId(freezeAppId);
        String out_order_no = "974697251044782759";
        String out_request_no = "110291302253320050803506001";
        AlipayFundAuthOperationDetailQueryResponse alipayFundAuthOperationDetailQueryResponse = aliService.fundAuthOperationDetailQuery(byAppId, out_order_no, out_request_no);

    }

}
