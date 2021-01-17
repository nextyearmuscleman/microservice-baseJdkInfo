package cn.study.aboutKafka.producer.serializer;

import lombok.Data;

/**
 * @author jixuelei
 * @date 2021/1/14
 */
@Data
public class Customer {
    private int customerId;
    private String name;
}
