package cn.study.note.MultiThread.middle;

/**
 * 深入学习可重入锁ReentrantLock
 * @author jixuelei
 * @date 2020/12/23
 */
public class DeepStudyReentrantLock {

    /**
     * ReentrantLock默认是非公平锁， 独占式的。 没有实现AQS中的*shared()方法。独占式的
     * 非公平锁的加锁：
     *      final boolean nonfairTryAcquire(int acquires) {
     *             final Thread current = Thread.currentThread();
     *             int c = getState(); // 获取共享资源的同步状态， volatile修饰
     *             if (c == 0) {  // 若没有被占用， 当前线程持有锁 && state+1， 当前线程设置为 【独享线程】
     *                 if (compareAndSetState(0, acquires)) {
     *                     setExclusiveOwnerThread(current);
     *                     return true;
     *                 }
     *             }
     *             else if (current == getExclusiveOwnerThread()) { // 否则，判断持有锁的线程是否是当前线程
     *                 int nextc = c + acquires;                 // Y -》 state+1， 表示支持重入，重复获取锁语义通过state+1来表示
     *                 if (nextc < 0) // overflow
     *                     throw new Error("Maximum lock count exceeded");
     *                 setState(nextc);
     *                 return true;
     *             }
     *             return false;
     *         }
     *
     * 非公平锁解锁:
     *      protected final boolean tryRelease(int releases) {
     *             int c = getState() - releases; // state 减去 releases
     *             if (Thread.currentThread() != getExclusiveOwnerThread())
     *                 throw new IllegalMonitorStateException();
     *             boolean free = false;
     *             if (c == 0) { // 当state == 0时，表示释放锁，将【独享线程】置为null
     *                 free = true;
     *                 setExclusiveOwnerThread(null);
     *             }
     *             setState(c);
     *             return free;
     *         }
     */
}
