package immanol.js.puppeteer

import scala.scalajs.js
import scala.scalajs.js._
import scala.scalajs.js.annotation.{ JSImport, JSName }
import org.scalajs.dom.Element

@js.native
@JSImport("puppeteer", "Page")
class Page extends js.Any {
  def goto(url: String): Promise[HTTPResponse | Null] = js.native
  def setDefaultTimeout(timeout: Int): Unit           = js.native

  @JSName("$$")
  def DollarDollar[Selector](selector: Selector): js.Promise[js.Array[ElementHandle[Element]]] = js.native
}
