import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import org.bson.Document
import rx.Observable

class HttpServer {

    private val reactiveMongoDriver = ReactiveMongoDriver()

    fun startServer() {
        HttpServer
            .newServer(8080)
            .start { httpServerRequest, httpServerResponse ->
                val request = httpServerRequest.decodedPath.substring(1).split("/").toTypedArray()
                val result: Observable<String>? =
                    when (request[0]) {
                        "register" -> registerUser(httpServerRequest)
                        "add_product" -> addProduct(httpServerRequest)
                        "get_products" -> getProducts(httpServerRequest)
                        "get_users" -> getUsers()
                        else -> null
                    }
                return@start if (result == null) {
                    Observable.error(NullPointerException("Path is incorrect"))
                } else httpServerResponse.writeString(result)

            }
            .awaitShutdown()
    }


    private fun registerUser(httpServerRequest: HttpServerRequest<ByteBuf>): Observable<String> {
        val queryParameters = httpServerRequest.queryParameters
        val userDocument = Document("_id", queryParameters["_id"]!![0].toInt())
            .append("name", queryParameters["name"]!![0])
            .append("login", queryParameters["login"]!![0])
            .append("currency", queryParameters["currency"]!![0])
        return reactiveMongoDriver.addUser(userDocument).map { it.toString() }
    }

    private fun addProduct(httpServerRequest: HttpServerRequest<ByteBuf>): Observable<String> {
        val queryParameters = httpServerRequest.queryParameters
        val productDocument = Document("_id", queryParameters["_id"]!![0].toInt())
            .append("name", queryParameters["name"]!![0])
            .append("price_dollars", queryParameters["price_dollars"]!![0].toDouble())
        return reactiveMongoDriver.addProduct(productDocument).map { it.toString() }
    }

    private fun getProducts(httpServerRequest: HttpServerRequest<ByteBuf>): Observable<String> {
        val queryParameters = httpServerRequest.queryParameters
        val stringId = queryParameters["_id"]
        var id = -1
        if (stringId != null) {
            id = stringId[0].toInt()
        }
        return reactiveMongoDriver.getProducts(id)
    }

    private fun getUsers(): Observable<String> {
        return reactiveMongoDriver.getUsers().map { it.toString() }
    }
}