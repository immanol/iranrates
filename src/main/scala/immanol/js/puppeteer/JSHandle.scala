package immanol.js.puppeteer

import scala.scalajs.js
import scala.scalajs.js._
import scala.scalajs.js.annotation.{ JSImport, JSName }

@JSImport("puppeteer", "JSHandle")
@js.native
class JSHandle[T]() extends js.Any {
  def getProperty(propertyName: String): js.Promise[JSHandle[Any]] = js.native
  def jsonValue(): js.Promise[T]                                   = js.native
}
