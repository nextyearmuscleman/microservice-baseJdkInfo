package cn.study.note.MultiThread.middle;

/**
 * @author jixuelei
 * @date 2020/12/21
 */

/**
 * synchronized学习
 */
public class AboutSynchronized {


}

/**
 * synchronized使用场景
 * 1- 方法：
 *      1.1- 实例方法， 锁住的是实例对象
 *      1.2- 静态方法， 锁住的是class对象
 * 2- 代码块：
 *      2.1- synchronized(this) 锁住的是实例对象
 *      2.2- synchronized(Object.class) 锁住的是class对象
 *      2.3- synchronized(some Object) 实例对象
 *
 *  synchronized能够实现同步的原因是：占有对象的monitor没从而进入到同步代码块/方法，此时和其他线程是互斥的。
 *  带来的性能影响主要集中在: 其他线程阻塞，以及唤醒带来的cpu切换
 *
 *  二、改进： CAS
 *      cas是一种乐观锁策略，CAS(V,O,N)。【V 内存地址存放的实际值；O 预期的值（旧值）；N 更新的新值】
 *          当v==o，则将n值更新至v
 *          当v!=o，表示该值已经被其他的线程更新，进行自旋（死循环）。
 *      cas的问题：
 *          ABA：AtomicStampedReference
 *          自旋时间过长
 *          只能保证一个共享变量的原子操作： 利用对象整合多个共享变量，即一个类中的成员变量就是这几个共享变量。然后将这个对象做CAS操作就可以保证其原子性。
 *                                      atomic中提供了AtomicReference来保证引用对象之间的原子性。
 *
 */
class Demo implements Runnable {

    static int count = 0;

    @Override
    public  void run() {
        for (int i = 0; i < 10; i++){
            synchronized (Demo.class) {
                count++; // 不是原子性操作，会因为重排序导致问题。
            }
            calculate();
        }
    }

    public static void main(String[] args) {

    }

    void calculate() {
        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(new Demo());
            thread.start();
        }
        System.out.println("sum is :"+ count);
    }
}