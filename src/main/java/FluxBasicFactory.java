import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

/**
 * Created by mtumilowicz on 2018-09-01.
 */
class FluxBasicFactory {
    static Flux<String> emptyFlux() {
        return Flux.empty();
    }

    static Flux<String> neverFlux() {
        return Flux.never();
    }
    
    static Flux<String> fromValues() {
        return Flux.just("foo", "bar");
    }

    static Flux<String> fromIterable() {
        return Flux.fromIterable(Arrays.asList("foo", "bar"));
    }

    static Flux<String> errorFlux() {
        return Flux.error(new IllegalStateException());
    }

    static Flux<Long> counterFrom0To9_by100ms() {
        return Flux.interval(Duration.ofMillis(100)).take(10);
    }
    
    static Flux<Long> counterFrom10To19_by1s() {
        return Flux.interval(Duration.ofSeconds(1)).skip(10).take(10);
    }
}
