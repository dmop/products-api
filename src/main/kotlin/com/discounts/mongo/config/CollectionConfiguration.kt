package com.discounts.mongo.config

import com.mongodb.reactivestreams.client.MongoClients
import io.github.gaplotech.PBCodecProvider
import io.micronaut.context.annotation.EachProperty
import io.micronaut.context.annotation.Parameter
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry

@Suppress("unused")
@EachProperty("mongodb.servers.discounts.collections")
class CollectionConfiguration constructor(@param:Parameter val name: String) {
    lateinit var database: String
    lateinit var collection: String

    // Codec registry to be used by the collection clients for Protobuffers on
    // MongoDB (https://github.com/gaplotech/kotlin-protobuf-bson-codec)
    val registry: CodecRegistry = CodecRegistries.fromRegistries(
        CodecRegistries.fromProviders(PBCodecProvider(includingDefaultValueFields = false)),
        MongoClients.getDefaultCodecRegistry()
    )
}
