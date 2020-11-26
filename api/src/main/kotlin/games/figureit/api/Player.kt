package games.figureit.api

data class Player (
        val id: Int,
        val position: Pixel,
        val name: String,
        val color: _root_ide_package_.games.figureit.api.Color,
        val points: Int,
        val online: Boolean
)
