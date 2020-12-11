package games.figureit.engine.first.gamecontrol

import games.figureit.engine.model.Position
import games.figureit.engine.model.Size

interface PositionGenerator {
    fun generate(mapSize: Size, positions: Collection<Position>): Position
}
