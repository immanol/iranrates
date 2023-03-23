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

  def promptSymbols(): IO[List[Symbol]] = {
    val symbolsQuestion = new js.Object {
      val `type`  = "multiselect"
      val name    = "symbols"
      val message = "Which Symbol?"
      val choices = Symbol.all.toJSArray.map { c =>
        new js.Object {
          val title    = c.toString()
          val value    = c.toString()
          val selected = c == Symbol.All
        }
      }
    }

    trait SymbolAnswer extends js.Object {
      val symbols: js.Array[String]
    }

    ioPrompt[SymbolAnswer](symbolsQuestion).map { a =>
      if (a.symbols.contains("All"))
        Symbol.allActualSymbols
      else
        a.symbols
          .toList
          .filterNot(_.equals("All"))
          .map(a => Symbol.unsafeWithName(a))

    }
  }
}
