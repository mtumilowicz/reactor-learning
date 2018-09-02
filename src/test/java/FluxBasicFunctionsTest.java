import org.junit.Test;
import reactor.test.StepVerifier;

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
}