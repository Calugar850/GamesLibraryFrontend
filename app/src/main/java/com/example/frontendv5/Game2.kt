package com.example.frontendv5

/**
 * This is a data class which is used to create a game model to retrieve object from API request
 * Parameters used to create is:
 * Integer idGame
 * String name of game
 * Int year of release
 * String studio which develop the game
 * Float price of the game
 * String gen
 * String description of the game
 */
data class Game2(
    val idGame : Integer,
    val nume: String,
    val anAparitie: Int,
    val publisher: String,
    val pret: Float,
    val gen: String,
    val descriere: String
)
