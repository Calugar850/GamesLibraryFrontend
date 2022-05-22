package com.example.frontendv5

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * This activity implement a page form for create a game
 * The BASE_URL_2 is a constant value which is used for communication with backend.
 */
const val BASE_URL_2 = "http://10.0.2.2:8080/"
class CreateGameActivity : AppCompatActivity() {
    var nume: EditText? = null
    var year: EditText? = null
    var publisher: EditText? = null
    var price: EditText? = null
    var gen: EditText? = null
    var description: EditText? = null
    var submit: Button? = null

    /**
     * This is a method which is called when this activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_game)

        nume = findViewById(R.id.name);
        year = findViewById(R.id.year)
        publisher = findViewById(R.id.publisher)
        price = findViewById(R.id.price)
        gen = findViewById(R.id.gen)
        description = findViewById(R.id.description)
        submit = findViewById(R.id.submit_game) as Button

        submit!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                checkData();
            }
        })
    }

    /**
     * This method check the values of fields and validate it.
     * If all the fields are not empty user can create the game,
     * else an error message will be displayed
     */
    private fun checkData() {
        var redirect: Boolean = true;
        if (isEmpty(nume)) {
            nume!!.setError("Enter a name!");
            redirect = false
        }
        if(isEmpty(publisher)){
            publisher!!.setError("Enter a publisher!");
            redirect = false
        }
        if(isEmpty(price)){
            price!!.setError("Enter a price!");
            redirect = false
        }
        if(isEmpty(gen)){
            gen!!.setError("Enter a type!");
            redirect = false
        }
        if(isEmpty(description)){
            description!!.setError("Enter a description!");
            redirect = false
        }
        val game2 = Game(Integer(2),
            nume?.text.toString(),
            Integer.parseInt(year?.text.toString()),
            publisher?.text.toString(),
            price?.text.toString().toFloat(),
            gen?.text.toString(),
            description?.text.toString());

        if(redirect){
            createGame(game2)
            val i = Intent(this, FirstActivity::class.java)
            startActivity(i)
        }
    }

    /**
     * Method to verify if a field is empty
     */
    fun isEmpty(text: EditText?): Boolean {
        val str: CharSequence = text?.text.toString()
        return TextUtils.isEmpty(str)
    }

    /**
     * Method to make a request to create a game in database and save it.
     */
    fun createGame(game2: Game) {

        val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

        val client = OkHttpClient.Builder()
            .connectionSpecs(specs)
            .build()

        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
            .baseUrl(BASE_URL_2).build().create(ApiInterface::class.java)
        val retrofitData = retrofitBuilder.createGame(post = game2)

        retrofitData.enqueue(object : Callback<Game2?> {
            override fun onResponse(
                call: Call<Game2?>,
                response: Response<Game2?>
            ) {
                val responseBody = response.body()!!
            }

            override fun onFailure(call: Call<Game2?>, t: Throwable) {
                Log.d(ContentValues.TAG,"onFailure: " +t.message)
            }
        })
    }
}