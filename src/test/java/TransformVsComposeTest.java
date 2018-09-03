import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

/**
 * Created by mtumilowicz on 2018-09-03.
 */
public class TransformVsComposeTest {

    private final static BiFunction<AtomicBoolean, Flux<String>, Flux<String>> toUpperCase = (toUpper, f) -> {
        if (toUpper.get()) {
            return f.map(String::toUpperCase);
        }
        return f;
    };

    private final static Flux<String> flux = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"));

    @Test
    public void transform() throws InterruptedException {
        AtomicBoolean toUpper = new AtomicBoolean();

        Flux<String> transform = flux.transform(f -> toUpperCase.apply(toUpper, f));

        toUpper.set(true);
        transform.subscribe(d -> System.out.println("transform - Subscriber 1 :" + d));

        toUpper.set(false);
        transform.subscribe(d -> System.out.println("transform - Subscriber 2 : " + d));

        Thread.sleep(1000);
    }

    @Test
    public void compose() throws InterruptedException {
        AtomicBoolean toUpper = new AtomicBoolean();

        Flux<String> compose = flux.compose(f -> toUpperCase.apply(toUpper, f));

        toUpper.set(true);
        compose.subscribe(d -> System.out.println("compose - Subscriber 1 :" + d));

        toUpper.set(false);
        compose.subscribe(d -> System.out.println("compose - Subscriber 2 : " + d));

        Thread.sleep(1000);
    }
}
