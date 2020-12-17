package games.figureit.engine.first.gamecontrol

import games.figureit.engine.model.Player

interface PlayerGenerator {
    fun generate(): Player
}
