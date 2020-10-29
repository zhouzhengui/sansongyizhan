/*
package cn.stylefeng.guns.config.listener;

import cn.stylefeng.guns.modular.suninggift.entity.QueueSchedule;
import cn.stylefeng.guns.modular.suninggift.service.QimenService;
import cn.stylefeng.guns.modular.suninggift.utils.ChatbotSendUtil;
import cn.stylefeng.guns.modular.suninggift.utils.KafkaProducer;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
*/
/**
 * @author cms
 * @version 1.0
 * @qq 1158816810
 * @date 2020-03-04 16:18
 *//*

@Slf4j
@Component
public class SimpleListener {


@Autowired
  QimenService qimenService;

  @Autowired
  ChatbotSendUtil chatbotSendUtil;

  @Autowired
  KafkaProducer kafkaProducer;


  @KafkaListener(topics = "${topicName.kafka}")
  public void listen1(String data) {
      log.info("kafka接受到的数据"+data);
      if(data.startsWith("suning_qimen")){
          //如果是苏宁的订单
          data = data.replace("suning_qimen","");
          QueueSchedule queueSchedule = JSONObject.parseObject(data, QueueSchedule.class);
          String s = qimenService.kafkaOrderDispose(queueSchedule);
          log.info("kafka订单处理响应参数：" + s);
          if(StringUtils.isNoneBlank(s)){
              chatbotSendUtil.sendMsg(s);
          }
      }else{
          //不是则放回队列
          kafkaProducer.send(data);
      }
  }

}
*/
