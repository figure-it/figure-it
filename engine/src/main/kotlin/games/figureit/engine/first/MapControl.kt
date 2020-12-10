package games.figureit.engine.first

import games.figureit.engine.model.Move
import games.figureit.engine.model.Player
import games.figureit.engine.model.Size

interface MapControl {
    fun move(playerId: Int, move: Move)
    fun getMapSize(): Size
}
