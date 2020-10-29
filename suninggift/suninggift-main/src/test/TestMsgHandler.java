
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.internal.tmc.MessageHandler;
import com.taobao.api.internal.tmc.MessageStatus;
import com.taobao.api.internal.tmc.TmcClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 淘宝消息队列长连接
 */
@Slf4j
public class TestMsgHandler {

    private static final String wsUrl="ws://mc.api.taobao.com";
    private static final String appkey="28315549";
    private static final String secret="a44159b142642bec2104ce7f729ece70";

    // 旧淘宝appkey
//    private static final String appkey="28315549";
//    private static final String secret="a44159b142642bec2104ce7f729ece70";

    @Test
    public void testHandler(){
        try{
            TmcClient client = new TmcClient(appkey, secret, "default"); // 关于default参考消息分组说明
            client.setMessageHandler(new MessageHandler() {
                public void onMessage(Message message, MessageStatus messageStatus) {
                    try {
                        String content = message.getContent();
                        log.info(messageStatus.getReason());
                        log.info(content);
                        log.info(message.getTopic());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("淘宝消息队列接收异常",e);
                        messageStatus.fail(); // 消息处理失败回滚，服务端需要重发
                        // 重试注意：不是所有的异常都需要系统重试。
                        // 对于字段不全、主键冲突问题，导致写DB异常，不可重试，否则消息会一直重发
                        // 对于，由于网络问题，权限问题导致的失败，可重试。
                        // 重试时间 5分钟不等，不要滥用，否则会引起雪崩
                    }
                }
            });
            client.connect(wsUrl); // 消息环境地址：ws://mc.api.tbsandbox.com/

            while (true){
                Thread.sleep(10000);
                log.info("等待淘宝消息回调");
            }

        }catch (Exception e){
            e.printStackTrace();
            log.error("淘宝消息队列连接异常",e);
        }
    }
}
