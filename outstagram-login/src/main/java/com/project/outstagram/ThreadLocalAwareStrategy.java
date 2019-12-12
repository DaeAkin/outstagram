package com.project.outstagram;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy {
    private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    public ThreadLocalAwareStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy) {
        //스프링 클라우드가 미리 정의한 병행성 클래스를 이 HystrixConcurrencyStrategy 클래스의 생성자에게 전달
        this.existingConcurrencyStrategy = existingConcurrencyStrategy;
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return existingConcurrencyStrategy != null ?
                existingConcurrencyStrategy.getBlockingQueue(maxQueueSize) :
                super.getBlockingQueue(maxQueueSize);
    }

//    @Override
//    public <T> Callable<T> wrapCallable(Callable<T> callable) {
//        return existingConcurrencyStrategy != null
//                ? existingConcurrencyStrategy.wrapCallable(
//                        new DelegatingUs
//        )
//    }
}
