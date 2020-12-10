package games.figureit.engine.model

data class Figure(
        val id: Int,
        val size: Size,
        val pixels: List<String>,
        val points: Int
)
