package main

/*
prerequisites
  type constructors: functions at the type level, e.g. List, Map, Function2
  implicit parameters, lookup in current scope, companion objects
  type class(e.g. for marshalling, comparing...),
def sort[A](l: List[A])(comparator: Comparator[A]): List[A]



trait Comparator[A] { def compare(a1: A, a2: A): Boolean }

  type classes vs OO interfaces,
   one type can have multiple instances of the same type class

definition
  monad trait with pure and flatMap, X is a monad = there is a typeclass instance for X
    that satisfies some rules
*/
trait Monad[M[_]] { // Monad is a type class for type constructors
  def pure[A](a: A): M[A] // aka return, pure wraps a value into a monad instance,
     // e.g. List.apply, Future.successful, Option.apply
  def flatMap[A, B](m: M[A])(f: A => M[B]): M[B] // aka bind, applies a function to a value
                                                 // in a monad
  //def map[A, B](m: M[A])(f: A => B) = flatMap(m)(a => pure(f(a)))
     // map can be defined in terms of flatMap and pure
}
/*
  examples
  Option
*/

object Monad {
  implicit object OptionMonad extends Monad[Option] {
    override def pure[A](a: A): Option[A] = Some(a)

    override def flatMap[A, B](m: Option[A])(f: A => Option[B]): Option[B] = m match {
      case None => None
      case Some(a) => f(a)
    }
  }


}

/*
  Either
  List
  Future
  others: IO, Reader, Writer, State, ID

laws

propspec

derived methods
  map: flatMap(x => pure(f(x))
  flatten: flatMap(identity)


syntactic sugar:
  monad ops
  for comprehensions

  val unused = bar
  return foo



  for {
    unused <- for {
    ...
    } ...
    x <- for {
    ...
    } ...
  } yield(x)

  foo

transformers

*/

object Main {

  def main(args: Array[String]): Unit = {

  }

}
