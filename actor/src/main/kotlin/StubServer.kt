import kotlin.random.Random

enum class SearchSystem {
    GOOGLE,
    YANDEX,
    BING
}

open class StubServer {

    open fun request(searchSystem: SearchSystem, requestMessage: String): String {
        return generateRandomString()
    }

    private fun generateRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..Random.nextInt(1, 200))
            .map { allowedChars.random() }
            .joinToString("")
    }
}