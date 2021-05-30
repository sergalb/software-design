import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.ReceiveTimeout
import dto.Request
import dto.Response
import scala.concurrent.duration.Duration
import java.util.*

class MasterActor(private val stubServer: StubServer) : AbstractActor() {
    private val responses = ArrayList<Response>()
    private lateinit var caller: ActorRef

    override fun createReceive(): Receive {
        return receiveBuilder()
            .match(String::class.java, this::onRequest)
            .match(Response::class.java, this::onResponse)
            .match(ReceiveTimeout::class.java, this::onTimeout)
            .build()
    }

    private fun onRequest(requestText: String) {
        caller = sender
        for (searcher in SearchSystem.values()) {
            val request = Request(searcher, requestText)
            val searcherName = searcher.name
            val childActor =
                context.actorOf(Props.create(ChildActor::class.java, stubServer), "${searcherName}_Actor")
            childActor.tell(request, self)
        }
        context.setReceiveTimeout(Duration.create("7 second"))
    }


    private fun onResponse(response: Response) {
        responses.add(response)
        if (responses.size == SearchSystem.values().size) {
            caller.tell(responses, self)
            context.stop(self())
        }
    }

    private fun onTimeout(message: ReceiveTimeout) {
        caller.tell(responses, self)
        context.stop(self())
    }
}