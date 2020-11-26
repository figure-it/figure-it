package it.figure.api.websocket.server

import it.figure.api.websocket.DashboardData
import it.figure.api.websocket.UpdatesData

class DashboardAction(
        val data: DashboardData
) {
    val action = "dashboard"
}
