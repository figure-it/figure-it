package games.figureit.web

import games.figureit.service.GameService
import games.figureit.web.model.PlayerResponse
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/game/authorize")
class AuthController(
    private val gameService: GameService
) {

    @PostMapping(produces = [APPLICATION_JSON_VALUE])
    fun request(): PlayerResponse {
        val player = gameService.addPlayer()
        return PlayerResponse(player)
    }
}
