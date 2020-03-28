# resilience4s

This project is just a scala wrapper around [resilience4j](https://github.com/resilience4j/resilience4j) 
which is a fault tolerance library designed for java.

With resilience4s you can easily add any fault-tolerance pattern to `F` of your choice.

Current support includes:
* cats-effects
* monix.Task
* ZIO

Resilience4s provides several core modules which mirrors those in resilience4j:

* resilience4s-circuitbreaker: Circuit breaking
* resilience4s-ratelimiter: Rate limiting
* resilience4s-bulkhead: Bulkheading
* resilience4s-retry: Automatic retrying (sync and async)
* resilience4s-timelimiter: Timeout handling
* resilience4s-cache: Result caching

Example
```scala mdoc

import cats.effect.IO
import sttp.resilience4s.cats.implicits._
import sttp.resilience4s.circuitbreaker.syntax._
import io.github.resilience4j.circuitbreaker.CircuitBreaker

def getUsersIds: IO[List[String]] = IO.pure(List("123" ,"234"))

val circuitBreaker = CircuitBreaker.ofDefaults("backendName");
getUsersIds
    .withCircuitBreaker(circuitBreaker)
    .unsafeRunSync()
```
