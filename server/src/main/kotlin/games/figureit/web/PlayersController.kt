package games.figureit.web

import games.figureit.service.GameService
import games.figureit.web.model.PlayerResponse
import games.figureit.web.model.PlayersResponse
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game/onlinePlayers")
class PlayersController(
    private val gameService: GameService
) {

    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    fun onlinePlayers(): PlayersResponse {
        val players = gameService.onlinePlayers()
        return PlayersResponse(players.map { PlayerResponse(it) })
    }
}
