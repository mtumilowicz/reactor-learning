import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

/**
 * Created by mtumilowicz on 2018-09-03.
 */
public class FluxGroupByTest {
    @Test
    public void groupBy_withoutFlat() throws InterruptedException {
        Flux<GroupedFlux<Integer, Integer>> groupedFluxFlux = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
                .groupBy(i -> i % 3);
        
        groupedFluxFlux.subscribe(x -> x.subscribe(y -> System.out.println("key = " + x.key() + ", value: " + y)));
        
        Thread.sleep(1000);
    }

    @Test
    public void groupBy_withFlat() throws InterruptedException {
        Flux<GroupedFlux<Integer, Integer>> groupedFluxFlux = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
                .groupBy(i -> i % 3);
        
        groupedFluxFlux
                .concatMap(g -> g.defaultIfEmpty(-1) //if empty groups, show them
                        .map(String::valueOf) //map to string
                        .startWith("key = " + g.key()))
                .subscribe(System.out::println);

        Thread.sleep(1000);
    }
}
