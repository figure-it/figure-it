package games.figureit.web

import games.figureit.service.GameService
import games.figureit.web.model.GameStateResponse
import games.figureit.web.model.PlayerResponse
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game/state")
class GameStateController(
    private val gameService: GameService
) {

    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    fun onlinePlayers(): GameStateResponse {
        return GameStateResponse(
            mapSize = gameService.getMapSize(),
            figure = gameService.getCurrentFigure(),
            players = gameService.onlinePlayers().map { PlayerResponse(it) }
        )
    }
}
