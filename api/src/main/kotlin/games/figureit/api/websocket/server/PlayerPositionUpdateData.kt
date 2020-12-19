package games.figureit.api.websocket.server

import games.figureit.api.websocket.PlayerPosition

data class PlayerPositionUpdateData(
    val playerPositionUpdateData: Collection<PlayerPosition>
)
