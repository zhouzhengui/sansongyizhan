//package cn.stylefeng.guns.config.listener;
//
//import cn.stylefeng.guns.modular.suninggift.entity.PromotionAccountInfo;
//import cn.stylefeng.guns.modular.suninggift.enums.ProfilesEnum;
//import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
//import cn.stylefeng.guns.modular.suninggift.model.InBizRespond;
//import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.CardCenterResponseVoIdentifyDealResponseData;
//import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestCreateOrder;
//import cn.stylefeng.guns.modular.suninggift.service.PromotionAccountInfoService;
//import cn.stylefeng.guns.modular.suninggift.service.QimenService;
//import cn.stylefeng.guns.modular.suninggift.utils.ChatbotSendUtil;
//import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
//import com.alibaba.fastjson.JSONObject;
//import com.taobao.api.internal.tmc.Message;
//import com.taobao.api.internal.tmc.MessageHandler;
//import com.taobao.api.internal.tmc.MessageStatus;
//import com.taobao.api.internal.tmc.TmcClient;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
///**
// * 监听天猫奇门下单信息
// */
//@Order(100)
//@Component
//@Slf4j
//public class TaobaoMsgListener  implements CommandLineRunner {
//    private static String wsUrl;
//
////    private static final String wsUrl = "ws://mc.api.taobao.com";//生产
////    private static final String wsUrl = "ws://premc.api.taobao.com";//测试
//
//    @Autowired
//    private QimenService qimenService;
//
//    @Autowired
//    private SysConfigService sysConfigService;
//
//    @Autowired
//    private PromotionAccountInfoService promotionAccountInfoService;
//
//    @Autowired
//    private ChatbotSendUtil chatbotSendUtil;
//
//    @Value("${profile}")
//    private String profile;
//
//    private long interval = 5000L;
//
//    @Override
//    public void run(String... args) throws Exception {
//        if(profile.equals(ProfilesEnum.pressure.getCode())){
//            log.info("压测环境不监听天猫");
//            return;
//        }
//
//        //监听地址
//        wsUrl = sysConfigService.getByCode("wsUrl");
//        log.info("{} -> [准备进行与服务端通信]");
//        //获取appkey跟私钥 ·
//        String appkey = sysConfigService.getByCode("appkey");//获取appkey
//        PromotionAccountInfo byAppId = promotionAccountInfoService.getByAppId(appkey);
//        String secret = byAppId.getPrivateKey();
//        try{
//            TmcClient client = new TmcClient(appkey, secret, "default"); // 关于default参考消息分组说明
//            client.setMessageHandler(new MessageHandler() {
//                public void onMessage(Message message, MessageStatus messageStatus) {
//                    try {
//                        String content = message.getContent();
//                        OutBizRequestCreateOrder outBizRequestCreateOrder = JSONObject.parseObject(content, OutBizRequestCreateOrder.class);
//                        outBizRequestCreateOrder.setApp_id(appkey);
//                        log.info("Tmc.message:"+JSONObject.toJSONString(message));
//                        log.info("Tmc.messageStatus:"+JSONObject.toJSONString(messageStatus));
//                        //调用奇门订单创建逻辑
//                        InBizRespond<CardCenterResponseVoIdentifyDealResponseData> order = qimenService.createOrder(outBizRequestCreateOrder);
//                        log.info("响应" + JSONObject.toJSONString(order));
//                        if(order.getCode().equals(ResponseStatusEnum.EXCEPTION.getCode())){
//                            chatbotSendUtil.sendMsg(order.getMessage());
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //睡眠5s后发
//                                    try{
//                                        Thread.sleep(interval);
//                                    }catch (Exception e2){
//                                        e2.printStackTrace();
//                                    }
//                                    messageStatus.fail();
//                                }
//                            }).start();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        log.error("淘宝消息队列接收异常",e);
//                        chatbotSendUtil.sendMsg("淘宝消息队列接收异常"+e.getMessage());
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //睡眠5s后发
//                                try{
//                                    Thread.sleep(interval);
//                                }catch (Exception e2){
//                                    e2.printStackTrace();
//                                }
//                                messageStatus.fail();
//                            }
//                        }).start();
//                        // 消息处理失败回滚，服务端需要重发
//                        // 重试注意：不是所有的异常都需要系统重试。
//                        // 对于字段不全、主键冲突问题，导致写DB异常，不可重试，否则消息会一直重发
//                        // 对于，由于网络问题，权限问题导致的失败，可重试。
//                        // 重试时间 5分钟不等，不要滥用，否则会引起雪崩
//                    }
//                }
//            });
//            client.connect(TaobaoMsgListener.wsUrl); // 消息环境地址：ws://mc.api.tbsandbox.com/
//        }catch (Exception e){
//            e.printStackTrace();
//            log.error("淘宝消息队列连接异常",e);
//        }
//    }
//}
