package games.figureit.api.websocket.server

import it.figureit.api.websocket.FigureData

class FigureAction(
        val data: FigureData
) {
    val action = "figure"
}
