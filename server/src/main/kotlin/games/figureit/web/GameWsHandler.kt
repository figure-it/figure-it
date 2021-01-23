package games.figureit.web

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import games.figureit.api.websocket.client.ClientAction.ACTIVATE
import games.figureit.api.websocket.client.ClientAction.DEACTIVATE
import games.figureit.api.websocket.client.ClientAction.MOVE
import games.figureit.api.websocket.client.ClientRequest
import games.figureit.api.websocket.client.MoveData
import games.figureit.engine.model.Move
import games.figureit.service.GameService
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.util.UriComponentsBuilder
import java.lang.Long.parseLong
import java.util.concurrent.ConcurrentHashMap

class GameWsHandler(private val gameService: GameService) : TextWebSocketHandler() {

    private val sessions = ConcurrentHashMap<Long, WebSocketSession>()

    private val mapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerKotlinModule()

    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            val action = mapper.readValue(message.payload, ClientRequest::class.java)
            handleAction(session, action)
        } catch (e: JsonProcessingException) {
            session.sendMessage(TextMessage("Error: " + e.message))
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)
        val userId = getUserId(session)

        if (userId == 0L) {
            session.sendMessage(TextMessage("Need positive int user_id parameter to establish connection"))
            session.close()
            return
        }

        if (!gameService.playerRegistered(userId)) {
            session.sendMessage(TextMessage("Player with user_id=$userId is not registered"))
            session.close()
            return
        }

        sessions[userId] ?. let {
            it.sendMessage(TextMessage("Another client connected"))
            it.close()
        }

        gameService.subscribePlayer(userId, session)

        sessions[userId] = session
        session.sendMessage(TextMessage("Welcome"))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        super.afterConnectionClosed(session, status)
        val userId = getUserId(session)

        sessions[userId] ?. let {
            if (it.id == session.id) {
                gameService.unsubscribePlayer(userId)
                gameService.deactivatePlayer(userId)
                sessions.remove(userId)
            }
        }
    }

    private fun handleAction(session: WebSocketSession, clientRequest: ClientRequest) {
        val userId = getUserId(session)
        val data = mapper.convertValue(clientRequest.data, clientRequest.action.dataClass)
        when (clientRequest.action) {
            ACTIVATE -> gameService.activatePlayer(userId)
            DEACTIVATE -> gameService.deactivatePlayer(userId)
            MOVE -> gameService.movePlayer(userId, Move.valueOf((data as MoveData).move.name))
        }
    }

    private fun getUserId(session: WebSocketSession): Long {
        val parameters = UriComponentsBuilder.fromUri(session.uri!!).build().queryParams
        return try {
            parseLong(parameters.getFirst("user_id"))
        } catch (e: NumberFormatException) {
            0
        }
    }
}
