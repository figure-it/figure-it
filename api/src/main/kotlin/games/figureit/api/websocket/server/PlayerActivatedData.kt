package games.figureit.api.websocket.server

import games.figureit.api.Pixel

data class PlayerActivatedData(
    val playerId: Long,
    val position: Pixel,
    val score: Int
)
