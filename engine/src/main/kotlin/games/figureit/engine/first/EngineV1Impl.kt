package games.figureit.engine.first

import games.figureit.engine.Engine
import games.figureit.engine.model.Figure
import games.figureit.engine.model.Move
import games.figureit.engine.model.Player
import games.figureit.engine.model.Size

class EngineV1Impl(
    val playerControl: PlayerControl,
    val mapControl: MapControl,
    val scoreControl: ScoreControl
) : Engine {

    override fun addPlayer(): Player {
        TODO("Not yet implemented")
    }

    override fun getAllPlayers(): List<Player> {
        TODO("Not yet implemented")
    }

    override fun getCurrentFigure(): Figure {
        TODO("Not yet implemented")
    }

    override fun getMapSize(): Size {
        TODO("Not yet implemented")
    }

    override fun move(playerId: Int, move: Move) {
        TODO("Not yet implemented")
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}
