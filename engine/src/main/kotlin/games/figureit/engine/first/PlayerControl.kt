package games.figureit.engine.first

interface PlayerControl : PlayerListStore {
    fun addPlayer()
    fun removePlayer(id: Int)
}
