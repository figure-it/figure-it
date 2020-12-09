package games.figureit.engine.first

import games.figureit.engine.model.Player

interface PlayerListStore {
    fun getAllPlayers(): Collection<Player>
}
