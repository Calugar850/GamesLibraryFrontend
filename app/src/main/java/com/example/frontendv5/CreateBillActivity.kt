package com.example.frontendv5

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

/**
 * This activity implement a page form for creating a bill
 */
class CreateBillActivity : AppCompatActivity() {

    var nameUser: EditText? = null
    var address: EditText? = null
    var sum: EditText? = null
    var submit: Button? = null

    /**
     * This is a method which is called when this activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_bill)

        nameUser = findViewById(R.id.name);
        address = findViewById(R.id.address)
        sum = findViewById(R.id.total)
        submit = findViewById(R.id.submit_bill) as Button

        submit!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                submitBill();
            }
        })
    }

    /**
     * Method for redirect user to main page
     */
    private fun submitBill() {
        val i = Intent(this, FirstActivity::class.java)
        startActivity(i)
    }
}