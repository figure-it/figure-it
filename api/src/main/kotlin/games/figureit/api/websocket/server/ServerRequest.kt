package games.figureit.api.websocket.server

data class ServerRequest(
    val action: ServerAction,
    val data: Any
)
