package games.figureit.api.websocket

import games.figureit.api.Pixel

data class PlayerPosition(
    val id: Int,
    val position: Pixel
)
