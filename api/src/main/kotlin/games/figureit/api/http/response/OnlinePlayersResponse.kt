package games.figureit.api.http.response

import it.figureit.api.Player

data class OnlinePlayersResponse(
        val players: Collection<Player>
)
