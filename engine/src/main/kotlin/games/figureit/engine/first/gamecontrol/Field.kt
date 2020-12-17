package games.figureit.engine.first.gamecontrol

import games.figureit.engine.model.Player
import games.figureit.engine.model.Position
import games.figureit.engine.model.Size

class Field(width: Int, height: Int) {
    private val field: Array<Array<Player?>> = Array(height) { arrayOfNulls(width) }

    fun freeAt(position: Position): Boolean {
        return freeAt(position.x, position.y)
    }

    fun freeAt(x: Int, y: Int): Boolean {
        return field[y][x] == null
    }

    fun playerAt(position: Position): Player? {
        return playerAt(position.x, position.y)
    }

    fun playerAt(x: Int, y: Int): Player? {
        return field[y][x]
    }

    fun set(position: Position, player: Player?) {
        set(position.x, position.y, player)
    }

    fun set(x: Int, y: Int, player: Player?) {
        field[y][x] = player
    }

    fun getSize(): Size {
        return Size(getWidth(), getHeight())
    }

    fun getWidth(): Int {
        return field[0].size
    }

    fun getHeight(): Int {
        return field.size
    }

    fun rightFor(position: Position): Position {
        return Position(minOf(position.x + 1, getWidth() - 1), position.y)
    }

    fun leftFor(position: Position): Position {
        return Position(maxOf(position.x - 1, 0), position.y)
    }

    fun upFor(position: Position): Position {
        return Position(position.x, maxOf(position.y - 1, 0))
    }

    fun downFor(position: Position): Position {
        return Position(position.x, minOf(position.y + 1, getHeight() - 1))
    }
}
