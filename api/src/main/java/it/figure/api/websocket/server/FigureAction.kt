package it.figure.api.websocket.server

import it.figure.api.websocket.FigureData
import it.figure.api.websocket.UpdatesData

class FigureAction(
        val data: FigureData
) {
    val action = "figure"
}
