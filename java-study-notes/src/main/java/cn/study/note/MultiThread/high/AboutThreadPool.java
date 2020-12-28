package cn.study.note.MultiThread.high;

import java.util.concurrent.*;

/**
 * @author jixuelei
 * @date 2020/12/28
 */
public class AboutThreadPool {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 10000L, TimeUnit.HOURS, new LinkedBlockingQueue<>(), r -> null,
                (r, executor) -> {
                    //
                });

        threadPoolExecutor.execute(() -> {

        });
    }
}
