package games.figureit.engine

import games.figureit.engine.first.EngineV1Impl
import games.figureit.engine.first.GameControl
import games.figureit.engine.first.ScoreControl
import games.figureit.engine.first.gamecontrol.GameControlSynchronized
import games.figureit.engine.first.gamecontrol.positiongenerator.PositionGeneratorFirstFree
import games.figureit.engine.first.scorecontrol.FigureGenerator
import games.figureit.engine.first.scorecontrol.ScoreControlImpl
import games.figureit.engine.first.scorecontrol.figuregenerator.FigureGeneratorDiagonal
import games.figureit.engine.first.scorecontrol.scorescheduler.ScoreSchedulerManual
import games.figureit.engine.model.Move.DOWN
import games.figureit.engine.model.Move.UP
import games.figureit.engine.model.PositionState.ON_MAP
import games.figureit.engine.model.PositionState.PENDING
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
    private lateinit var figureGenerator: FigureGenerator
    private lateinit var scoreScheduler: ScoreSchedulerManual

    @BeforeMethod
    fun beforeEach() {
        scoreScheduler = ScoreSchedulerManual()
        figureGenerator  = mock(FigureGenerator::class.java)
        gameControl = GameControlSynchronized(PositionGeneratorFirstFree(), 50)
        scoreControl = ScoreControlImpl(gameControl, gameControl, figureGenerator, scoreScheduler)
        engine = EngineV1Impl(gameControl, gameControl, scoreControl)
    }

    @Test
    fun addNoPlayerActive() {
        val players = engine.getAllPlayers()
        assertThat(players, hasSize(0))
    }

    @Test
    fun addPlayerOnStopStillPending() {
        val p = engine.addPlayer()
        val players = engine.getAllPlayers()
        assertThat(players, hasSize(1))
        assertThat(players, hasItem(p))
        assertThat(p.positionState, equalTo(PENDING))
    }

    @Test
    fun addPlayerAndStartBecameActive() {
        val p = engine.addPlayer()
        engine.start()
        val players = engine.getAllPlayers()
        assertThat(players, hasSize(1))
        assertThat(players, hasItem(p))
        assertThat(p.positionState, equalTo(ON_MAP))
    }

    @Test
    fun winOneRound() {
        `when`(figureGenerator.generate(anyInt())).thenReturn(FigureGeneratorDiagonal().generate(2))
        val p1 = engine.addPlayer()
        val p2 = engine.addPlayer()

        engine.start()
        engine.move(p2.id, DOWN)
        scoreScheduler.run()

        assertThat(p1.score, equalTo(2))
        assertThat(p2.score, equalTo(2))
    }

    @Test
    fun winTwoRounds() {
        `when`(figureGenerator.generate(anyInt())).thenReturn(FigureGeneratorDiagonal().generate(2))
        val p1 = engine.addPlayer()
        val p2 = engine.addPlayer()
        val p3 = engine.addPlayer()

        engine.start()
        engine.move(p2.id, DOWN)
        scoreScheduler.run()
        engine.move(p2.id, UP)
        engine.move(p3.id, DOWN)
        scoreScheduler.run()

        assertThat(p1.score, equalTo(2))
        assertThat(p2.score, equalTo(4))
        assertThat(p3.score, equalTo(2))
    }

    @Test
    fun winOneRoundBigWinner() {
        `when`(figureGenerator.generate(anyInt())).thenReturn(FigureGeneratorDiagonal().generate(2))
        val p1 = engine.addPlayer()
        val p2 = engine.addPlayer()
        val p3 = engine.addPlayer()

        engine.start()
        engine.move(p2.id, DOWN)
        engine.move(p3.id, DOWN)
        engine.move(p3.id, DOWN)
        scoreScheduler.run()

        assertThat(p1.score, equalTo(2))
        assertThat(p2.score, equalTo(4))
        assertThat(p3.score, equalTo(2))
    }

    @Test
    fun addAndRemovePlayerBetweenScoreCount() {
        `when`(figureGenerator.generate(anyInt())).thenReturn(FigureGeneratorDiagonal().generate(2))
        val p1 = engine.addPlayer()
        val p2 = engine.addPlayer()

        engine.start()
        engine.move(p2.id, DOWN)
        engine.removePlayer(p1.id)
        val p3 = engine.addPlayer()
        scoreScheduler.run() //win with p1 and p2, p2 deleted, p3 added instead p1

        scoreScheduler.run() //win with p2 and p3

        assertThat(p1.score, equalTo(2))
        assertThat(p2.score, equalTo(4))
        assertThat(p3.score, equalTo(2))
    }

}
