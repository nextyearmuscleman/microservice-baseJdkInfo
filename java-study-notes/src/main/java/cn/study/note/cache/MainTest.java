package cn.study.note.cache;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;

/**
 * @author jixuelei
 * @date 2020/12/21
 */
public class MainTest {

    @Test
    public void testRedis() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        pingRedis(jedis);

        jedis.set("today", LocalDateTime.now().toString());

        System.out.println(jedis.get("today"));
    }


    void pingRedis(Jedis jedis) {

        // 如果 Redis 服务设置来密码，需要下面这行，没有就不需要
        // jedis.auth("123456");
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: "+jedis.ping());
    }
}
