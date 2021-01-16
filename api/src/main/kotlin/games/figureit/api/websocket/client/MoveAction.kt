package games.figureit.api.websocket.client

import games.figureit.api.websocket.MoveData
import games.figureit.api.websocket.client.ClientAction.MOVE

class MoveAction(
    val data: MoveData
) {
    val action = MOVE
}
