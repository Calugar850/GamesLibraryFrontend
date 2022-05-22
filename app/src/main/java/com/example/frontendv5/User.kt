package com.example.frontendv5

/**
 * This is a data class which is used to create a user model to retrieve object from API request
 * Parameters used to create is:
 * Integer idUser
 * String username
 * String email
 * String password
 * String address
 * Int type of user (BaseUser, PremiumUser, AdminUser)
 * Game2 list games
 * Bill list bills
 */
data class User(val idUser: Integer,
                val username: String,
                val email: String,
                val parola: String,
                val adresa: String,
                val tip: Int,
                val games: List<Game2>,
                val bills: List<Bill>)
