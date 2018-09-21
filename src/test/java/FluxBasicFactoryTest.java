import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by mtumilowicz on 2018-09-01.
 */
public class FluxBasicFactoryTest {

    @Test
    public void empty() {
        Flux<String> emptyFlux = Flux.empty();
        StepVerifier.create(emptyFlux)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void never() {
        Flux<String> neverFlux = Flux.never();
        StepVerifier.create(neverFlux)
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .thenCancel()
                .verify();
    }

    @Test
    public void just() {
        Flux<String> justFlux = Flux.just("foo", "bar");
        StepVerifier.create(justFlux)
                .expectSubscription()
                .expectNext("foo")
                .expectNext("bar")
                .verifyComplete();
    }

    @Test
    public void fromIterable() {
        Flux<String> iterableFlux = Flux.fromIterable(Arrays.asList("foo", "bar"));
        StepVerifier.create(iterableFlux)
                .expectSubscription()
                .expectNext("foo")
                .expectNext("bar")
                .verifyComplete();
    }

    @Test
    public void error() {
        Flux<Object> errorFlux = Flux.error(new IllegalStateException());
        StepVerifier.create(errorFlux)
                .expectSubscription()
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    public void interval_by100ms_countFrom0To9() {
        Flux<Long> intervalFlux = Flux.interval(Duration.ofMillis(100)).take(10);
        StepVerifier.create(intervalFlux)
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
        Flux<Object> generateFlux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next(3 * state); // new signal
                    if (state == 10) sink.complete();
                    return state + 1; // new state
                });
        StepVerifier.create(generateFlux)
                .expectSubscription()
                .expectNext(0, 3, 6, 9)
                .expectNextCount(7)
                .verifyComplete();
    }
    
    @Test
    public void combineLatest() {
        Flux<String> combineLatestFlux = Flux.combineLatest(Flux.just("a", "b", "c"),
                Flux.just("1", "2", "3"),
                (letter, number) -> letter + number);
        StepVerifier.create(combineLatestFlux)
                .expectSubscription()
                .expectNext("c1")
                .expectNext("c2")
                .expectNext("c3")
                .verifyComplete();
    }

    @Test
    public void concat() {
        Flux<String> concatFlux = Flux.concat(Flux.just("a", "b", "c"), Flux.just("1", "2", "3"));
        StepVerifier.create(concatFlux)
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
        Flux<String> mergeFlux = Flux.merge(
                Flux.just("a", "b", "c").delayElements(Duration.ofMillis(10)), 
                Flux.just("1", "2", "3"));
        StepVerifier.create(mergeFlux)
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
        Flux<String> switchOnNextFlux = Flux.switchOnNext(Flux.just(
                Flux.just("a", "b", "c").delayElements(Duration.ofMillis(2)),
                Flux.just("1", "2", "3"),
                Flux.just("!", "@", "#")));
        StepVerifier.create(switchOnNextFlux)
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