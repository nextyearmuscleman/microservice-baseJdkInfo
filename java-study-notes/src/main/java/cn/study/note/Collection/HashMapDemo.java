package cn.study.note.Collection;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author jixuelei
 * @date 2020/11/25
 */
public class HashMapDemo {

    /**
     * 记录HashMap数据结构的知识点： 每次学习到新的关于该数据结构的知识点就在此累加记录
     */

    /**
     * 1- HashMap推荐的循环方式
     *      1.1- Map.forEach( (k, v) -> {})
     *      1.2- Map.entrySet()方法构造 Map.Entry<>对象进行遍历
     */

    @Test
    public void t2() {
        Map<String, String> map = new HashMap<>(16);
        Set<String> strings = map.keySet();     // HashMap的内部类 KeySet extends AbstarctSet
        strings.forEach(s -> {
            System.out.println(s);
        });
        map.forEach( (k, v) -> {

        });
    }

    /**
     * 2- Map类/v存储null值记录
     *                                           K           V
     *      2.1- HashMap                        允许null    允许null        [线程按权]
     *      2.2- TreeMap                        不允许null   允许null        [non-thread-safe]
     *      2.3- ConcurrentHashMap              不允许null    不允许null     [锁分段 jdk8：CAS]
     *      2.4- HashTable                      不允许null    不允许null      [non-thread-safe]
     */

}
