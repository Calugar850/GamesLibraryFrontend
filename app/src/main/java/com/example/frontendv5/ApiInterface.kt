package com.example.frontendv5

import retrofit2.Call
import retrofit2.http.*

/**
 * This is an interface to make request to backend.
 */
interface ApiInterface {

    /**
     * Request to get all games from database.
     */
    @GET("games")
    fun getData(): Call<List<Game2>>

    /**
     * Request to create a game and save into database.
     */
    @POST("games")
    fun createGame(
        @Body post: Game
    ): Call<Game2>

    /**
     * Request to delete a game from database.
     */
    @DELETE("games/{idGame}")
    fun deleteGame(
        @Path("idGame") idGame: Integer
    ): Call<Unit>

    /**
     * Request to create user and save into database.
     */
    @POST("users/{tip}")
    fun registerUser(
        @Path("tip") tip: EnumUsers,
        @Body post: UserFactory
    ): Call<User>

    /**
     * Request to get all users from database.
     */
    @GET("users/{tip}")
    fun getUsers(
        @Path("tip") tip: EnumUsers
    ): Call<List<User>>

    /**
     * Request to validate data fields from login page.
     */
    @POST("users/login/{tip}")
    fun loginUser(
        @Path("tip") tip: EnumUsers,
        @Body post: UserFactory
    ): Call<User>

    /**
     * Request to add a game in cart user
     */
    @PUT("users/addGame/{idUser}/{tip}/{idGame}")
    fun addGameinCart(
        @Path("idUser") idUser: Integer,
        @Path("tip") tip: EnumUsers,
        @Path("idGame") idGame: Integer
    ): Call<List<Game2>>

    /**
     * Request to get a user
     */
    @GET("users/oneUser/{idUser}")
    fun getUser(
        @Path("idUser") idUser: Integer,
    ): Call<User>

    /**
     * Request to create the bill
     */
    @PUT("bills/generateBill/{idUser}/{tip}")
    fun generateBill(
        @Path("idUser") idUser: Integer,
        @Path("tip") tip: EnumUsers
    ): Call<Bill>

    /**
     * Request to generate the bill for the user
     */
    @PUT("users/generateBillPerClient/{idUser}/{tip}")
    fun clientBill(
        @Path("idUser") idUser: Integer,
        @Path("tip") tip: EnumUsers
    ): Call<List<Bill>>
}