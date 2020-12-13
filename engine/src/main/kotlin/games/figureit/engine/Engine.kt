package games.figureit.engine

import games.figureit.engine.model.Figure
import games.figureit.engine.model.Move
import games.figureit.engine.model.Player
import games.figureit.engine.model.Size

interface Engine {
    fun addPlayer(): Player
    fun removePlayer(playerId: Long)
    fun getAllPlayers(): List<Player>
    fun getCurrentFigure(): Figure
    fun getMapSize(): Size
    fun move(playerId: Long, move: Move)
    fun start()
    fun stop()
}
