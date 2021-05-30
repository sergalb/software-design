import event.EntranceEvent
import event.Event
import event.LeaveEvent

class TurnstileService(
    private val eventStore: EventStore,
    private val managerService: ManagerService
) {
    fun passEntrance(membershipId: Int): Boolean {
        val event: Event = EntranceEvent(membershipId, System.currentTimeMillis())
        return passThrough(event)
    }

    fun passLeave(membershipId: Int): Boolean {
        val event: Event = LeaveEvent(membershipId, System.currentTimeMillis())
        return passThrough(event)
    }

    private fun passThrough(event: Event): Boolean {
        val membership = managerService.getMembershipById(event.membershipId)
        if (membership == null || membership.expiresAtMillis <= event.timeMillis) {
            return false
        }
        try {
            eventStore.handleTurnstileEvent(event)
        } catch (e: TurnstileActionException) {
            System.err.println(e.message)
            return false
        }
        return true
    }
}