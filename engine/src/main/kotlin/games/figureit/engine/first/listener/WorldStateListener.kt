package games.figureit.engine.first.listener

import games.figureit.engine.model.Position

interface WorldStateListener {
    fun worldStopped()
    fun worldStarted()
    fun playerPositionUpdate(playerId: Long, position: Position)
}
