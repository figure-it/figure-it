package games.figureit.api.websocket.server

import it.figureit.api.websocket.DashboardData

class DashboardAction(
        val data: DashboardData
) {
    val action = "dashboard"
}
