import org.junit.Test;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.util.Arrays;

/**
 * Created by mtumilowicz on 2018-09-02.
 */
public class FluxBasicFunctionsTest {

    @Test
    public void handle() {
        StepVerifier.create(FluxBasicFunctions.handle())
                .expectSubscription()
                .thenRequest(10)
                .expectNext(1)
                .expectNext(3)
                .expectNext(5)
                .expectNext(7)
                .expectNext(9)
                .verifyComplete();
    }
    
    @Test
    public void onErrorReturn() {
        StepVerifier.create(FluxBasicFunctions.onErrorReturn())
                .expectSubscription()
                .thenRequest(3)
                .expectNext(1)
                .expectNext(2)
                .expectNext(0)
                .verifyComplete();
                
    }

    @Test
    public void onErrorResume() {
        StepVerifier.create(FluxBasicFunctions.onErrorResume())
                .expectSubscription()
                .thenRequest(5)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();

    }

    @Test
    public void onErrorMap() {
        StepVerifier.create(FluxBasicFunctions.onErrorMap())
                .expectSubscription()
                .thenRequest(5)
                .expectNext(1)
                .expectNext(2)
                .expectError(BusinessException.class)
                .verify();

    }

    @Test
    public void onErrorMap_sout() {
        FluxBasicFunctions.onErrorMap().subscribe(System.out::println, System.out::println);
    }
    
    @Test
    public void retry() {
        StepVerifier.create(FluxBasicFunctions.retry())
                .expectSubscription()
                .thenRequest(6)
                .expectNext(0)
                .expectNext(1)
                .expectNext(2)
                .expectNext(0)
                .expectNext(1)
                .expectNext(2)
                .verifyError();
    }
    
    @Test
    public void zip() {
        StepVerifier.create(FluxBasicFunctions.zip())
                .expectSubscription()
                .thenRequest(5)
                .expectNext(Tuples.of(1 ,6))
                .expectNext(Tuples.of(2 ,7))
                .expectNext(Tuples.of(3 ,8))
                .expectNext(Tuples.of(4 ,9))
                .expectNext(Tuples.of(5 ,10))
                .verifyComplete();
    }
    
    @Test
    public void buffer() {
        StepVerifier.create(FluxBasicFunctions.buffer())
                .expectNext(Arrays.asList(1, 2, 3, 4, 5))
                .expectNext(Arrays.asList(6, 7, 8, 9, 10))
                .verifyComplete();
    }
}