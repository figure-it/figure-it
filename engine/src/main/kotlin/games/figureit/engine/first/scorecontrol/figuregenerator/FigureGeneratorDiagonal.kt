package games.figureit.engine.first.scorecontrol.figuregenerator

import games.figureit.engine.model.Figure
import games.figureit.engine.model.Size
import games.figureit.engine.first.scorecontrol.FigureGenerator
import java.util.ArrayList

class FigureGeneratorDiagonal: FigureGenerator {
    override fun generate(playersCount: Int): Figure {
        val cnt = maxOf(2, playersCount)
        val pixels = ArrayList<String>(cnt)
        for(i in 0 until cnt) {
            val s = "0".repeat(i) + "1" + "0".repeat(cnt - i - 1)
            pixels.add(s)
        }

        return Figure(
            id = 1,
            size = Size(cnt, cnt),
            pixels = pixels,
            points = cnt
        )
    }
}
