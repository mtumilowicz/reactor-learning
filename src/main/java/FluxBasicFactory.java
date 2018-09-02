import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

/**
 * Created by mtumilowicz on 2018-09-01.
 */
class FluxBasicFactory {
    static Flux<String> empty() {
        return Flux.empty();
    }

    static Flux<String> never() {
        return Flux.never();
    }
    
    static Flux<String> just() {
        return Flux.just("foo", "bar");
    }

    static Flux<String> fromIterable() {
        return Flux.fromIterable(Arrays.asList("foo", "bar"));
    }

    static Flux<String> error() {
        return Flux.error(new IllegalStateException());
    }

    static Flux<Long> interval_by100ms_countFrom0To9() {
        return Flux.interval(Duration.ofMillis(100)).take(10);
    }
    
    static Flux<Long> interval_by1s_countFrom10To19() {
        return Flux.interval(Duration.ofSeconds(1)).skip(10).take(10);
    }
    
    static Flux<Integer> generate() {
        return Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next(3*state);
                    if (state == 10) sink.complete();
                    return state + 1;
                });
    }
}
