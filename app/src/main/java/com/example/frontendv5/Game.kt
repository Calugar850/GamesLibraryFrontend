package com.example.frontendv5

/**
 * This is a class which is used to create a game model to send in API request body
 * Parameters used to create is:
 * Integer idGame
 * String name of game
 * Int year of release
 * String studio which develop the game
 * Float price of the game
 * String gen
 * String description of the game
 */
class Game(val id: Integer, val nume: String?, val anAparitie: Int?, val publisher: String?, val pret: Float?, val gen: String?, val descriere: String?) {

    /**
     * Method to create a list of games
     */
    companion object {
        fun createGameList(): ArrayList<Game> {
            val games2 = ArrayList<Game>()
            games2.add(0,Game(Integer(1), "GTA San Andreas", 2001, "RockStar", 30.5.toFloat(), "Action", "Old game but very good"))
            return games2
        }
    }

    /**
     * This method is used to display in terminal the value of parameters, for creating the object model
     */
    override fun toString(): String {
        return "Name: $nume \n" +
                "Year of release: $anAparitie \n" +
                "Publisher: $publisher \n" +
                "Price: $pret $\n" +
                "Gen: $gen \n" +
                "Description: $descriere"
    }


}
