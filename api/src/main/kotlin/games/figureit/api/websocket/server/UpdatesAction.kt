package games.figureit.api.websocket.server

import it.figureit.api.websocket.UpdatesData

class UpdatesAction(
        val data: UpdatesData
) {
    val action = "updates"
}
