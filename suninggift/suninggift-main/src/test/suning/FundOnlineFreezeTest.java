import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundAuthOrderAppFreezeRequest;
import com.alipay.api.response.AlipayFundAuthOrderAppFreezeResponse;

/**
 * @description:
 * @author: Xie JianBin
 * @time: 2020/6/16 16:48
 */
public class FundOnlineFreezeTest {

  public static void main(String[] args) throws AlipayApiException {

    //乐芃12
//        String appId = "2019051364474397";
//        String outPublicKey ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0MZXQGzdX9OP7tyZKGRvOmca0tmEV8T8dRc4IOs47bOAZ0iZuVUYLWPBloKEMCMrG1rNHM6Pg7cRbddngg1ovs493uk3bhaEAq9+8E0x/EfPTyLPXqnYQ6/7BxZ2c8rZNPc0FjUwdDokcyIOgJRdLmXlVFO+Lnlrl+859VhrXAt7lEzKdx0ba8dXMGqKoUu8hnpCs9EF73GuF8ZWYocCU9MD/QPQoQl3Fsm/5MD6mM0AUkjwRS3Hm6Uash5xP6O8nWkYqRyHmBykeOjnJw542dA9XeD0Jh0XNpf3oDQa3Xxxb84o2nGk3Nzm36YtvvVa/3YIWl3s9QcqpCHPIOMjAwIDAQAB";
//        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCvoXVq/f0d1wVk+jidKH4y9C/EVJzCXy7WuFc0d6pHLuqpFgRvPDCZnJGVPFpXi49HjJ58HFfz9qMupMm0ChS+pO686B1enk6NLeG56P7z5cK4sPkcPwqB20wWdKvNOL4U2Jy/9s2+thj43XjLRvbvlBino2xD2Y02GRhbrVQKj29N62MdH9UYlN6ECaUK9UkFSIPZMPhzMkBf+nII2USLrI0cydUR7Vt74RvfxZhhfU7ejtOV3G357NA+EfUYi4GQFcxHMQmm/KcpeY2RJ2ODjYADzfG8E6088mcSNZEKsb59C5pJO/DYcx52zYkt41DGkclpGN8WHRU+qyA/Z/8pAgMBAAECggEBAJIVGuQeVJ2wToVZ9yMJHkblgqGruyOVJhFlDOvQKOrCZF61uISJLOdoKlNV+tLcexWJKf2TWbjLSZmvaTH+PJkB+K1Z56vewvl800bePMnDLv6N/PX4nZW7QQNgkdHFw5z1OxQUhbd+ITFGjYWPBi/qH8O4E4Iv/tujbfY7Ki8JFC179sKa1iVHAd0c4sgYhtKLlkvuMdRMFZI3Ok349wsy7iei2lbPPEVHymojHGJ22ZEt4+TiY6quKU9A6tsalm3WGtDgqLe9OgapMnmBZclI0IfQi5QUb/lt9AffGn60zICePFkxTFlwNCL38fXi12oVcVnRRqaSmTSLH5cKObECgYEA2eVm+GG1qkVAaYGcCm8MlGKOmRt9V7MXUD6fvf311sEogqoxO9IFyG/excpRt7J5DWc3qSVSq6/phAmZqhJYZepmbH549rWTYVUc2MECyu0c0YhtecEHOGZZQ5YV2O87gHdyaP+LHkiwEJhJH1ZzN7p+tTKu1moLx80oySv02XUCgYEAzlfz5rdn7aamCl/uweOStkihaF++xMhmOGZ2GChFmanDj7FuuzIxggQFOwnECoLKXA516ExIBuWjwZZHVpwhskFr1prv8W2oI3edJfZgShJbzX0YZvCGMF3pu/5KZb1hKNu/B+DpyTxnmumRN0SUsKOuX8jArawyqoOVanda5GUCgYBi6YYivogu8RzaSCHDgsGdIlhFHHt7siqlHKh7IKvWfvyaxBNxaleBs3bXJo6N6xCUiwRzXiP3F/XTIGCHGajTKuKv4YGbzLVIe9CKfKN95sEsRNgdawiSC7P9wEXOolrKaG6hR0+pwala/yZCeYX4CEq4indfkegjHPikybVueQKBgH4/rglBuiLbzceXPpv+w2soMSmNO0lnW7OybRsGuN710aN/akK6yTPFhfXDWeEcePeuGVISKheeNX1ily9UxAEuqe5aSeirsS5SJyJtBikL5oNpwJ5rF49MRpAxXA+3uVSobs9BO4lWngVYVvM70MXqv+v3Bny8WRue+rJzaHdJAoGADW9WwtJlUP7u6Hi31//FQAL3l1stV/h/6IFhq9KI0AEc6PuE2BpYvLAco4GMqfDjldd4KVfzNVzCqSSGX6hD2dubRZ+m9qr7XNy1FEz3OUDarba/Y+cA3IJfLvUllrLYjiMMd0iRzVdpdr0Uh44WQOTuRAjulpyvzM1FGBp0tH0=";
//        String signType = "RSA2";

    //珍贵
//        String appId = "2017120700424080";
//        String outPublicKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
//        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJeCbO8dLTcPaqnFz6Sd7HXykeisBq8edyR9jJ9v6MjD8N7AdsSNf8c32wkcc9ZntRKlUr/BkAc/bZBv/hg+yn19ih0xx15w9ZyhLvJSp3rfVSgqpZ6qaM3EEKwXzWV7NIKmutT2rwwDLQUlKvX1EpqDKd2QYqTmrd693A3C6tARAgMBAAECgYBF4a6FC5wTTVURQV/oub6bGGscOlxK4W5qsk3uLBveniG3XGZreih5ptWseicjuWnXU9G7CHh6H1NVz5IPdMbIK0NnLurS0BknmvcqLuYiyQfvggq2KHIE/fkwoxzUzy/9KdG3Z8OSLuYSq0lMDeC2wioCd0+Je6myjfdkhNQYAQJBAOP4eB581bVdya5HS/W4WMD3Kwo7hjmdXKczsg7QbJP+JXIa8qvVbjxafL3M/XFQdarTmBUGqYL6TsM6vyaWEPECQQCqI0pWCyM/8kZuQeKtsQZHmtvyBQIW9FFfARmjhHn4WMdszNxaAFHoW3rO27CRvht4kDpKqacURPkH7UUFnLEhAkEAz/11ElPQ400zYac43XIYv82CXi6astwomYAn1y4/sfJxFZ8SgsYSckwYfU6WeRjwki/FFIHkjFR1IdfqaHkH8QJARJ0i13x6ZMmSOdj8T8v6mloLpZj7dMJnJodNDbLZdZxoIKqlmu5SGgD6j/3tQXmHnqGbwIyWwwfl3pqDoXXFwQJBAK8j0hzBWWoEijulv7toYrXp1qJqHIymni/imEhEOHuqh4rKXqclou5zy0BHWRXc5oXpqm+/a3l3MF6/AWLU1FI=";
//        String signType = "RSA";

    //林经理
    String appId = "2021001144634445";
    String outPublicKey ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA07xb8NA2pobiLsLFvpvylma2BRTjm9V37ZsY31Y7OSoVq7b5/JFeqM5TeQMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA07xb8NA2pobiLsLFvpvylma2BRTjm9V37ZsY31Y7OSoVq7b5/JFeqM5TeQ+UOJ5FODh0euDBE4yCorc3vyBX4o8gzjB9WtL/LjN0BX5pm7hMduf+wQtXKbCYd1rLFYxKk1K9hdIEEFIrhJa5A/FaMKUDPUDZhkVCr37K5T7qT8eDTnO/IWtGWj/uOMajqCF15K/m3TWE8aAoAfdfzjUSvBc3Q3sh5xqETsU8QKXpbszc9zBrFPpZLQEMPQXaQwLYc4kqdg51TgDxVYwXpy4IYWMkSFxrtNvcMCkiDtU35/yD7nnCIFSfjzu+APThUrKwE/ey01c9hRlrmwbS8e1YywIDAQAB";
    String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCCjTkKiNIkz51Ie64W5NthaxjiSVAuRxBa9yfbfHXmSUdMwGJ7xXLugXROxCmA2AERcFztun3CmwqSUEZSvIQATwEbqTm6TWahGcxylBehijNPxQyavSJXydqjRCPX7saY5OVXGOgiCNt1q7k0kGccKGLiGAyWtRQ8EYmKMLAMUekM1Vyh+27Ma5jxMUY42m6k9T1xIiHO/1o73G/1V1gYy1/xtAXamhci9+Pr6GQaP7n9z1FumO24aU/IjcRn9zwDXPH2gQYhcUz8g835rPOuvr4+qbx4L32BqKTpf1l0OtdciYqnSC7R7PDRpHrQEuDOP5SG41SgqGvD/Dg/APkZAgMBAAECggEAfHK04lbkw5RpoTPNEyPcALR/xw4N8d9qNtyN0QuGtBw8bGykNMpWsgcTYRZbXegKtJ4A59kzkrQK0sjVEH+F8WlebAjX78FiyMV3/+0tzZ6ZJTx4tHUA7OCNm78Tz/5csFjaSjmC5xa8ZSJqbTIOKGhTQqWcMnmxLUWmb+XTnBmIFg4pA4NkrHk0YdRJuO+ZBmkas2q8doVisCgKFXYwocjndL/BaEqYTSw7ehh0HsK92jH9x4XUrsDxgtfsCejsWY58ww4f28A4VfsMiRsUVfsKkfX3Bc22LlIA1NriAs+q1TGHm4EnhvN0RvtDL0WkAmAUmAyU/cFUKxmv1Ka3kQKBgQC+RqeYIX6pbaf4BN5N/S1zTWMYP9ARrwd/TMWkf47ldJ3zVIxG6e2yo4mwX0ANxNAXH8Z2igaGH+ZIWMbGuxpGIgmCMJcxL4n7pHck9reOXz2l88D9tYWgnX1FzIQHB9nYI1g6fpKpN2d5ywtB/Wu3iGyrqdMclJ9zJMODStnj1QKBgQCvpWIg7KJSejlM/x82anVpf5CIzqxTN3QAGkOm7jYURQ/Yd/eViTu/HNs+lVlmqIskW2xDUX4CXK6L2UfkD47eQjfnAwteSlhup4tsMA5doY+CMxDum18AMYWzwNQBIQKKbNwkS91kn9LtokqE/n7oXS9Usy3oOJfHXn1YQ86WNQKBgGE4nL2Ar8VGQELbyfKzYBu/+NgVEu1WL1B+G6Z3fr4DHgmMs7gwIsHaCapSoNnoLCMEm1VKJRc3CWUB5173ju8yyQT6auY40hn/6Ni8LwHR7B/1MjDwJPzyO2YEn5kwC8ECHjyVG77WjkqO0ou/PuCiFlSIC1RZJDejGzrkbfyZAoGAIwSr/bhv3FW12t36URUEd5YRMNVTkfu6StLxrfRMH9ptAFFasRt9J+sO4vlPq6Hqq8L2YK18m+fyfLtDE/ruFu3Iy2vV9mSIIPeSWm1yT8SIIgMgWxHq7NTyvRWyrs7v/L0bXrkpWIhsuyfiB4Xds2Hwr8hRXLz7tanm/5P1OgkCgYB/k9V5kW0zYcDLhJmOsIp4q+yVHR9iIpWKzm9mMpmkU7e5baJsal0b//ERxTaOr4CWLlMpg5loYU6VijAua2ukG0ohyYWAEVLYPE6gjDNIReTIhaUNDrHHIZyC9dkupZRc980DpwHOqx5To7Rdz882qkZ4ItlBfafsWarBCjR/2w==";
    String signType = "RSA2";

    String charset="UTF-8";

    com.alipay.api.AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
        appId,privateKey,"json",charset,outPublicKey,signType);
    AlipayFundAuthOrderAppFreezeRequest request = new AlipayFundAuthOrderAppFreezeRequest();
    request.putOtherTextParam("app_auth_token","202006BBaeb011f6668749e48fcae81f62d1bA76");
    request.setBizContent("{"
        + "\"out_order_no\":\""+System.currentTimeMillis()+"\","
        + "\"out_request_no\":\""+System.currentTimeMillis()+"\","
        + "\"order_title\":\"测试线上冻结\","
        + "\"amount\":"+10+","
        + "\"product_code\":\"PRE_AUTH_ONLINE\","//销售产品码，新接入线上预授权的业务，本字段取值固定为PRE_AUTH_ONLINE 。
        + "\"payee_logon_id\":\"gzlplink12@126.com\","
        + "\"payee_user_id\":\"2088531117626761\","
        + "\"pay_timeout\":\""+30+"m\","
//    				+ "\"extra_param\":\"{\\\"category\\\":\\\"CHARGE_PILE_CAR\\\"}\","
        //余额宝（MONEY_FUND）、花呗（PCREDIT_PAY）以及芝麻信用（CREDITZHIMA）
        + "\"enable_pay_channels\":\"[{\\\"payChannelType\\\":\\\"MONEY_FUND\\\"},{\\\"payChannelType\\\":\\\"PCREDIT_PAY\\\"}]\""
        + "  }");
    System.out.println(JSONObject.toJSONString(request));
    AlipayFundAuthOrderAppFreezeResponse response = alipayClient
        //乐芃12，无效的应用令牌；珍贵账号，系统繁忙
//                .execute(request,null,"202006BBaeb011f6668749e48fcae81f62d1bA76");
//                .execute(request);//系统繁忙
        .sdkExecute(request);//可以获取
    System.out.println("响应参数："+ JSONObject.toJSONString(response));

    if(response.isSuccess()){
      System.out.println("调用成功");
    } else {
      System.out.println("调用失败");
    }
  }

}
