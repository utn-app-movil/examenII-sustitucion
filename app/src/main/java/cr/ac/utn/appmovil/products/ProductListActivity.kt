package cr.ac.utn.appmovil.products

import Service.OnItemClickListener
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import controller.ProductController
import kotlinx.coroutines.launch
import model.Product

//private val ProductListActivity.EXTRA_MESSAGE_PERSONID: String

class ProductListActivity: AppCompatActivity(), OnItemClickListener {
    private lateinit var customAdapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.productList)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recycler =  findViewById<RecyclerView>(R.id.rvperson)
        val productController = ProductController(this)
        val mycontext = this

        lifecycleScope.launch {
            customAdapter = ProductListAdapter(productController.getProduct(), mycontext)
            val layoutManager = LinearLayoutManager(applicationContext)
            recycler.layoutManager = layoutManager
            recycler.adapter = customAdapter
            customAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemClicked(product: Product) {
        //util.util.openActivity(this, ProductActivity::class.java, EXTRA_MESSAGE_PERSONID, product.Id)
        //Toast.makeText(this,"Person name ${person.FullName()} \n Phone:${person.Phone.toString()}"
        //    ,Toast.LENGTH_LONG).show()
        //Log.i("CONTACT", contact.FullName)
    }
}