package cn.stylefeng.guns.core.xxljob;

import cn.stylefeng.guns.modular.suninggift.entity.OrderInfo;
import cn.stylefeng.guns.modular.suninggift.entity.QueueSchedule;
import cn.stylefeng.guns.modular.suninggift.model.OutBizResponse;
import cn.stylefeng.guns.modular.suninggift.service.OrderInfoService;
import cn.stylefeng.guns.modular.suninggift.service.QimenService;
import cn.stylefeng.guns.modular.suninggift.service.QueueScheduleService;
import cn.stylefeng.guns.modular.suninggift.service.RightsNotifyService;
import cn.stylefeng.guns.modular.suninggift.utils.ChatbotSendUtil;
import cn.stylefeng.guns.modular.suninggift.utils.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import java.util.ArrayList;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * XxlJob开发示例（Bean模式）
 * <p>
 * 开发步骤：
 * 1、在Spring Bean实例中，开发Job方法，方式格式要求为 "public ReturnT<String> execute(String param)"
 * 2、为Job方法添加注解 "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy = "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 3、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2019-12-11 21:52:51
 */
@Slf4j
@Component
public class SampleXxlJob {

    @Autowired
    private QueueScheduleService queueScheduleService;

    @Autowired
    private QimenService qimenService;

    @Autowired
    private ChatbotSendUtil chatbotSendUtil;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private RightsNotifyService rightsNotifyService;

    /**
     * 定时查询苏宁订单订购状态
     */
    @XxlJob("taskOrderDispose")
    public ReturnT<String> taskOrderDispose(String param) {
        XxlJobLogger.log("开始查询苏宁订单订购状态");
        log.info("开始查询苏宁订单订购状态");
        try {
            QueryWrapper<QueueSchedule> queryWrapper = new QueryWrapper<QueueSchedule>();
            queryWrapper.eq("status", 0);
            List<QueueSchedule> queueScheduleList = queueScheduleService.list(queryWrapper);
            if (queueScheduleList == null || queueScheduleList.size() <= 0) {
                log.info("暂无需要查询订购状态的订单：");
                return ReturnT.SUCCESS;
            }

            for (int i = 0; i < queueScheduleList.size(); i++) {
                String str = qimenService.taskOrderDispose(queueScheduleList.get(i));
                if (!StringUtils.isEmpty(str)) {
                    chatbotSendUtil.sendMsg(str);
                }
            }
        } catch (Exception e) {
            log.error("查询苏宁订单订购状态异常", e);
            XxlJobLogger.log("查询苏宁订单订购状态异常");
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 定时查询苏宁新用户入网订单号码激活状态
     */
    @XxlJob("taskSuNingNewUnicomOrderPhoneStaus")
    public ReturnT<String> taskSuNingNewUnicomOrderPhoneStaus(String param) {
        XxlJobLogger.log("开始查询苏宁新用户入网订单号码激活状态");
        log.info("开始查询苏宁新用户入网订单号码激活状态");
        String startTime = null;
        String endTime = null;
        if(!StringUtils.isEmpty(param)){
            startTime = JSONObject.parseObject(param).getString("startTime");
            endTime = JSONObject.parseObject(param).getString("endTime");
        }

        try {
            //获取系统当前时间
            String startDateTime = DateUtil.formatDate(DateUtil.getDayStart(-12) ,"yyyy-MM-dd HH:mm:ss");
            String endDateTime = DateUtil.formatDate(DateUtil.getDayStart(0) ,"yyyy-MM-dd HH:mm:ss");
            //获取12天内等待发货的订单数据
            QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<OrderInfo>();
            queryWrapper.eq("status", 4);
            if(!StringUtils.isEmpty(startTime)){
                queryWrapper.gt("create_time" , startTime) ;
            }else {
                queryWrapper.gt("create_time" , startDateTime) ;
            }

            if(!StringUtils.isEmpty(endTime)){
                queryWrapper.le("create_time" , endTime);
            }else{
                queryWrapper.le("create_time" , endDateTime);
            }

            queryWrapper.eq("business_type" ,"xyhrw");
            List<OrderInfo> orderInfoList = orderInfoService.list(queryWrapper);
            if (orderInfoList == null || orderInfoList.size() <= 0) {
                log.info("时间段为:startDateTime="+startDateTime+" , endDateTime= "+endDateTime+" ,苏宁新用户入网订单号码激活状态的订单数为空");
                return ReturnT.SUCCESS;
            }

            //收集失败和成功的订单信息
            List<JSONObject> successList = new ArrayList<JSONObject>();
            List<JSONObject> failList = new ArrayList<JSONObject>();

            //遍历集合
            for (OrderInfo orderInfo : orderInfoList) {

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("unicomPhoneNo" , orderInfo.getPhone());
                //查询联通苏宁新用户入网订单号卡的消息状态
                OutBizResponse unfreezeMessagerVo = rightsNotifyService.snCardQueryByMobile(jsonBody.toJSONString());
                if("10000".equals(unfreezeMessagerVo.getSubCode())) {
                    JSONObject successObject = new JSONObject();
                    successObject.put("outOrderNo" ,orderInfo.getAuthOrderNo());
                    successObject.put("phone" ,orderInfo.getPhone());
                    successObject.put("isSuccess" ,"success");
                    successObject.put("subMsg" , unfreezeMessagerVo.getSubMsg());
                    successList.add(successObject);
                    continue;
                }else {
                    JSONObject failObject = new JSONObject();
                    failObject.put("outOrderNo" ,orderInfo.getAuthOrderNo());
                    failObject.put("phone" ,orderInfo.getPhone());
                    failObject.put("isSuccess" ,"fail");
                    failObject.put("subMsg" , unfreezeMessagerVo.getSubMsg());
                    failList.add(failObject);
                    continue;
                }

            }

            log.info("查询苏宁新用户入网订单号码激活状态处理成功订单信息 ," + JSONObject.toJSONString(successList));
            log.info("查询苏宁新用户入网订单号码激活状态处理失败订单信息 ," + JSONObject.toJSONString(failList));
        } catch (Exception e) {
            log.error("查询苏宁新用户入网订单号码激活状态异常", e);
            XxlJobLogger.log("查询苏宁新用户入网订单号码激活状态异常");
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 定时查询苏宁新用户入网订单在12天都没有激活的数据进行返销操作
     */
    @XxlJob("taskSuNingNewUnicomOrderRefund")
    public ReturnT<String> taskSuNingNewUnicomOrderRefund(String param) {
        XxlJobLogger.log("开始查询苏宁新用户入网订单在12天都没有激活的数据进行返销操作");
        log.info("开始查询苏宁新用户入网订单在12天都没有激活的数据进行返销操作");
        String startTime = null;
        String endTime = null;
        if(!StringUtils.isEmpty(param)){
            startTime = JSONObject.parseObject(param).getString("startTime");
            endTime = JSONObject.parseObject(param).getString("endTime");
        }

        try {
            //获取系统当前时间
            String startDateTime = DateUtil.formatDate(DateUtil.getDayStart(-13) ,"yyyy-MM-dd HH:mm:ss");
            String endDateTime = DateUtil.formatDate(DateUtil.getDayEnd(-12) ,"yyyy-MM-dd HH:mm:ss");
            //获取12天后等待发货的订单数据
            QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<OrderInfo>();
            queryWrapper.eq("status", 4);
//            if(!StringUtils.isEmpty(startTime)){
//                queryWrapper.gt("create_time" , startTime) ;
//            }else {
//                queryWrapper.gt("create_time" , startDateTime) ;
//            }

//            if(!StringUtils.isEmpty(endTime)){
//                queryWrapper.le("create_time" , endTime);
//            }else{
//                queryWrapper.le("create_time" , endDateTime);
//            }

            if(!StringUtils.isEmpty(endTime) && !StringUtils.isEmpty(startTime)){
                queryWrapper.between("create_time" ,startTime ,endTime);
            }else{
                queryWrapper.between("create_time" ,startDateTime ,endDateTime);
            }

            queryWrapper.eq("business_type" ,"xyhrw");
            List<OrderInfo> orderInfoList = orderInfoService.list(queryWrapper);
            if (orderInfoList == null || orderInfoList.size() <= 0) {
                log.info("时间段为:startDateTime="+startDateTime+" , endDateTime= "+endDateTime+" ,苏宁新用户入网订单号码等待发货的订单数为空");
                return ReturnT.SUCCESS;
            }

            //收集失败和成功的订单信息
            List<JSONObject> successList = new ArrayList<JSONObject>();
            List<JSONObject> failList = new ArrayList<JSONObject>();

            //遍历集合
            for (OrderInfo orderInfo : orderInfoList) {

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("unicomPhoneNo" , orderInfo.getPhone());
                //查询联通苏宁新用户入网订单号卡的消息状态
                OutBizResponse unfreezeMessagerVo = rightsNotifyService.taskSuNingNewUnicomOrderRefund(jsonBody.toJSONString() ,orderInfo);
                if("10000".equals(unfreezeMessagerVo.getSubCode())) {
                    JSONObject successObject = new JSONObject();
                    successObject.put("outOrderNo" ,orderInfo.getAuthOrderNo());
                    successObject.put("phone" ,orderInfo.getPhone());
                    successObject.put("isSuccess" ,"success");
                    successObject.put("subMsg" , unfreezeMessagerVo.getSubMsg());
                    successList.add(successObject);
                    continue;
                }else {
                    JSONObject failObject = new JSONObject();
                    failObject.put("outOrderNo" ,orderInfo.getAuthOrderNo());
                    failObject.put("phone" ,orderInfo.getPhone());
                    failObject.put("isSuccess" ,"fail");
                    failObject.put("subMsg" , unfreezeMessagerVo.getSubMsg());
                    failList.add(failObject);
                    continue;
                }

            }

            log.info("查询查询苏宁新用户入网订单在12天都没有激活的数据进行返销操作处理成功订单信息 ," + JSONObject.toJSONString(successList));
            log.info("查询苏宁新用户入网订单在12天都没有激活的数据进行返销操作处理失败订单信息 ," + JSONObject.toJSONString(failList));
        } catch (Exception e) {
            log.error("查询苏宁新用户入网订单在12天都没有激活的数据进行返销操作异常", e);
            XxlJobLogger.log("查询苏宁新用户入网订单在12天都没有激活的数据进行返销操作异常");
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    public static void main(String[] args) {
        String startDateTime = DateUtil.formatDate(DateUtil.getDayStart(-13) ,"yyyy-MM-dd HH:mm:ss");
        String endDateTime = DateUtil.formatDate(DateUtil.getDayEnd(-12) ,"yyyy-MM-dd HH:mm:ss");
        System.out.println("startDateTime=" + startDateTime);
        System.out.println("endDateTime=" + endDateTime);
    }
}
