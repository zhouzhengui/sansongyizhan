package cn.stylefeng.guns.modular.suninggift.controller.api;

import cn.stylefeng.guns.modular.suninggift.entity.QueueSchedule;
import cn.stylefeng.guns.modular.suninggift.entity.WopayFtp;
import cn.stylefeng.guns.modular.suninggift.enums.OperatorEnum;
import cn.stylefeng.guns.modular.suninggift.model.InBizRespond;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.CardCenterOrderCreateResponseData;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.CardCenterResponseVo;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.CardCenterResponseVoIdentifyDealResponseData;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestBaseofferGetData;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestCreateOrder;
import cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo.BaseofferGetResponseData;
import cn.stylefeng.guns.modular.suninggift.model.constant.CardCenterInterfInfo;
import cn.stylefeng.guns.modular.suninggift.model.params.WopayFtpParam;
import cn.stylefeng.guns.modular.suninggift.service.InInterfaceService;
import cn.stylefeng.guns.modular.suninggift.service.QimenService;
import cn.stylefeng.guns.modular.suninggift.service.QueueScheduleService;
import cn.stylefeng.guns.modular.suninggift.service.TmllOrderDisposeService;
import cn.stylefeng.guns.modular.suninggift.tools.QimenServiceTool;
import cn.stylefeng.guns.modular.suninggift.tools.SysConfigServiceTool;
import cn.stylefeng.guns.modular.suninggift.utils.IdGeneratorSnowflake;
import cn.stylefeng.guns.modular.suninggift.utils.KafkaProducer;
import cn.stylefeng.guns.modular.suninggift.service.WopayFtpService;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Controller
@RequestMapping("/api/test")
@Api(tags = "testController", description = "测试控制器")
public class TestController extends BaseController {

    @Autowired
    private QimenService qimenService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private QueueScheduleService queueScheduleService;

    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private QimenServiceTool qimenServiceTool;
    @Autowired
    private SysConfigServiceTool sysConfigServiceTool;
    @Autowired
    private InInterfaceService inInterfaceService;

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    private WopayFtpService wopayFtpService;

    @Autowired
    private TmllOrderDisposeService tmllOrderDisposeService;

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }

    @GetMapping("/send/{messge}")
    @ResponseBody
    public String send(@PathVariable String messge) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("aa","123");
        jsonObject.put("bb",33);
        jsonObject.put("data", new Date());

        kafkaTemplate.send("red-package-test",  jsonObject.toJSONString());
        return messge;
    }

    @GetMapping("createOrder")
    @ResponseBody
    @ApiOperation(value = "订单创建", notes = "订单创建")
    public InBizRespond createOrder(OutBizRequestCreateOrder outBizRequestCreateOrder) {
        long startTime = System.currentTimeMillis();
        log.info("订单创建请求:" + JSONObject.toJSONString(outBizRequestCreateOrder));
        InBizRespond<CardCenterOrderCreateResponseData> order = qimenService.createOrder(outBizRequestCreateOrder);
        log.info("订单创建响应:" + JSONObject.toJSONString(order));
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("订单创建耗时 {} 毫秒",(endTime - startTime));
        return order;
    }

    @GetMapping("kafkaOrderDispose")
    @ResponseBody
    @ApiOperation(value = "kafkaOrderDispose", notes = "kafka消息推送订单信息")
    public String kafkaOrderDispose(String flowNo) {
        QueueSchedule queueSchedule = queueScheduleService.getById(flowNo);
        long l = System.currentTimeMillis();
        //String s = qimenService.kafkaOrderDispose(queueSchedule);
        String s = null;
        System.out.println("响应参数："+s);
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
        return s;
    }

    @GetMapping("kafkaTest")
    @ResponseBody
    @ApiOperation(value = "kafkaTest", notes = "kafka消息推送订单信息")
    public String kafkaTest(String outTradeNo){ //插入队列

        //落库队列执行查询操作
        QueueSchedule queueSchedule = new QueueSchedule();
        queueSchedule.setFlowNo(IdGeneratorSnowflake.generateLongId()+"");
        queueSchedule.setCreateTime(new Date());
        queueSchedule.setNotifyCount(0);
        queueSchedule.setOutTradeNo(outTradeNo);
        queueSchedule.setStatus(0);
        queueSchedule.setNextNotifyTime(queueSchedule.getCreateTime());
        queueSchedule.setType("tmall_qimen");
        //插入数据库
        queueScheduleService.myAdd(queueSchedule);
        //插入kafka队列
//        kafkaProducer.send(kafkaProducer.kafkaMessageTest, JSON.toJSONString(crmsFtpContent));
        boolean aBoolean = kafkaProducer.addToQueue(queueSchedule);
        return aBoolean+"";
    }

    @GetMapping("flashConfig")
    @ResponseBody
    @ApiOperation(value = "flashConfig", notes = "刷新配置表")
    public String flashConfig(){
        //落库队列执行查询操作
        String s = sysConfigService.flushConfig();
        return s;
    }

    @GetMapping("threeAccountVerify")
    @ResponseBody
    @ApiOperation(value = "threeAccountVerify", notes = "三户校验")
    public CardCenterResponseVo threeAccountVerify(String userPhone){
        long startTime = System.currentTimeMillis();
        OperatorEnum operatorEnum = qimenServiceTool.getOperatorByMobile(userPhone);
        log.info("threeAccountVerify请求:" + JSONObject.toJSONString(operatorEnum));
        CardCenterInterfInfo cardCenterInterfInfo = sysConfigServiceTool.getByOperatorCode(operatorEnum.getCode());
        CardCenterResponseVo threeAccountVerifyDataCardCenterResponseVo = null;
        if(operatorEnum == OperatorEnum.lpunicom){
            threeAccountVerifyDataCardCenterResponseVo = inInterfaceService.threeAccountVerify(userPhone, cardCenterInterfInfo);
        }else{
            threeAccountVerifyDataCardCenterResponseVo = inInterfaceService.threeAccountVerifyMiddleSystem(userPhone, cardCenterInterfInfo, operatorEnum.getCode());
        }
        log.info("threeAccountVerify响应:" + JSONObject.toJSONString(threeAccountVerifyDataCardCenterResponseVo));
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) > 6000){
            log.info("接口大于6秒 {}", (endTime - startTime));
        }
        log.info("threeAccountVerify耗时 {} 毫秒",(endTime - startTime));
        return threeAccountVerifyDataCardCenterResponseVo;
    }

    @GetMapping("getPackList")
    @ResponseBody
    @ApiOperation(value = "getPackList", notes = "可办理套餐")
    public InBizRespond getPackList(String userPhone){
        long l = System.currentTimeMillis();
        OutBizRequestBaseofferGetData outBizRequestBaseofferGetData = new OutBizRequestBaseofferGetData();
        outBizRequestBaseofferGetData.setMobile(userPhone);
        InBizRespond<List<BaseofferGetResponseData>> listInBizRespond = qimenService.baseofferGet(outBizRequestBaseofferGetData);
        System.out.println(JSONObject.toJSONString(listInBizRespond));
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
        return listInBizRespond;
    }

    @GetMapping("deleteWopayFtp")
    @ResponseBody
    @ApiOperation(value = "deleteWopayFtp", notes = "删除WopayFtp")
    public String deleteWopayFtp(String tradeNo){

        WopayFtpParam wopayFtpParam = new WopayFtpParam();
        wopayFtpParam.setTradeNo(tradeNo);
        wopayFtpService.delete(wopayFtpParam);

        return "已执行";
    }

    @GetMapping("putSuNingOrderToWopayFtp")
    @ResponseBody
    @ApiOperation(value = "putSuNingOrderToWopayFtp", notes = "推送苏宁易购订单给WopayFtp")
    public String putSuNingOrderToWopayFtp(WopayFtp wopayFtp){

        if(wopayFtp == null){
            return "请求参数为空";
        }
        tmllOrderDisposeService.orderSyncWsnForftp(wopayFtp);

        return "已执行";
    }
}
