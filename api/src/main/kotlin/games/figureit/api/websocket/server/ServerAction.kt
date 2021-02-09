package games.figureit.api.websocket.server

enum class ServerAction(val dataClass: Class<out Any>) {
    WORLD_STOPPED(WorldStoppedData::class.java),
    WORLD_STARTED(WorldStartedData::class.java),
    PLAYER_ACTIVATED(PlayerActivatedData::class.java),
    PLAYER_DEACTIVATED(PlayerDeactivatedData::class.java),
    PLAYER_POSITION_UPDATE(PlayerPositionUpdateData::class.java),
    TASK_COMPLETED(TaskCompletedData::class.java),
    TASK_UPDATED(TaskUpdatedData::class.java)
}
