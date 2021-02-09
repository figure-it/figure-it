package games.figureit.engine.first.scorecontrol.figuregenerator

import games.figureit.engine.first.scorecontrol.FigureGenerator
import games.figureit.engine.model.Figure
import games.figureit.engine.model.Size
import java.util.ArrayList
import java.util.concurrent.atomic.AtomicLong

class FigureGeneratorDiagonal : FigureGenerator {
    private val counter = AtomicLong(1)

    override fun generate(playersCount: Int): Figure {
        val cnt = maxOf(2, playersCount)
        val image = ArrayList<String>(cnt)
        for (i in 0 until cnt) {
            val s = "0".repeat(i) + "1" + "0".repeat(cnt - i - 1)
            image.add(s)
        }

        return Figure(
            id = counter.getAndIncrement(),
            size = Size(cnt, cnt),
            image = image,
            points = cnt,
            timeout = 20
        )
    }
}
