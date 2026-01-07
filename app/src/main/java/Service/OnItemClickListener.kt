package Service

import model.Product

interface OnItemClickListener {
    fun onItemClicked (product: Product)
}