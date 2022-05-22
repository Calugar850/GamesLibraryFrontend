package com.example.frontendv5

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * This activity implement a page for user details which contains some data about user, list of games and bills of user
 * The BASE_URL_8 is a constant value which is used for communication with backend.
 */
const val BASE_URL_8 = "http://10.0.2.2:8080/"
class UserDetailsActivity : AppCompatActivity() {
    var back: Button? = null
    var detailtext: TextView? = null

    /**
     * This is a method which is called when this activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        detailtext = findViewById(R.id.details) as TextView
        displayDetails()

        setupUI();
        setupListeners();
    }

    /**
     * This method display the elements of user details page, and make a request to backend for retrieving user data
     */
    private fun displayDetails() {
        val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

        val client = OkHttpClient.Builder()
            .connectionSpecs(specs)
            .build()

        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
            .baseUrl(BASE_URL_8).build().create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getUser(idUser = USERID)
        retrofitData.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User?>,
                response: Response<User?>
            ) {
                val responseBody = response.body()!!
                var string = "Username: " + responseBody.username + "\n"+
                        "Email: " + responseBody.email + "\n"+
                        "Adresa: " + responseBody.adresa
                var gamesString: String? = "\nGames:";
                for(g in responseBody.games){
                    gamesString = gamesString + "\nNume: "+ g.nume + ""
                }
                string = string + gamesString
                var billsString: String? = "\nBills:";
                for(b in responseBody.bills){
                    billsString = billsString + "\nSuma factura: " + b.suma + ""
                }
                string = string + billsString
                detailtext?.setText(string)
            }
            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.d(ContentValues.TAG,"onFailure: " +t.message)
            }
        })
    }

    /**
     * Method for find the buttons of this activity
     */
    private fun setupUI() {
        back = findViewById(R.id.back) as Button
    }

    /**
     * Method to instantiate the listeners for buttons
     */
    private fun setupListeners() {
        back!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                val context: Context = view!!.getContext()
                val i = Intent(context, FirstActivity::class.java)
                context.startActivity(i)
            }
        })
    }
}