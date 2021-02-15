package com.discounts.grpc.service

import com.discounts.service.ProductService
import com.product.v1.CreateProductRequest
import com.product.v1.CreateProductResponse
import com.product.v1.DeleteProductRequest
import com.product.v1.DeleteProductResponse
import com.product.v1.GetOneProductRequest
import com.product.v1.GetOneProductResponse
import com.product.v1.ProductAPIGrpcKt.ProductAPICoroutineImplBase
import mu.KotlinLogging
import javax.inject.Inject
import javax.inject.Singleton
import io.grpc.Status
import io.grpc.StatusException

@Singleton
@Suppress("unused")
class ProductEndpoint @Inject constructor(
    private val productService: ProductService
) : ProductAPICoroutineImplBase() {
    private val logger = KotlinLogging.logger {}

    override suspend fun getOne(request: GetOneProductRequest): GetOneProductResponse {
        logger.info { "Processing getOne request: $request" }

        return try {
            val product = productService.getOne(request.productId)
            logger.info { "Successfully processed getOne request: $request" }

            GetOneProductResponse
                .newBuilder()
                .setProduct(product)
                .build()
        } catch (ex: Exception) {
            val status = when (ex) {
                is IllegalArgumentException -> Status.INVALID_ARGUMENT.withDescription(ex.message)
                else -> Status.UNKNOWN.withDescription(ex.message)
            }

            throw StatusException(status)
        }
    }

    override suspend fun create(request: CreateProductRequest): CreateProductResponse {
        logger.info { "Processing create request: $request" }

        return try {
            productService.create(request.product)
            logger.info { "Successfully processed create request: $request" }

            CreateProductResponse
                .newBuilder()
                .setProduct(request.product)
                .build()
        } catch (ex: Exception) {
            val status = when (ex) {
                is IllegalArgumentException -> Status.INVALID_ARGUMENT.withDescription(ex.message)
                else -> Status.UNKNOWN.withDescription(ex.message)
            }

            throw StatusException(status)
        }
    }

    override suspend fun delete(request: DeleteProductRequest): DeleteProductResponse {
        logger.info { "Processing delete request: $request" }

        return try {
            productService.deleteOne(request.productId)
            logger.info { "Successfully processed delete request: $request" }

            DeleteProductResponse
                .newBuilder()
                .setMessage("Deleted with success")
                .build()
        } catch (ex: Exception) {
            val status = when (ex) {
                is IllegalArgumentException -> Status.INVALID_ARGUMENT.withDescription(ex.message)
                else -> Status.UNKNOWN.withDescription(ex.message)
            }

            throw StatusException(status)
        }
    }
}