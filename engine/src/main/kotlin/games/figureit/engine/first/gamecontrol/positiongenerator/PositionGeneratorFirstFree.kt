package games.figureit.engine.first.gamecontrol.positiongenerator

import games.figureit.engine.first.gamecontrol.PositionGenerator
import games.figureit.engine.model.Position
import games.figureit.engine.model.Size

class PositionGeneratorFirstFree: PositionGenerator {

    override fun generate(mapSize: Size, positions: Collection<Position>): Position {
        var counter = 0
        do {
            val position = Position(counter % mapSize.width, counter / mapSize.width)
            if (!pixelIsOccupied(position, positions)) {
                return position
            }
            counter++
        } while (counter < mapSize.height * mapSize.width)

        throw RuntimeException("Not enough space")
    }

    private fun pixelIsOccupied(testPosition: Position, positions: Collection<Position>): Boolean {
        for (position in positions) {
            if (testPosition == position) {
                return true
            }
        }
        return false
    }
}
