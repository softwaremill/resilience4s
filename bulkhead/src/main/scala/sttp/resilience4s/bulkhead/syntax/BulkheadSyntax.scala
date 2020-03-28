package sttp.resilience4s.bulkhead.syntax

import sttp.resilience4s.bulkhead.BulkheadOps

trait BulkheadSyntax {
  implicit final def bulkheadSyntax[F[_], A](action: => F[A]): BulkheadOps[F, A] = new BulkheadOps[F, A](action)
}
