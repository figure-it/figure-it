package games.figureit.api.websocket.server

import games.figureit.api.Pixel

data class TaskCompletedData(
    val image: Collection<String>,
    val position: Pixel,
    val winners: Collection<Long>
)
