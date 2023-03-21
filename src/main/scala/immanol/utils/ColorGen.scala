package immanol.utils

import scala.io.AnsiColor
import cats.data.State
import cats.effect.kernel.Ref
import cats.effect.IO
import immanol.models.Color

trait ColorGen {
  def nextColor(): IO[Color]
}

object ColorGen {
  def make(): IO[ColorGen] = Ref.of[IO, Color](Color.CYAN).map { ref =>
    new ColorGen {
      override def nextColor(): IO[Color] =
        ref.modify {
          case c @ Color.CYAN    => (Color.GREEN, c)
          case c @ Color.GREEN   => (Color.MAGENTA, c)
          case c @ Color.MAGENTA => (Color.CYAN, c)
        }
    }
  }

}
