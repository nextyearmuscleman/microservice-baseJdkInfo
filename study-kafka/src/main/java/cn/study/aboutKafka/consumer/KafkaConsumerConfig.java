package cn.study.aboutKafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/**
 * kafka消费者相关概念
 * @author jixuelei
 * @date 2021/1/13
 */
public class KafkaConsumerConfig {
    /**
     * kafka消费者配置:
     * 1- 多个消费者群组之间互不影响，
     * 2- group.id = ""; 指定了消费者属于的群组标识。
     * 3- fetch.min.bytes = 1【默认值】：消费者从服务器获取的最小字节数，服务器会等到可用数据接近于该值时返回给消费者
     * 4- fetch.max.wait.ms = 500【默认值】：指定服务器的等待时间，和【3】的配置是or的关系。
     * 5- max.partition.fetch.bytes = 1048576【默认值1MB】：服务器从【每个】分区返回给消费者的最大字节数，poll()方法从分区中拉取的数据大小不超过该值。
     *      该值过大会出现: poll()方法的数据太多，消费者处理的时间过长，导致此次的轮询期间没有发送心跳，发生会话过期。
     * 6- session.timeout.ms =  30000 【默认值30s】：消费者被broker认定为死亡状态的最长时间，超过该值消费者没有发送心跳，则被认定为死亡状态，
     *      协调器Coordinator进行再均衡，将其消费的分区交给其他的消费者。 关联属性[heartbeat.interval.ms]
     * 7- heartbeat.interval.ms = 3000【默认值】:指定了poll()方法向协调器发送心跳的频率
     * 8- auto.offset.reset = latest【默认值】: 指定了消费者在进行读取一个没有偏移量的分区or偏移量无效的情况下该作何处理，
     *      默认值是从最新的记录开始读取数据(在消费者启动之后生成的数据)。另一个值【earliest】表示消费者从起始位置开始消费数据。
     * 9- enable.auto.commit = true【默认值】: 指定了消费者是否可以自动提交偏移量。关联属性【auto.commit.interval.ms】
     * 10- auto.commit.interval.ms = 1000【默认值】:消费者提交信息的频率
     * 11- partition.assignment.strategy = [org.apache.kafka.clients.consumer.RangeAssignor]【默认值】:
     *      PartitionAssigner根据给定的topic和消费者决定分区的分配策略
     *      11.1- RangeAssignor： 将topic下的若干个连续的分区分配给consumer
     *      11.2- RoundRobinAssignor: 将topic下的所有partition依次分配给消费者。
     * 12- client.id：broker用来标识客户端来源，主要用于日志、度量指标、配额
     * 13- max.poll.records = 2147483647【默认值】: 控制单次poll()方法返回的记录数量
     * 14- receive.buffer.bytes = 65536【默认值】 / send.buffer.bytes = 131072【默认值】 :socket读写数据时用到的缓冲区大小
     */

    void simpleConsumerDemo() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "42.193.184.93:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // 订阅主题
        consumer.subscribe(Collections.singletonList("testTopic"));

        try {
            while (true) { // 1-消费者实际上是一个长期运行的程序，通过轮询向kafka服务器请求数据
                ConsumerRecords<String, String> records = consumer.poll(100); // 2- poll方法的参数表示方法的阻塞时间。

                for (ConsumerRecord<String, String> record : records) {
                    // records是一个记录列表。每个记录包含了消息所属的主题信息、所在分区的信息、消息在分区里的offset、key，value
                }

            }
        } finally {
            consumer.close();
        }
    }
}
