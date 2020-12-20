package games.figureit.engine.first.scorecontrol

import games.figureit.engine.first.PlayerListStore
import games.figureit.engine.first.listener.TaskUpdateListener
import games.figureit.engine.first.TimerControl
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.first.gamecontrol.playergenerator.PlayerGeneratorImpl
import games.figureit.engine.first.scorecontrol.figuregenerator.FigureGeneratorDiagonal
import games.figureit.engine.model.Player
import games.figureit.engine.model.Position
import games.figureit.engine.model.PositionState.ACTIVE
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class ScoreControlImplTest {

    private lateinit var timerControl: TimerControl
    private lateinit var figureGenerator: FigureGenerator
    private lateinit var playerGenerator: PlayerGenerator
    private lateinit var playerListStore: PlayerListStore
    private lateinit var scoreScheduler: ScoreScheduler
    private lateinit var taskUpdateListener: TaskUpdateListener

    @BeforeMethod
    fun setUp() {
        timerControl = mock(TimerControl::class.java)
        figureGenerator = mock(FigureGenerator::class.java)
        playerListStore = mock(PlayerListStore::class.java)
        playerGenerator = PlayerGeneratorImpl()
        scoreScheduler = mock(ScoreScheduler::class.java)
        taskUpdateListener = mock(TaskUpdateListener::class.java)
    }

    @Test
    fun testWithoutPlayers() {
        `when`(playerListStore.getActivePlayers()).thenReturn(emptyList())
        `when`(figureGenerator.generate(anyInt())).thenReturn(FigureGeneratorDiagonal().generate(2))
        val scoreControl = ScoreControlImpl(timerControl, playerListStore,
            figureGenerator, scoreScheduler, taskUpdateListener)
        scoreControl.start()
        scoreControl.run()
    }

    @Test
    fun testRunLose() {
        val p1 = generatePlayer(0, 0)
        val p2 = generatePlayer(1, 0)
        `when`(playerListStore.getActivePlayers()).thenReturn(listOf(p1, p2))
        `when`(figureGenerator.generate(anyInt())).thenReturn(FigureGeneratorDiagonal().generate(2))
        val scoreControl = ScoreControlImpl(timerControl, playerListStore,
            figureGenerator, scoreScheduler, taskUpdateListener)
        scoreControl.start()
        scoreControl.run()
        assertThat(p1.score, equalTo(0))
        assertThat(p2.score, equalTo(0))
    }

    @Test
    fun testRunWin() {
        val p1 = generatePlayer(1, 1)
        val p2 = generatePlayer(2, 1)
        val p3 = generatePlayer(2, 2)
        `when`(playerListStore.getActivePlayers()).thenReturn(listOf(p1, p2, p3))
        `when`(figureGenerator.generate(anyInt())).thenReturn(FigureGeneratorDiagonal().generate(2))
        val scoreControl = ScoreControlImpl(timerControl, playerListStore,
            figureGenerator, scoreScheduler, taskUpdateListener)
        scoreControl.start()
        scoreControl.run()
        assertThat(p1.score, equalTo(2))
        assertThat(p2.score, equalTo(0))
        assertThat(p3.score, equalTo(2))
    }

    @Test
    fun testRunWinDouble() {
        val p1 = generatePlayer(1, 1)
        val p2 = generatePlayer(3, 3)
        val p3 = generatePlayer(2, 2)
        `when`(playerListStore.getActivePlayers()).thenReturn(listOf(p1, p2, p3))
        `when`(figureGenerator.generate(anyInt())).thenReturn(FigureGeneratorDiagonal().generate(2))
        val scoreControl = ScoreControlImpl(timerControl, playerListStore,
            figureGenerator, scoreScheduler, taskUpdateListener)
        scoreControl.start()
        scoreControl.run()
        assertThat(p1.score, equalTo(2))
        assertThat(p2.score, equalTo(2))
        assertThat(p3.score, equalTo(4))
    }

    private fun generatePlayer(x: Int, y: Int): Player {
        val player = playerGenerator.generate()
        player.position = Position(x, y)
        player.positionState = ACTIVE
        return player
    }
}
