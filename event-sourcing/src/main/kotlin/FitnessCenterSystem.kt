
class FitnessCenterSystem {

    var reportService = ReportService(LocalStorage())

    var managerService: ManagerService

    var turnstileService: TurnstileService

    init {
        val eventStore = EventStore(reportService)
        managerService = ManagerService(eventStore)
        turnstileService = TurnstileService(eventStore, managerService)
    }
}