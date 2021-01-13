package cn.study.note.kafka.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jixuelei
 * @date 2021/1/13
 */
@RestController
public class KafkaTest {
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @GetMapping("/send")
    public String send(String msg){
        kafkaTemplate.send("testTopic", msg); //使用kafka模板发送信息
        return "success";
    }
}
