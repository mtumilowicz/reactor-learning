import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * Created by mtumilowicz on 2018-09-02.
 */
public class FluxSchedulersTest {
    @Test
    public void parallel() {
        Flux.interval(Duration.ofSeconds(1))
                .log()
                .take(5)
                .subscribe(System.out::println);

        Flux.interval(Duration.ofSeconds(1))
                .log()
                .take(5)
                .subscribe(System.out::println);

        Flux.interval(Duration.ofSeconds(1))
                .log()
                .take(5)
                .blockLast();
    }

    @Test
    public void elastic() {
        Flux.interval(Duration.ofSeconds(1), Schedulers.elastic())
                .log()
                .take(5)
                .subscribe(System.out::println);

        Flux.interval(Duration.ofSeconds(1), Schedulers.elastic())
                .log()
                .take(5)
                .subscribe(System.out::println);

        Flux.interval(Duration.ofSeconds(1), Schedulers.elastic())
                .log()
                .take(5)
                .blockLast();
    }

    @Test
    public void single() {
        Flux.interval(Duration.ofSeconds(1), Schedulers.single())
                .log()
                .take(5)
                .subscribe(System.out::println);

        Flux.interval(Duration.ofSeconds(1), Schedulers.single())
                .log()
                .take(5)
                .subscribe(System.out::println);

        Flux.interval(Duration.ofSeconds(1), Schedulers.single())
                .log()
                .take(5)
                .blockLast();
    }

    @Test
    public void newSingle() {
        Flux.interval(Duration.ofSeconds(1), Schedulers.newSingle("first"))
                .log()
                .take(5)
                .subscribe(System.out::println);

        Flux.interval(Duration.ofSeconds(1), Schedulers.newSingle("second"))
                .log()
                .take(5)
                .subscribe(System.out::println);

        Flux.interval(Duration.ofSeconds(1), Schedulers.newSingle("third"))
                .log()
                .take(5)
                .blockLast();
    }
}
