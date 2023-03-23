package immanol.exchanges

import scalajs.js
import cats.effect.IO
import immanol.js.puppeteer._
import cats.syntax.all._
import immanol.models.Symbol
import org.scalajs.dom.Element

object Bonbast extends ExchangeService {
  def get(symbols: List[Symbol])(page: Page): IO[List[(Symbol, Option[String])]] = {
    for {
      _ <- page.goto("https://www.bonbast.com/").io
      selectorsOpt <- symbols.traverse {
        case c @ Symbol.GGRAM        => page.DollarDollar(s"#gol18").io.map(v => (c -> v))
        case c @ Symbol.GMISQAL      => page.DollarDollar(s"#mithqal").io.map(v => (c -> v))
        case c @ Symbol.GOUNCE       => page.DollarDollar(s"#ounce").io.map(v => (c -> v))
        case c @ Symbol.AZADI        => page.DollarDollar(s"#azadi1").io.map(v => (c -> v))
        case c @ Symbol.AZADIHALF    => page.DollarDollar(s"#azadi1_2").io.map(v => (c -> v))
        case c @ Symbol.AZADIQUARTER => page.DollarDollar(s"#azadi1_4").io.map(v => (c -> v))
        case c @ Symbol.AZADIGRAM    => page.DollarDollar(s"#azadi1g").io.map(v => (c -> v))
        case c: Symbol               => page.DollarDollar(s"#${c.toString.toLowerCase()}1").io.map(v => (c -> v))
        case other                   => IO.raiseError(new NoSuchElementException(s"The ${other} is not a valid symbol input!"))
      }
      rs <- selectorsOpt.traverse { case (c, selector) => selectorToValue(selector, false).map(v => c -> v.headOption) }

    } yield rs
  }
}
