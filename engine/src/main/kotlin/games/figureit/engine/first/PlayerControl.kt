package games.figureit.engine.first

import games.figureit.engine.model.Player

interface PlayerControl : PlayerListStore {
    fun addPlayer(): Player
    fun removePlayer(id: Long)
}
