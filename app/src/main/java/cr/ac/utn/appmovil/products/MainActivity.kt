package cr.ac.utn.appmovil.products

import Service.APIService
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import model.DTOLogin
import model.LoginRequest
import util.util

class MainActivity : AppCompatActivity() {
    lateinit var txtUsername: EditText
    lateinit var txtPassword: EditText
    lateinit var btnLogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            login()
        }



    }

    private fun login(){
        var mycontext = this
        lifecycleScope.launch {
            try {
                txtPassword = findViewById(R.id.txtPassword)
                txtUsername = findViewById(R.id.txtUsername)
                val response = APIService.apiPeople.authUser(convertToDTO())
                if (response.responseCode.equals("INFO_FOUND")) {
                    Log.e("API CALL", "WE GOOOOD")
                    util.openActivity(mycontext, HomeActivity::class.java)

                }
            }catch (e: Exception){
                throw e
            }
        }
    }

    private fun convertToDTO(): DTOLogin{
        txtPassword = findViewById(R.id.txtPassword)
        txtUsername = findViewById(R.id.txtUsername)

        return DTOLogin(txtUsername.text.toString().trim(), txtPassword.text.toString().trim())
    }
}