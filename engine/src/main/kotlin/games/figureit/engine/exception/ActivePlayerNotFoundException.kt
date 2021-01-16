package games.figureit.engine.exception

import java.lang.RuntimeException

class ActivePlayerNotFoundException(playerId: Long) : RuntimeException("Player $playerId not found in active players")
