package games.figureit.engine.first.gamecontrol.state

import games.figureit.engine.first.gamecontrol.GameControlState
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.first.gamecontrol.PositionGenerator
import games.figureit.engine.first.gamecontrol.Field
import games.figureit.engine.model.Move
import games.figureit.engine.model.Player
import games.figureit.engine.model.Size
import java.util.HashMap
import java.util.HashSet

class GameControlRunning(
    private val positionGenerator: PositionGenerator,
    private val field: Field,
    private val playerGenerator: PlayerGenerator,
    private val players: MutableMap<Long, Player> = HashMap(),
    private val pendingAddPlayers: MutableMap<Long, Player> = HashMap(),
    private val pendingRemovePlayers: MutableSet<Long> = HashSet()
): GameControlStateAbstract(playerGenerator, players, pendingAddPlayers, pendingRemovePlayers) {
    override fun stopTheWorld(): GameControlState {
        return GameControlStopped(
            positionGenerator = positionGenerator,
            playerGenerator = playerGenerator,
            field = field,
            playersToAdd = pendingAddPlayers,
            playersToRemove = pendingRemovePlayers,
            players = players
        )
    }

    override fun startTheWorld(): GameControlState {
        return this
    }

    override fun move(playerId: Long, move: Move) {
        players[playerId] ?. let {
            move.move(field, it)
        }
    }

    override fun getMapSize(): Size {
        return field.getSize()
    }

}
