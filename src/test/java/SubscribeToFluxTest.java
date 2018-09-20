import org.junit.Test;
import reactor.core.publisher.Flux;

/**
 * Created by mtumilowicz on 2018-09-02.
 */
public class SubscribeToFluxTest {
    @Test
    public void subscribe_consumer() throws InterruptedException {
        Flux.range(1, 5)
                .subscribe(System.out::println);
        
        Thread.sleep(100);
    }

    @Test
    public void subscribe_baseSubscriber() throws InterruptedException {
        Flux.range(1, 5)
                .subscribe(new BaseSubscriberRequestOne());

        Thread.sleep(100);
    }

    @Test
    public void subscribe_errorConsumer_noError() throws InterruptedException {
        Flux.range(1, 5)
                .subscribe(System.out::println, x -> System.out.println("error occured: " + x));

        Thread.sleep(100);
    }

    @Test
    public void subscribe_errorConsumer_error() throws InterruptedException {
        Flux.range(1, 5)
                .map(i -> {
                    if (i < 3) {
                        return  i;
                    }
                    
                    throw new IllegalStateException("illegal state!");
                })
                .subscribe(System.out::println, x -> System.out.println("error occured: " + x));

        Thread.sleep(100);
    }

    @Test
    public void subscribe_completeConsumer() throws InterruptedException {
        Flux.range(1, 5)
                .subscribe(System.out::println, 
                        x -> System.out.println("error occured: " + x),
                        () -> System.out.println("completed!"));

        Thread.sleep(100);
    }

    @Test
    public void subscribe_subscriptionConsumer() throws InterruptedException {
        Flux.range(1, 5)
                .subscribe(System.out::println,
                        x -> System.out.println("error occured: " + x),
                        () -> System.out.println("completed!"),
                        x -> x.request(10));

        Thread.sleep(100);
    }
}
