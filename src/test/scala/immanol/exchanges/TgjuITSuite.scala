package immanol.exchanges

import munit.CatsEffectSuite
import immanol.exchanges.Bonbast
import immanol.models.Currency
import immanol.js.puppeteer.Puppeteer
import cats.effect.IO

class TgjuITSuite extends CatsEffectSuite {

  test("Should be able to load all the rates from Tgju.org") {
    for {
      browser <- Puppeteer.launch().io
      page    <- browser.newPage().io
      res     <- Tgju.get(Currency.allWithoutAll)(page)
    } yield res.foreach { r => assert(r._2.nonEmpty) }
  }
}
