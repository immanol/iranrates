package immanol.exchanges

import scalajs.js
import cats.effect.IO
import immanol.js.puppeteer._
import cats.syntax.all._
import immanol.models.Currency
import org.scalajs.dom.Element

object Bonbast extends ExchangeService {
  def get(currencies: List[Currency])(page: Page): IO[List[(Currency, Option[String])]] = {
    for {
      _ <- page.goto("https://www.bonbast.com/").io
      selectorsOpt <- currencies.traverse {
        case c: Currency => page.DollarDollar(s"#${c.toString.toLowerCase()}1").io.map(v => (c -> v))
        case other       => IO.raiseError(new NoSuchElementException(s"The ${other} is not a valid currency input!"))
      }
      rs <- selectorsOpt.traverse { case (c, selector) => selectorToValue(selector, false).map(v => c -> v.headOption) }

    } yield rs
  }
}
