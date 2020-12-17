package games.figureit.engine.first.gamecontrol

import games.figureit.engine.first.MapControl
import games.figureit.engine.first.PlayerControl

interface GameControlState: MapControl, PlayerControl {
    fun stopTheWorld(): GameControlState
    fun startTheWorld(): GameControlState
}
