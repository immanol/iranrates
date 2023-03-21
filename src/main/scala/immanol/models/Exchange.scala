package immanol.models

import cats.syntax.all._

sealed trait Exchange {
  def name: String
}
object Exchange {
  case object Bonbast extends Exchange {
    val name = "bonbast.com"
  }
  case object Tgju extends Exchange {
    val name = "tgju.org"
  }
  case object All extends Exchange {
    val name = "All"
  }

  def unsafeWithName(s: String): Exchange =
    all.find(_.toString().equals(s)).getOrElse(throw new NoSuchElementException(s))

  def withName(s: String): Either[Throwable, Exchange] =
    all
      .find(_.toString().equalsIgnoreCase(s)) match {
      case Some(exchange) => exchange.asRight[Throwable]
      case None           => new NoSuchElementException(s"Invalid input $s for exchange").asLeft[Exchange]
    }

  val all = List(Bonbast, Tgju, All)
}
