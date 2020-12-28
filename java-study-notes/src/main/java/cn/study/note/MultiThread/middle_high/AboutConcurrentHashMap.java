package cn.study.note.MultiThread.middle_high;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 学习 ConcurrentHashMap（jdk1.8）
 * @author jixuelei
 * @date 2020/12/28
 */
public class AboutConcurrentHashMap {

    /**Collections.synchronizedMap(Map<K,V> m);
     * 可以将Map封装成 SynchronizedMap, 改类为Collections的static class
     * 有两个属性：
     *      private final Map<K,V> m;     //需要封装的Map对象
     *      final Object  mutex;      // 该类的实例对象
     *  其调用的方法都是使用synchronized将mutes对象lock起来
     *        public V put(K key, V value) {
     *             synchronized (mutex) {return m.put(key, value);}
     *         }
     */
    public void t1() {
        Map<String, String> map = new HashMap<>();
        map.put("nice", "me too");


        Map<String, String> synchronizedMap = Collections.synchronizedMap(map);
        synchronizedMap.put("nice", "u too");

    }


    /**
     * CopyOnWriteArrayList: 线程安全：写时复制。 弱一致性
     * 读操作(get()没有加锁/CAS) 读线程只是会读取数据容器中的数据，并不会进行修改
     * 写操作(add(e)): 写操作会通过ReentrantLock加锁，copy出新的数组，将元素插入到新的数组中，之后将原型的数组引用指向新的数组。
     * 读/写不冲突，不在同一个容器中操作。
     * array属性通过【volatile修饰】。保证了写线程对数组的引用修改 happens-before 读线程对数组引用的获取。保证了数组的可见性。
     */
    @Test
    public void t3() {
        List<String> list = new CopyOnWriteArrayList<String>();
        Thread thread1 = new Thread(() -> {
            for (int i = 1; i < 100; i++) {
                list.add("i: " + i);
                System.out.println(i);
            }
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("list : " + list);
        });
        thread1.start();
        thread2.start();

    }


    /**
     * 带标签的continue语法. 跳出内层循环，回到标签处。
     */
    @Test
    public void t2() {
//        for(int i=0;i<9;i++) {
//            if(i!=5)
//                continue;
//            System.out.println("i="+i);
//        }
        //-----------------
        for(int i=0;i<5;i++) {
            for(int j=0;j<5;j++) {
                if(j>=i)
                    continue;
                System.out.println("i="+ i + " j="+j);
            }
        }

    }
}
