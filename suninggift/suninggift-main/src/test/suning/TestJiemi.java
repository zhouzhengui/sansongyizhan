import cn.stylefeng.guns.modular.suninggift.utils.SignUtil;
import com.alibaba.fastjson.JSONObject;

public class TestJiemi {

  public static void main(String[] args) {
    String a = "{\"app_key\":\"20200422\",\"target_appkey\":\"20200422\",\"data\":\"eyJvdXRSZXF1ZXN0Tm8iOiIyMDIwMDgxODEwNDIwODAzNzA1MzM5NjEzIiwiY29udHJhY3RQaG9uZSI6IjE1NTY4MTkxMDUxIiwiY2VydENoZWNrTXNnIjoi5a6e5ZCN6K6k6K+B5oiQ5YqfIiwicG9zdFBob25lIjoiMTgwNjE3NTM4MDIiLCJwaG9uZUJlbG9uZyI6IjM0MDAwMCwzNDAxMDAiLCJwb3N0QWRkcmVzcyI6Iuaxn+iLj+ecgeWNl+S6rOW4gueOhOatpuWMuuiLj+WugeWkp+mBkzHlj7ciLCJmaXJzdE1vbnRoTmFtZSI6IuWll+mkkOWHj+WNiiIsImZyZWV6ZVByaWNlIjoiMTIuMDAiLCJwb2xpY3lObyI6IlBQSUQyMDIwMDcwMTU4OTE3MzYyMjciLCJwcm9rZXkiOiI5OTk5OTIwMjAwODE4MTA0MjA3IiwiY3VzdE5hbWUiOiLolKHku5XmnaUiLCJvdXRPcmRlck5vIjoiMjAyMDA4MTgxMDQyMDgwMzQ0NzI5NzMzMiIsInBvc3RDdXN0TmFtZSI6IuS4peW4heW4hSIsImNlcnRObyI6IjMyMDEwMzE5NjIwMTI5MTAzMiIsIm9yZGVyRGV0YWlsc1BzbklkIjoiUFNJRDIwMjAwNzAxMjQ1NTI5NDA3MiIsImZpcnN0TW9udGhJZCI6IkEwMDAwMTFWMDAwMDAyIiwiY3VzdFR5cGUiOiIxIiwiY2VydENoZWNrQ29kZSI6IjAwMDAiLCJwb3N0QXJlYUNvZGUiOiIzMjAwMDAsMzIwMTAwLDMyMDEwMiJ9\",\"method\":\"haoka.opennew.conserve.new.unicom.order\",\"format\":\"json\",\"sign_method\":\"md5\",\"sign\":\"8986CC5EA4B3F7B6DE1F69C523459A9A\",\"timestamp\":\"2020-08-18 10:42:08\"}";
    String datastr = JSONObject.parseObject(a).getString("data");
    String baseConvertStr = SignUtil.baseConvertStr(datastr);
    JSONObject dataObject = JSONObject.parseObject(baseConvertStr);
    System.out.println(dataObject);

  }

}
