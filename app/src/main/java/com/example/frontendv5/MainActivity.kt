package com.example.frontendv5

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
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
 * This activity implement a page for user details which contains some data about user, list of games and bills of user
 * The BASE_URL_5 is a constant value which is used for communication with backend.
 * The USERID is a var to memorize the value user which is connected
 * The TYPE is a var  to memorize type of user
 */
var USERID : Integer = Integer(0)
var TYPE: Int = 0
const val BASE_URL_5 = "http://10.0.2.2:8080/"
class MainActivity : AppCompatActivity() {
    var username: EditText? = null
    var password: EditText? = null
    var type: EditText? = null
    var register: Button? = null
    var login: Button? = null

    /**
     * This is a method which is called when this activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI();
        setupListeners();
    }

    /**
     * Method for find the buttons of this activity
     */
    private fun setupUI() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        type = findViewById(R.id.type)
        register = findViewById(R.id.register_user) as Button
        login = findViewById(R.id.login_user) as Button
    }

    /**
     * Method to instantiate the listeners for buttons
     */
    private fun setupListeners() {
        login!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                checkUsername();
            }
        })

        register!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                redirectToLogIn()
            }
        })
    }

    /**
     * This method redirect user registration page of app
     */
    private fun redirectToLogIn() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    /**
     * This method check the data introduce by user, validate the fields and make a request to backend to verify
     * if the data are valid.
     */
    private fun checkUsername() {
        if (isEmpty(username.toString())) {
            username?.setError("You must enter username to login!")
        }
        if (isEmpty(password.toString())) {
            password?.setError("You must enter password to login!")
        } else {
            if (password?.toString()?.length ?: 0 < 4) {
                password?.setError("Password must be at least 4 chars long!")
            }
        }
        val usernameValue: String = username?.getText().toString()
        val passwordValue: String = password?.getText().toString()
        val type: Int = Integer.parseInt(type?.text.toString())
        var user = UserFactory(Integer(2),
            usernameValue,
            "",
            passwordValue,
            "",
            type);
        val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

        val client = OkHttpClient.Builder()
            .connectionSpecs(specs)
            .build()

        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
            .baseUrl(BASE_URL_5).build().create(ApiInterface::class.java)
        println(type)
        if(type==0){
            TYPE =type
            val retrofitData = retrofitBuilder.loginUser(tip = EnumUsers.BaseUser, post = user)

            retrofitData.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User?>,
                    response: Response<User?>
                ) {
                    val responseBody = response.body()!!
                    USERID = responseBody.idUser
                    if(usernameValue.equals(responseBody.username) && passwordValue.equals(responseBody.parola)){
                        redirect()
                    }
                }
                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Log.d(ContentValues.TAG,"onFailure: " +t.message)
                }
            })

        }else if(type == 1){
            TYPE =type
            val retrofitData = retrofitBuilder.loginUser(tip = EnumUsers.PremiumUser, post = user)

            retrofitData.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User?>,
                    response: Response<User?>
                ) {
                    val responseBody = response.body()!!
                    USERID = responseBody.idUser
                    if(usernameValue.equals(responseBody.username) && passwordValue.equals(responseBody.parola)){
                        redirect()
                    }
                }
                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Log.d(ContentValues.TAG,"onFailure: " +t.message)
                }
            })

        }else if(type==2){
            TYPE =type
            val retrofitData = retrofitBuilder.loginUser(tip = EnumUsers.AdminUser, post = user)

            retrofitData.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User?>,
                    response: Response<User?>
                ) {
                    val responseBody = response.body()!!
                    USERID = responseBody.idUser
                    if(usernameValue.equals(responseBody.username) && passwordValue.equals(responseBody.parola)){
                        redirect()
                    }
                }
                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Log.d(ContentValues.TAG,"onFailure: " +t.message)
                }
            })
        }
    }

    /**
     * This method redirect user to main page of app
     */
    fun redirect(){
        val i = Intent(this, FirstActivity::class.java)
        startActivity(i)
    }

}