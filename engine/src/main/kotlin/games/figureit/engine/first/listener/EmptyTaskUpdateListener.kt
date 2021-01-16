package games.figureit.engine.first.listener

import games.figureit.engine.model.Figure
import games.figureit.engine.model.Position

class EmptyTaskUpdateListener : TaskUpdateListener {
    override fun taskCompleted(figure: Figure, position: Position, players: List<Long>) {
        // do nothing
    }

    override fun taskUpdated(figure: Figure) {
        // do nothing
    }
}
