package games.figureit.api.websocket.server

import games.figureit.api.websocket.UpdatesData

class UpdatesAction(
        val data: UpdatesData
) {
    val action = "updates"
}
