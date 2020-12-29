package games.figureit.engine.first.listener

import games.figureit.engine.model.Figure
import games.figureit.engine.model.Position

interface TaskUpdateListener {
    fun taskCompleted(figure: Figure, position: Position, players: List<Long>)
    fun taskUpdated(figure: Figure)
}
