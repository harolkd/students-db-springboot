package com.unisalle.backend.exception

// error 404
class ResourceNotFoundException : Exception {
    constructor(message: String) : super(message)
}