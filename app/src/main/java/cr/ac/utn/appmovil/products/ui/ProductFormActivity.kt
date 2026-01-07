package cr.ac.utn.appmovil.products.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.products.R
import cr.ac.utn.appmovil.products.databinding.ActivityProductFormBinding
import cr.ac.utn.appmovil.products.model.Category
import cr.ac.utn.appmovil.products.model.Product
import cr.ac.utn.appmovil.products.viewmodel.ProductViewModel
import com.google.gson.Gson

class ProductFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductFormBinding
    private val viewModel: ProductViewModel by viewModels()
    private var productToEdit: Product? = null
    private var categories: List<Category> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productJson = intent.getStringExtra("product_json")
        if (productJson != null) {
            productToEdit = Gson().fromJson(productJson, Product::class.java)
            populateForm(productToEdit!!)
            binding.btnSave.text = getString(R.string.update_button)
        }

        viewModel.categories.observe(this) { catList ->
            categories = catList
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, catList.map { it.name })
            binding.spCategory.adapter = adapter
            
            productToEdit?.let { product ->
                 val index = catList.indexOfFirst { it.name == product.category }
                 if (index >= 0) binding.spCategory.setSelection(index)
            }
        }
        
        viewModel.statusMessage.observe(this) { response ->
             if (response.responseCode == "SUCESSFUL") {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                finish()
            } else if (response.responseCode != "INFO_FOUND") {
                Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.loadCategories()

        binding.btnSave.setOnClickListener {
            saveProduct()
        }
    }

    private fun populateForm(product: Product) {
        binding.etName.setText(product.name)
        binding.etPrice.setText(product.price.toString())
        binding.etQuantity.setText(product.quantity.toString())
    }

    private fun saveProduct() {
        val name = binding.etName.text.toString()
        val priceStr = binding.etPrice.text.toString()
        val qtyStr = binding.etQuantity.text.toString()
        
        if (name.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_fill_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val category = if (categories.isNotEmpty()) categories[binding.spCategory.selectedItemPosition].name else ""
        
        if (productToEdit == null) {
            val newProduct = Product(
                name = name,
                category = category,
                price = priceStr.toDouble(),
                quantity = qtyStr.toInt()
            )
            viewModel.createProduct(newProduct)
        } else {
             val updatedProduct = productToEdit!!.copy(
                name = name,
                category = category,
                price = priceStr.toDouble(),
                quantity = qtyStr.toInt()
            )
            viewModel.updateProduct(updatedProduct.id!!, updatedProduct)
        }
    }
}
