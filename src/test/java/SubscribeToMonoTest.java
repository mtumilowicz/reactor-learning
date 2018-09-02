import org.junit.Test;
import reactor.core.publisher.Mono;

/**
 * Created by mtumilowicz on 2018-09-02.
 */
public class SubscribeToMonoTest {
    @Test
    public void subscribe_consumer() throws InterruptedException {
        Mono.just(1)
                .subscribe(System.out::println);

        Thread.sleep(100);
    }

    @Test
    public void subscribe_coreSubscriber() throws InterruptedException {
        Mono.just(1)
                .subscribe(new BaseSubscriberRequestOne());

        Thread.sleep(100);
    }

    @Test
    public void subscribe_errorConsumer_noError() throws InterruptedException {
        Mono.just(1)
                .subscribe(System.out::println, x -> System.out.println("error occured: " + x));

        Thread.sleep(100);
    }

    @Test
    public void subscribe_errorConsumer_error() throws InterruptedException {
        Mono.error(new IllegalStateException("illegal state!"))
                .subscribe(System.out::println, x -> System.out.println("error occured: " + x));

        Thread.sleep(100);
    }

    @Test
    public void subscribe_completeConsumer() throws InterruptedException {
        Mono.just(1)
                .subscribe(System.out::println,
                        x -> System.out.println("error occured: " + x),
                        () -> System.out.println("completed!"));

        Thread.sleep(100);
    }
}
