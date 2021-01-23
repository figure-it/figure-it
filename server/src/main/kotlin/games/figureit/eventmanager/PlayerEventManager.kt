package games.figureit.eventmanager

import org.springframework.web.socket.WebSocketSession

interface PlayerEventManager {
    fun subscribePlayer(playerId: Long, session: WebSocketSession)
    fun unsubscribePlayer(playerId: Long)
}
