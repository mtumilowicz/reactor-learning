# reactor-learning

_Reference_: http://projectreactor.io/docs/core/release/reference/  
_Reference_: https://www.youtube.com/watch?v=Cj4foJzPF80

Cold observables are lazy. They don’t do anything until someone starts 
observing them (subscribe in RX). They only start running when they are 
consumed. Cold streams are used to represent asynchronous actions, for 
example, that it won’t be executed until someone is interested in the 
result. Another example would be a file download. It won’t start pulling 
the bytes if no one is going to do something with the data. The data 
produced by a cold stream is not shared among subscribers and when you 
subscribe you get all the items.

Hot streams are active before the subscription like a stock ticker, or 
data sent by a sensor or a user. The data is independent of an individual 
subscriber.  When an observer subscribes to a hot observable, it will get 
all values in the stream that are emitted after it subscribes. The values 
are shared among all subscribers. For example, even if no one has subscribed 
to a thermometer, it measures and publishes the current temperature. When 
a subscriber registers to the stream, it automatically receives the next 
measure.

Why it’s so important to understand whether your streams are hot or cold? 
Because it changes how your code consumes the conveyed items. If you are 
not subscribed to a hot observable, you won’t receive the data, and this 
data is lost.