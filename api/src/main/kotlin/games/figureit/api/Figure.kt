package games.figureit.api

data class Figure(
    val id: Long,
    val size: Size,
    val image: Collection<String>,
    val points: Int
)
