import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

/**
 * Created by mtumilowicz on 2018-09-03.
 */
public class ConnectableFluxTest {
    @Test
    public void connectableFlux() throws InterruptedException {
        Flux<Integer> source = Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("subscribed to source"));
        
        ConnectableFlux<Integer> co = source.publish();

        co.subscribe(x -> System.out.println("Subscribe 1: " + x));
        co.subscribe(x -> System.out.println("Subscribe 2: " + x));

        System.out.println("done subscribing");
        Thread.sleep(500);
        System.out.println("will now connect");

        co.connect();
    }

    @Test
    public void connectableFlux_autoConnect() throws InterruptedException {
        Flux<Integer> source = Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("subscribed to source"));

        Flux<Integer> autoCo = source.publish().autoConnect(2);

        autoCo.subscribe(x -> System.out.println("Subscribe 1: " + x));
        System.out.println("subscribed first");
        Thread.sleep(500);
        System.out.println("subscribing second");
        autoCo.subscribe(x -> System.out.println("Subscribe 2: " + x));
    }
}
