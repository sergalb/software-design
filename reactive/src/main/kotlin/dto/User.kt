package dto

import org.bson.Document

enum class Currency {
    DOLLARS, EURO, RUBLES;

    fun dollarExchangeRate(): Double {
        return when (this) {
            DOLLARS -> 1.0
            EURO -> 0.82
            RUBLES -> 73.47
        }
    }
}

data class User(val id: Int, val name: String?, val login: String?, val currency: Currency) {
    constructor(doc: Document) : this(
        doc.getInteger("_id"),
        doc.getString("name"),
        doc.getString("login"),
        Currency.valueOf(doc.getString("currency"))
    )


}