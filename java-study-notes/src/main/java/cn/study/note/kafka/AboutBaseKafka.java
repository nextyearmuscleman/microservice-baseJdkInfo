package cn.study.note.kafka;


/**
 * @author jixuelei
 * @date 2021/1/7
 */
public class AboutBaseKafka {

    /**
     * @kafka_link https://zhuanlan.zhihu.com/p/72328153
     *
     * 1- kafka采用【发布-订阅】模式，
     *     1.1- 查看源码发现，消费者使用while(true)死循环依据一定的时间去生产者中拉取数据【org.springframework.kafka.listener.KafkaMessageListenerContainer#run()】
     *     1.2- producer生产消息至队列。 consumer等待消费消息 【多个consumer-->producer】
     *     1.3- 优点: 解耦（消息服务和业务服务分开）
     *               扩展性（可以对kafka集群进行自定义配置以满足不同的需求）
     *               顺序保证（kafka保证一个partition内的数据是有序的）
     *     1.4- 依赖zookeeper服务来管理注册
     *
     *  2- 概念：
     *      2.1- Topic: 简单理解成一个队列，该队列可以拥有多个Partition，每个partition内的消息都是有序的
     *      2.2- Broker（代理）: 表示一个kafka服务器，可以拥有多个Topic；多个Broker组成kafka集群，
     *      2.3- Partition: Topic可以分布在多个Broker中，该Topic以多个Partiton的方式存在，Kafka不保证多个Partiotion之间的顺序
     *
     *   3- kafka的Producer的生成过程：
     *      3.1- Producer使用push的方式将每个Record追加（append)到分区中，参考【KakdaProducer#doSend()】
     *            通过计算record等信息算出该消息该放到哪个分区。参考【DefaultPartition#partition()】
     *      3.2- 副本机制
     *          * 同一个分区下的所有副本保存有相同的消息序列
     *          * 副本分散保存在不同的 Broker 上，从而能够对抗部分 Broker 宕机带来的数据不可用（Kafka 是有若干主题概，每个主题可进一步划分成若干个分区。每个分区配置有若干个副本）
     *
     *   4- Broker保存消息的策略:
     *      4.1- 无论消息是否被消费，kafka都会保留所有消息。有两种策略可以删除旧数据：
     *          * 基于时间：log.retention.hours=
     *          * 基于大小：log.retention.bytes=
     *
     *   5-
     *
     */
}
