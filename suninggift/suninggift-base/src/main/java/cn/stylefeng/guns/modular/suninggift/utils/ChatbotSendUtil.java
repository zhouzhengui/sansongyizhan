package cn.stylefeng.guns.modular.suninggift.utils;

import cn.stylefeng.guns.modular.suninggift.enums.ProfilesEnum;
import cn.stylefeng.guns.modular.suninggift.model.constant.DingDingInfo;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 钉钉机器人发送信息工具类
 */
@Slf4j
@Component
public class ChatbotSendUtil {

    @Autowired
    private SysConfigService configService;

    @Value("${profile}")
    private String profile;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 发送警告信息
     * @param msg
     * @return
     */
    public String sendMsg(String msg){
        String suninggiftUrl = null;
        try {
            suninggiftUrl =  InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            suninggiftUrl = "unknown-ip";
        }

        JSONObject jo = new JSONObject();
        jo.put("时间", sdf.format(new Date()));
        jo.put("系统地址", suninggiftUrl + " " + configService.getByCode("suninggiftUrl"));
        jo.put("错误信息", msg);
        ChatBotMsgVO msgVO = new ChatBotMsgVO(jo.toJSONString());
        if(profile.equals(ProfilesEnum.pressure.getCode())){
            //压测环境不发
            return null;
        }
        return send(msgVO);
    }

    private String send(ChatBotMsgVO msgVO){
        try {
            DingDingInfo dingDingInfo = JSONObject.parseObject(configService.getByCode("dingDingInfo"), DingDingInfo.class);
            String WEBHOOK_TOKEN = dingDingInfo.getWEBHOOK_TOKEN();//"https://oapi.dingtalk.com/robot/send?access_token=7dd3eb2e23b1b372f337631656fe8f3eed24ef070b14150d9a30b31f7cde4cf9";
            String secret = dingDingInfo.getSecret();//"SEC7f440b6108deb88d974b95435f30e33b04e44f42434c6e8754bea6d0624ae5ba";

            HttpClient httpclient = HttpClients.createDefault();
            //组装签名
            Long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String str = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
            WEBHOOK_TOKEN = WEBHOOK_TOKEN + "&timestamp=" +timestamp + "&sign=" +str;
            HttpPost httppost = new HttpPost(WEBHOOK_TOKEN);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");

            ChatbotSendVO sendVO=new ChatbotSendVO();

            sendVO.setMsgtype("text");
            sendVO.setText(msgVO);

            String textMsg = JSONObject.toJSONString(sendVO);
            StringEntity se = new StringEntity(textMsg, "utf-8");
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                log.info(result);
            }
            return null;
        }catch (Exception e){
            log.error("发错错误",e);
            return "发错错误"+e.getMessage();
        }
    }

}
