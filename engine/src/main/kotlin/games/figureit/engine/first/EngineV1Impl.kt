package games.figureit.engine.first

import games.figureit.engine.Engine
import games.figureit.engine.model.Figure
import games.figureit.engine.model.Move
import games.figureit.engine.model.Player
import games.figureit.engine.model.Size

class EngineV1Impl(
    private val playerControl: PlayerControl,
    private val mapControl: MapControl,
    private val scoreControl: ScoreControl
) : Engine {

    override fun addPlayer(): Player {
        return playerControl.addPlayer()
    }

    override fun removePlayer(playerId: Long) {
        playerControl.removePlayer(playerId)
    }

    override fun getAllPlayers(): List<Player> {
        return playerControl.getActivePlayers() + playerControl.getPendingPlayers()
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
