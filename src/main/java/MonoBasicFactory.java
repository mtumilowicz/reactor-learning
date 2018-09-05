import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.Duration;
import java.util.Optional;

/**
 * Created by mtumilowicz on 2018-09-01.
 */ 
class MonoBasicFactory {
    static Mono<String> empty() {
        return Mono.empty();
    }

    static Mono<String> never() {
        return Mono.never();
    }

    static Mono<String> just_null() {
        return Mono.just(null);
    }

    static Mono<String> just_notNull() {
        return Mono.just("bar");
    }

    static Mono<String> justOrEmpty_null() {
        String str = null;
        return Mono.justOrEmpty(str);
    }

    static Mono<String> justOrEmpty_optional_null() {
        Optional<String> empty = Optional.empty();
        return Mono.justOrEmpty(empty);
    }

    static Mono<String> justOrEmpty_optional_notNull() {
        Optional<String> empty = Optional.of("bar");
        return Mono.justOrEmpty(empty);
    }

    static Mono<String> error() {
        return Mono.error(new IllegalStateException());
    }
    
    static Mono<String> first() {
        return Mono.first(Mono.just("slower").delayElement(Duration.ofSeconds(1)), Mono.just("faster"));
    }

    public static void main(String[] args) {
        Mono<? extends Serializable> first = Mono.first(Mono.delay(Duration.ofSeconds(1)), Mono.just("faster"));
        System.out.println(first.block());
    }
}
