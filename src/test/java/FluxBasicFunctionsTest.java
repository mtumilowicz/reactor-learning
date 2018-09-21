import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Created by mtumilowicz on 2018-09-02.
 */
public class FluxBasicFunctionsTest {

    @Test
    public void handle() {
//        given
        UnaryOperator<Integer> transform = i -> {
            if (isNull(i) || i % 2 == 0) {
                return null;
            }
            return i;
        };

//        and
        Flux<Object> flux = Flux.range(1, 10)
                .handle((i, sink) -> {
                    Integer transformed = transform.apply(i);
                    if (nonNull(transformed)) sink.next(i);
                });
        
//        expect
        StepVerifier.create(flux)
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
//        given
        UnaryOperator<Integer> doSomethingDangerous = i -> {
            if (i == 3) {
                throw new IllegalStateException("illegal state");
            }
            return i;
        };

//        and
        Flux<Integer> flux = Flux.range(1, 5)
                .map(doSomethingDangerous)
                .onErrorReturn(0);
        
//        expect
        StepVerifier.create(flux)
                .expectSubscription()
                .thenRequest(3)
                .expectNext(1)
                .expectNext(2)
                .expectNext(0)
                .verifyComplete();

    }

    @Test
    public void onErrorResume() {
//        given
        UnaryOperator<Integer> doSomethingDangerous = i -> {
            if (i == 3) {
                throw new IllegalStateException("illegal state");
            }
            return i;
        };
        
//        and
        Flux<Integer> onErrorResumeFlux = Flux.range(1, 5)
                .map(doSomethingDangerous)
                .onErrorResume(err -> Flux.range(3, 3));

//        expect
        StepVerifier.create(onErrorResumeFlux)
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
//        given
        UnaryOperator<Integer> doSomethingDangerous = i -> {
            if (i == 3) {
                throw new IllegalStateException("illegal state");
            }
            return i;
        };
        
//        and
        Flux<Integer> onErrorMapFlux = Flux.range(1, 5)
                .map(doSomethingDangerous)
                .onErrorMap(err -> new BusinessException(err.getLocalizedMessage()));
        
//        expect
        StepVerifier.create(onErrorMapFlux)
                .expectSubscription()
                .thenRequest(5)
                .expectNext(1)
                .expectNext(2)
                .expectError(BusinessException.class)
                .verify();

    }

    @Test
    public void onErrorMap_sout() {
//        given
        UnaryOperator<Integer> doSomethingDangerous = i -> {
            if (i == 3) {
                throw new IllegalStateException("illegal state");
            }
            return i;
        };

//        and
        Flux<Integer> onErrorMapFlux = Flux.range(1, 5)
                .map(doSomethingDangerous)
                .onErrorMap(err -> new BusinessException(err.getLocalizedMessage()));

//        then
        onErrorMapFlux.subscribe(System.out::println, System.out::println);
    }

    @Test
    public void retry() {
//        given
        UnaryOperator<Integer> doSomethingDangerous = i -> {
            if (i == 3) {
                throw new IllegalStateException("illegal state");
            }
            return i;
        };
        
//        and
        Flux<Integer> retry = Flux.range(0, 5)
                .map(doSomethingDangerous)
                .retry(1);

//        expect
        StepVerifier.create(retry)
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
//        given
        Flux<Integer> first = Flux.range(1, 5);
        Flux<Integer> second = Flux.range(6, 5);

//        and
        Flux<Tuple2<Integer, Integer>> zipFlux = Flux.zip(first, second);
        
//        expect
        StepVerifier.create(zipFlux)
                .expectSubscription()
                .thenRequest(5)
                .expectNext(Tuples.of(1, 6))
                .expectNext(Tuples.of(2, 7))
                .expectNext(Tuples.of(3, 8))
                .expectNext(Tuples.of(4, 9))
                .expectNext(Tuples.of(5, 10))
                .verifyComplete();
    }

    @Test
    public void buffer() {
//        given
        Flux<List<Integer>> bufferFlux = Flux.range(1, 10)
                .buffer(5);
        
//        expect
        StepVerifier.create(bufferFlux)
                .expectNext(Arrays.asList(1, 2, 3, 4, 5))
                .expectNext(Arrays.asList(6, 7, 8, 9, 10))
                .verifyComplete();
    }

    @Test
    public void window() {
//        given
        Flux<Flux<Integer>> windowFlux = Flux.range(1, 10)
                .window(5);

//        and
        Mono<List<Flux<Integer>>> collect = windowFlux
                .collect(Collectors.toList());

//        when
        List<Flux<Integer>> block = collect.block();

//        then
        block.get(0).log().blockLast();
        block.get(1).log().blockLast();
    }
}