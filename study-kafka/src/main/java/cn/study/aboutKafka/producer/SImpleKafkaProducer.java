package cn.study.aboutKafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

/**
 * 1- 简单的kafka生产者，发送信息至腾讯云远程服务器，testTopic的topic
 * @author jixuelei
 * @date 2021/1/13
 */
@RestController
@Slf4j
public class SImpleKafkaProducer {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/send")
    public void test() {
        // 同步发送消息.内部封装的ProducerRecord
        //kafkaTemplate.send("testTopic", "config test");

        // 回调 org.apache.kafka.clients.producer.Callback
        Callback callback = new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                log.info("callback res: {}", metadata);
            }
        };

        Producer<String, String> producer = new KafkaProducer<>(getKafkaProp());
        ProducerRecord<String,String> record = new ProducerRecord<>("testTopic", "testKey", "testValue");
        // 异步发送消息
         producer.send(record, callback);
    }

    private Properties getKafkaProp() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "42.193.184.93:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return props;
    }




}
