package games.figureit.engine.model

data class Player(
    val id: Long,
    var positionState: PositionState = PositionState.INACTIVE,
    var position: Position = Position(0, 0),
    var score: Int = 0
)
