package games.figureit.engine.first.gamecontrol.playercontrol

import games.figureit.engine.first.gamecontrol.Field
import games.figureit.engine.first.gamecontrol.PlayerControl
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.first.gamecontrol.PositionGenerator
import games.figureit.engine.first.listener.EmptyPlayerUpdateListener
import games.figureit.engine.first.listener.PlayerUpdateListener
import games.figureit.engine.model.Player
import games.figureit.engine.model.PlayerDto
import games.figureit.engine.model.Position
import games.figureit.engine.model.PositionState
import java.util.HashMap
import java.util.HashSet

class PlayerControlImpl(
    private val playerGenerator: PlayerGenerator,
    private val positionGenerator: PositionGenerator,
    private val playerUpdateListener: PlayerUpdateListener = EmptyPlayerUpdateListener(),
    private val allPlayers: MutableMap<Long, Player> = HashMap()
): PlayerControl {

    private val activePlayers: MutableMap<Long, Player> = HashMap()
    private val pendingAddPlayers: MutableMap<Long, Player> = HashMap()
    private val pendingRemovePlayers: MutableSet<Long> = HashSet()

    override fun addPlayer(): Player {
        val player = playerGenerator.generate()
        allPlayers[player.id] = player
        return player
    }

    override fun activatePlayer(id: Long) {
        pendingRemovePlayers.remove(id)
        allPlayers[id] ?. let { pendingAddPlayers.put(id, it) }
    }

    override fun deactivatePlayer(id: Long) {
        pendingAddPlayers.remove(id)
        activePlayers[id] ?. let { pendingRemovePlayers.add(id) }
    }

    override fun submitPreparations(field: Field) {
        for (playerId in pendingRemovePlayers) {
            val player = activePlayers[playerId]
            player?.let {
                val position = it.position
                field.set(position, null)
                activePlayers.remove(playerId)
                playerUpdateListener.playerDeactivated(it.id)
            }
        }
        pendingRemovePlayers.clear()
        val currentPositions = activePlayers.values.map { it.position } .toMutableList()
        for (player in pendingAddPlayers.values) {
            currentPositions.add(positionPlayer(player, currentPositions, field))
        }
        pendingAddPlayers.clear()
    }

    override fun getActivePlayer(id: Long): Player? {
        return activePlayers[id]
    }

    override fun getActivePlayers(): Collection<Player> {
        return activePlayers.values
    }

    override fun getPendingPlayers(): Collection<Player> {
        return pendingAddPlayers.values
    }

    override fun getAllPlayers(): Collection<Player> {
        return allPlayers.values
    }

    private fun positionPlayer(player: Player, currentPositions: Collection<Position>, field: Field): Position {
        val position = positionGenerator.generate(field.getSize(), currentPositions)
        player.position = position
        player.positionState = PositionState.ACTIVE
        field.set(position, player)
        activePlayers[player.id] = player
        playerUpdateListener.playerActivated(PlayerDto(player))
        return position
    }
}
