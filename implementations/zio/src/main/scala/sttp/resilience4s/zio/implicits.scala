package sttp.resilience4s.zio

import sttp.resilience4s.monad.MonadError
import zio.Task

object implicits {
  implicit val zioMonadError: MonadError[Task] = TaskMonadError
}
