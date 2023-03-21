package immanol.js.puppeteer

import scala.scalajs.js
import scala.scalajs.js._
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("puppeteer", "Browser")
class Browser extends js.Any {
  def newPage(): Promise[Page] = js.native
  def close(): Promise[Unit]   = js.native
}
