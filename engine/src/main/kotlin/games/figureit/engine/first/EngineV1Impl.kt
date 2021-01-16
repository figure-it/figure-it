package games.figureit.engine.first

import games.figureit.engine.Engine
import games.figureit.engine.first.gamecontrol.PlayerControl
import games.figureit.engine.model.Figure
import games.figureit.engine.model.Move
import games.figureit.engine.model.PlayerDto
import games.figureit.engine.model.Size

class EngineV1Impl(
    private val playerControl: PlayerControl,
    private val mapControl: MapControl,
    private val scoreControl: ScoreControl
) : Engine {

    override fun addPlayer(): PlayerDto {
        return PlayerDto(playerControl.addPlayer())
    }

    override fun activatePlayer(playerId: Long) {
        playerControl.activatePlayer(playerId)
    }

    override fun deactivatePlayer(playerId: Long) {
        playerControl.deactivatePlayer(playerId)
    }

    override fun getOnlinePlayers(): List<PlayerDto> {
        return (playerControl.getActivePlayers() + playerControl.getPendingPlayers()).map { PlayerDto(it) }
    }

    override fun getAllPlayers(): List<PlayerDto> {
        return playerControl.getAllPlayers().map { PlayerDto(it) }
    }

    override fun getCurrentFigure(): Figure {
        return scoreControl.getFigure()
    }

    override fun getMapSize(): Size {
        return mapControl.getMapSize()
    }

    override fun move(playerId: Long, move: Move) {
        mapControl.move(playerId, move)
    }

    override fun start() {
        scoreControl.start()
    }

    override fun stop() {
        scoreControl.stop()
    }
}
