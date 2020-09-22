package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class config extends AsyncConfigurerSupport {

    @Bean("ktj-async")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1); // 스레드 풀을 해당 개수 까지 기본적으로 생성한다. 처음 요청이 들어올대 pool size만큼 생성한다.
        executor.setMaxPoolSize(10); // 현제 core 스레드를 모두 사용하고 있을때, 큐에 만들어 대기시킨다.
        executor.setQueueCapacity(500); // 대기하는 작업이 큐에 꽉 찰경우, 풀을 해당 개수까지 더 생성한다.
        executor.setThreadNamePrefix("taejun-thread");
        executor.initialize();
        return executor;
    }

    @Bean("ktj-async2")
    public Executor getAsyncExecutor2() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1); // 스레드 풀을 해당 개수 까지 기본적으로 생성한다. 처음 요청이 들어올대 pool size만큼 생성한다.
        executor.setMaxPoolSize(10); // 현제 core 스레드를 모두 사용하고 있을때, 큐에 만들어 대기시킨다.
        executor.setQueueCapacity(500); // 대기하는 작업이 큐에 꽉 찰경우, 풀을 해당 개수까지 더 생성한다.
        executor.setThreadNamePrefix("kimCoding-thread");
        executor.initialize();
        return executor;
    }
}
