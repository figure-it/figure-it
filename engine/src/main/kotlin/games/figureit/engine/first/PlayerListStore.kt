package games.figureit.engine.first

import games.figureit.engine.model.Player

interface PlayerListStore {
    fun getActivePlayers(): Collection<Player>
    fun getPendingPlayers(): Collection<Player>
    fun getAllPlayers(): Collection<Player>
}
