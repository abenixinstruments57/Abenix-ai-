package com.example.data.Local

import android.content.Context
import com.example.data.Model.AbenixProduct
import org.json.JSONArray
import org.json.JSONObject

class AbenixProductStore(context: Context) {

    private val preferences = context.getSharedPreferences(
        "abenix_products",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val PRODUCTS_KEY = "products"
    }

    fun getProducts(): List<AbenixProduct> {
        val json = preferences.getString(PRODUCTS_KEY, null)
            ?: return emptyList()

        return try {
            val array = JSONArray(json)

            buildList {
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)

                    add(
                        AbenixProduct(
                            id = item.optString("id"),
                            name = item.optString("name"),
                            category = item.optString("category"),
                            description = item.optString("description"),
                            price = item.optString("price"),
                            currency = item.optString("currency", "USD"),
                            available = item.optBoolean("available", true)
                        )
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveProducts(products: List<AbenixProduct>) {
        val array = JSONArray()

        products.forEach { product ->
            val item = JSONObject()

            item.put("id", product.id)
            item.put("name", product.name)
            item.put("category", product.category)
            item.put("description", product.description)
            item.put("price", product.price)
            item.put("currency", product.currency)
            item.put("available", product.available)

            array.put(item)
        }

        preferences.edit()
            .putString(PRODUCTS_KEY, array.toString())
            .apply()
    }

    fun addProduct(product: AbenixProduct) {
        val products = getProducts().toMutableList()
        products.add(product)
        saveProducts(products)
    }

    fun updateProduct(product: AbenixProduct) {
        val products = getProducts().toMutableList()

        val index = products.indexOfFirst {
            it.id == product.id
        }

        if (index >= 0) {
            products[index] = product
            saveProducts(products)
        }
    }

    fun deleteProduct(productId: String) {
        val products = getProducts()
            .filterNot { it.id == productId }

        saveProducts(products)
    }
}
