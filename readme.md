# resilience4s

This project is just a scala wrapper around [resilience4j](https://github.com/resilience4j/resilience4j) 
which is a fault tolerance library designed for java.

With resilience4s you can easily add any fault-tolerance pattern to `F` of your choice.

Current support includes:
* [cats-effects](#cats-effect)
* [monix.Task](#monix)
* [ZIO](#zio)

Resilience4s provides several core modules which mirrors those in resilience4j:

* [resilience4s-circuitbreaker](#circuitbreaker): Circuit breaking
* [resilience4s-ratelimiter](#ratelimiter): Rate limiting
* [resilience4s-bulkhead](#bulkhead): Bulkheading
* [resilience4s-retry](#retry): Automatic retrying (sync and async)
* [resilience4s-timelimiter](#timelimiter): Timeout handling
* [resilience4s-cache](#cache): Result caching

## integrations

# cats-effect

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "cats" % "0.1.0-SNAPSHOT"
```

```scala
import sttp.resilience4s.cats.implicits._
```

# monix

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "monix" % "0.1.0-SNAPSHOT"
```

```scala
import sttp.resilience4s.monix.implicits._
```

# zio

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "zio" % "0.1.0-SNAPSHOT"
```

```scala
import sttp.resilience4s.zio.implicits._
```

## modules

### circuitbreaker

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "circuitbreaker" % "0.1.0-SNAPSHOT"
```

```scala
def exampleCircuitbreaker = {
    import cats.effect.IO
    import sttp.resilience4s.cats.implicits._
    import sttp.resilience4s.circuitbreaker.syntax._
    import io.github.resilience4j.circuitbreaker.CircuitBreaker
    
    def getUsersIds: IO[List[String]] = IO.pure(List("123" ,"234"))
    
    val circuitBreaker = CircuitBreaker.ofDefaults("backendName");
    getUsersIds
        .withCircuitBreaker(circuitBreaker)
        .unsafeRunSync()
}
```

### ratelimiter

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "ratelimiter" % "0.1.0-SNAPSHOT"
```

```scala
def exampleRateLimiter = {

import cats.effect.IO
import sttp.resilience4s.cats.implicits._
import sttp.resilience4s.ratelimiter.syntax._
import io.github.resilience4j.ratelimiter.{RateLimiterConfig, RateLimiterRegistry}
import java.time.Duration

def getUsersIds: IO[List[String]] = IO.pure(List("123" ,"234"))

val config = RateLimiterConfig.custom()
  .limitRefreshPeriod(Duration.ofMillis(1))
  .limitForPeriod(10)
  .timeoutDuration(Duration.ofMillis(25))
  .build();

// Create registry
val rateLimiterRegistry = RateLimiterRegistry.of(config);

// Use registry
val rateLimiter = rateLimiterRegistry
  .rateLimiter("name1");

getUsersIds
    .withRateLimiter(rateLimiter)
    .unsafeRunSync()
}
```
