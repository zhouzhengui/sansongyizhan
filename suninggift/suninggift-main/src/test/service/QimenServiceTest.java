package service;

import cn.stylefeng.guns.GunsApplication;
import cn.stylefeng.guns.modular.suninggift.entity.QueueSchedule;
import cn.stylefeng.guns.modular.suninggift.model.InBizRespond;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.CardCenterOrderCreateResponseData;
import cn.stylefeng.guns.modular.suninggift.model.api.in.response_vo.CardCenterResponseVoIdentifyDealResponseData;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestBaseofferGetData;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestCreateOrder;
import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.OutBizRequestIdentifyDealData;
import cn.stylefeng.guns.modular.suninggift.model.api.out.response_vo.BaseofferGetResponseData;
import cn.stylefeng.guns.modular.suninggift.service.QimenService;
import cn.stylefeng.guns.modular.suninggift.service.QueueScheduleService;
import cn.stylefeng.guns.modular.suninggift.tools.QimenServiceTool;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.List;

/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-03-09 17:59
 */

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GunsApplication.class)
public class QimenServiceTest {

    @Autowired
    private QimenService qimenService;

    @Autowired
    private QimenServiceTool qimenServiceTool;

    @Autowired
    private QueueScheduleService queueScheduleService;

    //套餐列表
    @Test
    public void baseofferGetTest(){
        long l = System.currentTimeMillis();
        OutBizRequestBaseofferGetData outBizRequestBaseofferGetData = new OutBizRequestBaseofferGetData();
        outBizRequestBaseofferGetData.setMobile("13150389312");//联通
        InBizRespond<List<BaseofferGetResponseData>> listInBizRespond = qimenService.baseofferGet(outBizRequestBaseofferGetData);
        System.out.println(JSONObject.toJSONString(listInBizRespond));
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
    }

    //短信测试
    @Test
    public void identifyDealTest(){
        long l = System.currentTimeMillis();
        OutBizRequestIdentifyDealData outBizRequestIdentifyDealData = new OutBizRequestIdentifyDealData();
        outBizRequestIdentifyDealData.setMobile("18819468832");
        outBizRequestIdentifyDealData.setBiz_type("CHECK_VERIFICATION_CODE");
        outBizRequestIdentifyDealData.setBiz_type("SEND_VERIFICATION_CODE");
        outBizRequestIdentifyDealData.setVerification_code("888888");
        InBizRespond<CardCenterResponseVoIdentifyDealResponseData> cardCenterResponseVoIdentifyDealResponseDataInBizRespond = qimenService.identifyDeal(outBizRequestIdentifyDealData);
        System.out.println(JSONObject.toJSONString(cardCenterResponseVoIdentifyDealResponseDataInBizRespond));
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
    }

    //中台套餐列表
    @Test
    public void baseofferGetMiddleSystemTest(){
        long l = System.currentTimeMillis();
        OutBizRequestBaseofferGetData outBizRequestBaseofferGetData = new OutBizRequestBaseofferGetData();
        outBizRequestBaseofferGetData.setMobile("18218773588");//移动
        InBizRespond<List<BaseofferGetResponseData>> listInBizRespond = qimenService.baseofferGet(outBizRequestBaseofferGetData);
        System.out.println(JSONObject.toJSONString(listInBizRespond));
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
    }

    //订单创建
//    @Rollback//事务执行完回滚
//    @Transactional//标记为事务
    @Test
    public void createOrderTest(){
        long l = System.currentTimeMillis();
        //联通存量升档
        String a = "{\n" +
                "    \"app_id\": \"28315549\",\n" +
                "    \"auth_order_no\": \"2020042915491199185945\",\n" +
                "    \"auth_request_no\": \"2020042915491199185945\",\n" +
                "    \"name\": \"测试标题\",\n" +
                "    \"operator\": \"中国联通\",\n" +
                "    \"phone\": \"13094634272\",\n" +
                "    \"status\": \"1\",\n" +
                "    \"taobao_id\": \"438785927\",\n" +
                "    \"tmall_order_id\": \"" + System.currentTimeMillis() + "\",\n" +
                "    \"total_fee\": \"120000\",\n" +
                "    \"transfer_id\": \"wt-WOF3zKAZitr6DGIhwslkyA==\",\n" +
                "    \"product_id\": \"22010000587104\",\n" +
                "    \"contract_id\": \"24\",\n" +
                "    \"item_id\": \"123\",\n" +
                "    \"user_id\": \"2088302753508680\"\n" +
                "}";

        //联通流量包
        a = "{\n" +
                "    \"app_id\": \"28315549\",\n" +
                "    \"auth_order_no\": \"2020042915491199185945\",\n" +
                "    \"auth_request_no\": \"2020042915491199185945\",\n" +
                "    \"name\": \"测试标题\",\n" +
                "    \"operator\": \"中国联通\",\n" +
                "    \"phone\": \"18611733335\",\n" +
                "    \"status\": \"1\",\n" +
                "    \"taobao_id\": \"438785927\",\n" +
                "    \"tmall_order_id\": \"" + System.currentTimeMillis() + "\",\n" +
                "    \"total_fee\": \"120000\",\n" +
                "    \"transfer_id\": \"wt-WOF3zKAZitr6DGIhwslkyA==\",\n" +
                "    \"product_id\": \"22010000587872\",\n" +
                "    \"contract_id\": \"24\",\n" +
                "    \"item_id\": \"123\",\n" +
                "    \"user_id\": \"2088302753508680\"\n" +
                "}";

        //广东移动
//        a = "{\n" +
//                "    \"app_id\": \"28315549\",\n" +
//                "    \"auth_order_no\": \"2020042915491199185945\",\n" +
//                "    \"auth_request_no\": \"2020042915491199185945\",\n" +
//                "    \"name\": \"测试标题\",\n" +
//                "    \"operator\": \"中国移动\",\n" +
//                "    \"phone\": \"13417184351\",\n" +
//                "    \"status\": \"1\",\n" +
//                "    \"taobao_id\": \"438785927\",\n" +
//                "    \"tmall_order_id\": \"" + System.currentTimeMillis() + "\",\n" +
//                "    \"total_fee\": \"120000\",\n" +
//                "    \"transfer_id\": \"wt-WOF3zKAZitr6DGIhwslkyA==\",\n" +
//                "    \"product_id\": \"22010000588034\",\n" +
//                "    \"contract_id\": \"24\",\n" +
//                "    \"item_id\": \"123\",\n" +
//                "    \"user_id\": \"2088302753508680\"\n" +
//                "}";
//        a = "{\n" +
//                "    \"app_id\": \"28315549\",\n" +
//                "    \"auth_order_no\": \"974697251044782759\",\n" +
//                "    \"auth_request_no\": \"110291302253320050803506001\",\n" +
//                "    \"contract_id\": \"\",\n" +
//                "    \"phone\": \"18641660485\",\n" +
//                "    \"product_id\": \"22010000588034\",\n" +
//                "    \"status\": \"3\",\n" +
//                "    \"taobao_id\": \"438785927\",\n" +
//                "    \"tmall_order_id\": \"974697251044782759\",\n" +
//                "    \"total_fee\": \"12\",\n" +
//                "    \"transfer_id\": \"wt-u3pJHawipQYXeVtBJMauMA==\",\n" +
//                "    \"user_id\": \"2088302753508681\"\n" +
//                "}";



        OutBizRequestCreateOrder outBizRequestCreateOrder = JSONObject.parseObject(a, OutBizRequestCreateOrder.class);
        InBizRespond<CardCenterOrderCreateResponseData> order = qimenService.createOrder(outBizRequestCreateOrder);
        System.out.println(JSONObject.toJSONString(order));
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
        Assert.assertNotNull(order);
    }

    //kafka触发创建调拨
//    @Rollback//事务执行完回滚
//    @Transactional//标记为事务
    @Test
    public void kafkaOrderDisposeTest(){
        long l = System.currentTimeMillis();
//        String flowNo = "701827509544550400";
        String flowNo = "710074654656999424";
        QueueSchedule queueSchedule = queueScheduleService.getById(flowNo);
        //String s = qimenService.kafkaOrderDispose(queueSchedule);
        String s = null;
        System.out.println("响应参数："+s);
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
    }

    //返回成功
    @Test
    public void returnSuccess(){
        long l = System.currentTimeMillis();

        qimenServiceTool.customerCallBack("974697251045782759","50000",false,"订购失败");
        long l2 = System.currentTimeMillis();
        System.out.println("耗时"+(l2-l));
    }
}
