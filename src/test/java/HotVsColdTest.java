import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.Arrays;

/**
 * Created by mtumilowicz on 2018-09-03.
 */
public class HotVsColdTest {
    @Test
    public void cold() throws InterruptedException {
        Flux<String> coldFlux = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"));
        
        coldFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: "+d));

        Thread.sleep(300);
        
        coldFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: "+d));

        Thread.sleep(300);
    }
    
    @Test
    public void hot() {
        UnicastProcessor<String> source = UnicastProcessor.create();

        Flux<String> hotFlux = source.publish()
                .autoConnect()
                .map(String::toUpperCase);
        
        source.onNext("blue");

        hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: "+d));

        source.onNext("green");
        source.onNext("orange");

        hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: "+d));

        source.onNext("purple");
        source.onComplete();
    }
}
