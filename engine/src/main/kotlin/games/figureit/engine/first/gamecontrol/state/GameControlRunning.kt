package games.figureit.engine.first.gamecontrol.state

import games.figureit.engine.first.gamecontrol.Field
import games.figureit.engine.first.gamecontrol.GameControlState
import games.figureit.engine.first.gamecontrol.PlayerControl
import games.figureit.engine.model.Move
import games.figureit.engine.model.Size

class GameControlRunning(
    private val field: Field,
    private val playerControl: PlayerControl
): GameControlState {
    override fun stopTheWorld(): GameControlState {
        return GameControlStopped(
            field = field,
            playerControl = playerControl,
        )
    }

    override fun startTheWorld(): GameControlState {
        return this
    }

    override fun move(playerId: Long, move: Move) {
        playerControl.getActivePlayer(playerId) ?. let {
            move.move(field, it)
        }
    }

    override fun getMapSize(): Size {
        return field.getSize()
    }
}
