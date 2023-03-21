package immanol

import com.monovore.decline.effect.CommandIOApp
import cats.effect.IOApp
import cats.effect.IO
import immanol.exchanges.Bonbast
import cats.effect.ExitCode
import com.monovore.decline.Opts
import models._
import cats.syntax.all._
import immanol.exchanges.ExchangeService
import scala.io.AnsiColor._
import immanol.js.Prompts
import cats.data.NonEmptyList

import immanol.utils.StdOut
object Main extends CommandIOApp(
      name = "rates",
      header = "Check the iran currency exchange rates",
      version = "0.0.1"
    ) {
  override def main = {

    val timeoutOpt = Opts.option[Int](
      "timeout",
      short = "t",
      metavar = "miliseconds",
      help = s"Defines the timeout for getting the information from each exchange. Default is 3000ms."
    ).map(IO.pure)
      .withDefault(IO.pure(3000))

    val outputOpt = Opts.option[String](
      "output",
      short = "o",
      metavar = "output type",
      help = s"Defines the type of output For example, [h, v, horizontal, vertical]. Default is Vertical"
    ).map(outputType => IO.fromEither(OutputType.withName(outputType)))
      .withDefault(IO.pure(OutputType.Vertical))

    val exchangeOpts =
      Opts
        .option[String](
          "exchange",
          short = "e",
          metavar = "name",
          help = s"The exchange to extract the rate. For example, [${Exchange.all.mkString(",")}]"
        )
        .map(exchange => IO.fromEither(Exchange.withName(exchange)))
        .withDefault(Prompts.promptExchanges)

    val currencyOpts =
      Opts
        .options[String](
          "currency",
          short = "c",
          metavar = "name",
          help = s"The currency you look for its rate. For example, [${Currency.all.mkString(",")}]"
        )
        .map { currencies =>
          currencies.toList.traverse(c => IO.fromEither(Currency.withName(c)))
        }
        .withDefault(Prompts.promptCurrency)

    val program =
      (exchangeOpts, currencyOpts, outputOpt, timeoutOpt).mapN { case (exchangeIO, currencyIO, outputIO, timeoutIO) =>
        for {
          e   <- exchangeIO
          c   <- currencyIO
          o   <- outputIO
          t   <- timeoutIO
          res <- ExchangeService.get(e, c, t)
          _   <- StdOut.print(res, o)
        } yield ExitCode.Success
      }
    program.map(_.handleErrorWith(ex => IO.println(s"${ex.getMessage()}").as(ExitCode.Error)))
  }

}
