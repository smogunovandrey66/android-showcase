package com.smogunov.showcase.core.network

import java.io.IOException

/** Single exception type for every network-layer failure (HTTP errors, malformed payloads, I/O). */
class NetworkException(message: String?, cause: Throwable? = null) : IOException(message, cause)
