package games.figureit.api.websocket.server

import games.figureit.api.websocket.FigureData

data class TaskUpdatedData(
    val data: FigureData
)
