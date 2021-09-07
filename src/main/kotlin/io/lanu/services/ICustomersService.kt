package io.lanu.services

import com.mongodb.client.result.DeleteResult
import io.lanu.models.Customer

interface ICustomersService {
    suspend fun saveCustomer(customer: Customer)
    suspend fun findAll(): List<Customer>
    suspend fun findOneById(id: String): Customer?
    suspend fun deleteById(id: String): DeleteResult
}
