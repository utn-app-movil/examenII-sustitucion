package cr.ac.utn.appmovil.products

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import cr.ac.utn.appmovil.products.Service.APIService
import cr.ac.utn.appmovil.products.adapter.ProductAdapter
import cr.ac.utn.appmovil.products.model.Product
import cr.ac.utn.appmovil.products.model.ProductResponse
import cr.ac.utn.appmovil.products.model.ProductsResponse
import cr.ac.utn.appmovil.products.util.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var fabAddProduct: FloatingActionButton
    private lateinit var btnLogout: Button
    private lateinit var btnSearch: Button
    private lateinit var etSearch: EditText
    private lateinit var emptyState: LinearLayout
    private lateinit var progressBar: ProgressBar

    private var allProducts: List<Product> = emptyList()

    companion object {
        const val EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID"
        const val REQUEST_CODE_ADD = 1001
        const val REQUEST_CODE_EDIT = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        initializeViews()
        setupRecyclerView()
        setupListeners()
        loadProducts()
    }

    override fun onResume() {
        super.onResume()
        // Reload products when returning from form
        loadProducts()
    }

    /**
     * Initialize all UI components
     */
    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewProducts)
        fabAddProduct = findViewById(R.id.fabAddProduct)
        btnLogout = findViewById(R.id.btnLogout)
        btnSearch = findViewById(R.id.btnSearch)
        etSearch = findViewById(R.id.etSearch)
        emptyState = findViewById(R.id.emptyState)
        progressBar = findViewById(R.id.progressBar)
    }

    /**
     * Setup RecyclerView with adapter
     */
    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            products = emptyList(),
            onEditClick = { product ->
                navigateToEditProduct(product)
            },
            onDeleteClick = { product ->
                showDeleteConfirmation(product)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    /**
     * Setup click listeners
     */
    private fun setupListeners() {
        fabAddProduct.setOnClickListener {
            navigateToAddProduct()
        }

        btnLogout.setOnClickListener {
            performLogout()
        }

        btnSearch.setOnClickListener {
            filterProducts()
        }

        etSearch.setOnEditorActionListener { _, _, _ ->
            filterProducts()
            true
        }
    }

    /**
     * Load all products from API
     */
    private fun loadProducts() {
        showLoading(true)

        APIService.apiPeople.getAllProducts().enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(call: Call<ProductsResponse>, response: Response<ProductsResponse>) {
                showLoading(false)

                if (response.isSuccessful) {
                    val productsResponse = response.body()
                    handleProductsResponse(productsResponse)
                } else {
                    showError("Error loading products: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                showLoading(false)
                showError("Connection error: ${t.message}")
            }
        })
    }

    /**
     * Handle products response from API
     */
    private fun handleProductsResponse(response: ProductsResponse?) {
        when (response?.responseCode) {
            "INFO_FOUND", "SUCESSFUL" -> {
                allProducts = response.data ?: emptyList()
                updateProductsList(allProducts)
            }
            "INFO_NOT_FOUND" -> {
                allProducts = emptyList()
                updateProductsList(allProducts)
                showEmptyState(true)
            }
            else -> {
                showError(response?.message ?: "Unknown error")
            }
        }
    }

    /**
     * Update products list in adapter
     */
    private fun updateProductsList(products: List<Product>) {
        adapter.updateProducts(products)

        if (products.isEmpty()) {
            showEmptyState(true)
        } else {
            showEmptyState(false)
        }
    }

    /**
     * Filter products by search query
     */
    private fun filterProducts() {
        val query = etSearch.text.toString().trim()
        adapter.filterProducts(query, allProducts)
    }

    /**
     * Navigate to add product screen
     */
    private fun navigateToAddProduct() {
        val intent = Intent(this, ProductFormActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_ADD)
    }

    /**
     * Navigate to edit product screen
     */
    private fun navigateToEditProduct(product: Product) {
        val intent = Intent(this, ProductFormActivity::class.java)
        intent.putExtra(EXTRA_PRODUCT_ID, product.id)
        startActivityForResult(intent, REQUEST_CODE_EDIT)
    }

    /**
     * Show delete confirmation dialog
     */
    private fun showDeleteConfirmation(product: Product) {
        util.showDialogCondition(
            context = this,
            titleQuestion = "Delete Product",
            questionText = "Are you sure you want to delete '${product.nombre}'?",
            positiveStr = "Delete",
            negativeStr = "Cancel",
            positiveCallback = {
                deleteProduct(product)
            },
            negativeCallback = {
                // Do nothing
            }
        )
    }

    /**
     * Delete product from API
     */
    private fun deleteProduct(product: Product) {
        showLoading(true)

        APIService.apiPeople.deleteProduct(product.id).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                showLoading(false)

                if (response.isSuccessful) {
                    val deleteResponse = response.body()
                    when (deleteResponse?.responseCode) {
                        "SUCESSFUL" -> {
                            showSuccess("Product deleted successfully")
                            loadProducts()
                        }
                        else -> {
                            showError(deleteResponse?.message ?: "Error deleting product")
                        }
                    }
                } else {
                    showError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                showLoading(false)
                showError("Connection error: ${t.message}")
            }
        })
    }

    /**
     * Perform logout
     */
    private fun performLogout() {
        util.showDialogCondition(
            context = this,
            titleQuestion = "Logout",
            questionText = "Are you sure you want to logout?",
            positiveStr = "Yes",
            negativeStr = "No",
            positiveCallback = {
                clearSession()
                navigateToLogin()
            },
            negativeCallback = {
                // Do nothing
            }
        )
    }

    /**
     * Clear user session
     */
    private fun clearSession() {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    /**
     * Navigate to login screen
     */
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    /**
     * Show/hide loading indicator
     */
    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    /**
     * Show/hide empty state
     */
    private fun showEmptyState(show: Boolean) {
        emptyState.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    /**
     * Show error message
     */
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Show success message
     */
    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_ADD, REQUEST_CODE_EDIT -> {
                    loadProducts()
                }
            }
        }
    }
}