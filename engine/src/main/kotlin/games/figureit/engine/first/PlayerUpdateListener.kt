package games.figureit.engine.first

import games.figureit.engine.model.PlayerDto
import games.figureit.engine.model.Position

interface PlayerUpdateListener {
    fun playerPositionUpdate(playerId: Long, position: Position)
    fun playerActivated(player: PlayerDto)
    fun playerDeactivated(playerId: Long)
}