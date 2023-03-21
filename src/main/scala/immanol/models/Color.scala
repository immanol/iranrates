package immanol.models

import scala.io.AnsiColor

sealed trait Color {
  def code: String
}
object Color {
  case object CYAN extends Color {
    val code = AnsiColor.CYAN
  }
  case object GREEN extends Color {
    val code = AnsiColor.GREEN
  }
  case object MAGENTA extends Color {
    val code = AnsiColor.MAGENTA
  }
}
