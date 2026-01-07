package cr.ac.utn.appmovil.products.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.products.R
import cr.ac.utn.appmovil.products.model.Product

class ProductAdapter(
    private var products: List<Product>,
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    /**
     * ViewHolder for product items
     */
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProductImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        // Set product data
        holder.tvProductName.text = product.nombre
        holder.tvCategory.text = product.categoria
        holder.tvPrice.text = "$${String.format("%.2f", product.precio)}"
        holder.tvQuantity.text = "Qty: ${product.cantidad}"

        // Load product image (placeholder for now)
        // TODO: Load image from URL or base64 if available
        holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground)

        // Set click listeners
        holder.btnEdit.setOnClickListener {
            onEditClick(product)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(product)
        }

        // Optional: Click on entire item to edit
        holder.itemView.setOnClickListener {
            onEditClick(product)
        }
    }

    /**
     * Return the size of the dataset
     */
    override fun getItemCount(): Int = products.size

    /**
     * Update the list of products
     */
    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    /**
     * Filter products by search query
     */
    fun filterProducts(query: String, allProducts: List<Product>) {
        val filteredList = if (query.isEmpty()) {
            allProducts
        } else {
            allProducts.filter { product ->
                product.nombre.contains(query, ignoreCase = true) ||
                        product.categoria.contains(query, ignoreCase = true)
            }
        }
        updateProducts(filteredList)
    }
}