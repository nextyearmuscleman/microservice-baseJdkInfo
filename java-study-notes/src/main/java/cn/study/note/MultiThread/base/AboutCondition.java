package cn.study.note.MultiThread.base;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jixuelei
 * @date 2020/12/25
 */
public class AboutCondition {

    /**
     * Condition内部维护了一个单向的等待队列。复用了Node。其内部属性nextWaiter。
     * 源码学习：final void await() throws InterruptedException {
     *          Node node = addConditionWaiter(); //1- 将当前线程封装成Node加入到等待队列中
     *             int savedState = fullyRelease(node); // 2- 释放当前线程获取的锁 && 唤醒头节点的下一个Node
     *             int interruptMode = 0;
     *             while (!isOnSyncQueue(node)) {
     *                 LockSupport.park(this);
     *                 if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
     *                     break;
     *             }
     *             if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
     *                 interruptMode = REINTERRUPT;
     *             if (node.nextWaiter != null) // clean up if cancelled
     *                 unlinkCancelledWaiters();
     *             if (interruptMode != 0)
     *                 reportInterruptAfterWait(interruptMode);
     * }
     */


    public static void main(String[] args) {
        Thread waiter = new Thread(new Waiter());
        waiter.start();
        Thread signaler = new Thread(new Signaler());
        signaler.start();
    }

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();
    private static volatile boolean flag = false;

    static class Waiter implements Runnable{
        @Override
        public void run() {
            lock.lock();
            try {
                while (!flag) {
                    System.out.println(Thread.currentThread().getName() + "当前条件不满足等待");
                    try {
                        condition.await();
                    }catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + "接收到通知条件满足");
            }finally {
                lock.unlock();
            }
        }
    }

    static class Signaler implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                flag = true;
                condition.signalAll();
            }finally {
                lock.unlock();
            }
        }
    }
}
