package games.figureit.api.websocket.client

enum class ClientAction(val dataClass: Class<out Any>) {
    MOVE(MoveData::class.java),
    ACTIVATE(ActivateData::class.java),
    DEACTIVATE(DeactivateData::class.java);
}
