package games.figureit.web

import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/status")
class PingController {
    @RequestMapping(produces = [APPLICATION_JSON_VALUE])
    fun request(): String {
        return "Up"
    }
}
