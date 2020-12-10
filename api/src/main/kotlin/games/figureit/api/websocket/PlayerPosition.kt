package games.figureit.api.websocket

import it.figureit.api.Pixel

data class PlayerPosition(
        val id: Int,
        val position: Pixel
)
