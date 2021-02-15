package com.discounts.mongo.client

import com.discounts.mongo.config.CollectionConfiguration
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.result.DeleteResult
import com.product.v1.Product
import com.mongodb.client.result.InsertOneResult
import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

interface ProductClient {
    val collection: MongoCollection<Product>
    fun getOne(productId: String): Single<Product>
    fun insertOne(product: Product): Single<InsertOneResult>
    fun deleteOne(productId: String): Single<DeleteResult>
}

@Singleton
class ProductClientImpl @Inject constructor(
    @Named("discountsProductDatabase") private val discountsDatabase: MongoDatabase,
    @Named("product") private val config: CollectionConfiguration
) : ProductClient {
    override val collection: MongoCollection<Product> = discountsDatabase
        .getCollection(config.collection)
        .withCodecRegistry(config.registry)
        .withDocumentClass(Product::class.java)

    override fun getOne(productId: String): Single<Product> {
        val query = eq(ID_FIELD, productId)
        return Single.fromPublisher(collection.find(query))
    }

    override fun insertOne(product: Product): Single<InsertOneResult> {
        return Single.fromPublisher(collection.insertOne(product))
    }

    override fun deleteOne(productId: String): Single<DeleteResult> {
        val query = eq(ID_FIELD, productId)
        return Single.fromPublisher(collection.deleteOne(query))
    }

    companion object {
        val ID_FIELD: String = Product.getDescriptor()
            .findFieldByNumber(Product.ID_FIELD_NUMBER).jsonName
    }
}
