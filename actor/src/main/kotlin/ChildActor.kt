import akka.actor.AbstractActor
import dto.Request
import dto.Response

class ChildActor(private val stubServer: StubServer) : AbstractActor() {
    override fun createReceive(): Receive {
        return receiveBuilder()
            .match(Request::class.java, this::onReceive)
            .build()

    }

    private fun onReceive(message: Any) {
        if (message is Request) {
            val responseText = stubServer.request(message.searcher, message.text)
            val response = Response(message.searcher, responseText)
            sender.tell(response, self())
            context.stop(self())
        }
    }
}