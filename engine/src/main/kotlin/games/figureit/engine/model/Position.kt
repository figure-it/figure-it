package games.figureit.engine.model

class Position (
    val x: Int,
    val y: Int
) {
    fun add(position: Position): Position {
        return Position(this.x + position.x, this.y + position.y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "Position(x=$x, y=$y)"
    }

}
