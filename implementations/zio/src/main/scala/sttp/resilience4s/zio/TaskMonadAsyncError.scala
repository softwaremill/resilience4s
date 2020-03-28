package sttp.resilience4s.zio

import sttp.resilience4s.monad.MonadError
import zio.Task

object TaskMonadError extends MonadError[Task] {
  override def unit[T](t: T): Task[T] = Task.succeed(t)

  override def map[T, T2](fa: Task[T])(f: T => T2): Task[T2] = fa.map(f)

  override def flatMap[T, T2](fa: Task[T])(f: T => Task[T2]): Task[T2] =
    fa.flatMap(f)

  override def raiseError[T](t: Throwable): Task[T] = Task.fail(t)

  override protected def handleWrappedError[T](rt: Task[T])(h: PartialFunction[Throwable, Task[T]]): Task[T] =
    rt.catchSome(h)

  override def eval[T](t: => T): Task[T] = Task(t)
}
