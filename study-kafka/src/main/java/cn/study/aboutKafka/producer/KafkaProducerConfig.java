package cn.study.aboutKafka.producer;

/**
 * @author jixuelei
 * @date 2021/1/13
 */
public class KafkaProducerConfig {
    /**
     * 生产者配置参数
     * 1- acks： 0、 1【默认值】、 all
     *      1.1- 【0】 发送出去就结束
     *      1.2- 【1】 只要Partition Leader接收到消息而且写入本地磁盘了，就认为成功了，不管他其他的Follower有没有同步过去这条消息
     *      1.3- 【all】Partition Leader接收到消息之后，还必须要求ISR列表里跟Leader保持同步的那些Follower都要把消息同步过去，才能认为这条消息是写入成功
     * 2- max.block.ms = 60000 【默认值】
     *      调用send() / partitionsFor()获取元数据时生产者的最大阻塞时间，若>此时间抛出异常
     * 3-  client.id 默认值为空
     *      broker用该参数来识别消息的来源。
     * 4- 时间参数
     *      4.1- request.timeout.ms = 30000【默认值】 生产者发送数据时等待服务器的响应时间，
     *      4.2- timeout.ms = 30000【默认值】 broker等待同步副本返回消息确认的时间，和acks=all相匹配，指定时间内没有收到副本的确认，抛出异常
     *      4.3- metadata.fetch.timeout.ms = 60000【默认值】: 生产者获取元数据时等待broker返回响应的时间，if 超时，重试or抛出异常or回调
     *      4.4- linger.ms = 0【默认值】: 生产者在发送批次之前等待更多消息加入批次的时间，批次满了or达到linger.ms的上限之后发送出去。设置大于0值，能够适当的提高吞吐量
     *  5- retries = 0【默认值】
     *      决定了生产者可以重发消息的次数。若到达这个次数时从broker得不到成功的消息则返回错误。默认情况下每次重试之间有100ms的等待（可以通过retry.backoff.ms配置修改）
     *  6- batch.size = 16384【默认值2MB】: 一个批次（发送缓存区？）可以使用的内存大小。按字节数计算。
     *  7- compression.type = none【默认值】:指定了消息发送时的压缩方式
     *  8- buffer.memory = 33554432【默认值4MB】: 生产者内存缓冲区的大小
     *  9- max.in.flight.requests.per.connection = 5【默认值】: 生产者收到broker响应之前可以发送多少消息。该值大于1会出现顺序不一致的情况
     *  10- max.request.size = 1048576【默认值128kB】: 生产者发送消息的大小。
     *
     *  kafka保证分区里面的消息是有序的。
     *      若同时设置retries>0 && max.in.flight.requests.per.connection > 1. 第一批次发送失败的时候第二批次发送成功，broker会重试写入第一批次，第一批次
     *      写入成功之后，两个批次的顺序就反掉了
     *
     */
}
