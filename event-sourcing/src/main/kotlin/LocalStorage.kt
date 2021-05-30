import dto.TurnstileAction
import event.EntranceEvent
import event.Event
import event.LeaveEvent
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import kotlin.math.roundToLong

class LocalStorage {
    private val _visitCountByDay: MutableMap<String, Int> = HashMap()
    val visitCountByDay: Map<String, Int>
        get() = _visitCountByDay.toMap()

    private var averageDuration: Double = 0.0

    private val lastTurnstileActionByUser: MutableMap<Int, TurnstileAction> = HashMap()

    private var visitCount: Int = 0

    private val formatter: DateFormat = SimpleDateFormat("yyyy/MM/dd")

    val averageDurationMinutes: Double
        get() = TimeUnit.MILLISECONDS.toMinutes(averageDuration.roundToLong()).toDouble()

    val averageDayVisitsCount: Double
        get() = _visitCountByDay.values.average().takeUnless { it.isNaN() } ?: 0.0

    fun handleTurnstileEvent(event: Event) {
        val dateString = formatter.format(Date(event.timeMillis))
        val clientId = event.membershipId
        when (event) {
            is EntranceEvent -> {
                val clientLastAction = lastTurnstileActionByUser[clientId]
                if (clientLastAction != null && clientLastAction.isEntranceType)
                    throw TurnstileActionException("Client $clientId double entrance error")
                val curVisitCountByDay = if (_visitCountByDay[dateString] != null) _visitCountByDay[dateString]!! else 0
                _visitCountByDay[dateString] = curVisitCountByDay + 1
                lastTurnstileActionByUser[event.membershipId] = TurnstileAction(clientId, event.timeMillis, true)
            }

            is LeaveEvent -> {
                val clientLastAction = lastTurnstileActionByUser[clientId]
                if (clientLastAction != null && !clientLastAction.isEntranceType)
                    throw TurnstileActionException("Client $clientId double leave error")
                val entranceTime = clientLastAction!!.timestamp
                val leaveTime = event.timeMillis
                averageDuration = (averageDuration * visitCount + (leaveTime - entranceTime)) / (visitCount + 1)
                visitCount += 1
                lastTurnstileActionByUser[event.membershipId] = TurnstileAction(clientId, event.timeMillis, false)
            }
        }
    }
}