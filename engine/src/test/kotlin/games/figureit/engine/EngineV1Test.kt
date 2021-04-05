package games.figureit.engine

import games.figureit.engine.first.EngineV1Impl
import games.figureit.engine.first.GameControl
import games.figureit.engine.first.ScoreControl
import games.figureit.engine.first.gamecontrol.GameControlSynchronized
import games.figureit.engine.first.gamecontrol.PlayerControl
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.first.gamecontrol.PositionGenerator
import games.figureit.engine.first.gamecontrol.playercontrol.PlayerControlImpl
import games.figureit.engine.first.gamecontrol.playergenerator.PlayerGeneratorImpl
import games.figureit.engine.first.gamecontrol.positiongenerator.PositionGeneratorFirstFree
import games.figureit.engine.first.scorecontrol.FigureGenerator
import games.figureit.engine.first.scorecontrol.ScoreControlImpl
import games.figureit.engine.first.scorecontrol.figuregenerator.FigureGeneratorDiagonal
import games.figureit.engine.first.scorecontrol.scorescheduler.ScoreSchedulerManual
import games.figureit.engine.model.Move.DOWN
import games.figureit.engine.model.Move.UP
import games.figureit.engine.model.PlayerDto
import games.figureit.engine.model.PositionState.ACTIVE
import games.figureit.engine.model.PositionState.INACTIVE
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class EngineV1Test {

    private lateinit var engine: Engine
    private lateinit var gameControl: GameControl
    private lateinit var scoreControl: ScoreControl
    private lateinit var playerControl: PlayerControl

    private val figureGenerator = mock(FigureGenerator::class.java)
    private val positionGenerator: PositionGenerator = PositionGeneratorFirstFree()
    private val playerGenerator: PlayerGenerator = PlayerGeneratorImpl()
    private val scoreScheduler = ScoreSchedulerManual()

    @BeforeMethod
    fun beforeEach() {
        playerControl = PlayerControlImpl(playerGenerator, positionGenerator)
        gameControl = GameControlSynchronized(playerControl, 50)
        scoreControl = ScoreControlImpl(
            timerControl = gameControl,
            playerListStore = playerControl,
            figureGenerator = figureGenerator,
            scoreScheduler = scoreScheduler
        )
        engine = EngineV1Impl(playerControl, gameControl, scoreControl)
    }

    @Test
    fun addNoPlayers() {
        val players = engine.getAllPlayers()
        assertThat(players, hasSize(0))
    }

    @Test
    fun addNoPlayersOnline() {
        val players = engine.getOnlinePlayers()
        assertThat(players, hasSize(0))
    }

    @Test
    fun addPlayerOnStopStillPending() {
        val p = engine.addPlayer()
        engine.activatePlayer(p.id)
        val players: Map<Long, PlayerDto> = engine.getOnlinePlayers().map { it.id to it }.toMap()
        assertThat(players.values, hasSize(1))
        assertThat(players.keys, hasItem(p.id))
        assertThat(players[p.id]!!.positionState, equalTo(INACTIVE))
    }

    @Test
    fun addPlayerAndStartBecameActive() {
        val p = engine.addPlayer()
        engine.activatePlayer(p.id)
        engine.start()
        val players: Map<Long, PlayerDto> = engine.getAllPlayers().map { it.id to it }.toMap()
        assertThat(players.values, hasSize(1))
        assertThat(players.keys, hasItem(p.id))
        assertThat(players[p.id]!!.positionState, equalTo(ACTIVE))
    }

    @Test
    fun winOneRound() {
        `when`(figureGenerator.generate(anyInt())).thenReturn(FigureGeneratorDiagonal().generate(2))
        val p1 = engine.addPlayer()
        val p2 = engine.addPlayer()
        engine.activatePlayer(p1.id)
        engine.activatePlayer(p2.id)

        engine.start()
        engine.move(p2.id, DOWN)
        scoreScheduler.run()

        val players: Map<Long, PlayerDto> = engine.getOnlinePlayers().map { it.id to it }.toMap()
        assertThat(players[p1.id]!!.score, equalTo(2))
        assertThat(players[p2.id]!!.score, equalTo(2))
    }

    @Test
    fun winTwoRounds() {
        `when`(figureGenerator.generate(anyInt())).thenReturn(FigureGeneratorDiagonal().generate(2))
        val p1 = engine.addPlayer()
        val p2 = engine.addPlayer()
        val p3 = engine.addPlayer()
        engine.activatePlayer(p1.id)
        engine.activatePlayer(p2.id)
        engine.activatePlayer(p3.id)

        engine.start()
        engine.move(p2.id, DOWN)
        scoreScheduler.run() // win p1 and p2
        engine.move(p2.id, UP)
        engine.move(p3.id, DOWN)
        scoreScheduler.run() // win p2 and p3

        val players: Map<Long, PlayerDto> = engine.getOnlinePlayers().map { it.id to it }.toMap()

        assertThat(players[p1.id]!!.score, equalTo(2))
        assertThat(players[p2.id]!!.score, equalTo(4))
        assertThat(players[p3.id]!!.score, equalTo(2))
    }

    @Test
    fun winOneRoundBigWinner() {
        `when`(figureGenerator.generate(anyInt())).thenReturn(FigureGeneratorDiagonal().generate(2))
        val p1 = engine.addPlayer()
        val p2 = engine.addPlayer()
        val p3 = engine.addPlayer()
        engine.activatePlayer(p1.id)
        engine.activatePlayer(p2.id)
        engine.activatePlayer(p3.id)

        engine.start()
        engine.move(p2.id, DOWN)
        engine.move(p3.id, DOWN)
        engine.move(p3.id, DOWN)
        scoreScheduler.run()

        val players: Map<Long, PlayerDto> = engine.getOnlinePlayers().map { it.id to it }.toMap()
        assertThat(players[p1.id]!!.score, equalTo(2))
        assertThat(players[p2.id]!!.score, equalTo(4))
        assertThat(players[p3.id]!!.score, equalTo(2))
    }

    @Test
    fun addAndRemovePlayerBetweenScoreCount() {
        `when`(figureGenerator.generate(anyInt())).thenReturn(FigureGeneratorDiagonal().generate(2))
        val p1 = engine.addPlayer()
        val p2 = engine.addPlayer()
        engine.activatePlayer(p1.id)
        engine.activatePlayer(p2.id)

        engine.start()
        engine.move(p2.id, DOWN)
        engine.deactivatePlayer(p1.id)
        val p3 = engine.addPlayer()
        engine.activatePlayer(p3.id)
        scoreScheduler.run() // win with p1 and p2, p2 deleted, p3 added instead p1

        scoreScheduler.run() // win with p2 and p3

        val players = engine.getAllPlayers().map { it.id to it }.toMap()
        assertThat(players[p1.id]!!.score, equalTo(2))
        assertThat(players[p2.id]!!.score, equalTo(4))
        assertThat(players[p3.id]!!.score, equalTo(2))
    }

    @Test
    fun scoreSchedulerGeneratesFigureAfterDeactivating() {
        val scoreControl = ScoreControlImpl(
            timerControl = gameControl,
            playerListStore = playerControl,
            figureGenerator = FigureGeneratorDiagonal(),
            scoreScheduler = scoreScheduler
        )
        val engine = EngineV1Impl(playerControl, gameControl, scoreControl)
        val p1 = engine.addPlayer()
        val p2 = engine.addPlayer()
        val p3 = engine.addPlayer()
        engine.activatePlayer(p1.id)
        engine.activatePlayer(p2.id)
        engine.activatePlayer(p3.id)

        engine.start()
        engine.move(p2.id, DOWN)
        engine.move(p3.id, DOWN)
        engine.move(p3.id, DOWN)
        engine.deactivatePlayer(p1.id)

        scoreScheduler.run() // win with p1 and p2 and p3, then delete p1 and generate figure for  2 players
        scoreScheduler.run() // win with p2 and p3

        val players = engine.getAllPlayers().map { it.id to it }.toMap()
        assertThat(players[p1.id]!!.score, equalTo(3))
        assertThat(players[p2.id]!!.score, equalTo(5))
        assertThat(players[p3.id]!!.score, equalTo(5))
    }

    @Test
    fun deactivationAndActivationOfTheSamePersonDoesntAffectAnything() {
        val scoreControl = ScoreControlImpl(
            timerControl = gameControl,
            playerListStore = playerControl,
            figureGenerator = FigureGeneratorDiagonal(),
            scoreScheduler = scoreScheduler
        )
        val engine = EngineV1Impl(playerControl, gameControl, scoreControl)
        val p1 = engine.addPlayer()
        val p2 = engine.addPlayer()
        val p3 = engine.addPlayer()
        engine.activatePlayer(p1.id)
        engine.activatePlayer(p2.id)
        engine.activatePlayer(p3.id)

        engine.start()
        engine.move(p2.id, DOWN)
        engine.move(p3.id, DOWN)
        engine.move(p3.id, DOWN)
        engine.deactivatePlayer(p2.id)
        engine.activatePlayer(p2.id)

        scoreScheduler.run()
        scoreScheduler.run()

        val players = engine.getAllPlayers().map { it.id to it }.toMap()
        assertThat(players[p1.id]!!.score, equalTo(6))
        assertThat(players[p2.id]!!.score, equalTo(6))
        assertThat(players[p3.id]!!.score, equalTo(6))
    }

    @Test
    fun activationAndDeactivationOfTheSamePersonDoesntAffectAnything() {
        val scoreControl = ScoreControlImpl(
            timerControl = gameControl,
            playerListStore = playerControl,
            figureGenerator = FigureGeneratorDiagonal(),
            scoreScheduler = scoreScheduler
        )
        val engine = EngineV1Impl(playerControl, gameControl, scoreControl)
        val p1 = engine.addPlayer()
        val p2 = engine.addPlayer()
        val p3 = engine.addPlayer()
        engine.activatePlayer(p1.id)
        engine.activatePlayer(p2.id)

        engine.start()
        engine.move(p2.id, DOWN)
        engine.activatePlayer(p3.id)
        engine.deactivatePlayer(p3.id)

        scoreScheduler.run()
        scoreScheduler.run()

        val players = engine.getAllPlayers().map { it.id to it }.toMap()
        assertThat(players[p1.id]!!.score, equalTo(4))
        assertThat(players[p2.id]!!.score, equalTo(4))
        assertThat(players[p3.id]!!.score, equalTo(0))
    }
}
