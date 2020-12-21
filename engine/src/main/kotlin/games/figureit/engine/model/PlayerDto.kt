package games.figureit.engine.model

class PlayerDto(player: Player) {
    val id = player.id
    val positionState = player.positionState
    val position = player.position
    val score = player.score
}
