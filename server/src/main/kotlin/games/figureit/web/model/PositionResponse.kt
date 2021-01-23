package games.figureit.web.model

import games.figureit.engine.model.Position

class PositionResponse(position: Position) {
    val x = position.x
    val y = position.y
}
