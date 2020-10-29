import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundAuthOperationDetailQueryRequest;
import com.alipay.api.response.AlipayFundAuthOperationDetailQueryResponse;

public class TestAlipayFreezeQuery {

  public static void main(String[] args) throws AlipayApiException {
    //乐芃12
    String appId = "2019051364474397";
    String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCvoXVq/f0d1wVk+jidKH4y9C/EVJzCXy7WuFc0d6pHLuqpFgRvPDCZnJGVPFpXi49HjJ58HFfz9qMupMm0ChS+pO686B1enk6NLeG56P7z5cK4sPkcPwqB20wWdKvNOL4U2Jy/9s2+thj43XjLRvbvlBino2xD2Y02GRhbrVQKj29N62MdH9UYlN6ECaUK9UkFSIPZMPhzMkBf+nII2USLrI0cydUR7Vt74RvfxZhhfU7ejtOV3G357NA+EfUYi4GQFcxHMQmm/KcpeY2RJ2ODjYADzfG8E6088mcSNZEKsb59C5pJO/DYcx52zYkt41DGkclpGN8WHRU+qyA/Z/8pAgMBAAECggEBAJIVGuQeVJ2wToVZ9yMJHkblgqGruyOVJhFlDOvQKOrCZF61uISJLOdoKlNV+tLcexWJKf2TWbjLSZmvaTH+PJkB+K1Z56vewvl800bePMnDLv6N/PX4nZW7QQNgkdHFw5z1OxQUhbd+ITFGjYWPBi/qH8O4E4Iv/tujbfY7Ki8JFC179sKa1iVHAd0c4sgYhtKLlkvuMdRMFZI3Ok349wsy7iei2lbPPEVHymojHGJ22ZEt4+TiY6quKU9A6tsalm3WGtDgqLe9OgapMnmBZclI0IfQi5QUb/lt9AffGn60zICePFkxTFlwNCL38fXi12oVcVnRRqaSmTSLH5cKObECgYEA2eVm+GG1qkVAaYGcCm8MlGKOmRt9V7MXUD6fvf311sEogqoxO9IFyG/excpRt7J5DWc3qSVSq6/phAmZqhJYZepmbH549rWTYVUc2MECyu0c0YhtecEHOGZZQ5YV2O87gHdyaP+LHkiwEJhJH1ZzN7p+tTKu1moLx80oySv02XUCgYEAzlfz5rdn7aamCl/uweOStkihaF++xMhmOGZ2GChFmanDj7FuuzIxggQFOwnECoLKXA516ExIBuWjwZZHVpwhskFr1prv8W2oI3edJfZgShJbzX0YZvCGMF3pu/5KZb1hKNu/B+DpyTxnmumRN0SUsKOuX8jArawyqoOVanda5GUCgYBi6YYivogu8RzaSCHDgsGdIlhFHHt7siqlHKh7IKvWfvyaxBNxaleBs3bXJo6N6xCUiwRzXiP3F/XTIGCHGajTKuKv4YGbzLVIe9CKfKN95sEsRNgdawiSC7P9wEXOolrKaG6hR0+pwala/yZCeYX4CEq4indfkegjHPikybVueQKBgH4/rglBuiLbzceXPpv+w2soMSmNO0lnW7OybRsGuN710aN/akK6yTPFhfXDWeEcePeuGVISKheeNX1ily9UxAEuqe5aSeirsS5SJyJtBikL5oNpwJ5rF49MRpAxXA+3uVSobs9BO4lWngVYVvM70MXqv+v3Bny8WRue+rJzaHdJAoGADW9WwtJlUP7u6Hi31//FQAL3l1stV/h/6IFhq9KI0AEc6PuE2BpYvLAco4GMqfDjldd4KVfzNVzCqSSGX6hD2dubRZ+m9qr7XNy1FEz3OUDarba/Y+cA3IJfLvUllrLYjiMMd0iRzVdpdr0Uh44WQOTuRAjulpyvzM1FGBp0tH0=";
    String aliPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0MZXQGzdX9OP7tyZKGRvOmca0tmEV8T8dRc4IOs47bOAZ0iZuVUYLWPBloKEMCMrG1rNHM6Pg7cRbddngg1ovs493uk3bhaEAq9+8E0x/EfPTyLPXqnYQ6/7BxZ2c8rZNPc0FjUwdDokcyIOgJRdLmXlVFO+Lnlrl+859VhrXAt7lEzKdx0ba8dXMGqKoUu8hnpCs9EF73GuF8ZWYocCU9MD/QPQoQl3Fsm/5MD6mM0AUkjwRS3Hm6Uash5xP6O8nWkYqRyHmBykeOjnJw542dA9XeD0Jh0XNpf3oDQa3Xxxb84o2nGk3Nzm36YtvvVa/3YIWl3s9QcqpCHPIOMjAwIDAQAB";

    AlipayClient alipayClient = new DefaultAlipayClient(
        "https://openapi.alipay.com/gateway.do",
        appId,
        privateKey,
        "json",
        "utf-8",
        aliPublicKey,
        "RSA2");

    AlipayFundAuthOperationDetailQueryRequest request = new AlipayFundAuthOperationDetailQueryRequest();
    request.setBizContent("{" +

//		"\"auth_no\":\"2020050810002001960589535136\"," +
//		"\"operation_id\":\"20200508056405469605\"," +
//		"\"auth_no\":\"110291302336220041203420001\"," +
//		"\"operation_id\":\"934919618113782759\"," +

        "\"out_order_no\":\"2020092516421009464722\"," +
        "\"out_request_no\":\"2020092516421009464722\"" +
        "  }");
    AlipayFundAuthOperationDetailQueryResponse response = alipayClient.execute(request);
    if(response.isSuccess()){
      System.out.println("调用成功");
      System.out.println(JSON.toJSONString(response));
    } else {
      System.out.println("调用失败");
      System.out.println(JSON.toJSONString(response));
    }

  }

}
