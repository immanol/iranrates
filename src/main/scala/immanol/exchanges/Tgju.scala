package immanol.exchanges

import scalajs.js
import cats.effect.IO
import immanol.js.puppeteer._
import cats.syntax.all._
import immanol.models.Currency
import org.scalajs.dom.Element

object Tgju extends ExchangeService {
  def get(currencies: List[Currency])(page: Page): IO[List[(Currency, Option[String])]] = {
    for {
      _ <- IO.fromPromise(IO(page.goto("https://www.tgju.org/")))
      selectorsOpt <- currencies.traverse {
        case Currency.USD =>
          page
            .DollarDollar("""tr[data-market-nameslug='price_dollar_rl'] > td.market-price""")
            .io
            .map(v => (Currency.USD -> v))
        case c: Currency =>
          page
            .DollarDollar(
              s"""tr[data-market-nameslug='price_${c.toString().toLowerCase()}'] > td.market-price"""
            )
            .io
            .map(v => (c -> v))
        case other => IO.raiseError(new NoSuchElementException(s"The ${other} is not a valid currency input!"))
      }
      rs <- selectorsOpt.traverse { case (c, selector) =>
        selectorToValue(selector, true).map(v => c -> v.headOption)
      }

    } yield rs
  }
}
