# resilience4s

This project is just a scala wrapper around [resilience4j](https://github.com/resilience4j/resilience4j) 
which is a fault tolerance library designed for java.

With resilience4s you can easily add any fault-tolerance pattern to `F` of your choice.

Current support includes:
* cats-effects
    ```scala
    libraryDependencies += "com.softwaremill.sttp.ressilience4s" % "cats" % "0.1.0-SNAPSHOT"
    ```
* monix.Task
    ```scala
    libraryDependencies += "com.softwaremill.sttp.ressilience4s" % "monix" % "0.1.0-SNAPSHOT"
    ```
* ZIO
    ```scala
    libraryDependencies += "com.softwaremill.sttp.ressilience4s" % "zio" % "0.1.0-SNAPSHOT"
    ```

Resilience4s provides several core modules which mirrors those in resilience4j:

* resilience4s-circuitbreaker: Circuit breaking
    ```scala
    libraryDependencies += "com.softwaremill.sttp.ressilience4s" % "circuitbreaker" % "0.1.0-SNAPSHOT"
    ```
* resilience4s-ratelimiter: Rate limiting
    ```scala
    libraryDependencies += "com.softwaremill.sttp.ressilience4s" % "ratelimiter" % "0.1.0-SNAPSHOT"
    ```
* resilience4s-bulkhead: Bulkheading
    ```scala
    libraryDependencies += "com.softwaremill.sttp.ressilience4s" % "bulkhead" % "0.1.0-SNAPSHOT"
    ```
* resilience4s-retry: Automatic retrying (sync and async)
    ```scala
    libraryDependencies += "com.softwaremill.sttp.ressilience4s" % "retry" % "0.1.0-SNAPSHOT"
    ```
* resilience4s-timelimiter: Timeout handling
    ```scala
    libraryDependencies += "com.softwaremill.sttp.ressilience4s" % "timelimiter" % "0.1.0-SNAPSHOT"
    ```
* resilience4s-cache: Result caching
    ```scala
    libraryDependencies += "com.softwaremill.sttp.ressilience4s" % "cache" % "0.1.0-SNAPSHOT"
    ```
## Example
```scala
import cats.effect.IO
import sttp.resilience4s.cats.implicits._
import sttp.resilience4s.circuitbreaker.syntax._
import io.github.resilience4j.circuitbreaker.CircuitBreaker

def getUsersIds: IO[List[String]] = IO.pure(List("123" ,"234"))

val circuitBreaker = CircuitBreaker.ofDefaults("backendName");
// circuitBreaker: CircuitBreaker = CircuitBreaker 'backendName';
getUsersIds
    .withCircuitBreaker(circuitBreaker)
    .unsafeRunSync()
// res0: List[String] = List("123", "234")
```
