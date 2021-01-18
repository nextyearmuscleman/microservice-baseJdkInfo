package cn.study.aboutKafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * kafka消费者相关概念
 * @author jixuelei
 * @date 2021/1/13
 */
@Slf4j
@RestController
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
     * 10- auto.commit.interval.ms = 5000【默认值5s】:消费者提交信息的频率
     * 11- partition.assignment.strategy = [org.apache.kafka.clients.consumer.RangeAssignor]【默认值】:
     *      PartitionAssigner根据给定的topic和消费者决定分区的分配策略
     *      11.1- RangeAssignor：Kaf 将topic下的若干个连续的分区分配给consumer
     *      11.2- RoundRobinAssignor: 将topic下的所有partition依次分配给消费者。
     * 12- client.id：broker用来标识客户端来源，主要用于日志、度量指标、配额
     * 13- max.poll.records = 2147483647【默认值】: 控制单次poll()方法返回的记录数量
     * 14- receive.buffer.bytes = 65536【默认值】 / send.buffer.bytes = 131072【默认值】 :socket读写数据时用到的缓冲区大小
     */


    KafkaConsumer getConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "42.193.184.93:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "default_consumer_group");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        return consumer;
    }
    void simpleConsumerDemo() {
        KafkaConsumer consumer = getConsumer();
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

    /**
     * kafka提交和偏移量的配置:
     * 1- 默认的重置offset方式为latest，更新分区当前位置成为【提交】
     * 提交偏移量的逻辑: 消费者往【__consumer_offsets】这个topic里面去推送当前分区的偏移量，以及消费者所消费到的最新的offset。
     * 保存offset的作用: 当消费者宕机or出现不可用的情况，协调器会开始再均衡，重新分配消费者消费的分区情况，此时该topic内保存着每个分区最后一次消费的位置。
     *
     * 2- 自动提交
     *     【enable.auto.commit】配置为是否自动提交offset的开关，
     *     【auto.commit.interval.ms】配置为提交偏移量的频率。
     *     可能存在的问题: 默认的5s间隔，提交offset 3s后，触发再均衡，消费者只能拿到上一次提交的offset，3s内到达的消息会被重复的处理，
     *     可以通过提交自动提交offset的频率来【尽量】避免。
     *
     * 3- 手动提交偏移量
     *      将【enable.auto.commit】配置改成false。
     *      commitAsync()方法会提交由poll()方法获取到的最新的offset。非堵塞。
     */

    // 提交特定的offset
    void commitSpecifiedOffset() {
        KafkaConsumer consumer = getConsumer();

        // <topic/分区对象, 偏移量/metadata>
        Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
        int count = 0;
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                // 业务逻辑处理
                TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1, ""); // offset是从0开始的，+1标识顺序
                currentOffsets.put(topicPartition, offsetAndMetadata); // 不停的更新offset
                if (count % 1000 == 0)
                    consumer.commitAsync(currentOffsets, null);

                count++;
            }
        }
    }

    // 消费者失去对partition所有权之前的处理过程
    void todoBeforeLoseControlOfPartition() {
        Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
        KafkaConsumer consumer = getConsumer();

        class HandleRebalance implements ConsumerRebalanceListener {

            // 该方法在 再均衡开始之前和消费者停止fetch data之后被调用
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                log.info("Lost Partition in rebalance. Committing offsets: {}", currentOffsets);
                consumer.commitSync(currentOffsets);
            }

            // 该方法在 重新分配分区之后和消费者开始fetch data之前被调用
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

            }
        }

        try {
            consumer.subscribe(Collections.singletonList("topics"), new HandleRebalance());
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1, "");
                    currentOffsets.put(topicPartition, offsetAndMetadata);
                }
                consumer.commitAsync(currentOffsets, null); // no callback
            }
        }catch (WakeupException wakeupException) {
            //
        }catch (Exception ex){
            log.error("error", ex);
        }finally {
            try {
                consumer.commitSync(currentOffsets);
            }finally {
                consumer.close();
            }
        }
    }

    /**
     * 从特定的偏移量处开始消费信息。需要将消息和消息的offset同时保存起来，保证是一个原子操作。
     * 需要ConsumerRebalanceListener / seek()方法配合使用。
     * seek(TopicPartition partition, long offset).
     */
    void consumerSpecifiedOffsetOnPartition() {
        // seekToBeginning() / seekToEnd()  将特定分区的偏移量重置为第一个/最后一个

        KafkaConsumer consumer = getConsumer();
        ConsumerRecords<String, String> records = consumer.poll(100);

    }

    // 独立的消费者，不需要配置消费者群组的消费者
    void singleConsumer() {
        KafkaConsumer consumer = getConsumer();
        List<PartitionInfo> topics = consumer.partitionsFor("testTopic");// 1- 向服务器获取topic可用的分区信息
        List<TopicPartition> topicPartitions = topics.stream().map(topic -> {
            return new TopicPartition(topic.topic(), topic.partition());
        }).collect(Collectors.toList());
        consumer.assign(topicPartitions); // 2- 手动将分区列表分配给消费者
    }

}
