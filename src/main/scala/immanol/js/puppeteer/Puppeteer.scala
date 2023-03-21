package immanol.js.puppeteer

import scala.scalajs.js
import scala.scalajs.js._
import scala.scalajs.js.annotation.{ JSGlobal, JSImport }

object Puppeteer {
  @js.native
  @JSImport("puppeteer", "launch")
  def launch(): Promise[Browser] = js.native
}
