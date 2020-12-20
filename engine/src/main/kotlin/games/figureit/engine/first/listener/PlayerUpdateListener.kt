package games.figureit.engine.first.listener

import games.figureit.engine.model.PlayerDto

interface PlayerUpdateListener {
    fun playerActivated(player: PlayerDto)
    fun playerDeactivated(playerId: Long)
}
