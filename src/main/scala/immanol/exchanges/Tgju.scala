package immanol.exchanges

import scalajs.js
import cats.effect.IO
import immanol.js.puppeteer._
import cats.syntax.all._
import immanol.models.Symbol
import org.scalajs.dom.Element

object Tgju extends ExchangeService {
  def get(symbols: List[Symbol])(page: Page): IO[List[(Symbol, Option[String])]] = {
    for {
      _ <- IO.fromPromise(IO(page.goto("https://www.tgju.org/")))
      selectorsOpt <- symbols.traverse {
        case c @ Symbol.GGRAM =>
          page.DollarDollar("tr[data-market-row='geram18'] td:nth-child(2)").io.map(v => (c -> v))
        case c @ Symbol.GMISQAL =>
          page.DollarDollar("tr[data-market-row='mesghal'] td:nth-child(2)").io.map(v => (c -> v))
        case c @ Symbol.GOUNCE =>
          page.DollarDollar("tr[data-market-row='ons'] td:nth-child(2)").io.map(v => (c -> v))
        case c @ Symbol.AZADI =>
          page.DollarDollar("tr[data-market-row='sekeb'] td:nth-child(2)").io.map(v => (c -> v))
        case c @ Symbol.AZADIHALF =>
          page.DollarDollar(s"""tr[data-market-row='nim'] td:nth-child(2)""").io.map(v => (c -> v))
        case c @ Symbol.AZADIQUARTER =>
          page.DollarDollar(s"""tr[data-market-row='rob'] td:nth-child(2)""").io.map(v => (c -> v))
        case c @ Symbol.AZADIGRAM =>
          page.DollarDollar(s"""tr[data-market-row='gerami'] td:nth-child(2)""").io.map(v => (c -> v))
        case Symbol.USD =>
          page
            .DollarDollar("""tr[data-market-nameslug='price_dollar_rl'] > td.market-price""")
            .io
            .map(v => (Symbol.USD -> v))
        case c: Symbol =>
          page
            .DollarDollar(
              s"""tr[data-market-nameslug='price_${c.toString().toLowerCase()}'] > td.market-price"""
            )
            .io
            .map(v => (c -> v))
        case other => IO.raiseError(new NoSuchElementException(s"The ${other} is not a valid symbol input!"))
      }
      rs <- selectorsOpt.traverse { case (c, selector) =>
        selectorToValue(selector, true).map(v => c -> v.headOption)
      }

    } yield rs
  }
}
