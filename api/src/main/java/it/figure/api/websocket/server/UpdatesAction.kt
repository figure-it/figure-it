package it.figure.api.websocket.server

import it.figure.api.websocket.UpdatesData

class UpdatesAction(
        val data: UpdatesData
) {
    val action = "updates"
}
