package games.figureit.engine.first.scorecontrol

import games.figureit.engine.model.Figure

interface FigureGenerator {
    fun generate(playersCount: Int): Figure
}
