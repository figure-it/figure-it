package games.figureit.engine.model

import games.figureit.engine.first.gamecontrol.Field
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.first.gamecontrol.playergenerator.PlayerGeneratorImpl
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.sameInstance
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class FieldTest {

    private val side = 50
    private lateinit var field: Field
    private lateinit var playerGenerator: PlayerGenerator

    @BeforeMethod
    fun beforeEach() {
        field = Field(side, side)
        playerGenerator = PlayerGeneratorImpl()
    }

    @Test
    fun testFreeAt() {
        assertThat(field.freeAt(0, 0), equalTo(true))
    }

    @Test
    fun testNotFreeAt() {
        field.setPlayerOnFieldPosition(0,0, generatePlayer())
        assertThat(field.freeAt(0, 0), equalTo(false))
    }

    @Test
    fun testTheSameObject() {
        val p = generatePlayer()
        field.setPlayerOnFieldPosition(0,0, p)
        assertThat(field.playerAt(0, 0), sameInstance(p))
    }

    @Test
    fun testPlayerAtByPosition() {
        val p = generatePlayer()
        field.setPlayerOnFieldPosition(0,0, p)
        assertThat(field.playerAt(Position(0, 0)), sameInstance(p))
    }

    @Test
    fun testLeftAcrossZeroBorder() {
        val actual = field.leftFor(Position(0,10))
        assertThat(actual, equalTo(Position(0, 10)))
    }

    @Test
    fun testLeft() {
        val actual = field.leftFor(Position(5,10))
        assertThat(actual, equalTo(Position(4, 10)))
    }

    @Test
    fun testRightAcrossZeroBorder() {
        val actual = field.rightFor(Position(side - 1,10))

        assertThat(actual, equalTo(Position(side - 1, 10)))
    }

    @Test
    fun testRight() {
        val actual = field.rightFor(Position(5,10))
        assertThat(actual, equalTo(Position(6, 10)))
    }

    @Test
    fun testUpAcrossZeroBorder() {
        val actual = field.upFor(Position(3,0))
        assertThat(actual, equalTo(Position(3, 0)))
    }

    @Test
    fun testUp() {
        val actual = field.upFor(Position(4,9))
        assertThat(actual, equalTo(Position(4, 8)))
    }

    @Test
    fun testDownAcrossZeroBorder() {
        val actual = field.downFor(Position(3,side - 1))
        assertThat(actual, equalTo(Position(3, side - 1)))
    }

    @Test
    fun testDown() {
        val actual = field.downFor(Position(4,11))
        assertThat(actual, equalTo(Position(4, 12)))
    }

    private fun generatePlayer(): Player {
        return playerGenerator.generate()
    }
}
