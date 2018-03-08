package main

import org.scalacheck.{Arbitrary, Properties}
import org.scalacheck.Prop.forAll

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/*object ListMonadLawsSpec extends {
  val m: Monad[List] = new Monad[List] {
    override def pure[A](a: A): List[A] = List(a)
    override def flatMap[A, B](m: List[A])(f: A => List[B]): List[B] = m.flatMap(f)
  }
  val a: Arbitrary[List[String]] = implicitly[Arbitrary[List[String]]]
} with Properties("List Monad") with MonadLawsSpec {
  type M[A] = List[A]

  override def equals(m1: List[String], m2: List[String]): Boolean = m1 == m2
}*/

object FutureMonadLawsSpec extends {
  val m: Monad[Future] = new Monad[Future] {
    override def pure[A](a: A): Future[A] = Future.successful(a)
    override def flatMap[A, B](m: Future[A])(f: A => Future[B]): Future[B] = m.flatMap(f)
  }
  val a: Arbitrary[Future[String]] = Arbitrary(implicitly[Arbitrary[String]].arbitrary.map(Future.successful))
} with Properties("Future Monad") with MonadLawsSpec {
  type M[A] = Future[A]
  override def equals(m1: Future[String], m2: Future[String]): Boolean =
    Await.result(m1, Duration.Inf) == Await.result(m2, Duration.Inf)
}

object OptionMonadLawsSpec extends {
  val m: Monad[Option] = Monad.OptionMonad
  val a: Arbitrary[Option[String]] = Arbitrary(implicitly[Arbitrary[String]].arbitrary.map(Option.apply))
} with Properties("Option Monad") with MonadLawsSpec {
  type M[A] = Option[A]
  override def equals(m1: Option[String], m2: Option[String]): Boolean = m1 == m2
}

trait MonadLawsSpec {
  self: Properties =>

  type M[A]
  val m: Monad[M]
  def equals(m1: M[String], m2: M[String]): Boolean
  val a: org.scalacheck.Arbitrary[M[String]]

  import m._

  implicit def monadArbitrary = a

  property("left identify") = forAll { (f: String => M[String], s: String) =>
    equals(flatMap(pure(s))(f), f(s))  // f("kieran") == Option("kieran").flatMap(f)
  }

  property("right identify") = forAll { (m: M[String]) =>
    equals(flatMap(m)(pure), m) //Option(1).flatMap(Option.apply) == Option(1)
  }

  property("associativity") = forAll { (m: M[String], f: String => M[String], g: String => M[String]) =>
    equals(flatMap(flatMap(m)(f))(g), flatMap(m)(x => flatMap(f(x))(g)))

    // Option(5).flatMap(f).flatMap(g) == Option.flatMap(x => f(x).flatMap(g))
  }



}