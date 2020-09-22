# Spring-Async
Void vs Future vs ListenableFuture vs CompletableFuture

 ### 설정 방법 
 - 1. Config 파일위에 @EnableAsync를 붙여 비동기를 사용하겠다고 명시
 - 2. 함수 위에 @Async 키워드를 붙인다. ( 해당 함수는 비동기로 동작한다. )
 
 ### @Async 사용시 주의사항
 - 1. @Async를 사용하려는 함수는 public으로 선언해야 한다.
 - 2. 같은 클래스의 메서드에 @Async를 설정하여 호출할 경우 동작하지 않음.
 - 3. 리턴 타입은 void나 Future<V> 인터페이스 형태만 가능
 
 ### 리턴 값
 - void, Future, ListenableFuture, CompletableFuture를 갖을 수 있다.  ( ListenableFuture, CompletableFuture는 Future를 상속한 구조이다. )
  1. void 
    - @Async 함수의 return 값이 없는 경우. 
  2. Future 
    - @Async 함수의 return 값을 갖는 경우. 주의할 점이 값을 return 받고 get()함수로 그 값을 얻어낼 수 있는데. get( )을 호출하는 순간 블락이된다. 즉, 동기적으로 실행됨..
      ```java
       while(true) {
            if(future.isDone()) {
                System.out.println(future.get()); // 이런식으로 처리 안하고 바로 future.get()을 호출하면 블로킹이된다.
                break;
            }
        }
     ```
     - 위와같이 future가 끝날때 까지 무한루프를 돌려준다.
     
  3. ListenableFuture
    - 자바스크립트의 콜백형식이라고 보면 됨. -> 콜백지옥 발생할수 있다.
     
  4. CompletableFuture
      - 자바 8에 새로 추가된 기능. 3번 콜백지옥을 해결 할 수 있다.
      - thenAccept -> return 값 없음 ( CompletableFuture<Void> ) -> 내부에 콜백함수 선언 가능
      - thenApply -> return 값 있음 ( CompletableFuture<T> )
      - thenCombine -> CompletableFuture를 2개를 실행해서 결과를 조합할때 사용한다. thenCombine은 병렬 실행을 해서 조합하는데, 순차적으로 실행하지 않는다.
      - thenCompose -> CompletableFuture를 순차적으로 실행한다.
      -  allOf -> thenCombine의 구현은 두 개의 CompletableFuture를 조합할때 사용한다. 만약, CompletableFuture가 3개 이상일때 allOf를 사용한다.
      - allOf 메서드에 CompletableFuture 리스트를 파라미터로 넣어주면 모든 Future가 완료 되었을때 thenApply가 실행된다.
      
     
### 스레드 풀 관련설정
    - 스레드풀 설정을 따로하지 않으면 기본적으로 SimpleAsyncTaskExecutor를 사용한다. 이는 각 비동기 호출마다 새로운 쓰레드를 생성한다.
    - 스레드 풀 설정은 다음과 같이 할 수 있다. ( 보통은 해당 소스를 돌리려는 pc의 core갯수로 쓰레드 풀을 설정하지만,  테스트를 위해 setCorePoolSize를 임의로 1로 지정했다. 이는 쓰레드를 풀에 1개의 쓰레드만을 만든다는 뜻이다. 즉, 1개의 요청이 와서 쓰레드를 사용하고 있으면 다른 요청은 block된다는 뜻이다. 실제로 해당 size를 바꿔가며 해당 깃 코드를 돌려보면 알 수 있다.
 ```java
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
 ```
    - 사용법은 @Async("ktj-async") 이런식으로 빈 이름으로 주입하면 된다.
    - 만약, 쓰레드풀 설정을 여러개 해주고 싶다면 빈 이름을 다르게 여러개 만들어주면 된다.
    - 해당 깃 코드의 TestController를 보면 Thread.currentThread().getName()로 현재 로직을 수행하고있는 쓰레드의 이름을 출력하게 해놓았다. 스레드 풀 설정을 하지않았을때, 1개 했을때, 여러개 했을때 각각 이름이 어떻게 찍히는 확인해보자
 
