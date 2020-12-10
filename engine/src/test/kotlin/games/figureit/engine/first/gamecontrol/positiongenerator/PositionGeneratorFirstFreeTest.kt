package games.figureit.engine.first.gamecontrol.positiongenerator

import games.figureit.engine.model.Position
import games.figureit.engine.model.Size
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

internal class PositionGeneratorFirstFreeTest {

    private val playerStrategy = PositionGeneratorFirstFree()

    private val defaultSize = Size(2, 2)

    @BeforeMethod
    fun generateFirst() {
        val position = playerStrategy.generate(defaultSize, emptyList())
        val expectedPosition = Position(0, 0)
        assertThat(position, equalTo(expectedPosition))
    }

    @Test
    fun generateSecond() {
        val positions = ArrayList<Position>()
        val firstPosition = playerStrategy.generate(defaultSize, positions)
        positions.add(firstPosition)
        val secondPosition = playerStrategy.generate(defaultSize, positions)
        val expectedPosition = Position(1, 0)
        assertThat(secondPosition, equalTo(expectedPosition))
    }

    @Test
    fun generateNewLine() {
        val positions = ArrayList<Position>()
        val firstPosition = playerStrategy.generate(defaultSize, positions)
        positions.add(firstPosition)
        val secondPosition = playerStrategy.generate(defaultSize, positions)
        positions.add(secondPosition)
        val thirdPosition = playerStrategy.generate(defaultSize, positions)
        val expectedPosition = Position(0, 1)
        assertThat(thirdPosition, equalTo(expectedPosition))
    }
}
