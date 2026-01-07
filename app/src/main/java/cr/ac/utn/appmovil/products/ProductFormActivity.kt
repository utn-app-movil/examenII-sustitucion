package cr.ac.utn.appmovil.products

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import cr.ac.utn.appmovil.products.Service.APIService
import cr.ac.utn.appmovil.products.model.CategoriesResponse
import cr.ac.utn.appmovil.products.model.Category
import cr.ac.utn.appmovil.products.model.Product
import cr.ac.utn.appmovil.products.model.ProductRequest
import cr.ac.utn.appmovil.products.model.ProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductFormActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var etName: TextInputEditText
    private lateinit var spinnerCategory: AutoCompleteTextView
    private lateinit var etPrice: TextInputEditText
    private lateinit var etQuantity: TextInputEditText
    private lateinit var ivProductImage: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var tvMessage: TextView
    private lateinit var progressBar: ProgressBar

    private var productId: Int? = null
    private var currentProduct: Product? = null
    private var categories: List<String> = emptyList()
    private var selectedImageBase64: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_form)

        initializeViews()
        loadCategories()
        checkEditMode()
        setupListeners()
    }

    /**
     * Initialize all UI components
     */
    private fun initializeViews() {
        tvTitle = findViewById(R.id.tvTitle)
        etName = findViewById(R.id.etName)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        etPrice = findViewById(R.id.etPrice)
        etQuantity = findViewById(R.id.etQuantity)
        ivProductImage = findViewById(R.id.ivProductImage)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
        tvMessage = findViewById(R.id.tvMessage)
        progressBar = findViewById(R.id.progressBar)
    }

    /**
     * Check if editing existing product
     */
    private fun checkEditMode() {
        productId = intent.getIntExtra(ProductListActivity.EXTRA_PRODUCT_ID, -1)

        if (productId != -1) {
            tvTitle.text = "Edit Product"
            loadProductData(productId!!)
        } else {
            tvTitle.text = "Add Product"
        }
    }

    /**
     * Load categories from API
     */
    private fun loadCategories() {
        APIService.apiPeople.getAllCategories().enqueue(object : Callback<CategoriesResponse> {
            override fun onResponse(call: Call<CategoriesResponse>, response: Response<CategoriesResponse>) {
                if (response.isSuccessful) {
                    val categoriesResponse = response.body()

                    when (categoriesResponse?.responseCode) {
                        "INFO_FOUND", "SUCESSFUL" -> {
                            val categoryList = categoriesResponse.data?.map { it.categoria } ?: emptyList()
                            setupCategorySpinner(categoryList)
                        }
                        else -> {
                            // Use default categories if API fails
                            setupCategorySpinner(getDefaultCategories())
                        }
                    }
                } else {
                    setupCategorySpinner(getDefaultCategories())
                }
            }

            override fun onFailure(call: Call<CategoriesResponse>, t: Throwable) {
                setupCategorySpinner(getDefaultCategories())
            }
        })
    }

    /**
     * Setup category spinner with data
     */
    private fun setupCategorySpinner(categoryList: List<String>) {
        categories = categoryList
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        spinnerCategory.setAdapter(adapter)
    }

    /**
     * Get default categories as fallback
     */
    private fun getDefaultCategories(): List<String> {
        return listOf(
            "Electrónica",
            "Hogar",
            "Ferretería",
            "Cosmético",
            "Oficina",
            "Limpieza",
            "Otros"
        )
    }

    /**
     * Load product data for editing
     */
    private fun loadProductData(id: Int) {
        showLoading(true)

        APIService.apiPeople.getProductById(id).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                showLoading(false)

                if (response.isSuccessful) {
                    val productResponse = response.body()

                    when (productResponse?.responseCode) {
                        "INFO_FOUND", "SUCESSFUL" -> {
                            currentProduct = productResponse.data
                            fillFormWithProductData(currentProduct!!)
                        }
                        else -> {
                            showError(productResponse?.message ?: "Error loading product")
                            finish()
                        }
                    }
                } else {
                    showError("Error: ${response.code()}")
                    finish()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                showLoading(false)
                showError("Connection error: ${t.message}")
                finish()
            }
        })
    }

    /**
     * Fill form with product data
     */
    private fun fillFormWithProductData(product: Product) {
        etName.setText(product.nombre)
        spinnerCategory.setText(product.categoria, false)
        etPrice.setText(product.precio.toString())
        etQuantity.setText(product.cantidad.toString())

        // TODO: Load image if available
    }

    /**
     * Setup click listeners
     */
    private fun setupListeners() {
        btnSave.setOnClickListener {
            saveProduct()
        }

        btnCancel.setOnClickListener {
            finish()
        }

        btnSelectImage.setOnClickListener {
            // TODO: Implement image picker
            showMessage("Image selection coming soon", false)
        }
    }

    /**
     * Validate and save product
     */
    private fun saveProduct() {
        if (!validateForm()) {
            return
        }

        val productRequest = createProductRequest()

        if (productId != null && productId != -1) {
            updateProduct(productId!!, productRequest)
        } else {
            createProduct(productRequest)
        }
    }

    /**
     * Validate form fields
     */
    private fun validateForm(): Boolean {
        val name = etName.text.toString().trim()
        val category = spinnerCategory.text.toString().trim()
        val priceText = etPrice.text.toString().trim()
        val quantityText = etQuantity.text.toString().trim()

        when {
            name.isEmpty() -> {
                showError("Please enter product name")
                etName.requestFocus()
                return false
            }
            category.isEmpty() -> {
                showError("Please select a category")
                spinnerCategory.requestFocus()
                return false
            }
            priceText.isEmpty() -> {
                showError("Please enter price")
                etPrice.requestFocus()
                return false
            }
            quantityText.isEmpty() -> {
                showError("Please enter quantity")
                etQuantity.requestFocus()
                return false
            }
            else -> return true
        }
    }

    /**
     * Create ProductRequest object
     */
    private fun createProductRequest(): ProductRequest {
        val name = etName.text.toString().trim()
        val category = spinnerCategory.text.toString().trim()
        val price = etPrice.text.toString().toDoubleOrNull() ?: 0.0
        val quantity = etQuantity.text.toString().toIntOrNull() ?: 0

        return ProductRequest(
            nombre = name,
            categoria = category,
            precio = price,
            cantidad = quantity,
            fotoBase64 = selectedImageBase64
        )
    }

    /**
     * Create new product
     */
    private fun createProduct(productRequest: ProductRequest) {
        showLoading(true)

        APIService.apiPeople.createProduct(productRequest).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                showLoading(false)

                if (response.isSuccessful) {
                    val productResponse = response.body()
                    handleSaveResponse(productResponse, "Product created successfully")
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
     * Update existing product
     */
    private fun updateProduct(id: Int, productRequest: ProductRequest) {
        showLoading(true)

        APIService.apiPeople.updateProduct(id, productRequest).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                showLoading(false)

                if (response.isSuccessful) {
                    val productResponse = response.body()
                    handleSaveResponse(productResponse, "Product updated successfully")
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
     * Handle save response
     */
    private fun handleSaveResponse(response: ProductResponse?, successMessage: String) {
        when (response?.responseCode) {
            "SUCESSFUL" -> {
                showSuccess(successMessage)
                setResult(RESULT_OK)
                finish()
            }
            "DUPLICATE_DATA" -> {
                showError("Product already exists")
            }
            "MISSING_PARAMETERS" -> {
                showError("Missing required fields")
            }
            else -> {
                showError(response?.message ?: "Error saving product")
            }
        }
    }

    /**
     * Show/hide loading indicator
     */
    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnSave.isEnabled = !show
        btnCancel.isEnabled = !show
        btnSelectImage.isEnabled = !show
    }

    /**
     * Show message to user
     */
    private fun showMessage(message: String, isError: Boolean) {
        tvMessage.text = message
        tvMessage.setTextColor(
            if (isError) getColor(android.R.color.holo_red_dark)
            else getColor(android.R.color.holo_green_dark)
        )
        tvMessage.visibility = View.VISIBLE
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
}