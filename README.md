# reactor-learning

_Reference_: http://projectreactor.io/docs/core/release/reference/  
_Reference_: https://www.youtube.com/watch?v=Cj4foJzPF80
_Reference_: https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html
_Reference_: https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Mono.html

# preface
**Reactive programming** is a declarative programming paradigm concerned 
with data streams and the propagation of change.

**Reactor** is a fully non-blocking reactive programming foundation for the JVM, 
with efficient demand management (in the form of managing "backpressure"). 
It integrates directly with the Java 8 functional APIs, notably `CompletableFuture`, 
`Stream`, and `Duration`. It offers composable asynchronous sequence APIs `Flux` 
(for [N] elements) and `Mono` (for [0|1] elements), extensively implementing the 
Reactive Extensions specification.

# difference to imperative approach
* imperative
    ```
    b = 1
    c = 2
    a = b + c
    b = 2
    print a; // 3
    ```
* reactive
    ```
    b = 1
    c = 2
    a = b + c
    b = 2
    print a; // 4
    ```
    
**So shortly - reactive programming is all about datatypes that represent 
a value 'over time'.**

# cold vs hot streams
* **Cold** streams are lazy. They don’t do anything until someone starts 
observing them (subscribe in RX). They only start running when they are 
consumed. Cold streams are used to represent asynchronous actions, for 
example, that it won’t be executed until someone is interested in the 
result. Another example would be a file download. It won’t start pulling 
the bytes if no one is going to do something with the data. The data 
produced by a cold stream is not shared among subscribers and when you 
subscribe you get all the items.

* **Hot** streams are active before the subscription like a stock ticker, or 
data sent by a sensor or a user. The data is independent of an individual 
subscriber.  When an observer subscribes to a hot observable, it will get 
all values in the stream that are emitted after it subscribes. The values 
are shared among all subscribers. For example, even if no one has subscribed 
to a thermometer, it measures and publishes the current temperature. When 
a subscriber registers to the stream, it automatically receives the next 
measure.

* Why it’s so important to understand whether your streams are hot or cold? 
Because it changes how your code consumes the conveyed items. If you are 
not subscribed to a hot observable, you won’t receive the data, and this 
data is lost.

# Reactor API
* **Mono**
    * static
        * `Mono<T> error(Throwable error)` - Create a Mono that terminates 
        with the specified error immediately after being subscribed to. 
        * `Mono<T> just(T data)` - Create a new Mono that emits the 
        specified item, which is captured at instantiation time. 
        * `Mono<T> justOrEmpty(@Nullable Optional<? extends T> data)`,
         `Mono<T> justOrEmpty(@Nullable T data)` - Create a new Mono that 
         emits the specified item if non null / `Optional.isPresent()`
         otherwise only emits onComplete.
        * `Mono<Long> delay(Duration duration)` - Create a Mono which delays 
        an onNext signal by a given duration on a default Scheduler and completes.
        * `Mono<T> empty()` - Create a Mono that completes without emitting any item. 
        * `Mono<T> first(Mono<? extends T>... monos)` - Pick the first Mono to 
        emit any signal (value, empty completion or error) and replay that signal, 
        effectively behaving like the fastest of these competing sources. 
        * `Mono<T> never()` - Create a Mono that will never signal any data, 
        error or completion signal.
        * `Mono<Tuple2<T1, T2>> zip(Mono<? extends T1> p1, Mono<? extends T2> p2)` -
        Merge given monos into a new Mono that will be fulfilled when all of the given 
        Monos have produced an item, aggregating their values into a Tuple2.
    * instance
        * `Mono<T> delayElement(Duration delay)` - Delay this Mono element 
        (Subscriber.onNext(T) signal) by a given duration.
        * `Disposable subscribe(Consumer<? super T> consumer)` -
         Subscribe a Consumer to this Mono that will consume all the sequence.
        * `Mono<R> map(Function<? super T, ? extends R> mapper)` - 
        Transform the item emitted by this Mono by applying a synchronous function to it.
        * `T block()` - 
        Subscribe to this Mono and block indefinitely until a next signal is received.
        * `Mono<V> compose(Function<? super Mono<T>, ? extends Publisher<V>> transformer)` -
        Defer the given transformation to this Mono in order to generate a target Mono type.
        * `Flux<T> concatWith(Publisher<? extends T> other)` -
        Concatenate emissions of this Mono with the provided Publisher (no interleave).
        * `Mono<T> defaultIfEmpty(T defaultV)` -
        Provide a default single value if this mono is completed without any data
        * `Mono<Tuple2<Long,T>> elapsed()` -
        Map this Mono into Tuple2<Long, T> of timemillis and source data. 
        The timemillis corresponds to the elapsed time between the subscribe 
        and the first next signal, as measured by the parallel scheduler.
        * `Mono<Tuple2<T, T2>> zipWith(Mono<? extends T2> other)` -
        Combine the result from this mono and another into a Tuple2.
        * `Mono<V> then(Mono<V> other)` - 
        Let this Mono complete then play another Mono.
        * `Mono<T> retry()` -
        Re-subscribes to this Mono sequence if it signals any error, indefinitely.
        * `Mono<T> doOnNext(Consumer<? super T> onNext)` -
        Add behavior triggered when the Mono emits a data successfully.
        * doOnCancel
        * doOnEach
        * doOnError
        * doOnRequest
        * doOnSubscribe
        * doOnSuccess
        * `Mono<T> log()` - 
        Observe all Reactive Streams signals and trace them using Logger support.
        * `Mono<R> handle(BiConsumer<? super T, SynchronousSink<R>> handler)` - 
        Handle the items emitted by this Mono by calling a biconsumer with the 
        output sink for each onNext. At most one SynchronousSink.next(Object) 
        call must be performed and/or 0 or 1 SynchronousSink.error(Throwable) 
        or SynchronousSink.complete().
* **Flux**
    * static
        * `Flux<Integer> range(int start, int count)` - Build a Flux that will only emit a 
        sequence of count incrementing integers, starting from start.
        * `Flux<T> from(Publisher<? extends T> source)` - Decorate the specified Publisher 
        with the Flux API.
        * `Flux<T> fromIterable(Iterable<? extends T> it)` - Create a Flux that emits the 
        items contained in the provided Iterable.
        * fromArray
        * fromStream
        * `Flux<Long> interval(Duration period)` - Create a Flux that emits long values starting with 0 
        and incrementing at specified time intervals on the global timer.
        * `<T1, T2, V> Flux<V> combineLatest(Publisher<? extends T1> source1,
          			Publisher<? extends T2> source2,
          			BiFunction<? super T1, ? super T2, ? extends V> combinator)` - 
          			Build a Flux whose data are generated by the combination of the most recently published value 
          			from each of two Publisher sources.
        * concat
        * create
        * defer
        * generate
        * merge
        * switchOnNext
    * instance
        * blockLast
        * collect
        * window
        * buffer
        * concatMap
        * take
        * skip
        * distinct
        * filter
        * reduce
        * groupBy
        * collectMap

# project description
* `BaseSubscriberRequestOneTest`
    * subscribe
    * error
    * cancel
* `ConnectableFluxTest`
    * subscribing to `ConnectableFlux` (with / without `autoConnect`)
* `FluxBasicFactoryTest`
    * empty
    * never
    * just
    * fromIterable
    * error
    * interval by 100 ms
    * interval by 1s (with virtual time simulation)
    * generate
    * combineLatest
    * concat
    * defer
    * merge
    * switchOnNext
* `FluxBasicFunctionsTest`
    * handle
    * onErrorReturn
    * onErrorResume
    * onErrorMap
    * retry
    * zip
    * buffer
    * window
* `FluxGroupByTest`
    * grouping finite streams
    * grouping finite streams with flatten
    * grouping infinite streams with flatten
* `FluxSchedulersTest`
    * default (parallel)
    * elastic
    * single
    * newSingle
    * publishOn
    * subscribeOn
* `HotVsColdTest`
    * creating hot flux by publishing on `UnicastProcessor`
* `MonoBasicFactoryTest`
    * empty
    * never
    * just
    * justOrEmpty
    * error
    * first
* `SubscribeToFluxTest`
    * subscribing with consumer
    * subscribing with `BaseSubscriber` (reactor class inheritance)
    * subscribing with consumer of error
    * subscribing with consumer of complete
    * subscribing with subscription consumer (requests for elements)
* `SubscribeToMonoTest` - same as above
* `TransformVsComposeTest`
    * transform (bounded before subscription)
    * compose (bounded just in time)
    
# remarks