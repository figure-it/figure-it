package games.figureit.engine.first.listener

import games.figureit.engine.model.Position

class EmptyWorldStateListener : WorldStateListener {
    override fun worldStopped() {
        // do nothing
    }

    override fun worldStarted() {
        // do nothing
    }

    override fun playerPositionUpdate(playerId: Long, position: Position) {
        // do nothing
    }
}
