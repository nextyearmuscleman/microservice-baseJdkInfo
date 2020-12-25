package cn.study.note.MultiThread.base;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jixuelei
 * @date 2020/12/25
 */
public class AboutCondition {

    public static void main(String[] args) {
        Lock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        boolean b = reentrantLock.tryLock();
    }
}
