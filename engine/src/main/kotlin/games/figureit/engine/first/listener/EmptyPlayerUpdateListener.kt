package games.figureit.engine.first.listener

import games.figureit.engine.model.PlayerDto

class EmptyPlayerUpdateListener : PlayerUpdateListener {
    override fun playerActivated(player: PlayerDto) {
        // do nothing
    }

    override fun playerDeactivated(playerId: Long) {
        // do nothing
    }
}
