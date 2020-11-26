package it.figure.api

data class Player (
        val id: Int,
        val position: Pixel,
        val name: String,
        val color: Color,
        val points: Int,
        val online: Boolean
)
