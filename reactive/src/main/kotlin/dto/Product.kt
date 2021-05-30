package dto

import org.bson.Document

data class Product(private val id: Int, private val name: String, private val priceDollars: Double) {
    constructor(doc: Document) : this(doc.getInteger("_id"), doc.getString("name"), doc.getDouble("price_dollars")) {}

    fun display(currency: Currency): String {
        return "id: $id, name: $name, price: ${currency.dollarExchangeRate() * priceDollars} ${currency.name}"
    }
}