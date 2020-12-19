package games.figureit.engine.first.gamecontrol

import games.figureit.engine.first.MapControl

interface GameControlState: MapControl {
    fun stopTheWorld(): GameControlState
    fun startTheWorld(): GameControlState
}
