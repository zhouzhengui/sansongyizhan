package cn.stylefeng.guns.modular.suninggift.utils;
import cn.stylefeng.guns.modular.suninggift.entity.QueueSchedule;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tianshl
 * @version 2017/9/1 下午05:03
 */
@Slf4j
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topicName.kafka}")
    public String kafkaMessageTest;


    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 向topic中发送消息
     */
    public void send (String msg) {
        kafkaTemplate.send(kafkaMessageTest, msg);
    }

    /**
     * 插入字符串到kafka
     * @param queueSchedule
     * @return
     */
    public boolean addToQueue(QueueSchedule queueSchedule){
        try{
            kafkaTemplate.send(kafkaMessageTest, "suning_qiment"+ JSONObject.toJSONString(queueSchedule));
        }catch (Exception e){
            log.error("插入队列错误", e);
            return false;
        }
        return true;
    }

    /**
     * 向topic中发送消息
     */
    public void send (String topic, List<String> msgs) {
        msgs.forEach(msg -> kafkaTemplate.send(topic, msg));
    }
}