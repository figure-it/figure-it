package games.figureit.api.websocket.server

data class ServerRequest(
    val action: String,
    val data: Any
)
