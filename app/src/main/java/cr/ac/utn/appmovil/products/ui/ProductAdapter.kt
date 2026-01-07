package cr.ac.utn.appmovil.products.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.products.databinding.ItemProductBinding
import cr.ac.utn.appmovil.products.model.Product

class ProductAdapter(
    private var products: List<Product>,
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(product: Product) {
            binding.tvName.text = product.name
            binding.tvCategory.text = product.category
            binding.tvPrice.text = "$${product.price}"
            binding.tvQuantity.text = "Qty: ${product.quantity}"
            
            binding.btnEdit.setOnClickListener { onEditClick(product) }
            binding.btnDelete.setOnClickListener { onDeleteClick(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    fun updateList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
