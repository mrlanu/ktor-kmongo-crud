package io.lanu.models

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class Customer(
    val firstName: String,
    val lastName: String,
    val email: String = "",
    @BsonId val id: String = UUID.randomUUID().toString())


data class CustomerRequest(
    val firstName: String,
    val lastName: String
)

data class LoginRequest(
    val username: String,
    val password: String
)
