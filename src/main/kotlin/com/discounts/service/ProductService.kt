package com.discounts.service

import com.discounts.mongo.client.ProductClient
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertOneResult
import com.product.v1.Product
import kotlinx.coroutines.rx2.await

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface ProductService {
    suspend fun getOne(productId: String): Product
    suspend fun create(product: Product): InsertOneResult
    suspend fun deleteOne(productId: String): DeleteResult
}

@Singleton
class ProductServiceImpl @Inject constructor(
    private val productClient: ProductClient
) : ProductService {
    override suspend fun getOne(productId: String): Product {
        return runCatching {
            productClient.getOne(productId).await()
        }.getOrThrow()
    }

    override suspend fun create(product: Product): InsertOneResult {
        return runCatching {
            val uuid = UUID.randomUUID().toString()

            val productWithId = Product.newBuilder()
                .setId(uuid)
                .setPriceInCents(product.priceInCents)
                .setTitle(product.title)
                .setDescription(product.description)
                .setDiscount(product.discount)
                .build()

            productClient.insertOne(productWithId).await()
        }.getOrThrow()
    }

    override suspend fun deleteOne(productId: String): DeleteResult {
        return runCatching {
            productClient.deleteOne(productId).await()
        }.getOrThrow()
    }
}