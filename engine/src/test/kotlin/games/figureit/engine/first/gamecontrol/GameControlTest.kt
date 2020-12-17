package games.figureit.engine.first.gamecontrol

import games.figureit.engine.first.GameControl
import games.figureit.engine.first.gamecontrol.positiongenerator.PositionGeneratorFirstFree
import games.figureit.engine.model.Move.DOWN
import games.figureit.engine.model.Move.LEFT
import games.figureit.engine.model.Move.RIGHT
import games.figureit.engine.model.Player
import games.figureit.engine.model.Position
import games.figureit.engine.model.PositionState
import games.figureit.engine.model.PositionState.ACTIVE
import games.figureit.engine.model.PositionState.INACTIVE
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
        val p = gameControl.addPlayer()
        gameControl.activatePlayer(p.id)
        val players = gameControl.getPendingPlayers()
        assertThat(players, hasSize(1))
        assertThat(players, hasItem(player(p.id, INACTIVE, 0, 0)))
    }

    @Test
    fun addTwoPlayers() {
        val p1 = gameControl.addPlayer()
        val p2 = gameControl.addPlayer()
        gameControl.activatePlayer(p1.id)
        gameControl.activatePlayer(p2.id)

        val players = gameControl.getPendingPlayers()
        assertThat(players, hasSize(2))
        assertThat(players, hasItem(player(2, INACTIVE, 0, 0)))
    }

    @Test
    fun addTwoPlayersActive() {
        val p1 = gameControl.addPlayer()
        val p2 = gameControl.addPlayer()
        gameControl.activatePlayer(p1.id)
        gameControl.activatePlayer(p2.id)
        gameControl.startTheWorld()

        val players = gameControl.getActivePlayers()
        assertThat(players, hasSize(2))
        assertThat(players, hasItem(player(2, ACTIVE, 1, 0)))
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

        gameControl.deactivatePlayer(1)
        gameControl.deactivatePlayer(2)

        val players = gameControl.getActivePlayers()
        assertThat(players, hasSize(0))
    }

    /* ADD PLAYERS BETWEEN startTheWorld() AND stopTheWorld() */

    @Test
    fun addPlayerAfterStartTheWorld() {
        gameControl.startTheWorld()
        val p = gameControl.addPlayer()
        gameControl.activatePlayer(p.id)
        val players = gameControl.getPendingPlayers()
        assertThat(players, hasItem(player(p.id, INACTIVE, 0, 0)))
    }

    @Test
    fun playerAddedAfterStopTheWorld() {
        gameControl.startTheWorld()
        val p = gameControl.addPlayer()
        gameControl.activatePlayer(p.id)
        gameControl.stopTheWorld()
        gameControl.startTheWorld()
        val players = gameControl.getActivePlayers()
        assertThat(players, hasItem(player(p.id, ACTIVE, 0, 0)))
    }

    /* MOVES PLAYERS */

    @Test
    fun movePlayerRight() {
        val p = gameControl.addPlayer()
        gameControl.activatePlayer(p.id)
        gameControl.startTheWorld()
        gameControl.move(p.id, RIGHT)
        val players = gameControl.getActivePlayers()
        assertThat(players, hasItem(player(p.id, ACTIVE, 1, 0)))
    }

    @Test
    fun movePlayerDown() {
        val p = gameControl.addPlayer()
        gameControl.activatePlayer(p.id)
        gameControl.startTheWorld()
        gameControl.move(p.id, DOWN)
        val players = gameControl.getActivePlayers()
        assertThat(players, hasItem(player(p.id, ACTIVE, 0, 1)))
    }

    @Test
    fun movePlayerToOccupiedPlace() {
        val p1 = gameControl.addPlayer()
        val p2 = gameControl.addPlayer()
        gameControl.activatePlayer(p1.id)
        gameControl.activatePlayer(p2.id)
        gameControl.startTheWorld()
        gameControl.move(p1.id, RIGHT)
        val players = gameControl.getActivePlayers()
        assertThat(players, hasItem(player(p1.id, ACTIVE, 0, 0)))
    }

    @Test
    fun movePlayerOverBorderOfMap() {
        val p = gameControl.addPlayer()
        gameControl.activatePlayer(p.id)
        gameControl.startTheWorld()
        gameControl.move(p.id, LEFT)
        val players = gameControl.getActivePlayers()
        assertThat(players, hasItem(player(p.id, ACTIVE, 0, 0)))
    }

    @Test
    fun movePlayerWithStoppedTimer() {
        val p = gameControl.addPlayer()
        gameControl.activatePlayer(p.id)
        gameControl.startTheWorld()
        gameControl.stopTheWorld()
        gameControl.move(p.id, RIGHT)
        val players = gameControl.getActivePlayers()
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
