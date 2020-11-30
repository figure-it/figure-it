package games.figureit.engine

interface Game {
    fun getAllPlayers(): List<Player>
    fun getCurrentFigure(): Figure
    fun getMapSize(): Size
    fun move(player: Player, move: Move)
    fun start()
    fun stop()
}