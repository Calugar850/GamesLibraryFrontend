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
 * This activity implement a page for bill details of the user
 * The BASE_URL_8 is a constant value which is used for communication with backend.
 */
const val BASE_URL_6 = "http://10.0.2.2:8080/"
class BillActivity : AppCompatActivity() {
    var back: Button? = null
    var finishOrder: Button? = null
    var billtext: TextView? = null

    /**
     * This is a method which is called when this activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)

        billtext = findViewById(R.id.bill) as TextView

        displayBillDetails()

        setupUI();
        setupListeners();
    }

    /**
     * Method for find the buttons of this activity
     */
    private fun setupUI() {
        back = findViewById(R.id.back) as Button
        finishOrder = findViewById(R.id.finish_order) as Button
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

        finishOrder!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                generateBill(view)
            }
        })
    }

    /**
     * This method make a request to backend for generating the bill for the client
     */
    private fun generateBill(view: View?) {
        if(TYPE==0){
            val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

            val client = OkHttpClient.Builder()
                .connectionSpecs(specs)
                .build()

            val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
                .baseUrl(BASE_URL_6).build().create(ApiInterface::class.java)
            val retrofitData = retrofitBuilder.generateBill(idUser = USERID, tip = EnumUsers.BaseUser)
            retrofitData.enqueue(object : Callback<Bill?> {
                override fun onResponse(
                    call: Call<Bill?>,
                    response: Response<Bill?>
                ) {
                    redirect(view)
                }
                override fun onFailure(call: Call<Bill?>, t: Throwable) {
                    Log.d(ContentValues.TAG,"onFailure: " +t.message)
                }
            })
        }else if(TYPE==1){
            val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

            val client = OkHttpClient.Builder()
                .connectionSpecs(specs)
                .build()

            val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
                .baseUrl(BASE_URL_6).build().create(ApiInterface::class.java)
            val retrofitData = retrofitBuilder.generateBill(idUser = USERID, tip = EnumUsers.PremiumUser)
            retrofitData.enqueue(object : Callback<Bill> {
                override fun onResponse(
                    call: Call<Bill?>,
                    response: Response<Bill?>
                ) {
                    redirect(view)
                }
                override fun onFailure(call: Call<Bill?>, t: Throwable) {
                    Log.d(ContentValues.TAG,"onFailure: " +t.message)
                }
            })
        }else if(TYPE==2){
            val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

            val client = OkHttpClient.Builder()
                .connectionSpecs(specs)
                .build()

            val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
                .baseUrl(BASE_URL_6).build().create(ApiInterface::class.java)
            val retrofitData = retrofitBuilder.generateBill(idUser = USERID, tip = EnumUsers.AdminUser)
            retrofitData.enqueue(object : Callback<Bill> {
                override fun onResponse(
                    call: Call<Bill?>,
                    response: Response<Bill?>
                ) {
                    redirect(view)
                }
                override fun onFailure(call: Call<Bill?>, t: Throwable) {
                    Log.d(ContentValues.TAG,"onFailure: " +t.message)
                }
            })
        }
    }

    /**
     * Method to redirect the user to final page of the purchase process
     */
    fun redirect(view: View?) {
        val context: Context = view!!.getContext()
        val i = Intent(context, CompleteOrderactivity::class.java)
        context.startActivity(i)
    }

    /**
     * Method to redirect user to main page of the app
     */
    fun redirectBack(view: View?) {
        val context: Context = view!!.getContext()
        val i = Intent(context, CompleteOrderactivity::class.java)
        context.startActivity(i)
    }

    /**
     * This method make a request to backend for get the data for the bill
     */
    private fun displayBillDetails(){
        val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

        val client = OkHttpClient.Builder()
            .connectionSpecs(specs)
            .build()

        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
            .baseUrl(BASE_URL_6).build().create(ApiInterface::class.java)

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
                var gamesString: String? = "";
                for(g in responseBody.games){
                    gamesString = gamesString + "\nNume: "+ g.nume + "" +
                            "\nYear of Release: " +g.anAparitie +
                            "\nPublisher: "+ g.publisher +
                            "\nPrice: " + g.pret + "$\nGen:" + g.gen
                }
                string = string + gamesString
                billtext?.setText(string)
            }
            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.d(ContentValues.TAG,"onFailure: " +t.message)
            }
        })
    }
}