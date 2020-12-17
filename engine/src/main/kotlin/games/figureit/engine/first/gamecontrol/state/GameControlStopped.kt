package games.figureit.engine.first.gamecontrol.state

import games.figureit.engine.first.gamecontrol.GameControlState
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.first.gamecontrol.PositionGenerator
import games.figureit.engine.first.gamecontrol.Field
import games.figureit.engine.model.Move
import games.figureit.engine.model.Player
import games.figureit.engine.model.Position
import games.figureit.engine.model.PositionState
import games.figureit.engine.model.Size
import java.util.HashMap

class GameControlStopped(
    private val positionGenerator: PositionGenerator,
    private val playerGenerator: PlayerGenerator,
    private val field: Field,
    private val players: MutableMap<Long, Player> = HashMap(),
    private val playersToAdd: MutableMap<Long, Player> = mutableMapOf(),
    private val playersToRemove: MutableSet<Long> = mutableSetOf()
): GameControlStateAbstract(playerGenerator, players, playersToAdd, playersToRemove) {

    override fun stopTheWorld(): GameControlState {
        return this
    }

    override fun startTheWorld(): GameControlState {
        for (playerId in playersToRemove) {
            val player = players[playerId]
            player?.let {
                val position = it.position
                field.set(position, null)
                players.remove(playerId)
            }
        }
        val currentPositions = players.values.map { it.position } .toMutableList()
        for (playerId in playersToAdd.values) {
            currentPositions.add(addPlayer(playerId, currentPositions))
        }
        return GameControlRunning(positionGenerator, field, playerGenerator, players)
    }

    override fun move(playerId: Long, move: Move) {
        //do nothing
    }

    override fun getMapSize(): Size {
        return field.getSize()
    }

    private fun addPlayer(player: Player, currentPositions: Collection<Position>): Position {
        val position = positionGenerator.generate(field.getSize(), currentPositions)
        player.position = position
        player.positionState = PositionState.ON_MAP
        field.set(position, player)
        players[player.id] = player
        return position
    }

}
