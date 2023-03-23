package immanol.models

class SymbolSuite extends munit.FunSuite {

  test("if list of selected symbols contains all it should return all symbols without ALL") {
    val symbols     = List(Symbol.AUD, Symbol.AZADI, Symbol.All)
    val cleanedList = Symbol.putify(symbols)
    assertEquals(cleanedList, Symbol.allActualSymbols)
  }

  test("purify should only retrun the symbols related to sekeh if user select SEKEH") {
    val symbols     = List(Symbol.SEKEH)
    val cleanedList = Symbol.putify(symbols)
    assertEquals(cleanedList, Symbol.sekeh)
  }

  test("purify should retrun the symbols related to sekeh and the rest if user has selected SEKEH") {
    val symbols     = List(Symbol.SEKEH, Symbol.AUD)
    val cleanedList = Symbol.putify(symbols)
    assertEquals(cleanedList, Symbol.sekeh :+ Symbol.AUD)
  }

  test("purify should retrun the symbols related to sekeh only once if user has selected SEKEH") {
    val symbols     = List(Symbol.SEKEH, Symbol.AZADI)
    val cleanedList = Symbol.putify(symbols)
    assertEquals(cleanedList, Symbol.sekeh)
  }
}
