package games.figureit.engine.first.gamecontrol

import games.figureit.engine.first.GameControl
import games.figureit.engine.model.Move
import games.figureit.engine.model.Player
import games.figureit.engine.model.Size

class GameControlSynchronized(
    positionGenerator: PositionGenerator
): GameControl {
    @Synchronized
    override fun move(playerId: Int, move: Move) {
        TODO("Not yet implemented")
    }

    @Synchronized
    override fun addPlayer() {
        TODO("Not yet implemented")
    }

    @Synchronized
    override fun getMapSize(): Size {
        TODO("Not yet implemented")
    }

    @Synchronized
    override fun removePlayer(id: Int) {
        TODO("Not yet implemented")
    }

    @Synchronized
    override fun getAllPlayers(): List<Player> {
        TODO("Not yet implemented")
    }

    @Synchronized
    override fun stopTheWorld() {
        TODO("Not yet implemented")
    }

    @Synchronized
    override fun startTheWorld() {
        TODO("Not yet implemented")
    }
}
