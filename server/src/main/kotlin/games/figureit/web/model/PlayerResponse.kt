package games.figureit.web.model

import games.figureit.engine.model.PlayerDto

class PlayerResponse(
    player: PlayerDto
) {
    val id = player.id
    val position = PositionResponse(player.position)
    val name = ""
    val color = ColorResponse()
    val score = player.score
}
