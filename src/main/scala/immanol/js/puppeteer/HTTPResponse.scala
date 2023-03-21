package immanol.js.puppeteer

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js._

@JSImport("puppeteer", "HTTPResponse")
@js.native
class HTTPResponse() extends js.Any {
  def text(): Promise[String] = js.native
}
