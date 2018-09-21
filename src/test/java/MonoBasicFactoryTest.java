import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by mtumilowicz on 2018-09-01.
 */
public class MonoBasicFactoryTest {

    @Test
    public void empty() {
//        given
        Mono<String> emptyMono = Mono.empty();

//        expect
        StepVerifier.create(emptyMono)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void never() {
//        given
        Mono<String> neverMono = Mono.never();
        
//        expect
        StepVerifier.create(neverMono)
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .thenCancel()
                .verify();
    }

    @Test(expected = NullPointerException.class)
    public void just_null() {
        Mono.just(null);
    }

    @Test
    public void just_notNull() {
//        given
        Mono<String> justMono = Mono.just("bar");
        
//        when
        StepVerifier.create(justMono)
                .expectSubscription()
                .expectNext("bar")
                .verifyComplete();
    }

    @Test
    public void justOrEmpty_null() {
        assertThat(Mono.justOrEmpty(null), is(Mono.empty()));
    }

    @Test
    public void justOrEmpty_optional_empty() {
        assertThat(Mono.justOrEmpty(Optional.empty()), is(Mono.empty()));
    }

    @Test
    public void justOrEmpty_optional_notNull() {
//        given
        Mono<String> justOrEmptyMono = Mono.justOrEmpty(Optional.of("bar"));
        
//        when
        StepVerifier.create(justOrEmptyMono)
                .expectSubscription()
                .expectNext("bar")
                .verifyComplete();
    }

    @Test
    public void error() {
//        given
        Mono<Object> errorMono = Mono.error(new IllegalStateException());
        
//        expect
        StepVerifier.create(errorMono)
                .expectError(IllegalStateException.class)
                .verify();
    }
    
    @Test
    public void first() {
//        given
        Mono<String> firstMono = Mono.first(Mono.just("slower").delayElement(Duration.ofSeconds(1)), Mono.just("faster"));
        
//        expect
        StepVerifier.create(firstMono)
                .expectSubscription()
                .expectNext("faster")
                .verifyComplete();
    }
}