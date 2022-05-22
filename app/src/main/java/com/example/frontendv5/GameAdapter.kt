package com.example.frontendv5

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This class represent an object which will be displayed in the main page of app.
 * The object contain the game and 2 buttons
 * The BASE_URL_3 is a constant value which is used for communication with backend.
 */
const val BASE_URL_3 = "http://10.0.2.2:8080/"
class GameAdapter (private val gamesList: List<Game>) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    var bill: Button? = null
    var delete: Button? = null
    var g: Game? =null

    /**
     * Provide a direct reference to each of the views within a data item
     * Used to cache the views within the item layout for fast access
     * Your holder should contain and initialize a member variable
     * for any view that will be set as you render a row
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.game_name)
        val messageButton = itemView.findViewById<Button>(R.id.add_to_cart)
        val messageButton2 = itemView.findViewById<Button>(R.id.delete_game)
    }

    /**
     * constructor and member variables
     * Usually involves inflating a layout from XML and returning the holder
     * Inflate the custom layout
     * Return a new holder instance
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.item, parent, false)
        return ViewHolder(contactView)
    }

    /**
     * Involves populating data into the item through holder
     * Set item views based on your views and data model
     */
    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: GameAdapter.ViewHolder, position: Int) {
        val game: Game = gamesList.get(position)
        g = game
        val textView = viewHolder.nameTextView
        textView.setText(game.toString())
        val button = viewHolder.messageButton
        button.text = "Buy"
        button!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                addGameInUserCart(view)
            }
        })
        val button2 = viewHolder.messageButton2
        button2.text = "Delete Game"
        button2!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){

                val context: Context = view!!.getContext()
                val i = Intent(context, FirstActivity::class.java)
                context.startActivity(i)
                deleteGame(game)
            }
        })
    }

    /**
     * This method make a request to backend to get the games which will pe displayed in main page
     */
    private fun addGameInUserCart(view: View?) {
        val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

        val client = OkHttpClient.Builder()
            .connectionSpecs(specs)
            .build()

        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
            .baseUrl(BASE_URL_3).build().create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.addGameinCart(idUser = USERID, tip = EnumUsers.BaseUser, idGame = g!!.id)
        retrofitData.enqueue(object : Callback<List<Game2>> {
            override fun onResponse(
                call: Call<List<Game2>?>,
                response: Response<List<Game2>?>
            ) {
                val responseBody = response.body()!!
                redirect(view)
            }
            override fun onFailure(call: Call<List<Game2>?>, t: Throwable) {
                Log.d(ContentValues.TAG,"onFailure: " +t.message)
            }
        })
    }

    /**
     * This method redirect bill page of the app
     */
    fun redirect(view: View?) {
        val context: Context = view!!.getContext()
        val i = Intent(context, BillActivity::class.java)
        context.startActivity(i)
    }

    /**
     * This method make a request to backend to delete a game from the list
     */
    private fun deleteGame(game: Game) {
        val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

        val client = OkHttpClient.Builder()
            .connectionSpecs(specs)
            .build()

        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
            .baseUrl(BASE_URL_3).build().create(ApiInterface::class.java)
        val retrofitData = retrofitBuilder.deleteGame(idGame = game.id)

        retrofitData.enqueue(object : Callback<Unit?> {
            override fun onResponse(
                call: Call<Unit?>,
                response: Response<Unit?>
            ) {
                val responseBody = response.body()!!

            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
                Log.d(ContentValues.TAG,"onFailure: " +t.message)
            }
        })
    }

    /**
     * Returns the total count of items in the list
     */
    override fun getItemCount(): Int {
        return gamesList.size
    }
}