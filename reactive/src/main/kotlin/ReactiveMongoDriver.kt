import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.Success
import dto.Currency
import dto.Product
import dto.User
import org.bson.Document
import rx.Observable

class ReactiveMongoDriver {
    private val client = MongoClients.create()

    private fun getUsersCollection() = client.getDatabase("market").getCollection("users")

    private fun getProductsCollection() = client.getDatabase("market").getCollection("products")

    fun getUsers(): Observable<User> =
        getUsersCollection().find().toObservable().map { User(it) }

    fun addUser(document: Document): Observable<Success> =
        getUsersCollection().insertOne(document)

    fun addProduct(document: Document): Observable<Success> =
        getProductsCollection().insertOne(document)

    fun getProducts(_id: Int): Observable<String> {
        return getUsers()
            .filter { user -> user.id == _id }
            .firstOrDefault(User(-1, null, null, Currency.DOLLARS))
            .flatMap { user ->
                getProductsCollection()
                    .find()
                    .toObservable()
                    .map { Product(it).display(user.currency) }
            }
    }


}