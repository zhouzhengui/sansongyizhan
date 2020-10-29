import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;

/**
 * 月月付退款测试类
 *
 * @author: zzg
 * @date: 2020-10-12 9:54
 */
public class MonthPayTradeRefund {

  public static void main(String[] args) throws AlipayApiException {

    String serverUrl = "https://openapi.alipay.com/gateway.do";
    String appId = "2017090608591210";
    String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCEualBduCDNPIUkSGukFdfNHCoRoNzdqR0rftYFPI2zeVSeNyX/0aBt8fWL/uuN+YUQ7c/muktRa4EBLJluT1ajpMEQ97KzNTgAkaJqdFNJTmhmEFBsoabmk3klO9OXTgI3eO0bJEvgRHS5cMDYByGxeIbX487bRScnHy0BwIw7wgC+rVyCFLDZuC6FGnDgKbBoMDb7lwNDCSiimzSzkwa0vd77JkMg7jW8E84Xhk5Fi4O1EH/xrr45x7KQXvGYimgXoXqgjiio2/KHgh7nLdmWT1zBLIxrzsP0LneJAKw1soMntz+/uoVYpSBbFU3lmM3nn9vRa1lKSP/e9bDSbUHAgMBAAECggEAUI5jRWGCmQAsogEIYWwoK5enfOr3Y8AxxsPGVsq3zD2hnzHOKc79m0PRXZXX+wDge5IyCb/bIOg22yeCo60PqQP6BU+wTISP+7pF184jQ+CBHyxLBzX070JzXnZq3LRFi1QpnVt7r0p2Z/ueJlFSXxaByr1wMNBe/0MXiSjr0oiRinZ+q2L/1RBs1TBdkFgfEJB2UFfCqFZIh00oDnN2jvHNlxVL5kSuAr6T0kezh5lZU4TOtNstjj21j8rldCxehOju3uBEa4GCrb+MHVWSp6cgp8H4TZJPUR1k9drVl+pCtEIXKa3xlC9p4d9/XcO+XVUpu7B7aVCtJEMxv+6GKQKBgQDGMjVCQ2NFl8lFjCzdU5s15KAK+p6LZr/wFe8RvjNq8e2jMqImT0je+ZOy7x9iOy8+rco9y5AZa2spPo3P4lqhBYg/My4nKvHh9u2BxnQtF2QRWVI6WsAzIpV7C9cmEnq1Iw/82KtOPyAqWej2pMVaIdvb5nXrBRGDdE4bVf8ppQKBgQCrbz3iOkbIoLpKowKvEGIlcx3E7a//z6Rj+/6iJD2n3xFIaa7uhE6nBmL/Z/DV4CtKgWrMOGxXnvtOpAu7ZPKbKgNcaaciXPqKyRpjPYLMD/b3RNvK4ntv1MvW/ws2gpijq3C4hOAb2IS6wwvv8GS+miFNwgm7qFjsMP82qQvsOwKBgA93/bYlQks9NIVMEnhXvSZwqJkSxGsG4vYfECCnYqX3Kd/x5LvutjZI7mEpzp5BFkfjEoTOLXn+a0e1zSWref1VTDP6j6CY9nX3asQVNB4M1E+SVBuf155mFZqb6vfau18F1IqccopR3PXRPKB7bbfCKsG38/4a0CodWrdBd//NAoGBAJuHEF1tAUqChn+v439vHZtcFGXUPReXCUysERaISmdBuCw5tLouziw5XxbW9WcTODMftgQQLDpRwSxXJ02bB349X083iqliMOo4B/iAlWkMEsHBdqVZUyLhqwwh1cpeMJxLrLe51JdulvvYd7v2G2/P5ObmYm4dDI865E5yjp6BAoGBAIE1le7cWZXAL1TR4aViUYtVfJ9vXL6xiNntUExwVYSRkl0nFUSwSTc1tF+aPFX7wWMl11BOx3pXpr3bMr17mn5DKZv0gePDWLhM14UksBWe8qw6DZFBLiopbGuLJFCK0lhPzLsIz+ILi/Ce8g1Mmcjq/wyn6vBrS5TivvEC7AAe";
    String format = "json";
    String charset = "UTF-8";
    String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtEx89j0Kda8aCFKV6F1TJchKpHe6rgAFzoM3Lumc9eQ4cl/9ivDMSclaU+gJ45Ygmmkeb41SzV0fi5IcDi44s4uLh+BVQbnTkCs5V9JXgP6111WMEpHb99ScUlfUVLMtUyP+Xva3U3yJ8hOIhrwAd/I7BBytWjF/3FpCqgHWItuPNxvQefKJogzPt4DDGoSsUC4mFxyb7leJrwu0YByvTS7OG5wLlSgktXt1IovYYtwgu/IQpbUbnS4CqN4omYpKYBgQHdRU/xeh+FnCie42PmIXn96zKR+777r7GbZ3VYxCFdNDLccrspe9txlsk/PeBVr93C/+0bm+1Xj6UfnSpQIDAQAB";
    String signType = "RSA2";

    AlipayClient alipayClient = new DefaultAlipayClient(serverUrl,appId,privateKey,format,charset,alipayPublicKey,signType);
    AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
    request.setBizContent("{" +
        "\"out_trade_no\":\"2020101316234085459077\"," +
        "\"trade_no\":\"2020101322001450271425425463\"," +
        "\"refund_amount\":79.20," +
        //"\"refund_currency\":\"USD\"," +
        "\"refund_reason\":\"正常退款\"," +
        "\"out_request_no\":\"2020101316234085459077\"" +
        //"\"operator_id\":\"OP001\"," +
        //"\"store_id\":\"NJ_S_001\"," +
        //"\"terminal_id\":\"NJ_T_001\"," +
        //"      \"goods_detail\":[{" +
        //"        \"goods_id\":\"apple-01\"," +
        //"\"alipay_goods_id\":\"20010001\"," +
        //"\"goods_name\":\"ipad\"," +
        //"\"quantity\":1," +
        //"\"price\":2000," +
        //"\"goods_category\":\"34543238\"," +
        //"\"categories_tree\":\"124868003|126232002|126252004\"," +
        //"\"body\":\"特价手机\"," +
        //"\"show_url\":\"http://www.alipay.com/xxx.jpg\"" +
        //"        }]," +
        //"      \"refund_royalty_parameters\":[{" +
        //"        \"royalty_type\":\"transfer\"," +
        //"\"trans_out\":\"2088101126765726\"," +
        //"\"trans_out_type\":\"userId\"," +
        //"\"trans_in_type\":\"userId\"," +
        //"\"trans_in\":\"2088101126708402\"," +
        //"\"amount\":0.1," +
        //"\"amount_percentage\":100," +
        //"\"desc\":\"分账给2088101126708402\"" +
        //"        }]," +
        //"\"org_pid\":\"2088101117952222\"," +
        //"      \"query_options\":[" +
        //"        \"refund_detail_item_list\"" +
        //"      ]" +
        "  }");
    AlipayTradeRefundResponse response = alipayClient.execute(request);
    System.out.println("调用响应报文=" + JSONObject.toJSONString(response));
    if(response.isSuccess()){
      System.out.println("调用成功");
    } else {
      System.out.println("调用失败");
    }

  }

}
