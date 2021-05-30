import event.EntranceEvent
import event.Event
import event.LeaveEvent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*

class FitnessCenterSystemTests {
    @Test
    fun membershipTest() {
        val fitnessCenterSystem = FitnessCenterSystem()
        assertTrue(fitnessCenterSystem.managerService.createMembershipForDuration(0, 0, 365))
        assertFalse(fitnessCenterSystem.managerService.createMembershipForDuration(0, 1, 100))
        val membership = fitnessCenterSystem.managerService.getMembershipById(0)
        assertNotNull(membership)
    }

    @Test
    fun turnstileTest() {
        val fitnessCenterSystem = FitnessCenterSystem()
        fitnessCenterSystem.managerService.createMembershipForDuration(0, 0, 365)
        assertTrue(fitnessCenterSystem.turnstileService.passEntrance(0))
        assertTrue(fitnessCenterSystem.turnstileService.passLeave(0))
        assertFalse(fitnessCenterSystem.turnstileService.passLeave(0))
        assertFalse(fitnessCenterSystem.turnstileService.passEntrance(1))
        assertTrue(fitnessCenterSystem.turnstileService.passEntrance(0))
        assertFalse(fitnessCenterSystem.turnstileService.passEntrance(0))
        fitnessCenterSystem.managerService.createMembershipUntil(
            3, 1,
            System.currentTimeMillis() - 100
        )
        assertFalse(fitnessCenterSystem.turnstileService.passEntrance(3))
        fitnessCenterSystem.managerService.extendMembership(3, 3)
        assertTrue(fitnessCenterSystem.turnstileService.passEntrance(3))
    }

    @Test
    fun reportTest() {
        val fitnessCenterSystem = FitnessCenterSystem()
        val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
        val time1 = sdf.parse("10-01-2021 10:30:45").time
        val time1_ = sdf.parse("10-01-2021 11:35:40").time
        val time2 = sdf.parse("10-01-2021 12:40:45").time
        val time2_ = sdf.parse("10-01-2021 13:25:30").time
        val time3 = sdf.parse("01-02-2021 10:40:45").time
        val time3_ = sdf.parse("01-02-2021 12:35:45").time
        val time4 = sdf.parse("01-02-2021 13:30:45").time
        val time4_ = sdf.parse("01-02-2021 14:15:45").time
        val time5 = sdf.parse("01-02-2021 20:30:45").time
        val time5_ = sdf.parse("01-02-2021 22:00:10").time
        val event1: Event = EntranceEvent(0, time1)
        val event1_: Event = LeaveEvent(0, time1_)
        val event2: Event = EntranceEvent(1, time2)
        val event2_: Event = LeaveEvent(1, time2_)
        val event3: Event = EntranceEvent(0, time3)
        val event3_: Event = LeaveEvent(0, time3_)
        val event4: Event = EntranceEvent(1, time4)
        val event4_: Event = LeaveEvent(1, time4_)
        val event5: Event = EntranceEvent(1, time5)
        val event5_: Event = LeaveEvent(1, time5_)
        fitnessCenterSystem.reportService.handleTurnstileEvent(event1)
        fitnessCenterSystem.reportService.handleTurnstileEvent(event1_)
        fitnessCenterSystem.reportService.handleTurnstileEvent(event2)
        fitnessCenterSystem.reportService.handleTurnstileEvent(event2_)
        fitnessCenterSystem.reportService.handleTurnstileEvent(event3)
        fitnessCenterSystem.reportService.handleTurnstileEvent(event3_)
        fitnessCenterSystem.reportService.handleTurnstileEvent(event4)
        fitnessCenterSystem.reportService.handleTurnstileEvent(event4_)
        fitnessCenterSystem.reportService.handleTurnstileEvent(event5)
        fitnessCenterSystem.reportService.handleTurnstileEvent(event5_)
        assertEquals(3, fitnessCenterSystem.reportService.getAverageDayVisitsCount())
        assertEquals(71, fitnessCenterSystem.reportService.getAverageDurationMinutes())
        val expected: MutableMap<String, Int> = HashMap()
        expected["2021/01/10"] = 2
        expected["2021/02/01"] = 3
        val visitCountByDay = fitnessCenterSystem.reportService.getVisitCountByDay()
        assertEquals(visitCountByDay, expected)
    }

}