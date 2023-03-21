package immanol.utils

import munit.CatsEffectSuite
import immanol.models.Color
import cats.syntax.all._

class ColorGenTest extends CatsEffectSuite {
  test("ColorGen should generate Color in order and rotate") {
    for {
      colorGen <- ColorGen.make()
      color1   <- colorGen.nextColor()
      color2   <- colorGen.nextColor()
      color3   <- colorGen.nextColor()
      color4   <- colorGen.nextColor()
      _ = assert(color1 == Color.CYAN)
      _ = assert(color2 == Color.GREEN)
      _ = assert(color3 == Color.MAGENTA)
      _ = assert(color4 == Color.CYAN)
    } yield ()
  }
}
