package games.figureit.eventmanager

import games.figureit.engine.model.Figure
import games.figureit.engine.model.Player
import games.figureit.engine.model.PlayerDto
import games.figureit.engine.model.Position
import games.figureit.engine.model.Size
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.only
import org.mockito.Mockito.verify
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class GameEventManagerAggregatorTest {

    private lateinit var subscriber1: GameEventManager
    private lateinit var subscriber2: GameEventManager
    private lateinit var aggregator: GameEventManagerAggregator

    @BeforeMethod
    fun beforeEach() {
        aggregator = GameEventManagerAggregator()
        subscriber1 = mock(GameEventManager::class.java)
        subscriber2 = mock(GameEventManager::class.java)
        aggregator.subscribe(subscriber1)
        aggregator.subscribe(subscriber2)
    }

    @Test
    fun testPlayerActivated() {
        val dto = PlayerDto(Player(1))
        aggregator.playerActivated(dto)
        verify(subscriber1, only()).playerActivated(dto)
        verify(subscriber2, only()).playerActivated(dto)
    }

    @Test
    fun testPlayerDeactivated() {
        aggregator.playerDeactivated(1)
        aggregator.playerDeactivated(2)
        verify(subscriber1).playerDeactivated(1)
        verify(subscriber1).playerDeactivated(2)
    }

    @Test
    fun testTaskCompleted() {
        val figure = Figure(1, Size(1, 1), emptyList(), 0, 0)
        aggregator.taskCompleted(figure, Position(0, 0), emptyList())
        verify(subscriber1, only()).taskCompleted(figure, Position(0, 0), emptyList())
    }

    @Test
    fun testTaskUpdated() {
        val figure = Figure(1, Size(1, 1), emptyList(), 0, 0)
        aggregator.taskUpdated(figure)
        verify(subscriber1, only()).taskUpdated(figure)
    }

    @Test
    fun testWorldStopped() {
        aggregator.unsubscribe(subscriber2)
        aggregator.worldStopped()
        verify(subscriber1, only()).worldStopped()
        verify(subscriber2, never()).worldStopped()
    }

    @Test
    fun testWorldStarted() {
        aggregator.unsubscribe(subscriber1)
        aggregator.unsubscribe(subscriber1)
        aggregator.worldStarted()
        verify(subscriber2, only()).worldStarted()
        verify(subscriber1, never()).worldStarted()
    }

    @Test
    fun testPlayerPositionUpdate() {
        aggregator.subscribe(subscriber1)
        aggregator.subscribe(subscriber1)
        aggregator.playerPositionUpdate(1, Position(0, 0))
        verify(subscriber1, only()).playerPositionUpdate(1, Position(0, 0))
    }
}
