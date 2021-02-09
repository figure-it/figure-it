package games.figureit.service

import games.figureit.api.Figure
import games.figureit.api.Size
import games.figureit.engine.Engine
import games.figureit.engine.model.Move
import games.figureit.engine.model.PlayerDto
import games.figureit.eventmanager.PlayerEventManager
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PostConstruct

@Service
class GameService(
    private val gameEngine: Engine,
    private val playerEventManager: PlayerEventManager
) {

    private val registeredPlayers = ConcurrentHashMap.newKeySet<Long>()

    @PostConstruct
    fun init() {
        gameEngine.start()
    }

    fun addPlayer(): PlayerDto {
        val player = gameEngine.addPlayer()
        registeredPlayers.add(player.id)
        return player
    }

    fun subscribePlayer(playerId: Long, session: WebSocketSession) {
        if (!registeredPlayers.contains(playerId)) {
            return
        }
        playerEventManager.subscribePlayer(playerId, session)
    }

    fun unsubscribePlayer(playerId: Long) {
        playerEventManager.unsubscribePlayer(playerId)
    }

    fun activatePlayer(playerId: Long) {
        if (!registeredPlayers.contains(playerId)) {
            return
        }
        gameEngine.activatePlayer(playerId)
    }

    fun deactivatePlayer(playerId: Long) {
        if (!registeredPlayers.contains(playerId)) {
            return
        }
        gameEngine.deactivatePlayer(playerId)
    }

    fun movePlayer(playerId: Long, move: Move) {
        gameEngine.move(playerId, move)
    }

    fun playerRegistered(userId: Long): Boolean {
        return registeredPlayers.contains(userId)
    }

    fun onlinePlayers(): List<PlayerDto> {
        return gameEngine.getOnlinePlayers()
    }

    fun getMapSize(): Size {
        val size = gameEngine.getMapSize()
        return Size(size.width, size.height)
    }

    fun getCurrentFigure(): Figure {
        val figure = gameEngine.getCurrentFigure()
        return Figure(
            id = figure.id,
            points = figure.points,
            size = Size(figure.size.width, figure.size.height),
            image = figure.image
        )
    }
}
