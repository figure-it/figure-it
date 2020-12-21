package games.figureit.engine.first.gamecontrol.state

import games.figureit.engine.first.gamecontrol.GameControlState
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.model.Player
import java.util.HashMap
import java.util.HashSet

abstract class GameControlStateAbstract(
    private val playerGenerator: PlayerGenerator,
    private val activePlayers: MutableMap<Long, Player> = HashMap(),
    private val pendingAddPlayers: MutableMap<Long, Player> = HashMap(),
    private val pendingRemovePlayers: MutableSet<Long> = HashSet(),
    private val allPlayers: MutableMap<Long, Player> = HashMap()
): GameControlState {
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

    override fun getActivePlayers(): Collection<Player> {
        return activePlayers.values
    }

    override fun getPendingPlayers(): Collection<Player> {
        return pendingAddPlayers.values
    }

    override fun getAllPlayers(): Collection<Player> {
        return allPlayers.values
    }
}
