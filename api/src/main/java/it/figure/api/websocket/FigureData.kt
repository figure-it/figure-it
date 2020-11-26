package it.figure.api.websocket

import it.figure.api.Size

data class FigureData(
        val timeout: Int,
        val size: Size,
        val image: Collection<String>,
        val points: Int
)
