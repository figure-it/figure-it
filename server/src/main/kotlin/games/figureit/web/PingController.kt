package games.figureit.web

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ping")
class PingController {

    @RequestMapping(produces = [MediaType.TEXT_PLAIN_VALUE])
    fun request(): String {
        return "pong"
    }
}
