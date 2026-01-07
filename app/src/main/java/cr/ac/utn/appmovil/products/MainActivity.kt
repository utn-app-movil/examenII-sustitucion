package cr.ac.utn.appmovil.products

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.products.databinding.ActivityMainBinding
import cr.ac.utn.appmovil.products.ui.ProductsActivity
import cr.ac.utn.appmovil.products.viewmodel.LoginViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(username, password)
            } else {
                Toast.makeText(this, getString(R.string.error_credentials), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnListUsers.setOnClickListener {
            viewModel.loadUsers()
        }

        viewModel.loginResult.observe(this) { response ->
            android.util.Log.d("LOGIN_DEBUG", "Response: ${response?.responseCode} - ${response?.message}")
            if (response != null && (response.responseCode == "SUCCESSFUL" || response.responseCode == "SUCESSFUL" || response.responseCode == "INFO_FOUND")) {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ProductsActivity::class.java))
                finish()
            } else if (response != null) {
                 Toast.makeText(this, "Login Failed: ${response.message}", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.statusMessage.observe(this) { response ->
            // If it's not a successful login (handled above)
            val isSuccess = response.responseCode == "SUCCESSFUL" || response.responseCode == "SUCESSFUL" || response.responseCode == "INFO_FOUND"
            if (!isSuccess) {
                 Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.usersResult.observe(this) { users ->
            val userNames = users.joinToString("\n") { it.username }
            AlertDialog.Builder(this)
                .setTitle("Users List (Dev)")
                .setMessage(if (userNames.isNotEmpty()) userNames else "No users found")
                .setPositiveButton("OK", null)
                .show()
        }
    }
}