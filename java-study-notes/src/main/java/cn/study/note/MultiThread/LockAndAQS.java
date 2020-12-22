package cn.study.note.MultiThread;


import org.junit.Test;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 学习Lock / AbstractQueuedSynchronizer
 * @author jixuelei
 * @date 2020/12/22
 */
public class LockAndAQS {

    /**
     * 1- Lock执行的lock()/unlock()实际上是调用的AQS的acquire()/release()方法
     * 2- AQS同步队列:
     *      2.1- static class Node {} 双向队列。
     *      2.2- Node的waitStatus属性，表示当前节点的等待状态
     *          CANCELLED 【1】表示当前结点已取消调度。当timeout或被中断（响应中断的情况下），会触发变更为此状态，进入该状态后的结点将不会再变化。
     *          SIGNAL 【-1】 表示后继结点在等待当前结点唤醒。后继结点入队时，会将前继结点的状态更新为SIGNAL。
     *          CONDITION 【-2】表示结点等待在Condition上，当其他线程调用了Condition的signal()方法后，CONDITION状态的结点将从等待队列转移到同步队列中，等待获取同步锁。
     *          PROPAGATE 【-3】共享模式下，前继结点不仅会唤醒其后继结点，同时也可能会唤醒后继的后继结点。
     *
     * 3- acquire()方法先执行 【1】的方法，获取锁成功则返回，否则执行【2】
     * public final void acquire(int arg) {
     *         if (!tryAcquire(arg) &&   // 1
     *             acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) //2
     *             selfInterrupt();
     *     }
     *     3.1- 【2】操作分为两步，第一步：addWaiter(Node mode), 默认以独占式模式将当前线程分装成Node并将其加入到同步队列的队尾。
     *          当Node的tail指针为null时执行enq()。
     *              enq()方法主要实现(死循环执行，初始化同步队列后&&第一个阻塞线程被加入到队列后return)： tail指针为null时，创建head/tail接口。带头节点
     *                               头节点的tail指向-->当前线程封装的Node节点, 当前线程封装的Node节点指向-->head
     *
     *          第二步：acquireQueued(final Node node, int arg)
     *              1- 如果当前线程封装的Node的prev == head && tryAcquire(arg) return true, 则跳出
     *              2- 否则尝试将前置节点的waitStatus置为 SINGAL【-1】使得其当前节点会被唤醒， 同时会使用LockSupport.park(当前线程)。
     *
     * 4- 可中断式获取锁（Lock:lockInterruptibly() --> AQS:acquireInterruptibly()） 获取不到锁则退出
     *      4.1- tryAcquire return false时，进入doAcquireInterruptibly();该方法和独占式acquire()方法类似，
     *              如果当前节点的前置节点==head && tryAcquire()成功 则return
     *              否则： 抛出中断异常。
     *
     * 5-  超时等待式获取锁 （Lock:tryLock(long timeout, TimeUnit unit) --> AQS:tryAcquireNanos(int arg, long nanosTimeout)）
     */


    /**
     * 共享锁：
     *
     */

    static ReentrantLock lock = new ReentrantLock();
    @Test
    public void testAQSInnerClassNode() {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                lock.lock();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            });
            thread.start();
        }
        System.out.println("SSSs");

    }


    /**
     * 模仿ReentrantLock内部类Sync实现AQS的逻辑，通过实现AQS部分protected方法来定义
     * 同步组件的语义，而AQS实现了对同步状态的管理以及阻塞现场的排队，出队。
     *
     * 模板方法：
     *  独占式：
     *      void acquire(int arg)：独占式获取同步状态，如果获取失败则插入同步队列进行等待；
     *      void acquireInterruptibly(int arg)：与acquire方法相同，但在同步队列中进行等待的时候可以检测中断；
     *      boolean tryAcquireNanos(int arg, long nanosTimeout)：在acquireInterruptibly基础上增加了超时等待功能，在超时时间内没有获得同步状态返回false;
     *      boolean release(int arg)：释放同步状态，该方法会唤醒在同步队列中的下一个节点
     *  共享式：
     *      void acquireShared(int arg)：共享式获取同步状态，与独占式的区别在于同一时刻有多个线程获取同步状态；
     *      void acquireSharedInterruptibly(int arg)：在acquireShared方法基础上增加了能响应中断的功能；
     *      boolean tryAcquireSharedNanos(int arg, long nanosTimeout)：在acquireSharedInterruptibly基础上增加了超时等待的功能；
     *      boolean releaseShared(int arg)：共享式释放同步状态
     */
    static Mutex mutex = new Mutex();

    @Test
    public void testAQSDemo() {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                mutex.lock();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.unlock();
                }
            });
            thread.start();
        }
    }



    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new InterruptDemo());
        thread.start();
        Thread.sleep(20);
        thread.interrupt();
    }

    static class InterruptDemo implements Runnable {

        @Override
        public void run() {
            while (true){
                if (Thread.currentThread().isInterrupted()) return;
                System.out.println(Thread.currentThread().getName());
            }
        }
    }

}


