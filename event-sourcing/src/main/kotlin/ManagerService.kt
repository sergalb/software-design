import dto.Membership
import event.CreateMembershipEvent
import event.Event
import event.ExtendMembershipEvent
import java.util.concurrent.TimeUnit

class ManagerService(private val eventStore: EventStore) {

    fun createMembershipForDuration(membershipId: Int, clientId: Int, durationDays: Int): Boolean {
        if (getMembershipById(membershipId) != null) {
            return false
        }
        val currentTimeMillis = System.currentTimeMillis()
        val event: Event = CreateMembershipEvent(
            membershipId, currentTimeMillis, clientId,
            currentTimeMillis + TimeUnit.DAYS.toMillis(durationDays.toLong())
        )
        eventStore.handleMembershipEvent(event)
        return true
    }

    fun createMembershipUntil(membershipId: Int, clientId: Int, timeMillis: Long): Boolean {
        if (getMembershipById(membershipId) != null) {
            return false
        }
        val event: Event = CreateMembershipEvent(
            membershipId, System.currentTimeMillis(), clientId,
            timeMillis
        )
        eventStore.handleMembershipEvent(event)
        return true
    }

    fun extendMembership(membershipId: Int, dayCount: Int) {
        val event: Event = ExtendMembershipEvent(membershipId, System.currentTimeMillis(), dayCount)
        eventStore.handleMembershipEvent(event)
    }

    fun getMembershipById(membershipId: Int): Membership? {
        val creationEvent = eventStore
            .events
            .asSequence()
            .filter { event: Event -> event is CreateMembershipEvent && event.membershipId == membershipId }
            .find { true } ?: return null

        val createMembershipEvent = creationEvent as CreateMembershipEvent
        val clientId = createMembershipEvent.clientId
        var expiredAtMillis = createMembershipEvent.expiresAtMillis
        val extendedOnDays = eventStore
            .events
            .asSequence()
            .filter { event: Event -> event is ExtendMembershipEvent && event.membershipId == membershipId }
            .map { event: Event -> (event as ExtendMembershipEvent).extendOnDays }
            .sum()
        expiredAtMillis += TimeUnit.DAYS.toMillis(extendedOnDays.toLong())
        return Membership(clientId, membershipId, expiredAtMillis)
    }
}