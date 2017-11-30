name: inverse
layout: true
class: center, middle, inverse

---

# Functional Programming
Hossam Karim .  Radwa Osama . Abdul-Rahman Salah
.footnote[[elmenus](https://www.elmenus.com)]

---

## What do we have for you today?

---

layout: false
.left-column[
  ### Functional Programming
]
.right-column[
- What is a function?
- Referential Transparency
- Laziness
- Purity 
]

---

.left-column[
  ### Functional Programming
  ### Scala
]
.right-column[
- `trait`, `class`, `object` and `type`
- Mixins
- Self-Type annotations
- `val`, `def`, `lazy` and type inference
- Generics
- Invariant, Covariant and Contravariant
- `Any`, `AnyVal`, `AnyRef` and `Nothing`
- `case class`, `sealed trait` and co-products
- `Option`, `Vector` and `Future`
- Pattern Matching
- Total and partial functions
- Generic - Polymorphic - functions
- By-name parameters
- Higher-Order functions
- Currying, Multiple parameter lists and Partially applied functions
- Composing functions
- Implicit values and parameters
]

---

.left-column[
  ### Functional Programming
  ### Scala
  ### Not Enough Functions
]
.right-column[
- `map` as a member method
- Generalizing `map` in a base trait
- Externalizing `map` 
- `Functor`, type constructors and higher-kinded types
- `Functor` is a type-class
- `Functor` instances
- `pure` 
- `flatMap`
- Implementing `map` in terms of `pure` and `flatMap`
- `Monad` is a type-class
- `for` comprehension
]

---

.left-column[
  ### Functional Programming
  ### Scala
  ### Not Enough Functions
  ### Domain Modeling, Services and Effects
]
.right-column[
- Everything is in the function (signature)
- The Tale of One City
  - Value Types
  - Entities
  - Aggregates
- IO, Kleisli, Future
  - Database actions
  - Security actions 
  - Composing actions
]

---

.left-column[
  ### Functional Programming
  ### Scala
  ### Not Enough Functions
  ### Domain Modeling, Services and Effects
  ### Streaming
]
.right-column[
- Unix Pipes
- Source - Flow - Sink
- Simple file processing, map-reduce flow
- From a database source to the browser and back again - a bi-directional streaming use case using Akka Streams and Akka HTTP WebSockets
]

---

template: inverse

## Let's get started

---

layout: false
## What is a function?
$$\huge{f\colon X\mapsto Y}$$

.center[
<img src="graphics/function.svg" width="40%">
]

---

layout: false
## What is a function?
$$\huge{f\colon X\mapsto Y}$$

.center[
<img src="graphics/total-partial-functions.svg" width="60%">
]

---

layout: false
## Referential Transparency

Compare those 2 programs:

.pull-left[
```java
int i = iterator.next();
int j = i;
```
]

.pull-right[
```java
int i = iterator.next();
int j = iterator.next();
```
]

- `iterator.next()` has a **Side-Effect**
- Each time we call `next()` on an `iterator` we might get a different value

---

layout: false
## Referential Transparency

Compare those 2 programs:

.pull-left[
```java
float a = MathLib.avg(2,3,4);
float b = a;
```
]

.pull-right[
```java
float a = MathLib.avg(2,3,4);
float b = MathLib.avg(2,3,4);
```
]

- `MathLib.avg(...)` has **No Side-Effect**
- Each time we call `avg()` with a particular set of arguments, we get the same result

---

template: inverse

## Scala Programming Language

---

layout: false
## Scala
### `trait`, `class`, `object` and `type`

```scala
trait Organization

class Company extends Organization

class University extends Organization

object IBM extends Company

object AUC extends University

object TypeAlias {

  type Name = String

  type Money = Double

}
```

---

layout: false
## Scala
### Mixins
```scala
trait Organization

trait Entity

class Company extends Organization with Entity

class University extends Organization with Entity

object IBM extends Company

object AUC extends University

```

---

layout: false
## Scala
### Self-Type Annotation

```scala
trait DatabaseAccess

trait Networking

trait Service {
  self: DatabaseAccess with Networking =>
}

trait PostgresDatabaseAccess extends DatabaseAccess

trait TcpNetworking extends Networking

object MyService 
  extends Service 
  with PostgresDatabaseAccess 
  with TcpNetworking

```

---

layout: false
## Scala
### `val`, `def` and `lazy`

```scala
// name and birthDate are immutable
class Person(
  val name: String, 
  val birthDate: LocalDate) 

val p01: Person = new Person("p01", LocalDate.of(1970, 4, 15))
val p02         = new Person("p02", LocalDate.of(1988, 10, 4))
```

```scala
def show(person: Person): String =
  s"The person's name is ${person.name}"
```

```scala
lazy val localDateOnFirstCall: LocalDate = LocalDate.now()
```

---

layout: false
## Scala
### Generics

```scala
object Generics {

  trait Combiner[A] {
    def combine(left: A, right: A): A
  }

  object StringCombiner extends Combiner[String] {
    def combine(left: String, right: String): String = 
      s"$left and $right"
  }

  object IntegerCombiner extends Combiner[Int] {
    def combine(left: Int, right: Int): Int = 
      left + right
  }

}
```

---

layout: false
## Scala
### Generics

```scala
import Generics._

@tailrec 
def combineAll[A](first: A, rest: A*)(combiner: Combiner[A]): A =
  if (rest.isEmpty)
    first
  else
    combineAll(
      combiner.combine(first, rest.head),
      rest.tail:_*
    )(combiner)


def main(args: Array[String]): Unit = {

  println(combineAll(1, 2, 3, 4)(IntegerCombiner))

  println(combineAll("1", "2", "3", "4")(StringCombiner))

}
```

---

layout: false
## Scala
### Generics

.pull-left[
```scala
class Invariant[A]

class Covariant[+A]

class Contravariant[-A]
```
]

.pull-right[
```scala
trait LivingBeing

trait Animal extends LivingBeing

class Cat extends Animal
```
]

---

layout: false
## Scala
### Generics

.pull-left[
```scala
class Invariant[A]

class Covariant[+A]

class Contravariant[-A]
```
]

.pull-right[
```scala
trait LivingBeing

trait Animal extends LivingBeing

class Cat extends Animal
```
]

And Given:

```scala
def invariant(instance: Invariant[Animal]): Unit
```

Then:
```scala
//invariant(new Invariant[LivingBeing]) // will not compile
invariant(new Invariant[Animal])        // compiles
//invariant(new Invariant[Cat])         // will not compile
```

---

layout: false
## Scala
### Generics

.pull-left[
```scala
class Invariant[A]

class Covariant[+A]

class Contravariant[-A]
```
]

.pull-right[
```scala
trait LivingBeing

trait Animal extends LivingBeing

class Cat extends Animal
```
]

And Given:

```scala
def covariant(instance: Covariant[Animal]): Unit 
```

Then:
```scala
//covariant(new Covariant[LivingBeing])  // will not compile
covariant(new Covariant[Animal])         // compiles
covariant(new Covariant[Cat])            // compiles
```

---

layout: false
## Scala
### Generics

.pull-left[
```scala
class Invariant[A]

class Covariant[+A]

class Contravariant[-A]
```
]

.pull-right[
```scala
trait LivingBeing

trait Animal extends LivingBeing

class Cat extends Animal
```
]

And Given:

```scala
def contravariant(instance: Contravariant[Animal]): Unit
```

Then:
```scala
contravariant(new Contravariant[LivingBeing]) // compiles
contravariant(new Contravariant[Animal])      // compiles
//contravariant(new Contravariant[Cat])       // will not compile
```

---

layout: false
## Scala
### `case class`

```scala
case class Name(value: String)
case class Person(name: Name, birthData: LocalDate)
```
---

layout: false
## Scala
### `case class`

```scala
case class Name(value: String)
case class Person(name: Name, birthData: LocalDate)
```

```scala
val p01 = Person(Name("p01"), LocalDate.of(1980, 11, 12))
val p02 = Person(Name("p02"), LocalDate.of(1980, 11, 12))
```
---

layout: false
## Scala
### `case class`

```scala
case class Name(value: String)
case class Person(name: Name, birthData: LocalDate)
```

```scala
val p01 = Person(Name("p01"), LocalDate.of(1980, 11, 12))
val p02 = Person(Name("p02"), LocalDate.of(1980, 11, 12))
```

```scala
p01.copy(name = Name("p02")) == p02 // true
```

```scala
p01.productIterator.mkString(", ") // Name(p01), 1980-11-12
```
---

layout: false
## Scala
### Pattern Matching

```scala
case class Name(value: String)

trait LivingBeing {
  def name: Name // notice Scala uniform access
}

case class Person(name: Name, birthData: LocalDate) extends LivingBeing
case class Animal(name: Name) extends LivingBeing
```

```scala
def classify(being: LivingBeing): String = being match {
  case Person(n, bd) => s"a person called: ${n.value} born ${bd.toString}"
  case Animal(n)     => s"an animal called: ${n.value}"
  case _             => s"a living being called: ${being.name.value}"
}
```
---

layout: false
## Scala
### Higher-Order Functions

Remember this one?

```scala
trait Combiner[A] {
  def combine(left: A, right: A): A
}

object StringCombiner extends Combiner[String] {
  def combine(left: String, right: String): String = s"$left and $right"
}

object IntegerCombiner extends Combiner[Int] {
  def combine(left: Int, right: Int): Int = left + right
}

def combineAll[A](first: A, rest: A*)(combiner: Combiner[A]): A =
  if (rest.isEmpty)
    first
  else
    combineAll(
      combiner.combine(first, rest.head),
      rest.tail:_*
    )(combiner)
```
---
layout: false
## Scala
### Higher-Order Functions

Tadaaaaaaaaaaaaaaaaaaaaaaaaaaa

```scala
def combineAll[A](first: A, rest: A*)(combine: (A, A) => A): A =
  rest.foldLeft(first)(combine)
```

What is `foldLeft`?

```scala
def foldLeft[B](z: B)(op: (B, A) => B): B
```

---

layout: false
## Scala
### Let's build a Binary Search Tree


A `Tree` in Haskell:
```haskell
data Tree a = Empty | Leaf a | Node (Tree a) a (Tree a)
```
Yeah, that's it! But,

A `Tree` in Scala:
```scala
sealed trait Tree[+A]

case class Node[+A](data: A, left: Tree[A], right: Tree[A]) extends Tree[A]

case class Leaf[+A](data: A) extends Tree[A]

case object Empty extends Tree[Nothing]
```
---

layout: false
## Scala
### Let's build a Binary Search Tree

The `insert` function:
```scala
def insert[A](tree: Tree[A], data: A)
             (ordering: Ordering[A]): Tree[A] = tree match {

  case Empty         =>
    Leaf(data)

  case Leaf(a)       =>
    if (ordering.compare(data, a) < 0)
      Node(a, Leaf(data), Empty)
    else
      Node(a, Empty, Leaf(data))

  case Node(a, l, r) =>
    if (ordering.compare(data, a) < 0)
      Node(a, insert(l, data)(ordering), r)
    else
      Node(a, l, insert(r, data)(ordering))

}
```
---

layout: false
## Scala
### Let's build a Binary Search Tree


Walk the `Tree` `inOrder`, sorting the `Tree`:
```scala
def inOrder[A](tree: Tree[A]): List[A] = tree match {
  case Empty         => List.empty[A]
  case Leaf(a)       => List(a)
  case Node(a, l, r) => inOrder(l) ++ List(a) ++ inOrder(r)
}
```

---
layout: false
## Scala
### Let's build a Binary Search Tree

The `insert` function *REVISITED*. Can you spot the changes?
```scala
def insert[A](tree: Tree[A], data: A)
             (implicit ordering: Ordering[A]): Tree[A] = tree match {

  case Empty         =>
    Leaf(data)

  case Leaf(a)       =>
    if (ordering.compare(data, a) < 0)
      Node(a, Leaf(data), Empty)
    else
      Node(a, Empty, Leaf(data))

  case Node(a, l, r) =>
    if (ordering.compare(data, a) < 0)
      Node(a, insert(l, data), r)
    else
      Node(a, l, insert(r, data))

}
```
---

template: inverse

## Talk to us

---
name: last-page
template: inverse

## That's all folks! Thank You

Code, Slides and Goodies @ https://github.com/hkarim/riseup-summit-2017
