package com.discounts.mongo

import com.discounts.mongo.config.CollectionConfiguration
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoDatabase
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import javax.inject.Named

/**
 * Factory to create the MongoDB database client.
 *
 * @Named with the client name.
 *
 * @param MongoClient database client that connects with the mongodb cluster
 * @param CollectionConfiguration dataClass with redispatch database info
 */
@Factory
class DatabaseFactory {
    @Bean
    @Named("discountsProductDatabase")
    @Requires(classes = [CollectionConfiguration::class])
    fun discountsProductDatabase(
        @Named("discounts") mongoClient: MongoClient,
        @Named("product") productConfig: CollectionConfiguration
    ): MongoDatabase {
        // coded registry using a provider that allows protobuffers as an mongodb entry.
        return mongoClient.getDatabase(productConfig.database)
    }
}
