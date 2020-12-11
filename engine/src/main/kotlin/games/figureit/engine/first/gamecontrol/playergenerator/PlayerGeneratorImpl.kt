package games.figureit.engine.first.gamecontrol.playergenerator

import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.model.Player
import games.figureit.engine.model.Position
import games.figureit.engine.model.PositionState

class PlayerGeneratorImpl : PlayerGenerator {
    private var playerIdCounter: Long = 1

    override fun generate(): Player {
        return Player(
            id = playerIdCounter++,
            positionState = PositionState.PENDING,
            position = Position(0, 0)
        )
    }

}
