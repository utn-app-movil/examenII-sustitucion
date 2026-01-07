package controller

import Service.APIService
import android.content.Context
import android.util.Log
import model.DTOProduct
import model.Product

class ProductController {

    private  var context: Context

    constructor(context: Context){
        this.context=context
    }

    private fun getProductObject (item: DTOProduct): Product {
        val product = Product()
        product.Id= item.Id
        product.Nombre= item.Nombre
        product.Categoria= item.Categoria
        product.Precio= item.Precio
        product.Cantidad= item.Cantidad
        product.FotoPath= item.FotoPath
        return product
    }

    suspend fun getProduct(): List<Product>{
        var products = mutableListOf<Product>()
        try {
            val response = APIService.apiPeople.getProducts()
            if (response.responseCode != 200)
                throw Exception(response.message)

            response.data.forEach { item ->
                products.add(getProductObject(item))
            }
            Log.d("API_Call", "Success: ${response.data}")
        } catch (e: Exception) {
            // Handle error
            Log.e("API_Call", "Error fetching data: ${e.message}")
            throw Exception(e.message)
        }
        return products
    }


}