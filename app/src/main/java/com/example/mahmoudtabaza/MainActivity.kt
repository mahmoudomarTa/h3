package com.example.mahmoudtabaza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class MainActivity : AppCompatActivity() {
    private lateinit var requestQueue:RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signup!!.setOnClickListener {
            val data = "{" +
                    "\"firstName\"" + ":" + "\"" + firstName!!.text.toString() + "\"," +
                    "\"lastName\"" + ":" + "\"" + lastName!!.text.toString() + "\"," +
                    "\"email\"" + ":" + "\"" + email!!.text.toString() + "\"," +
                    "\"password\"" + ":" + "\"" + password!!.text.toString() + "\"" +
                    "}"
            Submit(data)
            getRegToken()
            var i = Intent(this,LoginActivity:: class.java)
            i.putExtra("email",email!!.text.toString())
            i.putExtra("password",password!!.text.toString())
            startActivity(i)
        }

    }

    fun getRegToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful){
                Log.e("dna","Failed to get token ${task.exception}")

                return@addOnCompleteListener
            }
            var token = task.result
            val data = "{" +

                    "\"email\"" + ":" + "\"" + email!!.text.toString() + "\"," +
                    "\"password\"" + ":" + "\"" + password!!.text.toString() + "\"" +
                    "\"reg_token\"" + ":" + "\"" + token!! + "\"," +
                    "}"
            val URL = "https://mcc-users-api.herokuapp.com/add_reg_token"
            requestQueue = Volley.newRequestQueue(applicationContext)
            Log.d("TAG", "requestQueue: $requestQueue")
            val stringRequest: StringRequest = object : StringRequest(Method.PUT, URL, Response.Listener<String?> { response ->
                try {
                    val objres = JSONObject(response)
                    Log.d("TAG", "onResponse: $objres")
                } catch (e: JSONException) {
                    Log.d("TAG", "Server Error ")
                }
            }, Response.ErrorListener { error -> Log.d("TAG", "onErrorResponse: $error") }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return try {
                        Log.d("TAG", "savedata: $data")
                        if (data == null) null else data.toByteArray(charset("utf-8"))
                    } catch (uee: UnsupportedEncodingException) {
                        null
                    }!!
                }
            }
            requestQueue!!.add(stringRequest)
            Log.e("dnaTok",token!!)

        }
    }

    private fun Submit(data: String) {
        val URL = "https://mcc-users-api.herokuapp.com/add_new_user"
        requestQueue = Volley.newRequestQueue(applicationContext)
        Log.d("TAG", "requestQueue: $requestQueue")
        val stringRequest: StringRequest = object : StringRequest(Request.Method.POST, URL, Response.Listener<String?> { response ->
            try {
                val objres = JSONObject(response)
                Log.d("TAG", "onResponse: $objres")
            } catch (e: JSONException) {
                Log.d("TAG", "Server Error ")
            }
        }, Response.ErrorListener { error -> Log.d("TAG", "onErrorResponse: $error") }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return try {
                    Log.d("TAG", "savedata: $data")
                    if (data == null) null else data.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    null
                }!!
            }
        }
        requestQueue!!.add(stringRequest)
    }
}