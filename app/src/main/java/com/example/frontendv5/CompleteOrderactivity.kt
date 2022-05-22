package com.example.frontendv5

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This activity implement a page for complete order
 * The BASE_URL_7 is a constant value which is used for communication with backend.
 */
const val BASE_URL_7 = "http://10.0.2.2:8080/"
class CompleteOrderactivity : AppCompatActivity() {
    var back: Button? = null

    /**
     * This is a method which is called when this activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_orderactivity)

        setupUI();
        setupListeners();
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
                generetaBillPerClient(view)
            }
        })
    }

    /**
     * This method make a request to backend to generate the bill for the client
     */
    private fun generetaBillPerClient(view: View?) {
        if(TYPE==0){
            val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

            val client = OkHttpClient.Builder()
                .connectionSpecs(specs)
                .build()

            val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
                .baseUrl(BASE_URL_7).build().create(ApiInterface::class.java)

            val retrofitData = retrofitBuilder.clientBill(idUser = USERID, tip = EnumUsers.BaseUser)
            retrofitData.enqueue(object : Callback<List<Bill>> {
                override fun onResponse(
                    call: Call<List<Bill>?>,
                    response: Response<List<Bill>?>
                ) {
                    redirect(view)
                }
                override fun onFailure(call: Call<List<Bill>?>, t: Throwable) {
                    Log.d(ContentValues.TAG,"onFailure: " +t.message)
                }
            })
        }else if(TYPE==1){
            val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

            val client = OkHttpClient.Builder()
                .connectionSpecs(specs)
                .build()

            val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
                .baseUrl(BASE_URL_7).build().create(ApiInterface::class.java)

            val retrofitData = retrofitBuilder.clientBill(idUser = USERID, tip = EnumUsers.PremiumUser)
            retrofitData.enqueue(object : Callback<List<Bill>> {
                override fun onResponse(
                    call: Call<List<Bill>?>,
                    response: Response<List<Bill>?>
                ) {
                    redirect(view)
                }
                override fun onFailure(call: Call<List<Bill>?>, t: Throwable) {
                    Log.d(ContentValues.TAG,"onFailure: " +t.message)
                }
            })

        }else if(TYPE==2){
            val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

            val client = OkHttpClient.Builder()
                .connectionSpecs(specs)
                .build()

            val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
                .baseUrl(BASE_URL_7).build().create(ApiInterface::class.java)

            val retrofitData = retrofitBuilder.clientBill(idUser = USERID, tip = EnumUsers.AdminUser)
            retrofitData.enqueue(object : Callback<List<Bill>> {
                override fun onResponse(
                    call: Call<List<Bill>?>,
                    response: Response<List<Bill>?>
                ) {
                    redirect(view)
                }
                override fun onFailure(call: Call<List<Bill>?>, t: Throwable) {
                    Log.d(ContentValues.TAG,"onFailure: " +t.message)
                }
            })
        }
    }

    /**
     * Method to redirect user to main page of the app
     */
    private fun redirect(view: View?) {
        val context: Context = view!!.getContext()
        val i = Intent(context, FirstActivity::class.java)
        context.startActivity(i)
    }


}