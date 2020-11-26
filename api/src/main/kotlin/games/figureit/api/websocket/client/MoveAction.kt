package games.figureit.api.websocket.client

import it.figureit.api.websocket.MoveData

class MoveAction(
        val data: MoveData
) {
    val action = "move"
}
