package games.figureit.engine.model

data class Figure(
    val id: Long,
    val size: Size,
    val image: List<String>,
    val points: Int,
    val timeout: Int
)
