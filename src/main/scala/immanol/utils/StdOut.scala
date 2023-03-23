package immanol.utils

import immanol.models._
import cats.syntax.all._
import scala.io.AnsiColor._
import cats.effect.IO
import immanol.utils.ColorGen

object StdOut {

  def verticalOutput(data: Map[Exchange, List[(Symbol, Option[String])]], gap: Int): IO[Unit] = {

    val symbolToExchangeMap: Map[Symbol, List[(Symbol, Option[String], Exchange)]] =
      (
        for ((e, v) <- data; (c, value) <- v.toMap)
          yield (c, value, e)
      )
        .toList
        .groupBy(_._1)

    val maxHeaderSize = data.keySet.map(_.name.length).max
    val maxSymbolSize = symbolToExchangeMap.keySet.map(_.toString().length()).max
    val maxValueSize  = data.values.flatMap(_.flatMap(_._2.map(_.length()))).max

    def printHeader() =
      for {
        _ <- IO.print(" " * (gap + maxSymbolSize))
        _ <- data.keys.toList.traverse { exchange =>
          val additionalGap = Math.max(maxHeaderSize, maxValueSize) - exchange.name.length()
          IO.print(s"${exchange.name}") *> IO.print(" " * (gap + additionalGap))
        }
        _ <- IO.println("")
      } yield ()

    def printRows(colorGen: ColorGen) = symbolToExchangeMap.toList.sortBy(_._1.toString()).traverse { case (c, l) =>
      for {
        _     <- IO.print(s"${c.toString()}")
        _     <- IO.print(" " * (gap + (maxSymbolSize - c.toString().length())))
        color <- colorGen.nextColor()
        _ <- l.traverse { case (_, valOpt, e) =>
          for {
            value <- IO(valOpt.getOrElse(""))
            _     <- IO.print(s"${color.code}${value}${RESET}")
            contentGap = gap + (Math.max(maxHeaderSize, maxValueSize) - value.length())
            _ <- IO.print(" " * contentGap)
          } yield ()
        }
        _ <- IO.println("")
      } yield ()
    }.void

    for {
      colorGen <- ColorGen.make()
      _        <- IO.println("")
      _        <- printHeader()
      _        <- printRows(colorGen)
      _        <- IO.println("")
    } yield ()
  }

  def horizontalOutput(data: Map[Exchange, List[(Symbol, Option[String])]], gap: Int): IO[Unit] = {
    for {
      colorGen <- ColorGen.make()
      max      <- IO(data.keys.map(_.toString().length()).max)
      _        <- IO.println("")
      _ <- data.toList.traverse { case (exchange, outputs) =>
        for {
          _     <- IO.print(s"${BOLD}${exchange.name}${RESET}")
          _     <- IO.print(" " * (gap + (max - exchange.toString().length())))
          color <- colorGen.nextColor()
          _ <- IO.print(outputs.map { case (symbol, valueOpt) =>
            s"${symbol.toString()}[${color.code}${valueOpt.getOrElse("")}${RESET}]"
          }.mkString(" " * gap))
          _ <- IO.println("")
        } yield ()
      }
    } yield ()
  }

  def print(data: Map[Exchange, List[(Symbol, Option[String])]], outputType: OutputType): IO[Unit] = {
    outputType match {
      case OutputType.Vertical   => verticalOutput(data, 10)
      case OutputType.Horizontal => horizontalOutput(data, 10)
    }
  }
}
