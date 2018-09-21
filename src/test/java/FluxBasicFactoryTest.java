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
//        given
        Flux<String> emptyFlux = Flux.empty();
        
//        expect
        StepVerifier.create(emptyFlux)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void never() {
//        given
        Flux<String> neverFlux = Flux.never();
        
//        expect
        StepVerifier.create(neverFlux)
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .thenCancel()
                .verify();
    }

    @Test
    public void just() {
//        given
        Flux<String> justFlux = Flux.just("foo", "bar");
        
//        expect
        StepVerifier.create(justFlux)
                .expectSubscription()
                .expectNext("foo")
                .expectNext("bar")
                .verifyComplete();
    }

    @Test
    public void fromIterable() {
//        given
        Flux<String> iterableFlux = Flux.fromIterable(Arrays.asList("foo", "bar"));
        
//        expect
        StepVerifier.create(iterableFlux)
                .expectSubscription()
                .expectNext("foo")
                .expectNext("bar")
                .verifyComplete();
    }

    @Test
    public void error() {
//        given
        Flux<Object> errorFlux = Flux.error(new IllegalStateException());
        
//        expect
        StepVerifier.create(errorFlux)
                .expectSubscription()
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    public void interval_by100ms_countFrom0To9() {
//        given
        Flux<Long> intervalFlux = Flux.interval(Duration.ofMillis(100)).take(10);
        
//        expect
        StepVerifier.create(intervalFlux)
                .expectSubscription()
                .expectNext(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)
                .verifyComplete();
    }
    
    @Test
    public void interval_by1s_countFrom10To19() {
//        expect
        StepVerifier.withVirtualTime(FluxBasicFactory::interval_by1s_countFrom10To19)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(20))
                .expectNextCount(10)
                .verifyComplete();
    }
    
    @Test
    public void generate() {
//        given
        Flux<Integer> generateFlux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next(3 * state); // new signal
                    if (state == 10) sink.complete();
                    return state + 1; // new state
                });
        
//        expect
        StepVerifier.create(generateFlux)
                .expectSubscription()
                .expectNext(0, 3, 6, 9)
                .expectNextCount(7)
                .verifyComplete();
    }
    
    @Test
    public void combineLatest() {
//        given
        Flux<String> combineLatestFlux = Flux.combineLatest(Flux.just("a", "b", "c"),
                Flux.just("1", "2", "3"),
                (letter, number) -> letter + number);
        
//        expect
        StepVerifier.create(combineLatestFlux)
                .expectSubscription()
                .expectNext("c1")
                .expectNext("c2")
                .expectNext("c3")
                .verifyComplete();
    }

    @Test
    public void concat() {
//        given
        Flux<String> concatFlux = Flux.concat(Flux.just("a", "b", "c"), Flux.just("1", "2", "3"));
        
//        expect
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
//        given
        Flux<String> mergeFlux = Flux.merge(
                Flux.just("a", "b", "c").delayElements(Duration.ofMillis(10)), 
                Flux.just("1", "2", "3"));
        
//        expect
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
//        given
        Flux<String> switchOnNextFlux = Flux.switchOnNext(Flux.just(
                Flux.just("a", "b", "c").delayElements(Duration.ofMillis(2)),
                Flux.just("1", "2", "3"),
                Flux.just("!", "@", "#")));
        
//        expect
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