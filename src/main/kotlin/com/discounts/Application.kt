package com.discounts

import io.micronaut.runtime.Micronaut.build
import mu.KLogger
import mu.KotlinLogging

object Application {
	private val logger: KLogger = KotlinLogging.logger {}

	@JvmStatic
	fun main(args: Array<String>) {
		logger.info("Initializing discounts app.")

		build()
			.packages("com.discounts")
			.mainClass(Application.javaClass)
			.start()
	}
}