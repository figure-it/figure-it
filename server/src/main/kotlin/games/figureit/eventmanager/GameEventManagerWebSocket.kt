package games.figureit.eventmanager

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import games.figureit.api.Pixel
import games.figureit.api.websocket.FigureData
import games.figureit.api.websocket.PlayerPosition
import games.figureit.api.websocket.server.PlayerActivatedData
import games.figureit.api.websocket.server.PlayerDeactivatedData
import games.figureit.api.websocket.server.PlayerPositionUpdateData
import games.figureit.api.websocket.server.ServerAction
import games.figureit.api.websocket.server.ServerAction.PLAYER_ACTIVATED
import games.figureit.api.websocket.server.ServerAction.PLAYER_DEACTIVATED
import games.figureit.api.websocket.server.ServerAction.PLAYER_POSITION_UPDATE
import games.figureit.api.websocket.server.ServerAction.TASK_COMPLETED
import games.figureit.api.websocket.server.ServerAction.TASK_UPDATED
import games.figureit.api.websocket.server.ServerAction.WORLD_STARTED
import games.figureit.api.websocket.server.ServerAction.WORLD_STOPPED
import games.figureit.api.websocket.server.ServerRequest
import games.figureit.api.websocket.server.TaskCompletedData
import games.figureit.api.websocket.server.TaskUpdatedData
import games.figureit.api.websocket.server.WorldStartedData
import games.figureit.api.websocket.server.WorldStoppedData
import games.figureit.engine.model.Figure
import games.figureit.engine.model.PlayerDto
import games.figureit.engine.model.Position
import games.figureit.engine.model.Size
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import javax.annotation.PostConstruct

class GameEventManagerWebSocket(
    private val gameEventSubscriber: GameEventSubscriber,
    private val executorService: ExecutorService
) : GameEventManager, PlayerEventManager {

    private val subscribes: MutableMap<Long, PlayerCallback> = ConcurrentHashMap()

    @PostConstruct
    fun init() {
        gameEventSubscriber.subscribe(this)
    }

    override fun subscribePlayer(playerId: Long, session: WebSocketSession) {
        subscribes[playerId] = PlayerCallback(session)
    }

    override fun unsubscribePlayer(playerId: Long) {
        subscribes.remove(playerId)
    }

    override fun playerActivated(player: PlayerDto) {
        broadcastUpdate(
            PLAYER_ACTIVATED,
            PlayerActivatedData(
                playerId = player.id,
                position = positionToPixel(player.position),
                score = player.score
            )
        )
    }

    override fun playerDeactivated(playerId: Long) {
        broadcastUpdate(PLAYER_DEACTIVATED, PlayerDeactivatedData(playerId))
    }

    override fun taskCompleted(figure: Figure, position: Position, players: List<Long>) {
        broadcastUpdate(
            TASK_COMPLETED,
            TaskCompletedData(
                image = figure.image,
                position = positionToPixel(position),
                winners = players
            )
        )
    }

    override fun taskUpdated(figure: Figure) {
        broadcastUpdate(
            TASK_UPDATED,
            TaskUpdatedData(
                FigureData(
                    id = figure.id,
                    image = figure.image,
                    points = figure.points,
                    size = convertSize(figure.size),
                    timeout = figure.timeout
                )
            )
        )
    }

    override fun worldStopped() {
        broadcastUpdate(WORLD_STOPPED, WorldStoppedData())
    }

    override fun worldStarted() {
        broadcastUpdate(WORLD_STARTED, WorldStartedData())
    }

    override fun playerPositionUpdate(playerId: Long, position: Position) {
        val data = PlayerPositionUpdateData(listOf(PlayerPosition(playerId, positionToPixel(position))))
        broadcastUpdate(PLAYER_POSITION_UPDATE, data)
    }

    private fun broadcastUpdate(action: ServerAction, data: Any) {
        if (data.javaClass != action.dataClass) {
            throw IllegalArgumentException("$data has type not acceptable for action $action")
        }
        val request = ServerRequest(action, data)
        val message = mapper.writeValueAsString(request)
        subscribes.values.forEach { executorService.submit { it.send(TextMessage(message)) } }
    }

    companion object {
        private val mapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerKotlinModule()

        private fun positionToPixel(pos: Position): Pixel {
            return Pixel(x = pos.x, y = pos.y)
        }

        private fun convertSize(size: Size): games.figureit.api.Size {
            return games.figureit.api.Size(width = size.width, height = size.height)
        }
    }
}
