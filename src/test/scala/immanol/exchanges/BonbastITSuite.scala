package immanol.exchanges

import munit.CatsEffectSuite
import immanol.exchanges.Bonbast
import immanol.models.Symbol
import immanol.js.puppeteer.Puppeteer
import cats.effect.IO

class BonbastITSuite extends CatsEffectSuite {

  test("Should be able to load all the rates from bonbast.com") {
    for {
      browser <- Puppeteer.launch().io
      page    <- browser.newPage().io
      res     <- Bonbast.get(Symbol.allActualSymbols)(page)
    } yield res.foreach { r => assert(r._2.nonEmpty) }
  }
}
