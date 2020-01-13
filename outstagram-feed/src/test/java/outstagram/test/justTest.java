package outstagram.test;

import org.junit.Test;
import reactor.core.publisher.Mono;


public class justTest {

    @Test
    public void test () throws InterruptedException {
        int a= 5;
        Mono<Integer> monoJust = Mono.just(a);
        int finalA = a;
        Mono<Integer> monoDefer = Mono.defer(() -> Mono.just(finalA));

        monoJust.subscribe(integer1 -> System.out.println(integer1));
        monoDefer.subscribe(integer1 -> System.out.println(integer1));

        a = 7;
        monoJust.subscribe(integer1 -> System.out.println(integer1));
        monoDefer.subscribe(integer1 -> System.out.println(integer1));
    }
}
