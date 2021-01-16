package games.figureit.engine.first

import games.figureit.engine.model.Figure

interface ScoreControl : Runnable {
    fun start()
    fun stop()
    fun getFigure(): Figure
}
