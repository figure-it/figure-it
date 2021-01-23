package games.figureit.eventmanager

import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

class PlayerCallback(
    private val session: WebSocketSession
) {
    @Synchronized
    fun send(text: TextMessage) {
        session.sendMessage(text)
    }
}
