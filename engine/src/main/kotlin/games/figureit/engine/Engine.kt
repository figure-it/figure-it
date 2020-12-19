package games.figureit.engine

import games.figureit.engine.first.PlayerUpdateListener
import games.figureit.engine.first.TaskUpdateListener
import games.figureit.engine.first.WorldStateListener
import games.figureit.engine.model.Figure
import games.figureit.engine.model.Move
import games.figureit.engine.model.PlayerDto
import games.figureit.engine.model.Size

interface Engine {
    fun addPlayer(): PlayerDto
    fun activatePlayer(playerId: Long)
    fun deactivatePlayer(playerId: Long)
    fun getOnlinePlayers(): List<PlayerDto>
    fun getAllPlayers(): List<PlayerDto>
    fun getCurrentFigure(): Figure
    fun getMapSize(): Size
    fun move(playerId: Long, move: Move)
    fun start()
    fun stop()
}
