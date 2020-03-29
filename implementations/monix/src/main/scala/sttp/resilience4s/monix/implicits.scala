package sttp.resilience4s.monix

import monix.eval.Task
import sttp.resilience4s.monad.MonadError

object implicits {
  implicit val taskMonadError: MonadError[Task] = TaskMonadError
}
