package games.figureit.eventmanager

import games.figureit.engine.model.Figure
import games.figureit.engine.model.PlayerDto
import games.figureit.engine.model.Position
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class GameEventManagerAggregator : GameEventManager, GameEventSubscriber {

    private val subscribes: MutableSet<GameEventManager> = ConcurrentHashMap.newKeySet()

    override fun subscribe(gameEventManager: GameEventManager) {
        subscribes.add(gameEventManager)
    }

    override fun unsubscribe(gameEventManager: GameEventManager) {
        subscribes.remove(gameEventManager)
    }

    override fun playerActivated(player: PlayerDto) {
        subscribes.forEach { subscribe -> subscribe.playerActivated(player) }
    }

    override fun playerDeactivated(playerId: Long) {
        subscribes.forEach { subscribe -> subscribe.playerDeactivated(playerId) }
    }

    override fun taskCompleted(figure: Figure, position: Position, players: List<Long>) {
        subscribes.forEach { subscribe -> subscribe.taskCompleted(figure, position, players) }
    }

    override fun taskUpdated(figure: Figure) {
        subscribes.forEach { subscribe -> subscribe.taskUpdated(figure) }
    }

    override fun worldStopped() {
        subscribes.forEach { subscribe -> subscribe.worldStopped() }
    }

    override fun worldStarted() {
        subscribes.forEach { subscribe -> subscribe.worldStarted() }
    }

    override fun playerPositionUpdate(playerId: Long, position: Position) {
        subscribes.forEach { subscribe -> subscribe.playerPositionUpdate(playerId, position) }
    }
}
