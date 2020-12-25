package cn.study.note.MultiThread.middle;

/**
 * 深入学习可重入的读写锁
 * @author jixuelei
 * @date 2020/12/24
 */
public class DeepStudyReentrantReadWriteLock {

    /**
     * ReentrantReadWriteLock代码结构类似于ReentrantLock
     * 1- 基于一个内部类Sync extends AbstractQueuedSynchronizer
     * 2- ReadLock / WriteLock
     *      2.1- ReadLock基于共享锁，其调用的lock()方法是Sync内部的 tryReleaseShared()方法。 unlock()方法是调用Sync内部的tryReleaseShared()
     *      2.2- WriteLock基于独占式锁，其调用的lock()方法是Sync内部的 tryAcquire()方法。 unlock()方法是调用Sync内部的tryRelease()
     *      2.3- 读写锁内部使用亮哥哥方法来表示读/写锁各自的获取次数
     *          sharedCount() / exclusiveCount()
     *          state[int 4字节*8=32位]。高16位表示读锁获取的次数 / 低16位表示写锁获取的次数
     *
     */

    /**
     * 2.1 读锁获取锁的方法：int tryAcquireShared(){}
     *    返回结果： 负数：fail / 0：
     *    Walkthrough:
     *      1-  如果此时写锁已被获取 && 获取写锁的现场 ！= 当前线程。 则fail
     *      2- 读线程不堵塞 && 读锁获取的次数 《 极限值 && CAS操作成功  success
     *
     *      Thread current = Thread.currentThread();
     *             int c = getState();
     *             if (exclusiveCount(c) != 0 && getExclusiveOwnerThread() != current) // 1- 写锁获取的次数！=0 && 当前获取的写锁不是本身 return -1， false
     *                 return -1;
     *             int r = sharedCount(c); // 2- 获取state 高16位， 读锁获取的次数
     *             if (!readerShouldBlock() && // 3.1- 本次读锁是否应该被阻塞 readerShouldBlock()方法有Fair/NonFair两种实现
     *                 r < MAX_COUNT &&       //3.2- 次数 < 极限值
     *                 compareAndSetState(c, c + SHARED_UNIT)) { // 3.3- CAS操作更新c值
     *                 // 处理读锁相关的属性赋值
     *                 if (r == 0) { // 4.1- 读锁获取的次数==0， 相关属性进行赋值
     *                     firstReader = current; // first thread to have acquired the read lock.
     *                     firstReaderHoldCount = 1; // 表示firstReader重入的次数
     *                 } else if (firstReader == current) { // 4.2- 特殊情况，读锁获取次数》0，判断firstReader是否为当前线程
     *                     firstReaderHoldCount++;
     *                 } else {
     *                     HoldCounter rh = cachedHoldCounter; // HoldCounter 读锁持有次数的计数器。
     *                     if (rh == null || rh.tid != getThreadId(current))
     *                         cachedHoldCounter = rh = readHolds.get(); // readHolds 是ThreadLocal<HoldCounter>
     *                     else if (rh.count == 0)
     *                         readHolds.set(rh);
     *                     rh.count++;
     *                 }
     *                 return 1;
     *             }
     *             return fullTryAcquireShared(current);
     *
     *        3.1- Fair实现： 队列中是否有比当前线程存在时间更长的线程
     *             NonFair实现： apparentlyFirstQueuedIsExclusive() {
     *                  Node h, s;
     *                  return (h = head) != null && // head != null
     *                          (s = h.next)  != null && // 队列中存在其他节点
     *                          !s.isShared()         && // 第一个节点不是共享模式，而是独占式模式
     *                          s.thread != null;      // 且当前node的thread属性 ！= null 。则当前线程堵塞。 否则不堵塞
     *             }
     */

    /**
     * 2.2 写锁获取锁方法：
     * Walkthrough方法规则:
     *      1- 如果 读次数 ！= 0 || (写次数 ！= 0 && 当前线程 ！= getExclusiveOwnerThread())  return false
     *      2- 如果 count 》 MAX_COUNT 则return false
     *      3- 如果满足可重入获取或者排队策略允许 则当前线程能够获取到锁
     *
     * protected final boolean tryAcquire(int acquires) {
     *          Thread current = Thread.currentThread();
     *             int c = getState();  // 1- 获取当前资源的同步状态
     *             int w = exclusiveCount(c); // 2- 获取 写锁获取的次数
     *             if (c != 0) { // 3- 已被加锁
     *                  // 4- 当前资源已被读锁获取 return false || 当前线程非当前获取写锁的线程 return false
     *                 if (w == 0 || current != getExclusiveOwnerThread())
     *                     return false;
     *                 if (w + exclusiveCount(acquires) > MAX_COUNT)
     *                     throw new Error("Maximum lock count exceeded");
     *
     *                 // 5- 当前线程设置可重入状态
     *                 setState(c + acquires);
     *                 return true;
     *             }
     *             // 6- 当前资源暂未被写锁/读锁获取
     *             if (writerShouldBlock() || // 7- writerShouldBlock()方法有Fair/NonFair两种实现
     *                 !compareAndSetState(c, c + acquires)) // 8- CAS操作失败 则return false获取锁失败
     *                 return false;
     *             setExclusiveOwnerThread(current); // 9- 设置当前线程为独占式线程。
     *             return true;
     *
     *      7- 步骤：   writerShouldBlock()
     *          NonFair的实现方式是直接 return false。写线程总是能直接占用锁
     *          Fair的实现方式是调用AQS里的 hasQueuedPredecessors()方法， 在队列中是否有其他的线程等待时间比当前线程更长， Y- return true/ N- return false
     */

    /**
     * 2.2 写锁的释放锁方法。 逻辑比较简单。根据目前的（state-releases）是否 == 0.
     *      protected final boolean tryRelease(int releases) {
     *             if (!isHeldExclusively()) // 1- 判断当前线程是否 == 独占式获取资源的线程
     *                 throw new IllegalMonitorStateException();
     *             int nextc = getState() - releases;
     *             boolean free = exclusiveCount(nextc) == 0; // 2- 写锁获取的次数是否==0
     *             if (free) // 同步资源暂未被任何写锁获取，则将独占式线程置为null
     *                 setExclusiveOwnerThread(null);
     *             setState(nextc);
     *             return free;
     *         }
     */


}
