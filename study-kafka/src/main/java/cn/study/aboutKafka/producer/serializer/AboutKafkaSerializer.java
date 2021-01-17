package cn.study.aboutKafka.producer.serializer;

import cn.study.aboutKafka.producer.serializer.Customer;
import lombok.Data;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 关于kafka序列化器
 * @author jixuelei
 * @date 2021/1/13
 */
@Data
public class AboutKafkaSerializer {

    /**
     * 自定义序列化器
     */
    static class CustomerSerializer implements Serializer<Customer> {

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
            // do nothing
        }

        @Override
        public byte[] serialize(String topic, Customer data) {
            ByteBuffer buffer;
            try {
                byte[] serializedName;
                int stringSize;
                if (data == null) return null;

                if (data.getName() != null) {
                    serializedName = data.getName().getBytes(StandardCharsets.UTF_8);
                    stringSize = serializedName.length;
                }else {
                    serializedName = new byte[0];
                    stringSize = 0;
                }

                buffer = ByteBuffer.allocate(4 + 4 + stringSize);
                buffer.putInt(data.getCustomerId());
                buffer.putInt(stringSize);
                buffer.put(serializedName);
            }catch (Exception exception){
                throw new SerializationException(exception);
            }
            return buffer.array();
        }

        @Override
        public void close() {

        }
    }


}


