package immanol.exchanges

import immanol.models._
import cats.effect.IO
import cats.syntax.all._
import immanol.js.puppeteer._
import immanol.exchanges.Bonbast

import immanol.exchanges.Tgju

trait ExchangeService {
  def get(currencies: List[Currency])(page: Page): IO[List[(Currency, Option[String])]]
}

object ExchangeService {

  def get(
      exchange: Exchange,
      currencies: List[Currency],
      timeout: Int
  ): IO[Map[Exchange, List[(Currency, Option[String])]]] =
    for {
      browser <- Puppeteer.launch().io
      c = currencies.find(_ == Currency.All).fold(currencies)(_ => Currency.allWithoutAll)
      result <- fetch(exchange, c, timeout)(browser)
      _      <- browser.close().io
    } yield result

  private def fetch(
      exchange: Exchange,
      currencies: List[Currency],
      timeout: Int
  )(browser: Browser): IO[Map[Exchange, List[(Currency, Option[String])]]] = {

    def fetchFromOne(
        exchangeSrv: ExchangeService,
        exch: Exchange
    ): IO[Map[Exchange, List[(Currency, Option[String])]]] =
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
      val bonbastIO: IO[Map[Exchange, List[(Currency, Option[String])]]] =
        fetchFromOne(Bonbast, Exchange.Bonbast).recoverWith { case e =>
          IO(Map.empty[Exchange, List[(Currency, Option[String])]])
        }

      val tgIO: IO[Map[Exchange, List[(Currency, Option[String])]]] =
        fetchFromOne(Tgju, Exchange.Tgju).recoverWith { case e =>
          IO(Map.empty[Exchange, List[(Currency, Option[String])]])
        }

      (bonbastIO, tgIO).parMapN { case (bon, tg) => bon ++ tg }
    }
  }
}
