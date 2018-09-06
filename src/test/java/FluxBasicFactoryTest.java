import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
    
    @Test
    public void combineLatest() {
        StepVerifier.create(FluxBasicFactory.combineLatest())
                .expectSubscription()
                .expectNext("c1")
                .expectNext("c2")
                .expectNext("c3")
                .verifyComplete();
    }

    @Test
    public void concat() {
        StepVerifier.create(FluxBasicFactory.concat())
                .expectSubscription()
                .expectNext("a")
                .expectNext("b")
                .expectNext("c")
                .expectNext("1")
                .expectNext("2")
                .expectNext("3")
                .verifyComplete();
    }

    @Test
    public void defer() {
        AtomicInteger i1 = new AtomicInteger();

        Flux<Integer> source = Flux.just(i1.incrementAndGet());

        assertThat(source.blockLast(), is(1));
        assertThat(source.blockLast(), is(1));
        assertThat(source.blockLast(), is(1));

        AtomicInteger i2 = new AtomicInteger();
        
        Flux<Integer> deferred =
                Flux.defer(() -> Flux.just(i2.incrementAndGet()));

        assertThat(deferred.blockLast(), is(1));
        assertThat(deferred.blockLast(), is(2));
        assertThat(deferred.blockLast(), is(3));
    }

    @Test
    public void merge() {
        StepVerifier.create(FluxBasicFactory.merge())
                .expectSubscription()
                .expectNext("1")
                .expectNext("2")
                .expectNext("3")
                .expectNext("a")
                .expectNext("b")
                .expectNext("c")
                .verifyComplete();
    }
    
    @Test
    public void switchOnNext() {
        StepVerifier.create(FluxBasicFactory.switchOnNext())
                .expectSubscription()
                .expectNext("1")
                .expectNext("2")
                .expectNext("3")
                .expectNext("!")
                .expectNext("@")
                .expectNext("#")
                .verifyComplete();
    }
}