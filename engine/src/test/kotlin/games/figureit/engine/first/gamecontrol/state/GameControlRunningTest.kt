package games.figureit.engine.first.gamecontrol.state

import games.figureit.engine.first.gamecontrol.Field
import games.figureit.engine.first.gamecontrol.GameControlState
import games.figureit.engine.first.gamecontrol.PlayerControl
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.first.gamecontrol.PositionGenerator
import games.figureit.engine.first.gamecontrol.playercontrol.PlayerControlImpl
import games.figureit.engine.first.gamecontrol.playergenerator.PlayerGeneratorImpl
import games.figureit.engine.first.gamecontrol.positiongenerator.PositionGeneratorFirstFree
import games.figureit.engine.model.Move.DOWN
import games.figureit.engine.model.Move.LEFT
import games.figureit.engine.model.Move.RIGHT
import games.figureit.engine.model.Move.UP
import games.figureit.engine.model.Position
import games.figureit.engine.model.Size
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.nullValue
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class GameControlRunningTest {

    private lateinit var playerControl: PlayerControl
    private lateinit var state: GameControlState
    private lateinit var field: Field
    private val defaultSize = Size(5, 5)

    @BeforeMethod
    fun beforeEach() {
        val positionGenerator: PositionGenerator = PositionGeneratorFirstFree()
        val playerGenerator: PlayerGenerator = PlayerGeneratorImpl()
        field = Field(defaultSize.width, defaultSize.height)
        playerControl = PlayerControlImpl(playerGenerator, positionGenerator)
        state = GameControlRunning(field, playerControl)
    }

    @Test
    fun movePlayer() {
        val player = playerControl.addPlayer()
        playerControl.activatePlayer(player.id)
        playerControl.submitPreparations(field)

        state.move(player.id, RIGHT)
        state.move(player.id, DOWN)
        state.move(player.id, RIGHT)

        val actual = playerControl.getActivePlayer(player.id)!!
        assertThat(actual.position, equalTo(Position(2, 1)))
        assertThat(field.playerAt(2, 2), nullValue())
        assertThat(field.playerAt(2, 1)!!.id, equalTo(player.id))
    }

    @Test
    fun moveTwoPlayers() {
        val player1 = playerControl.addPlayer()
        val player2 = playerControl.addPlayer()
        playerControl.activatePlayer(player1.id)
        playerControl.activatePlayer(player2.id)
        playerControl.submitPreparations(field)

        state.move(1, RIGHT) // occupied
        state.move(1, DOWN)
        state.move(1, RIGHT)
        state.move(1, UP) // occupied
        state.move(2, LEFT)
        state.move(1, UP)

        val actual1 = playerControl.getActivePlayer(player1.id)!!
        val actual2 = playerControl.getActivePlayer(player2.id)!!
        assertThat(actual1.position, equalTo(Position(1, 0)))
        assertThat(actual2.position, equalTo(Position(0, 0)))
        assertThat(field.playerAt(1, 0)!!.id, equalTo(player1.id))
        assertThat(field.playerAt(0, 0)!!.id, equalTo(player2.id))
    }

    @Test
    fun startTheWorld() {
        val actual = state.startTheWorld()
        assertThat(actual, equalTo(state))
    }

    @Test
    fun stopTheWorld() {
        val actual = state.stopTheWorld()
        assertThat(actual, instanceOf(GameControlStopped::class.java))
    }

    @Test
    fun size() {
        val actual = state.getMapSize()
        assertThat(actual, equalTo(defaultSize))
    }
}
