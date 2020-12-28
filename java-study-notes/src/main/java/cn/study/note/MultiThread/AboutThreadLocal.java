package cn.study.note.MultiThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jixuelei
 * @date 2020/12/28
 */
public class AboutThreadLocal {

    static ThreadLocal<SimpleDateFormat> sdfThreadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new DateUtil("2020-12-20 10:00:00"));
    }

    static class DateUtil implements Runnable  {

        String date;

        public DateUtil(String date) {this.date = date;}

        @Override
        public void run() {
            if (sdfThreadLocal.get() == null) {
                sdfThreadLocal.set(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss"));
            }
            try {
                System.out.println(sdfThreadLocal.get().parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }



    /** ThreadLocal源码学习：
     * 1- public T get()get(){
     *       Thread t = Thread.currentThread();
     *       ThreadLocalMap map = getMap(t); // 1- 获取当前线程的内部属性 ThreadLocalMap
     *       if (map != null) {
     *           ThreadLocalMap.Entry e = map.getEntry(this);  //2- ThreadLocalMap内部是以Entry对象来存信息的，key为this（ThreadLocal）， key为 value。
     *           if (e != null) {
     *               T result = (T)e.value;
     *               return result;
     *           }
     *       }
     *       return setInitialValue(); // 该方法内的initialValue()可以由extends ThreadLocal的类去重写，实现赋值其他的默认值。
     * }
     * 2- public void set(T value) {
     *         Thread t = Thread.currentThread();
     *         ThreadLocalMap map = getMap(t);// 1- 获取当前线程的内部属性 ThreadLocalMap
     *         if (map != null)
     *             map.set(this, value);
     *         else
     *             createMap(t, value); // 3- 赋值当前线程的threadLocals属性。
     *     }
     *
     *     void createMap(Thread t, T firstValue) {
     *         t.threadLocals = new ThreadLocalMap(this, firstValue);
     *     }
     *
     */

}
