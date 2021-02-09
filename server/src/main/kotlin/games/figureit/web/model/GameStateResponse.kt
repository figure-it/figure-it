package games.figureit.web.model

import games.figureit.api.Figure
import games.figureit.api.Size

data class GameStateResponse(
    val mapSize: Size,
    val figure: Figure,
    val players: List<PlayerResponse>
)
