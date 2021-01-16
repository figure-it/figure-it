package games.figureit.api.http.response

import games.figureit.api.Player

data class OnlinePlayersResponse(
    val players: Collection<Player>
)
