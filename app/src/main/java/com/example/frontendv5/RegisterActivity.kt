package com.example.frontendv5

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
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
 * This activity implement a page for user registration which contains some fields about the user
 * The BASE_URL_4 is a constant value which is used for communication with backend.
 */
const val BASE_URL_4 = "http://10.0.2.2:8080/"
class RegisterActivity : AppCompatActivity() {
    var username: EditText? = null
    var email: EditText? = null
    var password: EditText? = null
    var address: EditText? = null
    var type: EditText? = null
    var register: Button? = null

    /**
     * This is a method which is called when this activity is created.
     * Also in this method the fields are find and a button listener is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        username = findViewById(R.id.username);
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        address = findViewById(R.id.address)
        type = findViewById(R.id.type)
        register = findViewById(R.id.register_user) as Button

        register!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                checkData();
            }
        })
    }

    /**
     * This method check the values of fields and validate it. If all the fields are not empty user can register,
     * else an error message will be displayed
     */
    private fun checkData() {
        var redirect: Boolean = true;
        if (isEmpty(username)) {
            username!!.setError("Enter an username!");
            redirect = false
        }
        if (isEmail(email) == false) {
            email!!.setError("Enter valid email!");
            redirect = false
        }
        if(isEmpty(password)){
            password!!.setError("Enter a pasword!");
            redirect = false
        }
        if(isEmpty(address)){
            address!!.setError("Enter an address!");
            redirect = false
        }
        var user = UserFactory(Integer(2),
            username?.text.toString(),
            email?.text.toString(),
            password?.text.toString(),
            address?.text.toString(),
            Integer.parseInt(type?.text.toString()));
        if(redirect){
            inregisterUser(user)
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    /**
     * This method send the data to database and create the user.
     * Also in this method is called API request to create user
     */
    private fun inregisterUser(user: UserFactory) {
        val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

        val client = OkHttpClient.Builder()
            .connectionSpecs(specs)
            .build()

        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
            .baseUrl(BASE_URL_4).build().create(ApiInterface::class.java)

        if(type?.text.toString().equals("0")){
            val retrofitData = retrofitBuilder.registerUser(tip = EnumUsers.BaseUser, post = user)
            retrofitData.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User?>,
                    response: Response<User?>
                ) {
                    val responseBody = response.body()!!
                }
                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Log.d(ContentValues.TAG,"onFailure: " +t.message)
                }
            })
        }else if(type?.text.toString().equals("1")){
             val retrofitData = retrofitBuilder.registerUser(tip = EnumUsers.PremiumUser, post = user)
            retrofitData.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User?>,
                    response: Response<User?>
                ) {
                    val responseBody = response.body()!!
                }
                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Log.d(ContentValues.TAG,"onFailure: " +t.message)
                }
            })
        }else if(type?.text.toString().equals("2")){
             val retrofitData = retrofitBuilder.registerUser(tip = EnumUsers.AdminUser, post = user)
            retrofitData.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User?>,
                    response: Response<User?>
                ) {
                    val responseBody = response.body()!!
                }
                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Log.d(ContentValues.TAG,"onFailure: " +t.message)
                }
            })
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
     * Method to verify if an email field is a valid input
     */
    fun isEmail(text: EditText?): Boolean {
        val email: CharSequence = text?.text.toString()
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
