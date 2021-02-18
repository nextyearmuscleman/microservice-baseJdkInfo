package cn.study.note.kafka.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

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

    @Test
    public void t1() {
        String str = "2021-01-26T13:10:58.000+08:00";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            // log
        }
        String pattern = "yyyyMMddHHmmss";
        df = new SimpleDateFormat(pattern);
        String format = df.format(date);

        System.out.println(format);


//        Date parse = parse("2021-01-26T13:10:58.000+08:00", new SimpleDateFormat("yyyyMMddHHmmss"));
//        System.out.println(parse);
    }


    static Date parse(String dateStr, DateFormat dateFormat) {
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            String pattern;
            if (dateFormat instanceof SimpleDateFormat) {
                pattern = ((SimpleDateFormat) dateFormat).toPattern();
            } else {
                pattern = dateFormat.toString();
            }
            throw new RuntimeException(e);
        }
    }
}
