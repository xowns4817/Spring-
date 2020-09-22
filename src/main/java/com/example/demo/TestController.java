package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/test")
    public void test() throws ExecutionException, InterruptedException {

        // case 1 : return void
        System.out.println("start!!!");
        testService.asyncVoid();
        System.out.println("end!!!");

        System.out.println("=====================================");

        // case 2 : return Future<V>
        System.out.println("start!!!");
        Future<String> future = testService.asyncReturnFuture( );
        System.out.println("end!!!");

        while(true) {
            if(future.isDone()) {
                System.out.println(future.get()); // 이런식으로 처리 안하고 바로 future.get()을 호출하면 블로킹이된다.
                break;
            }
        }

        System.out.println("=====================================");

        // case 3: return ListenableFuture<T> extends Future<T>
        /*
        System.out.print("start !!");
        testService.asyncReturnListenableFuture().addCallback((result) -> {
            System.out.println(result);
        }, (e) -> {
            System.out.println(e.getMessage());
        });

        System.out.print("end !!");
    }
*/

        System.out.println("=====================================");


        // case 4: CompletableFuture implements Future\
        // thenAccept -> return 값 없음 ( CompletableFuture<Void> ) -> 내부에 콜백함수 선언 가능
        // thenApply -> return 값 있음 ( CompletableFuture<T> )
        // thenCombine -> CompletableFuture를 2개를 실행해서 결과를 조합할때 사용한다. thenCombine은 병렬 실행을 해서 조합하는데, 순차적으로 실행하지 않는다.
        // thenCompose -> CompletableFuture를 순차적으로 실행한다.
        // allOf -> thenCombine의 구현은 두 개의 CompletableFuture를 조합할때 사용한다. 만약, CompletableFuture가 3개 이상일때 allOf를 사용한다.
        // allOf 메서드에 CompletableFuture 리스트를 파라미터로 넣어주면 모든 Future가 완료 되었을때 thenApply가 실행된다.
        System.out.println("start !!");
        testService.asyncReturnCompletableFuture()
                .thenApply(p -> {
                    System.out.println("thenApply ! : " + p);
                    return p + "???";
                })
                .thenAccept(p -> {
                    System.out.println("Result : " + p);
                });

        System.out.println("end !!");
    }
}
