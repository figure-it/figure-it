package games.figureit.web

import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class PingController {
    @RequestMapping(value = arrayOf("/status"), produces = [APPLICATION_JSON_VALUE])
    fun status(): String {
        return "Up"
    }
    @RequestMapping(value = arrayOf("/"), produces = [APPLICATION_JSON_VALUE])
    fun hello(): String {
        return "Figure-it"
    }
}
