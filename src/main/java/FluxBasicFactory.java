import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Created by mtumilowicz on 2018-09-01.
 */
class FluxBasicFactory {
    static Flux<Long> interval_by1s_countFrom10To19() {
        return Flux.interval(Duration.ofSeconds(1)).skip(10).take(10);
    }
}
