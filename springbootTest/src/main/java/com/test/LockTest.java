package com.test;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.concurrent.locks.Lock;

/**
 * Created by zxc on 2020/4/2.
 */
public class LockTest implements WebApplicationInitializer {
    public final Lock lock ;
    public final String s="";

    public LockTest(Lock lock) {
        this.lock = lock;
    }


//    protected final boolean tryAcquire(int acquires) {
//        Thread current = Thread.currentThread();
//        int c = getState(); // 取到当前锁的个数
//        int w = exclusiveCount(c); // 取写锁的个数w
//        if (c != 0) { // 如果已经有线程持有了锁(c!=0)
//            // (Note: if c != 0 and w == 0 then shared count != 0)
//            if (w == 0 || current != getExclusiveOwnerThread()) // 如果写线程数（w）为0（换言之存在读锁） 或者持有锁的线程不是当前线程就返回失败
//                return false;
//            if (w + exclusiveCount(acquires) > MAX_COUNT)    // 如果写入锁的数量大于最大数（65535，2的16次方-1）就抛出一个Error。
//                throw new Error("Maximum lock count exceeded");
//            // Reentrant acquire
//            setState(c + acquires);
//            return true;
//        }
//        if (writerShouldBlock() || !compareAndSetState(c, c + acquires)) // 如果当且写线程数为0，并且当前线程需要阻塞那么就返回失败；或者如果通过CAS增加写线程数失败也返回失败。
//            return false;
//        setExclusiveOwnerThread(current); // 如果c=0，w=0或者c>0，w>0（重入），则设置当前线程或锁的拥有者
//        return true;
//    }

    public void lock(String[] args) {
        lock.lock();
        synchronized (this){


        }
    }


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

    }
}
