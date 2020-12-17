package games.figureit.engine.first.gamecontrol

import games.figureit.engine.first.GameControl
import games.figureit.engine.first.gamecontrol.positiongenerator.PositionGeneratorFirstFree
import games.figureit.engine.model.Move.DOWN
import games.figureit.engine.model.Move.LEFT
import games.figureit.engine.model.Move.RIGHT
import games.figureit.engine.model.Player
import games.figureit.engine.model.Position
import games.figureit.engine.model.PositionState
import games.figureit.engine.model.PositionState.ON_MAP
import games.figureit.engine.model.PositionState.PENDING
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.testng.annotations.BeforeMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Factory
import org.testng.annotations.Test


class GameControlTest
@Factory(dataProvider = "gameControl") constructor(
    private var gameControlInit: () -> GameControl
) {

    private lateinit var gameControl: GameControl

    @BeforeMethod
    fun beforeEach() {
        gameControl = gameControlInit.invoke()
    }

    /* ADD AND DELETE PLAYERS */

    @Test
    fun addNoPlayerActive() {
        val players = gameControl.getActivePlayers()
        assertThat(players, hasSize(0))
    }

    @Test
    fun addNoPlayerPending() {
        val players = gameControl.getPendingPlayers()
        assertThat(players, hasSize(0))
    }

    @Test
    fun addOnePlayer() {
        gameControl.addPlayer()
        val players = gameControl.getPendingPlayers()
        assertThat(players, hasSize(1))
        assertThat(players, hasItem(player(1, PENDING, 0, 0)))
    }

    @Test
    fun addTwoPlayers() {
        gameControl.addPlayer()
        gameControl.addPlayer()

        val players = gameControl.getPendingPlayers()
        assertThat(players, hasSize(2))
        assertThat(players, hasItem(player(2, PENDING, 0, 0)))
    }

    @Test
    fun addTwoPlayersActive() {
        gameControl.addPlayer()
        gameControl.addPlayer()
        gameControl.startTheWorld()

        val players = gameControl.getActivePlayers()
        assertThat(players, hasSize(2))
        assertThat(players, hasItem(player(2, ON_MAP, 1, 0)))
    }

    @Test
    fun addPlayersMoveToActiveAfterStart() {
        gameControl.addPlayer()
        gameControl.startTheWorld()

        val players = gameControl.getPendingPlayers()
        assertThat(players, hasSize(0))
    }


    @Test
    fun removeToZero() {
        gameControl.addPlayer()
        gameControl.addPlayer()

        gameControl.removePlayer(1)
        gameControl.removePlayer(2)

        val players = gameControl.getActivePlayers()
        assertThat(players, hasSize(0))
    }

    /* ADD PLAYERS BETWEEN startTheWorld() AND stopTheWorld() */

    @Test
    fun addPlayerAfterStartTheWorld() {
        gameControl.startTheWorld()
        gameControl.addPlayer()
        val players = gameControl.getPendingPlayers()
        assertThat(players, hasItem(player(1, PENDING, 0, 0)))
    }

    @Test
    fun playerAddedAfterStopTheWorld() {
        gameControl.startTheWorld()
        gameControl.addPlayer()
        gameControl.stopTheWorld()
        gameControl.startTheWorld()
        val players = gameControl.getActivePlayers()
        assertThat(players, hasItem(player(1, ON_MAP, 0, 0)))
    }

    /* MOVES PLAYERS */

    @Test
    fun movePlayerRight() {
        gameControl.addPlayer()
        gameControl.startTheWorld()
        gameControl.move(1, RIGHT)
        val players = gameControl.getActivePlayers()
        assertThat(players, hasItem(player(1, ON_MAP, 1, 0)))
    }

    @Test
    fun movePlayerDown() {
        gameControl.addPlayer()
        gameControl.startTheWorld()
        gameControl.move(1, DOWN)
        val players = gameControl.getActivePlayers()
        assertThat(players, hasItem(player(1, ON_MAP, 0, 1)))
    }

    @Test
    fun movePlayerToOccupiedPlace() {
        gameControl.addPlayer()
        gameControl.addPlayer()
        gameControl.startTheWorld()
        gameControl.move(1, RIGHT)
        val players = gameControl.getActivePlayers()
        assertThat(players, hasItem(player(1, ON_MAP, 0, 0)))
    }

    @Test
    fun movePlayerOverBorderOfMap() {
        gameControl.addPlayer()
        gameControl.startTheWorld()
        gameControl.move(1, LEFT)
        val players = gameControl.getActivePlayers()
        assertThat(players, hasItem(player(1, ON_MAP, 0, 0)))
    }

    @Test
    fun movePlayerWithStoppedTimer() {
        gameControl.addPlayer()
        gameControl.startTheWorld()
        gameControl.stopTheWorld()
        gameControl.move(1, RIGHT)
        val players = gameControl.getActivePlayers()
        assertThat(players, hasItem(player(1, ON_MAP, 0, 0)))
    }


    companion object {
        fun player(id: Long, state: PositionState, x: Int, y: Int): Player {
            return Player(
                id = id,
                position = Position(x, y),
                positionState = state
            )
        }

        private val positionGenerator = PositionGeneratorFirstFree()

        @DataProvider
        @JvmStatic
        fun gameControl(): Array<Array<() -> GameControl>> {
            return arrayOf(
                arrayOf( { GameControlSynchronized(positionGenerator, 20) } )
            )
        }
    }

}
