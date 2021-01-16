package games.figureit.engine.first.gamecontrol.state

import games.figureit.engine.exception.ActivePlayerNotFoundException
import games.figureit.engine.first.gamecontrol.Field
import games.figureit.engine.first.gamecontrol.GameControlState
import games.figureit.engine.first.gamecontrol.PlayerControl
import games.figureit.engine.model.Move
import games.figureit.engine.model.Position
import games.figureit.engine.model.Size

class GameControlStopped(private val field: Field, private val playerControl: PlayerControl) : GameControlState {

    override fun stopTheWorld(): GameControlState {
        return this
    }

    override fun startTheWorld(): GameControlState {
        playerControl.submitPreparations(field)
        return GameControlRunning(
            field = field,
            playerControl = playerControl
        )
    }

    // world stopped - players can't move
    override fun move(playerId: Long, move: Move): Position {
        playerControl.getActivePlayer(playerId) ?. let {
            return it.position
        }
        throw ActivePlayerNotFoundException(playerId)
    }

    override fun getMapSize(): Size {
        return field.getSize()
    }
}
