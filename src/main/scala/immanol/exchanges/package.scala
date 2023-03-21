package immanol

import scala.scalajs.js
import immanol.js.puppeteer._
import cats.effect.IO
import org.scalajs.dom.Element
import cats.syntax.all._

package object exchanges {
  private val formatter = java.text.NumberFormat.getIntegerInstance
  def selectorToValue(selector: js.Array[ElementHandle[Element]], convertToToman: Boolean): IO[List[String]] = {
    def formatThePrice(s: String): String = {
      formatter.format(
        s
          .trim()
          .dropRight(if (convertToToman) 1 else 0)
          .replaceAll("\\D", "")
          .toLong
      ).toString
    }

    selector.toList.traverse {
      el =>
        for {
          property  <- el.getProperty("textContent").io
          jsonValue <- property.jsonValue.io
        } yield formatThePrice(jsonValue.asInstanceOf[String])
    }
  }

  implicit class PromiseOps[T](p: js.Promise[T]) {
    def io: IO[T] = IO.fromPromise(IO(p))
  }
}
