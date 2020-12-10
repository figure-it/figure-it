package games.figureit.engine.first.scorecontrol

import games.figureit.engine.first.PlayerListStore
import games.figureit.engine.first.ScoreControl
import games.figureit.engine.first.TimerControl

class ScoreControlImpl(
    val timerControl: TimerControl,
    val playerListStore: PlayerListStore,
    val figureGenerator: FigureGenerator
): ScoreControl {
    override fun start() {
        TODO("Not yet implemented")
    }
}
