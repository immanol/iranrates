package immanol.exchanges

import immanol.models._
import cats.effect.IO
import cats.syntax.all._
import immanol.js.puppeteer._
import immanol.exchanges.Bonbast

import immanol.exchanges.Tgju
import java.util.Currency

trait ExchangeService {
  def get(symbols: List[Symbol])(page: Page): IO[List[(Symbol, Option[String])]]
}

object ExchangeService {

  def get(
      exchange: Exchange,
      symbols: List[Symbol],
      timeout: Int
  ): IO[Map[Exchange, List[(Symbol, Option[String])]]] =
    for {
      browser <- Puppeteer.launch().io
      s = Symbol.putify(symbols)
      result <- fetch(exchange, s, timeout)(browser)
      _      <- browser.close().io
    } yield result

  private def fetch(
      exchange: Exchange,
      currencies: List[Symbol],
      timeout: Int
  )(browser: Browser): IO[Map[Exchange, List[(Symbol, Option[String])]]] = {

    def fetchFromOne(
        exchangeSrv: ExchangeService,
        exch: Exchange
    ): IO[Map[Exchange, List[(Symbol, Option[String])]]] =
      for {
        page <- browser.newPage().io
        _ = page.setDefaultTimeout(timeout)
        res <- exchangeSrv.get(currencies)(page).map(r => Map(exch -> r))
      } yield res

    if (exchange == Exchange.Bonbast)
      fetchFromOne(Bonbast, exchange)
    else if (exchange == Exchange.Tgju)
      fetchFromOne(Tgju, exchange)
    else {
      val bonbastIO: IO[Map[Exchange, List[(Symbol, Option[String])]]] =
        fetchFromOne(Bonbast, Exchange.Bonbast).recoverWith { case e =>
          IO(Map.empty[Exchange, List[(Symbol, Option[String])]])
        }

      val tgIO: IO[Map[Exchange, List[(Symbol, Option[String])]]] =
        fetchFromOne(Tgju, Exchange.Tgju).recoverWith { case e =>
          IO(Map.empty[Exchange, List[(Symbol, Option[String])]])
        }

      (bonbastIO, tgIO).parMapN { case (bon, tg) => bon ++ tg }
    }
  }
}
