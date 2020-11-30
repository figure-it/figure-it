package games.figureit.engine

interface Game {
    fun getAllPlayers(): List<Player>
    fun getCurrentFigure(): Figure
    fun getMapSize(): Size

}