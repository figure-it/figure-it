package games.figureit.eventmanager

import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.only
import org.mockito.Mockito.verify
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GameEventManagerWebSocketTest {

    private lateinit var gameEventManager: GameEventManagerWebSocket
    private lateinit var executorService: ExecutorService
    private lateinit var gameEventSubscriber: GameEventSubscriber
    private lateinit var webSocketSession: WebSocketSession

    @BeforeMethod
    fun setUp() {
        webSocketSession = mock(WebSocketSession::class.java)
        executorService = Executors.newCachedThreadPool()
        gameEventSubscriber = mock(GameEventSubscriber::class.java)
        gameEventManager = GameEventManagerWebSocket(gameEventSubscriber, executorService)
    }

    @Test
    fun initTest() {
        gameEventManager.init()
        verify(gameEventSubscriber, only()).subscribe(gameEventManager)
    }

    @Test
    fun correctSubscribe() {
        gameEventManager.subscribePlayer(1, webSocketSession)
        gameEventManager.worldStarted()

        waitForBroadcast()

        verify(webSocketSession, only())
            .sendMessage(TextMessage("{\"action\":\"WORLD_STARTED\",\"data\":{}}"))
    }

    @Test
    fun correctUnsubscribe() {
        gameEventManager.subscribePlayer(1, webSocketSession)
        gameEventManager.unsubscribePlayer(1)
        gameEventManager.worldStopped()

        waitForBroadcast()

        verify(webSocketSession, never()).sendMessage(TextMessage(anyString()))
    }

    @Test
    fun twoSubscribes() {
        gameEventManager.subscribePlayer(1, webSocketSession)
        gameEventManager.playerDeactivated(2)

        waitForBroadcast()

        verify(webSocketSession, only())
            .sendMessage(TextMessage("{\"action\":\"PLAYER_DEACTIVATED\",\"data\":{\"playerId\":2}}"))
    }

    private fun waitForBroadcast() {
        executorService.shutdown()
        executorService.awaitTermination(10, TimeUnit.SECONDS)
    }
}
