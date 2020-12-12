package games.figureit.engine.first.gamecontrol

import games.figureit.engine.first.GameControl
import games.figureit.engine.first.gamecontrol.playergenerator.PlayerGeneratorImpl
import games.figureit.engine.first.gamecontrol.state.GameControlStopped
import games.figureit.engine.model.Move
import games.figureit.engine.model.Player
import games.figureit.engine.model.Size

class GameControlSynchronized(
    positionGenerator: PositionGenerator,
    mapSide: Int
): GameControl {

    private var currentState: GameControlState = GameControlStopped(
        positionGenerator = positionGenerator,
        playerGenerator = PlayerGeneratorImpl(),
        field = Field(mapSide, mapSide)
    )

    @Synchronized
    override fun move(playerId: Long, move: Move) {
        currentState.move(playerId, move)
    }

    @Synchronized
    override fun addPlayer(): Player {
        return currentState.addPlayer()
    }

    @Synchronized
    override fun getMapSize(): Size {
        return currentState.getMapSize()
    }

    @Synchronized
    override fun removePlayer(id: Long) {
        return currentState.removePlayer(id)
    }

    @Synchronized
    override fun getActivePlayers(): Collection<Player> {
        return currentState.getActivePlayers()
    }

    @Synchronized
    override fun getPendingPlayers(): Collection<Player> {
        return currentState.getPendingPlayers()
    }

    @Synchronized
    override fun stopTheWorld() {
        this.currentState = currentState.stopTheWorld()
    }

    @Synchronized
    override fun startTheWorld() {
        this.currentState = currentState.startTheWorld()
    }

}
