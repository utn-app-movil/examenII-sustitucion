package cr.ac.utn.appmovil.products

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnViewProducts: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        loadUserData()
        setupListeners()
    }

    private fun initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome)
        btnViewProducts = findViewById(R.id.btnViewProducts)
    }

    private fun loadUserData() {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "User")
        tvWelcome.text = "Welcome, $name!"
    }

    private fun setupListeners() {
        btnViewProducts.setOnClickListener {
            navigateToProducts()
        }
    }

    private fun navigateToProducts() {
        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
    }
}