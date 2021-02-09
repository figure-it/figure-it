package games.figureit.api.websocket

import games.figureit.api.Size

data class FigureData(
    val id: Long,
    val timeout: Int,
    val size: Size,
    val image: Collection<String>,
    val points: Int
)
