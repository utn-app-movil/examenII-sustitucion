package cr.ac.utn.appmovil.products

import Service.OnItemClickListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import model.Product

class CustomViewHolder (view: View): RecyclerView.ViewHolder(view){
    var txtNombre: TextView = view.findViewById(R.id.txtProductNameItem_recycler)
    var txtCategoria: TextView = view.findViewById(R.id.txtCategoryItem_recycler)
    var txtPrecio: TextView = view.findViewById(R.id.txtPriceItem_recycler)
    var imgPhoto: ImageView = view.findViewById(R.id.imgPhoto_ItemRecycler)

    fun bind (item: Product, clickListener: OnItemClickListener){
        txtNombre.setText(item.Nombre.toString())
        txtCategoria.setText(item.Categoria.toString())
        txtPrecio.setText(item.Precio.toString())
        //imgPhoto.setImageBitmap(item.Photo)

        itemView.setOnClickListener{
            clickListener.onItemClicked(item)
        }
    }
}

class ProductListAdapter (private var itemList: List<Product>, val itemClickListener: OnItemClickListener): RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_product, parent, false)
        return CustomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        var item = itemList[position]
        holder.bind(item, itemClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}