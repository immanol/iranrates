package immanol.models

import cats.syntax.all._

sealed trait OutputType {
  def name: String
}
object OutputType {
  case object Horizontal extends OutputType {
    val name = "H"
  }
  case object Vertical extends OutputType {
    val name = "V"
  }
  val all = List(Horizontal, Vertical)
  def withName(s: String): Either[Throwable, OutputType] =
    all
      .find(o => o.toString().equalsIgnoreCase(s) || o.name.equalsIgnoreCase(s)) match {
      case None    => new NoSuchElementException(s"Invalid input $s for OutputType").asLeft[OutputType]
      case Some(o) => o.asRight[Throwable]
    }
}
