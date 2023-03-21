package immanol.models

import cats.syntax.all._
import scala.io.AnsiColor

sealed trait Currency

object Currency {
  case object EUR extends Currency
  case object USD extends Currency

  case object GBP extends Currency
  case object CHF extends Currency
  case object CAD extends Currency
  case object AUD extends Currency
  case object SEK extends Currency
  case object NOK extends Currency
  case object RUB extends Currency
  case object DKK extends Currency
  case object TRY extends Currency
  case object All extends Currency

  def unsafeWithName(s: String): Currency =
    all
      .find(_.toString().equalsIgnoreCase(s))
      .getOrElse(throw new NoSuchElementException(s))

  def withName(s: String): Either[Throwable, Currency] =
    all
      .find(_.toString().equalsIgnoreCase(s)) match {
      case None           => new NoSuchElementException(s"Invalid input $s for currency").asLeft[Currency]
      case Some(currency) => currency.asRight[Throwable]
    }

  val all           = List(All, EUR, USD, GBP, CHF, CAD, AUD, SEK, NOK, RUB, DKK, TRY)
  val allWithoutAll = all.filterNot(_ == All)
}
