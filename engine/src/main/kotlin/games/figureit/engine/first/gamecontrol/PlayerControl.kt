package games.figureit.engine.first.gamecontrol

import games.figureit.engine.first.PlayerListStore
import games.figureit.engine.model.Player

interface PlayerControl : PlayerListStore {
    fun addPlayer(): Player
    fun activatePlayer(id: Long)
    fun deactivatePlayer(id: Long)
    fun submitPreparations(field: Field)
    fun getActivePlayer(id: Long): Player?
}
