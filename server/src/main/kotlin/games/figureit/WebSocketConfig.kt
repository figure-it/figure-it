package games.figureit

import games.figureit.service.GameService
import games.figureit.web.GameWsHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(
    val gameService: GameService
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        val commonHandler = myHandler()
        registry
            .addHandler(commonHandler, "/game/handle")
            .setAllowedOrigins("*")
    }

    fun myHandler(): WebSocketHandler {
        return GameWsHandler(gameService)
    }
}
