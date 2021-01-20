package com.ppio.bd.ksdata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public Executor kafkaThreadPool() {

        ExecutorService pool = Executors.newFixedThreadPool(5);
        return pool;
    }


    @Bean
    public ScheduledThreadPoolExecutor sessionCommitThread() {

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
//        executor.setCorePoolSize(5);
        executor.setKeepAliveTime(11, TimeUnit.MINUTES);
        //配置最大线程数
        //配置队列大小
//        executor.;
        //配置线程池中的线程的名称前缀
        executor.setMaximumPoolSize(10);
//        executor.setThreadFactory(r -> new Thread("sessionCommitThread-"));
        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
//        executor.initialize();
        return executor;
    }


}
