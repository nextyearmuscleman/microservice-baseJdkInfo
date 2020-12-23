package cn.study.note.MultiThread.base;

import java.util.concurrent.*;

/**
 * @author jixuelei
 * @date 2020/12/21
 */
/**
 * 创建新线程的三种方式
 */
public class BaseThread {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        createThreadByExtendsThread();
        createThreadByImpleCallable();
        createThreadByFutureTask();
    }



    // 1- extends Thread ,由于java不支持多继承，因此不推荐。
    static void createThreadByExtendsThread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("gen new Thread by extends Thread");
                super.run();
            }
        };
        thread.start();
    }

    // 2- implements Runnable
    static void createThreadByImpleRunnable() {
        Thread thread = new Thread(() -> {
            System.out.println("gen new Thread by implements Runnable");
        });
        thread.start();
    }

    // 3- implements Callable
    static void createThreadByImpleCallable() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Object> callbackRes = executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("gen new Thread by implements Callable");
                return "callback result";
            }
        });
        if (callbackRes.isDone()) {
            Object o = null;
            try {
                o = callbackRes.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println(o);
        }

    }

    // callable包装后交给FutureTask, 之后交由ExecutorService处理
    static void createThreadByFutureTask() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        FutureTask<Object> futureTask = new FutureTask<>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(300);
                return "callable res";
            }
        });
        executorService.submit(futureTask);

        System.out.println(futureTask.get());
    }

    // Runnable转换成Callable
    static void runnableConvert2Callable() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Thread runnable = new Thread(() -> {
            System.out.println("");
        });

        Callable<String> callable = Executors.callable(runnable, "");
    }
}


