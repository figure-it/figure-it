package games.figureit.engine.first.gamecontrol

import games.figureit.engine.first.GameControl
import games.figureit.engine.first.gamecontrol.state.GameControlStopped
import games.figureit.engine.model.Move
import games.figureit.engine.model.Size

class GameControlSynchronized(
    playerControl: PlayerControl,
    mapSide: Int
): GameControl {

    private var currentState: GameControlState = GameControlStopped(
        playerControl = playerControl,
        field = Field(mapSide, mapSide)
    )

    @Synchronized
    override fun move(playerId: Long, move: Move) {
        currentState.move(playerId, move)
    }

    @Synchronized
    override fun getMapSize(): Size {
        return currentState.getMapSize()
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
