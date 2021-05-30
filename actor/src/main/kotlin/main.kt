import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.Patterns
import akka.util.Timeout
import dto.Response
import scala.concurrent.Await
import java.util.*
import java.util.concurrent.TimeUnit


fun main() {
    request("cat", StubServer())
}

fun request(text: String, server: StubServer): ArrayList<Response> {
    val system = ActorSystem.create("ActorSystem")
    val master = system.actorOf(Props.create(MasterActor::class.java, server), "master")
    val timeout = Timeout(17L, TimeUnit.SECONDS)
    val responses = Await.result(Patterns.ask(master, text, timeout), timeout.duration())
    try {
        if (responses is ArrayList<*>) {

            for (response in responses) {
                if (response !is Response) {
                    throw ActorResponseException("")
                }
            }
            return responses as ArrayList<Response>
        } else throw ActorResponseException("")
    } finally {
        system.terminate()
    }
}

class ActorResponseException(message: String) : Exception("wrong type of actor response $message")
