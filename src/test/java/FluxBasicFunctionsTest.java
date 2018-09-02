import org.junit.Test;
import reactor.test.StepVerifier;

import static org.junit.Assert.*;

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
}