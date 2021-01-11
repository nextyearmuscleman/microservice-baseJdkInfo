package cn.study.note.kafka;

/**
 * 学习关于KafkaConsumer Group
 * @author jixuelei
 * @date 2021/1/11
 */
public class AboutKafkaConsumerGroup {

    /**
     * 学习关于消费者组:
     *  1- consumer组下可以有一个或多个consumer实例，共享一个group.id; consumer实例可以是一个进程，也可以是一个线程
     *  2- group.id是一个字符串，唯一标识一个consumer group
     *  3- consumer group下订阅的topic下的每个分区只能分配给某个group下的一个consumer(当然该分区还可以被分配给其他group)
     *
     *  位移，消费者位置。
     *      offset：标识消费者消费的位置，kafka的消费组保存自己的offset信息，
     *      offset管理：kafka默认是自动管理，【ENABLE_AUTO_COMMIT_CONFIG】。
     *          自动提交的话，下次自动提交offset时间截止至：nextAutoCommitDeadline = 【auto.commit.interval.ms】+ 【new SystemTime()】
     */

}
