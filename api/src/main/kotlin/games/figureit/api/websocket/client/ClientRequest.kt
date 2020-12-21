package games.figureit.api.websocket.client


data class ClientRequest (
    val action: ClientAction,
    val data: Any
)
