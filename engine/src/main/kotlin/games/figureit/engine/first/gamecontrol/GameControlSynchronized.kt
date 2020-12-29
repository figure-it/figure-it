package games.figureit.engine.first.gamecontrol

import games.figureit.engine.first.GameControl
import games.figureit.engine.first.listener.WorldStateListener
import games.figureit.engine.first.gamecontrol.state.GameControlStopped
import games.figureit.engine.first.listener.EmptyWorldStateListener
import games.figureit.engine.model.Move
import games.figureit.engine.model.Position
import games.figureit.engine.model.Size

class GameControlSynchronized(
    playerControl: PlayerControl,
    mapSide: Int,
    private val worldStateListener: WorldStateListener = EmptyWorldStateListener()
): GameControl {

    private var currentState: GameControlState = GameControlStopped(
        playerControl = playerControl,
        field = Field(mapSide, mapSide)
    )

    @Synchronized
    override fun move(playerId: Long, move: Move): Position {
        val position = currentState.move(playerId, move)
        worldStateListener.playerPositionUpdate(playerId, position)
        return position
    }

    @Synchronized
    override fun getMapSize(): Size {
        return currentState.getMapSize()
    }

    @Synchronized
    override fun stopTheWorld() {
        this.currentState = currentState.stopTheWorld()
        worldStateListener.worldStopped()
    }

    @Synchronized
    override fun startTheWorld() {
        this.currentState = currentState.startTheWorld()
        worldStateListener.worldStarted()
    }

}
