package cr.ac.utn.appmovil.products

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import cr.ac.utn.appmovil.products.Service.APIService
import cr.ac.utn.appmovil.products.model.AuthRequest
import cr.ac.utn.appmovil.products.model.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvMessage: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupListeners()
        checkIfAlreadyLoggedIn()
    }

    private fun initializeViews() {
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvMessage = findViewById(R.id.tvMessage)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInput(username, password)) {
                performLogin(username, password)
            }
        }
    }

    private fun checkIfAlreadyLoggedIn() {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            navigateToMainActivity()
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        return when {
            username.isEmpty() -> {
                showMessage("Please enter username", true)
                etUsername.requestFocus()
                false
            }
            password.isEmpty() -> {
                showMessage("Please enter password", true)
                etPassword.requestFocus()
                false
            }
            else -> true
        }
    }

    private fun performLogin(username: String, password: String) {
        showLoading(true)
        hideMessage()

        val authRequest = AuthRequest(username, password)

        APIService.apiPeople.authenticateUser(authRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                showLoading(false)

                if (response.isSuccessful) {
                    val authResponse = response.body()

                    if (authResponse != null) {
                        handleAuthResponse(authResponse)
                    } else {
                        showMessage("Empty response from server", true)
                    }
                } else {
                    showMessage("Error: ${response.code()} - ${response.message()}", true)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                showLoading(false)
                showMessage("Connection error: ${t.message ?: "Unknown error"}", true)
            }
        })
    }

    private fun handleAuthResponse(response: AuthResponse) {
        when (response.responseCode) {
            "SUCESSFUL", "INFO_FOUND" -> {
                showMessage(response.message, false)

                response.data?.let { user ->
                    saveUserData(user.user, user.name, user.lastname, user.email)
                }

                navigateToMainActivity()
            }
            "INFO_NOT_FOUND" -> {
                showMessage(response.message, true)
            }
            "MISSING_PARAMETERS" -> {
                showMessage("Missing required parameters", true)
            }
            else -> {
                showMessage(response.message, true)
            }
        }
    }

    private fun saveUserData(username: String, name: String, lastname: String, email: String) {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("username", username)
            putString("name", name)
            putString("lastname", lastname)
            putString("email", email)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnLogin.isEnabled = !show
        etUsername.isEnabled = !show
        etPassword.isEnabled = !show
    }

    private fun showMessage(message: String, isError: Boolean) {
        tvMessage.text = message
        tvMessage.setTextColor(
            if (isError) getColor(android.R.color.holo_red_dark)
            else getColor(android.R.color.holo_green_dark)
        )
        tvMessage.visibility = View.VISIBLE
    }

    private fun hideMessage() {
        tvMessage.visibility = View.GONE
    }
}