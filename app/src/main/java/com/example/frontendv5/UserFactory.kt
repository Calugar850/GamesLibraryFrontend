package com.example.frontendv5

/**
 * This is a class which is used to create a user model to send in API request body
 * Parameters used to create is:
 * Integer idUser
 * String username
 * String email
 * String password
 * String address
 * Int type of user (BaseUser, PremiumUser, AdminUser)
 */
class UserFactory(val idUser: Integer,
                  val username: String,
                  val email: String,
                  val parola: String,
                  val adresa: String,
                  val tip: Int) {

    /**
     * This method is used to display in terminal the value of parameters, for creating the object model
     */
    override fun toString(): String {
        return "UserFactory(idUser=$idUser, username='$username', email='$email', parola='$parola', adresa='$adresa', tip=$tip)"
    }
}