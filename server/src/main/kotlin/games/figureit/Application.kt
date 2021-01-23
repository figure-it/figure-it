@file:JvmName("Application")

package games.figureit

import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    SpringApplication.run(ServerApplication::class.java, *args)
}
