import reactor.core.publisher.Flux;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Created by mtumilowicz on 2018-09-02.
 */
class FluxBasicFunctions {
    static Flux<Integer> handle() {
        return Flux.range(1, 10)
                .handle((i, sink) -> {
                    Integer transformed = transform(i);
                    if (nonNull(transformed)) sink.next(i);
                });
    }
    
    static Flux<Integer> onErrorReturn() {
        return Flux.range(1, 5)
                .map(FluxBasicFunctions::doSomethingDangerous)
                .onErrorReturn(0);
    }
    
    private static int doSomethingDangerous(int i) {
        if (i == 3) {
            throw new IllegalStateException("illegal state");
        }
        return i;
    }
    
    private static Integer transform(Integer i) {
        if (isNull(i) || i % 2 == 0) {
            return null;
        }
        
        return i;
    }
}
