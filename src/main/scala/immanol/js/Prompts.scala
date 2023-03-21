package immanol.js

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.Promise
import scala.scalajs.js.Thenable
import cats.effect.IO
import scala.scalajs.js.JSConverters._
import immanol.models._

object Prompts {
  @js.native
  @JSImport("prompts", JSImport.Namespace)
  object prompts extends js.Any {
    def apply[T](x: js.Object): Promise[T] = js.native
  }

  def ioPrompt[T](x: js.Object): IO[T] =
    IO.fromPromise(IO(prompts.apply(x)))

  def promptExchanges(): IO[Exchange] = {
    val exchangesQuestion = new js.Object {
      val `type`  = "autocomplete"
      val name    = "exchange"
      val message = "Which exchange?"
      val choices = Exchange.all.toJSArray.map { exchange =>
        new js.Object {
          val title    = exchange.name
          val value    = exchange.toString()
          val selected = exchange == Exchange.Bonbast
        }
      }
    }

    trait ExchangeAnswer extends js.Object {
      val exchange: String
    }

    ioPrompt[ExchangeAnswer](exchangesQuestion).map(a => Exchange.unsafeWithName(a.exchange))
  }

  def promptCurrency(): IO[List[Currency]] = {
    val currencyQuestion = new js.Object {
      val `type`  = "multiselect"
      val name    = "currencies"
      val message = "Which currency?"
      val choices = Currency.all.toJSArray.map { c =>
        new js.Object {
          val title    = c.toString()
          val value    = c.toString()
          val selected = c == Currency.All
        }
      }
    }

    trait CurrencyAnswer extends js.Object {
      val currencies: js.Array[String]
    }

    ioPrompt[CurrencyAnswer](currencyQuestion).map { a =>
      if (a.currencies.contains("All"))
        Currency.allWithoutAll
      else
        a.currencies
          .toList
          .filterNot(_.equals("All"))
          .map(a => Currency.unsafeWithName(a))

    }
  }
}
