package it.figure.api.websocket.client

import it.figure.api.websocket.MoveData

class MoveAction(
        val data: MoveData
) {
    val action = "move"
}
