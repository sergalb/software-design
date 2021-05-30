import event.Event
import kotlin.math.roundToLong

class ReportService(private val localStorage: LocalStorage) {

    fun getVisitCountByDay() = localStorage.visitCountByDay

    fun getAverageDurationMinutes() =
        localStorage.averageDurationMinutes.roundToLong()

    fun getAverageDayVisitsCount() =
        localStorage.averageDayVisitsCount.roundToLong()

    fun handleTurnstileEvent(event: Event) =
        localStorage.handleTurnstileEvent(event)

}