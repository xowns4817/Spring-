package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 *  비동기 언제 쓰면 좋을까 ?
 *    1. 요청이 긴 경우
 *    2. 로그 처리
 *    3. 푸시 처리
 *
 *
 */

@Service
public class TestService {

    @Async("ktj-async")
    public void asyncVoid( ) {
        System.out.println(Thread.currentThread().getName());
        System.out.println("async return Void !");
    }

    //ThreadPool을 명시적으로 선언하지 않으면 기본적으로 SimpleAsyncTaskExecutor를 사용한다. 이는 각 비동기 호출마다 새로운 쓰레드를 생성한다.
    @Async
    public Future<String> asyncReturnFuture( ) {
        System.out.println(Thread.currentThread().getName());
        System.out.println("async Return Future ! ");
        return new AsyncResult<>("결과!");
    }

    @Async
    public ListenableFuture<String> asyncReturnListenableFuture() {
        System.out.println(Thread.currentThread().getName());
        System.out.println("async Return ListenableFuture ! ");
        return new AsyncResult<>("결과!");
    }

    @Async("ktj-async2")
    public CompletableFuture<String> asyncReturnCompletableFuture() {
        System.out.println(Thread.currentThread().getName());
        System.out.println("async Return CompletableFuture ! " );
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync !!");
            return "OK!!";
        });
    }
}
