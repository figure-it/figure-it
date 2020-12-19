package games.figureit.engine.first

import games.figureit.engine.model.Player

interface PlayerListStore {
    fun getActivePlayers(): Collection<Player>
    fun getPendingAddPlayers(): Collection<Player>
    fun getAllPlayers(): Collection<Player>
    fun getPendingRemovePlayers(): Collection<Long>
}
