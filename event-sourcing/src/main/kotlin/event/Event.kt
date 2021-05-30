package event

sealed class Event(val membershipId: Int, val timeMillis: Long)

class CreateMembershipEvent(membershipId: Int, timeMillis: Long, val clientId: Int, val expiresAtMillis: Long) :
    Event(membershipId, timeMillis)

class EntranceEvent(membershipId: Int, timeMillis: Long) : Event(membershipId, timeMillis)

class ExtendMembershipEvent(membershipId: Int, timeMillis: Long, val extendOnDays: Int) :
    Event(membershipId, timeMillis)

class LeaveEvent(membershipId: Int, timeMillis: Long) : Event(membershipId, timeMillis)