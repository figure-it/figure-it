package games.figureit.engine.first.gamecontrol

import games.figureit.engine.first.GameControl
import games.figureit.engine.first.gamecontrol.playercontrol.PlayerControlImpl
import games.figureit.engine.first.gamecontrol.playergenerator.PlayerGeneratorImpl
import games.figureit.engine.first.gamecontrol.positiongenerator.PositionGeneratorFirstFree
import games.figureit.engine.model.Move.DOWN
import games.figureit.engine.model.Move.LEFT
import games.figureit.engine.model.Move.RIGHT
import games.figureit.engine.model.Player
import games.figureit.engine.model.Position
import games.figureit.engine.model.PositionState
import games.figureit.engine.model.PositionState.ACTIVE
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test


class GameControlTest {

    private val positionGenerator = PositionGeneratorFirstFree()
    private lateinit var gameControl: GameControl
    private lateinit var playerControl: PlayerControl

    @BeforeMethod
    fun beforeEach() {
        playerControl = PlayerControlImpl(PlayerGeneratorImpl(), positionGenerator)
        gameControl = GameControlSynchronized(playerControl, 20)
    }

    /* ADD AND DELETE PLAYERS */



    /* MOVES PLAYERS */

    @Test
    fun movePlayerRight() {
        val p = playerControl.addPlayer()
        playerControl.activatePlayer(p.id)
        gameControl.startTheWorld()
        gameControl.move(p.id, RIGHT)
        val players = playerControl.getActivePlayers()
        assertThat(players, hasItem(player(p.id, ACTIVE, 1, 0)))
    }

    @Test
    fun movePlayerDown() {
        val p = playerControl.addPlayer()
        playerControl.activatePlayer(p.id)
        gameControl.startTheWorld()
        gameControl.move(p.id, DOWN)
        val players = playerControl.getActivePlayers()
        assertThat(players, hasItem(player(p.id, ACTIVE, 0, 1)))
    }

    @Test
    fun movePlayerToOccupiedPlace() {
        val p1 = playerControl.addPlayer()
        val p2 = playerControl.addPlayer()
        playerControl.activatePlayer(p1.id)
        playerControl.activatePlayer(p2.id)
        gameControl.startTheWorld()
        gameControl.move(p1.id, RIGHT)
        val players = playerControl.getActivePlayers()
        assertThat(players, hasItem(player(p1.id, ACTIVE, 0, 0)))
    }

    @Test
    fun movePlayerOverBorderOfMap() {
        val p = playerControl.addPlayer()
        playerControl.activatePlayer(p.id)
        gameControl.startTheWorld()
        gameControl.move(p.id, LEFT)
        val players = playerControl.getActivePlayers()
        assertThat(players, hasItem(player(p.id, ACTIVE, 0, 0)))
    }

    @Test
    fun movePlayerWithStoppedTimer() {
        val p = playerControl.addPlayer()
        playerControl.activatePlayer(p.id)
        gameControl.startTheWorld()
        gameControl.stopTheWorld()
        gameControl.move(p.id, RIGHT)
        val players = playerControl.getActivePlayers()
        assertThat(players, hasItem(player(p.id, ACTIVE, 0, 0)))
    }


    companion object {
        fun player(id: Long, state: PositionState, x: Int, y: Int): Player {
            return Player(
                id = id,
                position = Position(x, y),
                positionState = state
            )
        }
    }

}
