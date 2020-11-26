package it.figure.api.http.response

import it.figure.api.Player

data class OnlinePlayersResponse(
        val players: Collection<Player>
)
