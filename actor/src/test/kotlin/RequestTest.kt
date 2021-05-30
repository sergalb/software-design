import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import java.util.concurrent.TimeUnit


class RequestTest {
    @Test
    fun allReceived() {
        val responses = request("", StubServer())

        assertEquals(3, responses.size)
        assertEquals(1, responses.count { it.searcher == SearchSystem.GOOGLE })
        assertEquals(1, responses.count { it.searcher == SearchSystem.YANDEX })
        assertEquals(1, responses.count { it.searcher == SearchSystem.BING })
    }

    @Test
    fun timeOutForOne() {
        val mockServer = mock(StubServer::class.java)
        doAnswer {
            val searcher = (it.getArgument<Any>(0) as SearchSystem)
            if (searcher == SearchSystem.GOOGLE) {
                try {
                    TimeUnit.SECONDS.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            " "
        }.`when`(mockServer).request(any(), any())

        val responses = request("", mockServer)

        assertEquals(2, responses.size)
        assertEquals(1, responses.count { it.searcher == SearchSystem.YANDEX })
        assertEquals(1, responses.count { it.searcher == SearchSystem.BING })
    }

    @Test
    fun timeOutForTwo() {
        val mockServer = mock(StubServer::class.java)
        doAnswer { invocation ->
            val searcher = (invocation.getArgument<Any>(0) as SearchSystem)
            if (searcher !== SearchSystem.GOOGLE) {
                try {
                    TimeUnit.SECONDS.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            ""
        }.`when`(mockServer).request(any(), any())

        val responses = request("", mockServer)

        assertEquals(1, responses.size)
        assertEquals(SearchSystem.GOOGLE, responses[0].searcher)
    }

    @Test
    fun timeOutForAll() {
        val mockServer = mock(StubServer::class.java)
        doAnswer {
            try {
                TimeUnit.SECONDS.sleep(10)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            ""
        }.`when`(mockServer).request(any(), any())

        val responses = request("", mockServer)

        assertTrue(responses.isEmpty())
    }
}

