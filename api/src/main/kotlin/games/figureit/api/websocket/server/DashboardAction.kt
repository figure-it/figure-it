package games.figureit.api.websocket.server

import games.figureit.api.websocket.DashboardData

class DashboardAction(
    val data: DashboardData
) {
    val action = "dashboard"
}
