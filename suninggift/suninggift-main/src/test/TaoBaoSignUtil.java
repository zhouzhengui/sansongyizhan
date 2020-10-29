
import cn.stylefeng.guns.modular.suninggift.enums.ProfilesEnum;
import cn.stylefeng.guns.modular.suninggift.enums.TmallResponseEnum;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

public class TaoBaoSignUtil {

    private static final String url="https://eco.taobao.com/router/rest";
    private static final String appkey="28315549";
    private static final String secret="a44159b142642bec2104ce7f729ece70";
    private static final String sessionKey="610151572f8a4ddacd98cb51880f89bc3fb78c26549c7332201230566418";
                                    //6100613eabffa0e0b9280145323012274794eb58b9a81dd2201230566418
    @Test
    public void testGiftQueryorderdetail() throws ApiException{

        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaWtOrderGiftQueryorderdetailRequest req = new AlibabaWtOrderGiftQueryorderdetailRequest();
        req.setOrderId("11111111111");
        req.setTaobaoId("111111");
        AlibabaWtOrderGiftQueryorderdetailResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());

    }

    @Transactional//标记为一个事务
    @Rollback//事务执行完后回滚
    @Test
    public void testGetproductinfo() throws ApiException{

        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaAlicomOpenTradeGetproductinfoRequest req = new AlibabaAlicomOpenTradeGetproductinfoRequest();
        req.setProductId("1111");
        req.setBizType("34");
        System.out.println("请求:"+ JSONObject.toJSONString(req));
        AlibabaAlicomOpenTradeGetproductinfoResponse rsp = client.execute(req);
        System.out.println("响应:"+ JSONObject.toJSONString(rsp));

    }

    @Test
    public void testGetchtoken() throws ApiException{

        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaAlicomOpenTradeFetchtokenRequest req = new AlibabaAlicomOpenTradeFetchtokenRequest();
        req.setUserId(123L);
        System.out.println("请求:"+ JSONObject.toJSONString(req));
        AlibabaAlicomOpenTradeFetchtokenResponse rsp = client.execute(req);
        System.out.println("响应:"+ JSONObject.toJSONString(rsp));

    }

    @Test
    public void testGetgiftdetails() throws ApiException{

        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaAlicomOpenTradeGetgiftdetailsRequest req = new AlibabaAlicomOpenTradeGetgiftdetailsRequest();
        req.setActivityId("123456");
        System.out.println("请求:"+ JSONObject.toJSONString(req));
        AlibabaAlicomOpenTradeGetgiftdetailsResponse rsp = client.execute(req);
        System.out.println("响应:"+ JSONObject.toJSONString(rsp));

    }


    @Test
    public void testTradeCreateOrder() throws ApiException {

        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaAlicomOpenTradeCreateorderRequest req = new AlibabaAlicomOpenTradeCreateorderRequest();
        AlibabaAlicomOpenTradeCreateorderRequest.CreateOrderRequestDto obj1 = new AlibabaAlicomOpenTradeCreateorderRequest.CreateOrderRequestDto();
        obj1.setActivityId(123456L);
        obj1.setTransferId("123456");
        obj1.setPhone("13600000000");
        obj1.setPrice("1");
        obj1.setSource("tmall_hfb");
        obj1.setSellerNick("maijiaNick");
        obj1.setAlipayId(123L);
        obj1.setTaobaoNick("taobaoNick");
        obj1.setTaobaoToken("taobaoToken");
        obj1.setGiftId(1234L);
        obj1.setProductName("测试产品");
        obj1.setExt("{\"param\":1234}");
        obj1.setProductId("123");
        req.setParam0(obj1);
        AlibabaAlicomOpenTradeCreateorderResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());


    }

    @Test
    public void openPermission() throws ApiException{
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TmcUserPermitRequest req = new TmcUserPermitRequest();
        req.setTopics("taobao_wt_OpenTradeMsg");
        TmcUserPermitResponse rsp = client.execute(req, sessionKey);
        System.out.println(rsp.getBody());
    }

    @Test
    public void testWtTradeOrderResultcallback() throws ApiException{
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        WtTradeOrderResultcallbackRequest req = new WtTradeOrderResultcallbackRequest();
        WtTradeOrderResultcallbackRequest.OrderResultDto obj1 = new WtTradeOrderResultcallbackRequest.OrderResultDto();
        obj1.setDesc("定单处理完成");
        obj1.setOrderNo(12345678L);
        obj1.setResultCode("0000");
        obj1.setSuccess(true);
        req.setParam0(obj1);
        WtTradeOrderResultcallbackResponse rsp = client.execute(req, sessionKey);
        if(null != rsp &&
            null != rsp.getResult()){
            WtTradeOrderResultcallbackResponse.CommonRtnDo result = rsp.getResult();
            String code = result.getCode();
            if(null != code){
                TmallResponseEnum tmallResponseEnum = TmallResponseEnum.byCode(code);
                if(null != tmallResponseEnum){
                    //不为空则不触发5秒轮询
                }
            }

        }


        System.out.println(rsp.getBody());
    }


}
