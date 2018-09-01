import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by mtumilowicz on 2018-09-01.
 */
public class MonoBasicFactoryTest {

    @Test
    public void empty() {
        StepVerifier.create(MonoBasicFactory.empty())
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void never() {
        StepVerifier.create(MonoBasicFactory.never())
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .thenCancel()
                .verify();
    }

    @Test(expected = NullPointerException.class)
    public void just_null() {
        MonoBasicFactory.just_null();
    }

    @Test
    public void just_notNull() {
        StepVerifier.create(MonoBasicFactory.just_notNull())
                .expectSubscription()
                .expectNext("bar")
                .verifyComplete();
    }

    @Test
    public void justOrEmpty_null() {
        assertThat(MonoBasicFactory.justOrEmpty_null(), is(Mono.empty()));
    }

    @Test
    public void justOrEmpty_optional_null() {
        assertThat(MonoBasicFactory.justOrEmpty_optional_null(), is(Mono.empty()));
    }

    @Test
    public void justOrEmpty_optional_notNull() {
        StepVerifier.create(MonoBasicFactory.justOrEmpty_optional_notNull())
                .expectSubscription()
                .expectNext("bar")
                .verifyComplete();
    }

    @Test
    public void error() {
        StepVerifier.create(MonoBasicFactory.error())
                .expectError(IllegalStateException.class)
                .verify();
    }
}