package games.figureit.engine.first.scorecontrol.figuregenerator

import games.figureit.engine.model.Figure
import games.figureit.engine.model.Size
import games.figureit.engine.first.scorecontrol.FigureGenerator
import java.util.ArrayList

class FigureGeneratorDiagonal: FigureGenerator {
    override fun generate(playersCount: Int): Figure {
        val pixels = ArrayList<String>(playersCount)

        for(i in 0 until playersCount) {
            val s = "0".repeat(i) + "1" + "0".repeat(playersCount - i - 1)
            pixels.add(s)
        }

        return Figure(
            id = 1,
            size = Size(playersCount, playersCount),
            pixels = pixels,
            points = playersCount
        )
    }
}
