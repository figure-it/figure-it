package games.figureit.engine.first.scorecontrol.figuregenerator

import org.testng.annotations.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo


class FigureGeneratorDiagonalTest {

    @Test
    fun generateFigureTwoPlayers() {
        val generator = FigureGeneratorDiagonal()
        val figure = generator.generate(2);

        val pixels = figure.pixels

        assertThat(pixels.size, equalTo(2))
        assertThat(pixels[0], equalTo("10"))
        assertThat(pixels[1], equalTo("01"))
    }

    @Test
    fun generateFigureThreePlayers() {
        val generator = FigureGeneratorDiagonal()
        val figure = generator.generate(3);

        val pixels = figure.pixels

        assertThat(pixels.size, equalTo(3))
        assertThat(pixels[0], equalTo("100"))
        assertThat(pixels[1], equalTo("010"))
        assertThat(pixels[2], equalTo("001"))
    }
}
