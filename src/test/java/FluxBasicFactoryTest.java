import org.junit.Test;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * Created by mtumilowicz on 2018-09-01.
 */
public class FluxBasicFactoryTest {

    @Test
    public void empty() {
        StepVerifier.create(FluxBasicFactory.empty())
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void never() {
        StepVerifier.create(FluxBasicFactory.never())
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .thenCancel()
                .verify();
    }

    @Test
    public void just() {
        StepVerifier.create(FluxBasicFactory.just())
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
    public void error() {
        StepVerifier.create(FluxBasicFactory.error())
                .expectSubscription()
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    public void interval_by100ms_countFrom0To9() {
        StepVerifier.create(FluxBasicFactory.interval_by100ms_countFrom0To9())
                .expectSubscription()
                .expectNext(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)
                .verifyComplete();
    }
    
    @Test
    public void interval_by1s_countFrom10To19() {
        StepVerifier.withVirtualTime(FluxBasicFactory::interval_by1s_countFrom10To19)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(20))
                .expectNextCount(10)
                .verifyComplete();
    }
    
    @Test
    public void generate() {
        StepVerifier.create(FluxBasicFactory.generate())
                .expectSubscription()
                .expectNextCount(11)
                .verifyComplete();
    }
}