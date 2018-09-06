import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.util.List;

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

    static Flux<Integer> onErrorResume() {
        return Flux.range(1, 5)
                .map(FluxBasicFunctions::doSomethingDangerous)
                .onErrorResume(err -> Flux.range(3, 3));
    }
    
    static Flux<Integer> onErrorMap() {
        return Flux.range(1, 5)
                .map(FluxBasicFunctions::doSomethingDangerous)
                .onErrorMap(err -> new BusinessException(err.getLocalizedMessage()));
    }
    
    static Flux<Integer> retry() {
        return Flux.range(0, 5)
                .map(FluxBasicFunctions::doSomethingDangerous)
                .retry(1);
    }
    
    static Flux<Tuple2<Integer, Integer>> zip() {
        Flux<Integer> first = Flux.range(1, 5);
        Flux<Integer> second = Flux.range(6, 5);
        
        return Flux.zip(first, second);
    }
    
    static Flux<List<Integer>> buffer() {
        return Flux.range(1, 10)
                .buffer(5);
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
