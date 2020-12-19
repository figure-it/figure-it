package games.figureit.engine.first.gamecontrol.state

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
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class GameControlStoppedTest {

    private lateinit var playerControl: PlayerControl
    private lateinit var gameState: GameControlState

    @BeforeMethod
    fun beforeEach() {
        val positionGenerator: PositionGenerator = PositionGeneratorFirstFree()
        val playerGenerator: PlayerGenerator = PlayerGeneratorImpl()
        val field: Field = Field(5, 5)
        playerControl = PlayerControlImpl(playerGenerator, positionGenerator)
        gameState = GameControlStopped(field, playerControl)
    }

    @Test
    fun pendingPlayersBecameActiveAfterStart() {
        val player = playerControl.addPlayer()
        playerControl.activatePlayer(player.id)
        gameState = gameState.startTheWorld()
        val players = playerControl.getActivePlayers()
        assertThat(players, hasSize(1))
        assertThat(players, hasItem(player))
    }

    @Test
    fun movePlayer() {
        val player = playerControl.addPlayer()
        playerControl.activatePlayer(player.id)
        gameState.move(player.id, RIGHT)

        val players = playerControl.getPendingPlayers()
        assertThat(players, hasSize(1))
        assertThat(players.stream().findAny().get().position, equalTo(Position(0,0)))
    }
}
