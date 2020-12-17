package games.figureit.api.websocket.client

import games.figureit.api.websocket.MoveData


class MoveAction(
        val data: MoveData
) {
    val action = "move"
}
