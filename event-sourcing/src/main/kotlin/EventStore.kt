import event.Event
import java.util.*

class EventStore(private val reportService: ReportService) {
    private val _events: MutableList<Event> = LinkedList()
    val events: List<Event>
        get() = _events.toList()

    fun handleMembershipEvent(event: Event) {
        _events.add(event)
    }

    fun handleTurnstileEvent(event: Event) {
        _events.add(event)
        reportService.handleTurnstileEvent(event)
    }
}