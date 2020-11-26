package games.figureit.api.websocket

import it.figureit.api.Size

data class FigureData(
        val timeout: Int,
        val size: Size,
        val image: Collection<String>,
        val points: Int
)
