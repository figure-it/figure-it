package games.figureit.eventmanager

interface GameEventSubscriber {
    fun subscribe(gameEventManager: GameEventManager)
    fun unsubscribe(gameEventManager: GameEventManager)
}
