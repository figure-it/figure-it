package games.figureit.engine.first.gamecontrol.state

import games.figureit.engine.first.gamecontrol.GameControlState
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.model.Player
import java.util.HashMap
import java.util.HashSet

abstract class GameControlStateAbstract(
    private val playerGenerator: PlayerGenerator,
    private val players: MutableMap<Long, Player> = HashMap(),
    private val pendingAddPlayers: MutableMap<Long, Player> = HashMap(),
    private val pendingRemovePlayers: MutableSet<Long> = HashSet()

): GameControlState {
    override fun addPlayer(): Player {
        val player = playerGenerator.generate()
        pendingAddPlayers[player.id] = player
        return player
    }

    override fun removePlayer(id: Long) {
        if (pendingAddPlayers[id] != null) {
            pendingAddPlayers.remove(id)
            return
        }
        if (players.containsKey(id)) {
            pendingRemovePlayers.add(id)
        }
    }

    override fun getActivePlayers(): Collection<Player> {
        return players.values
    }

    override fun getPendingPlayers(): Collection<Player> {
        return pendingAddPlayers.values
    }


}