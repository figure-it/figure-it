package games.figureit.engine.first.gamecontrol.state

import games.figureit.engine.exception.ActivePlayerNotFoundException
import games.figureit.engine.first.gamecontrol.Field
import games.figureit.engine.first.gamecontrol.GameControlState
import games.figureit.engine.first.gamecontrol.PlayerControl
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.first.gamecontrol.PositionGenerator
import games.figureit.engine.first.gamecontrol.playercontrol.PlayerControlImpl
import games.figureit.engine.first.gamecontrol.playergenerator.PlayerGeneratorImpl
import games.figureit.engine.first.gamecontrol.positiongenerator.PositionGeneratorFirstFree
import games.figureit.engine.model.Move.RIGHT
import games.figureit.engine.model.Position
import games.figureit.engine.model.Size
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class GameControlStoppedTest {

    private lateinit var playerControl: PlayerControl
    private lateinit var state: GameControlState
    private val defaultSize = Size(5, 5)

    @BeforeMethod
    fun beforeEach() {
        val positionGenerator: PositionGenerator = PositionGeneratorFirstFree()
        val playerGenerator: PlayerGenerator = PlayerGeneratorImpl()
        val field: Field = Field(defaultSize.width, defaultSize.height)
        playerControl = PlayerControlImpl(playerGenerator, positionGenerator)
        state = GameControlStopped(field, playerControl)
    }

    @Test
    fun pendingPlayersBecameActiveAfterStart() {
        val player = playerControl.addPlayer()
        playerControl.activatePlayer(player.id)
        state = state.startTheWorld()
        val players = playerControl.getActivePlayers()
        assertThat(players, hasSize(1))
        assertThat(players, hasItem(player))
    }

    @Test(expectedExceptions = [ActivePlayerNotFoundException::class])
    fun moveInactivePlayer() {
        val player = playerControl.addPlayer()
        playerControl.activatePlayer(player.id)
        state.move(player.id, RIGHT)
    }

    @Test
    fun startTheWorld() {
        val actual = state.startTheWorld()
        assertThat(actual, Matchers.instanceOf(GameControlRunning::class.java))
    }

    @Test
    fun stopTheWorld() {
        val actual = state.stopTheWorld()
        assertThat(actual, equalTo(state))
    }

    @Test
    fun size() {
        val actual = state.getMapSize()
        assertThat(actual, equalTo(defaultSize))
    }

}
