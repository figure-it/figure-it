package games.figureit.engine.model

data class Player (
        val id: Long,
        var positionState: PositionState,
        var position: Position,
        var score: Int = 0
)
