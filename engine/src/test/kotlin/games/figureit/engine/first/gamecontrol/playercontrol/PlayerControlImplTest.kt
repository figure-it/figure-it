package games.figureit.engine.first.gamecontrol.playercontrol

import games.figureit.engine.first.gamecontrol.Field
import games.figureit.engine.first.gamecontrol.GameControlTest
import games.figureit.engine.first.gamecontrol.PlayerControl
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.first.gamecontrol.playergenerator.PlayerGeneratorImpl
import games.figureit.engine.first.gamecontrol.positiongenerator.PositionGeneratorFirstFree
import games.figureit.engine.model.PositionState
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class PlayerControlImplTest {

    private lateinit var playerControl: PlayerControl
    private lateinit var field: Field

    @BeforeMethod
    fun setUp() {
        val playerGenerator: PlayerGenerator = PlayerGeneratorImpl()
        playerControl = PlayerControlImpl(playerGenerator, PositionGeneratorFirstFree())
        field = Field(50, 50)
    }

    @Test
    fun addNoPlayerActive() {
        val players = playerControl.getActivePlayers()
        MatcherAssert.assertThat(players, Matchers.hasSize(0))
    }

    @Test
    fun addNoPlayerPending() {
        val players = playerControl.getPendingPlayers()
        MatcherAssert.assertThat(players, Matchers.hasSize(0))
    }

    @Test
    fun addOnePlayer() {
        val p = playerControl.addPlayer()
        playerControl.activatePlayer(p.id)
        val players = playerControl.getPendingPlayers()
        MatcherAssert.assertThat(players, Matchers.hasSize(1))
        MatcherAssert.assertThat(players, Matchers.hasItem(GameControlTest.player(p.id, PositionState.INACTIVE, 0, 0)))
    }

    @Test
    fun addTwoPlayers() {
        val p1 = playerControl.addPlayer()
        val p2 = playerControl.addPlayer()
        playerControl.activatePlayer(p1.id)
        playerControl.activatePlayer(p2.id)

        val players = playerControl.getPendingPlayers()
        MatcherAssert.assertThat(players, Matchers.hasSize(2))
        MatcherAssert.assertThat(players, Matchers.hasItem(GameControlTest.player(2, PositionState.INACTIVE, 0, 0)))
    }

    @Test
    fun addTwoPlayersActive() {
        val p1 = playerControl.addPlayer()
        val p2 = playerControl.addPlayer()
        playerControl.activatePlayer(p1.id)
        playerControl.activatePlayer(p2.id)
        playerControl.submitPreparations(field)

        val players = playerControl.getActivePlayers()
        MatcherAssert.assertThat(players, Matchers.hasSize(2))
        MatcherAssert.assertThat(players, Matchers.hasItem(GameControlTest.player(2, PositionState.ACTIVE, 1, 0)))
    }

    @Test
    fun addPlayersMoveToActiveAfterStart() {
        playerControl.addPlayer()
        playerControl.submitPreparations(field)

        val players = playerControl.getPendingPlayers()
        MatcherAssert.assertThat(players, Matchers.hasSize(0))
    }


    @Test
    fun removeToZero() {
        playerControl.addPlayer()
        playerControl.addPlayer()

        playerControl.deactivatePlayer(1)
        playerControl.deactivatePlayer(2)

        val players = playerControl.getActivePlayers()
        MatcherAssert.assertThat(players, Matchers.hasSize(0))
    }

    /* ADD PLAYERS BETWEEN startTheWorld() AND stopTheWorld() */

    @Test
    fun addPlayerAfterStartTheWorld() {
        playerControl.submitPreparations(field)
        val p = playerControl.addPlayer()
        playerControl.activatePlayer(p.id)
        val players = playerControl.getPendingPlayers()
        MatcherAssert.assertThat(players, Matchers.hasItem(GameControlTest.player(p.id, PositionState.INACTIVE, 0, 0)))
    }

    @Test
    fun playerAddedAfterStopTheWorld() {
        playerControl.submitPreparations(field)
        val p = playerControl.addPlayer()
        playerControl.activatePlayer(p.id)
        playerControl.submitPreparations(field)
        val players = playerControl.getActivePlayers()
        MatcherAssert.assertThat(players, Matchers.hasItem(GameControlTest.player(p.id, PositionState.ACTIVE, 0, 0)))
    }

}