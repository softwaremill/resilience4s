# resilience4s
[![Build Status](https://travis-ci.org/softwaremill/resilience4s.svg?branch=master)](https://travis-ci.org/softwaremill/resilience4s)

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

### cats-effect

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "cats" % "@VERSION@"
```

```scala
import sttp.resilience4s.cats.implicits._
```

### monix

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "monix" % "@VERSION@"
```

```scala
import sttp.resilience4s.monix.implicits._
```

### zio

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "zio" % "@VERSION@"
```

```scala
import sttp.resilience4s.zio.implicits._
```

## modules

All examples assume existence of following service:
```scala mdoc
import cats.effect.{ContextShift, IO, Timer}

object Service {
    def getUsersIds: IO[List[String]] = IO.pure(List("123" ,"234"))
}

```

### circuitbreaker

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "circuitbreaker" % "@VERSION@"
```

```scala mdoc
def exampleCircuitbreaker(implicit cs: ContextShift[IO], timer: Timer[IO]) = {
    import sttp.resilience4s.cats.implicits._
    import sttp.resilience4s.circuitbreaker.syntax._
    import io.github.resilience4j.circuitbreaker.CircuitBreaker
    
    val circuitBreaker = CircuitBreaker.ofDefaults("backendName")
    Service.getUsersIds
        .withCircuitBreaker(circuitBreaker)
        .unsafeRunSync()
}
```

### ratelimiter

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "ratelimiter" % "@VERSION@"
```

```scala mdoc
def exampleRateLimiter(implicit cs: ContextShift[IO], timer: Timer[IO]) = {
    import sttp.resilience4s.cats.implicits._
    import sttp.resilience4s.ratelimiter.syntax._
    import io.github.resilience4j.ratelimiter.{RateLimiterConfig, RateLimiterRegistry}
    import java.time.Duration
    
    val config = RateLimiterConfig.custom()
      .limitRefreshPeriod(Duration.ofMillis(1))
      .limitForPeriod(10)
      .timeoutDuration(Duration.ofMillis(25))
      .build()
    
    // Create registry
    val rateLimiterRegistry = RateLimiterRegistry.of(config)
    
    // Use registry
    val rateLimiter = rateLimiterRegistry
      .rateLimiter("name1")
    
    Service.getUsersIds
        .withRateLimiter(rateLimiter)
        .unsafeRunSync()
}
```

### bulkhead

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "bulkhead" % "@VERSION@"
```

```scala mdoc
def exampleBulkhead(implicit cs: ContextShift[IO], timer: Timer[IO]) = {
    import sttp.resilience4s.cats.implicits._
    import sttp.resilience4s.bulkhead.syntax._
    import io.github.resilience4j.bulkhead.{BulkheadConfig, Bulkhead}
    import java.time.Duration

    val config = BulkheadConfig.custom()
        .maxConcurrentCalls(150)
        .maxWaitDuration(Duration.ofMillis(25))
        .build()
    
    val bulkhead = Bulkhead.of("backendName", config)

    Service.getUsersIds
        .withBulkhead(bulkhead)
        .unsafeRunSync()
}
```

### retry

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "retry" % "@VERSION@"
```

```scala mdoc
def exampleRetry(implicit cs: ContextShift[IO], timer: Timer[IO]) = {
    import sttp.resilience4s.cats.implicits._
    import sttp.resilience4s.retry.syntax._
    import io.github.resilience4j.retry.Retry

    val retry = Retry.ofDefaults("backendName")

    Service.getUsersIds
        .withRetry(retry)
        .unsafeRunSync()
}
```

### timelimiter

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "timelimiter" % "@VERSION@"
```

```scala mdoc
def exampleTimeLimiter(implicit cs: ContextShift[IO], timer: Timer[IO]) = {
    import sttp.resilience4s.cats.implicits._
    import sttp.resilience4s.timelimiter.syntax._
    import io.github.resilience4j.timelimiter.{TimeLimiterConfig, TimeLimiterRegistry}
    import java.time.Duration

    val config = TimeLimiterConfig.custom()
       .cancelRunningFuture(true)
       .timeoutDuration(Duration.ofMillis(500))
       .build()
    
    // Create a TimeLimiterRegistry with a custom global configuration
    val registry = TimeLimiterRegistry.of(config)
    
    // Get or create a TimeLimiter from the registry - 
    // TimeLimiter will be backed by the default config
    val timeLimiterWithDefaultConfig = registry.timeLimiter("name1")

    Service.getUsersIds
        .withTimeLimiter(timeLimiterWithDefaultConfig)
        .unsafeRunSync()
}
```

### cache

```scala
libraryDependencies += "com.softwaremill.sttp.resilience4s" % "cache" % "@VERSION@"
```

```scala mdoc
def exampleCache(implicit cs: ContextShift[IO], timer: Timer[IO]) = {
    import sttp.resilience4s.cats.implicits._
    import sttp.resilience4s.cache.syntax._
    import io.github.resilience4j.cache.Cache
    import javax.cache.Caching

    val cacheInstance = Caching.getCache("cacheName", classOf[String], classOf[List[String]])
    val cacheContext = Cache.of(cacheInstance)

    Service.getUsersIds
        .withCache(cacheContext, "cacheKey")
        .unsafeRunSync()
}
```
