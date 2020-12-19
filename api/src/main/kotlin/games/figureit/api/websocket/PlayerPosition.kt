package games.figureit.api.websocket

import games.figureit.api.Pixel

data class PlayerPosition(
    val id: Long,
    val position: Pixel
)
