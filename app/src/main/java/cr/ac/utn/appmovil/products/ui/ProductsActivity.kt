package cr.ac.utn.appmovil.products.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.utn.appmovil.products.databinding.ActivityProductsBinding
import cr.ac.utn.appmovil.products.viewmodel.ProductViewModel
import com.google.gson.Gson

class ProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductsBinding
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, ProductFormActivity::class.java))
        }

        viewModel.products.observe(this) { products ->
            adapter.updateList(products)
        }

        viewModel.statusMessage.observe(this) { response ->
            if (response.responseCode != "INFO_FOUND") {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startPolling()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopPolling()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(emptyList(), 
            onEditClick = { product ->
                val intent = Intent(this, ProductFormActivity::class.java)
                intent.putExtra("product_json", Gson().toJson(product)) 
                startActivity(intent)
            },
            onDeleteClick = { product ->
                product.id?.let { viewModel.deleteProduct(it) }
            }
        )
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter
    }
}
