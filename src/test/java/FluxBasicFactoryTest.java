import org.junit.Test;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * Created by mtumilowicz on 2018-09-01.
 */
public class FluxBasicFactoryTest {

    @Test
    public void emptyFlux() {
        StepVerifier.create(FluxBasicFactory.emptyFlux())
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void neverFlux() {
        StepVerifier.create(FluxBasicFactory.neverFlux())
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .thenCancel()
                .verify();
    }

    @Test
    public void fromValues() {
        StepVerifier.create(FluxBasicFactory.fromValues())
                .expectSubscription()
                .expectNext("foo")
                .expectNext("bar")
                .verifyComplete();
    }

    @Test
    public void fromIterable() {
        StepVerifier.create(FluxBasicFactory.fromIterable())
                .expectSubscription()
                .expectNext("foo")
                .expectNext("bar")
                .verifyComplete();
    }

    @Test
    public void errorFlux() {
        StepVerifier.create(FluxBasicFactory.errorFlux())
                .expectSubscription()
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    public void counter_from0To9_by100ms() {
        StepVerifier.create(FluxBasicFactory.counter_from0To9_by100ms())
                .expectSubscription()
                .expectNext(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)
                .verifyComplete();
    }
    
    @Test
    public void counter_from10To19_by1s() {
        StepVerifier.withVirtualTime(FluxBasicFactory::counter_from10To19_by1s)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(20))
                .expectNextCount(10)
                .verifyComplete();
    }
}