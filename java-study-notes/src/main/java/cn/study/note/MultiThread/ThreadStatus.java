package cn.study.note.MultiThread;

/**
 * @author jixuelei
 * @date 2020/12/21
 */

/**
 * 线程状态转换
 */
public class ThreadStatus {

    public static void main(String[] args) throws InterruptedException {
        //testThreadInterrupt();
        testThreadJoinMethod();
    }

    /**
     * 线程状态：
     *
     * 1- NEW: 初始的状态，线程被构建但还没有调用 start()
     * 2- RUNNABLE: 运行状态，就绪/运行中 归类于RUNNABLE，调用start()后转换
     * 3- BLOCKED:阻塞状态， 线程进入了synchronized方法/块会转换成此状态
     * 4- WAITING: 等待状态，表示当前的线程需要等待其他线程做出通知or中断
     *      Object.wait() / Object.join() / LockSupport.park()会触发线程转换为该状态
     * 5- TIME_WAITING: 超时等待状态， 表示线程进入一定时间的等待状态，可以在指定时间之后自行返回
     *      Thread.sleep(long ms) / Object.wait(long ms) / Thread.join(long ms) / LockSupport.parkNanos() / LockSupport.parkUntil()
     * 6- TERMINATED:终止状态，当前线程已经执行完毕
     *
     * 4/5 状态转化为2时，需要通过一定的时间之后或者Object.notify() / Object.notifyAll() / LockSupport.unpark(Thread)唤醒线程
     */

    /**
     * 线程的中断  线程间一种简便的交互方式。
     * 1- interrupt() 尝试中断当前线程
     * 2- isInterrupted() 判断当前线程是否被中断，
     * 3- static boolean interrupted(), 静态方法，会重置线程的标志位。false。
     * 4- 如果线程阻塞时被调用wait() / wait(long) / join() / sleep(long)会抛出异常InterruptedException
     */
    static void testThreadInterrupt() throws InterruptedException {

        final Thread sleepThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                // sleep()方法会因为调用了interrupt()方法
                // 而抛出异常‘java.lang.InterruptedException: sleep interrupted’
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {}
        });


        Thread busyThread = new Thread(() -> {
            while (true) {}
        });


        sleepThread.start();
        busyThread.start();

        sleepThread.interrupt();  // 尝试中断sleepThread线程
        busyThread.interrupt(); // 尝试中断busyThread线程
        while (sleepThread.isInterrupted()) { // 等sleepThread内抛出异常后，中断标志位重置为false，跳出循环
            System.out.println("ssss");
        }; // 测试sleepThread线程是否被中断，该线程的中断状态不被该方法影响
        System.out.println(sleepThread.isInterrupted());  // false 该线程抛出了异常，没有被中断
        System.out.println(busyThread.isInterrupted()); // true 该线程被中断了
    }

    /**
     * join() {
     *     while (isAlive()) {
     *           wait(0);
     *       }
     * }
     *   内部调用.isAlive()方法，如果当前线程已经被start同时还没有died，则return true
     *
     * 1- final void join(){}
     * 2- final synchronized void join(long millis)
     * 3- final synchronized void join(long millis, int nanos)
     */
    static void testThreadJoinMethod() {

        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
        });
        thread1.start();
        try {
            // thread1线程加入，此时Main线程挂起, Main线程执行了wait()，等待thread1线程结束后再继续执行
            // 在哪个线程范围内执行，那该线程就挂起，调用方线程开始执行
            // thread1线程执行结束后，调用 notify() / notifyAll()唤醒当前线程
            thread1.join(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main");
    }

    /**
     * sleep(long millis):当前线程按照指定的时间休眠，如果当前线程获得了锁，sleep方法并不会失去锁
     *  1- static native void sleep(long millis)静态方法
     *  2- sleep(long millis) & wait()的比较
     *      2.1- sleep()方法是Thread的静态方法，而wait是Object实例方法
     *      2.2- wait()方法[必须]要在同步方法或者同步块中调用，也就是必须已经获得对象锁。源码中表示wait()方法的使用前提是获取到对象的monitor
     *          而sleep()方法没有这个限制可以在任何地方种使用。
     *          另外，wait()方法会释放占有的对象锁，使得该线程进入等待池中，等待下一次获取资源。而sleep()方法只是会让出CPU并不会释放掉对象锁；
     *      2.3- sleep()方法在休眠时间达到后如果再次获得CPU时间片就会继续执行，
     *          而wait()方法必须等待Object.notify()/Object.notifyAll()通知后，才会离开等待池，并且再次获得CPU时间片才会继续执行。
     */
    static void testThreadSleepMethod() throws InterruptedException {
        Thread thread1 = new Thread(() -> {});
        thread1.start();

        thread1.wait();
    }

    /**
     * yield():
     * 方法签名：static native void yield()
     * :使得当前线程让出cpu时间片，交由【相同优先级 same priority(默认是5，min:1 / max: 10)】的线程去竞争，而sleep()则使得当前线程让出的时间片可以由所有的线程去竞争
     */
}






















