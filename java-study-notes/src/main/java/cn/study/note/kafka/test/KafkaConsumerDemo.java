package cn.study.note.kafka.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author jixuelei
 * @date 2021/1/13
 */
@Component
public class KafkaConsumerDemo {

    @KafkaListener(topics = {"testTopic"})
    public void listen (ConsumerRecord<?, ?> record){
        System.out.printf("topic is %s, offset is %d, value is %s \n", record.topic(), record.offset(), record.value());
    }
}
