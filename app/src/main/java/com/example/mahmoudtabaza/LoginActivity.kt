package com.example.mahmoudtabaza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class LoginActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        if (intent != null) {
            var em = intent.getStringExtra("email").toString()
            var pass = intent.getStringExtra("password").toString()
            login!!.setOnClickListener {
                val data = "{" +
                        "\"email\"" + ":" + "\"" + email!!.text.toString() + "\"," +
                        "\"password\"" + ":" + "\"" + password!!.text.toString() + "\"" +
                        "}"
                if (email!!.text.toString() == em && password!!.text.toString() == pass) {
                    Submit(data)
                } else {
                    Toast.makeText(this, "No data", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
    private fun Submit(data: String) {

        val URL = "https://mcc-users-api.herokuapp.com/login"
        requestQueue = Volley.newRequestQueue(applicationContext)
        Log.d("TAG", "requestQueue: $requestQueue")
        val stringRequest: StringRequest = object :
            StringRequest(Request.Method.POST, URL, Response.Listener<String?> { response ->
                try {
                    val objres = JSONObject(response)

                    Log.d("TAG", "onResponse: $objres")
                    Toast.makeText(this, "onResponse: $objres", Toast.LENGTH_LONG).show()
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