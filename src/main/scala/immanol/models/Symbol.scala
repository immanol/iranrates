package immanol.models

import cats.syntax.all._
import scala.io.AnsiColor

sealed trait Symbol

object Symbol {
  case object EUR          extends Symbol
  case object USD          extends Symbol
  case object GBP          extends Symbol
  case object CHF          extends Symbol
  case object CAD          extends Symbol
  case object AUD          extends Symbol
  case object SEK          extends Symbol
  case object NOK          extends Symbol
  case object RUB          extends Symbol
  case object DKK          extends Symbol
  case object TRY          extends Symbol
  case object AZADI        extends Symbol
  case object AZADIHALF    extends Symbol
  case object AZADIQUARTER extends Symbol
  case object AZADIGRAM    extends Symbol
  case object SEKEH        extends Symbol
  case object GGRAM        extends Symbol
  case object GMISQAL      extends Symbol
  case object GOUNCE       extends Symbol
  case object GOLD         extends Symbol
  case object All          extends Symbol

  def unsafeWithName(s: String): Symbol =
    all
      .find(_.toString().equalsIgnoreCase(s))
      .getOrElse(throw new NoSuchElementException(s))

  def withName(s: String): Either[Throwable, Symbol] =
    all
      .find(_.toString().equalsIgnoreCase(s)) match {
      case None         => new NoSuchElementException(s"Invalid input $s for symbol").asLeft[Symbol]
      case Some(symbol) => symbol.asRight[Throwable]
    }

  val all =
    List(
      All,
      EUR,
      USD,
      GBP,
      CHF,
      CAD,
      AUD,
      SEK,
      NOK,
      RUB,
      DKK,
      TRY,
      AZADI,
      AZADIHALF,
      AZADIQUARTER,
      AZADIGRAM,
      SEKEH,
      GGRAM,
      GMISQAL,
      GOUNCE,
      GOLD
    )

  val sekeh = List(AZADI, AZADIGRAM, AZADIHALF, AZADIQUARTER)

  val gold = List(GGRAM, GMISQAL, GOUNCE)

  val allActualSymbols = all.filterNot(c => c == All || c == SEKEH || c == GOLD)

  def putify(symbols: List[Symbol]): List[Symbol] =
    if (symbols.contains(Symbol.All))
      Symbol.allActualSymbols
    else if (symbols.contains(Symbol.SEKEH))
      Symbol.sekeh ++ symbols.dropWhile(_ == Symbol.SEKEH).dropWhile(c => Symbol.sekeh.contains(c))
    else if (symbols.contains(Symbol.GOLD))
      Symbol.gold ++ symbols.dropWhile(_ == Symbol.GOLD).dropWhile(c => Symbol.gold.contains(c))
    else
      symbols

}
