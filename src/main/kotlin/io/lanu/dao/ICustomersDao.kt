package io.lanu.dao

import com.mongodb.client.result.DeleteResult
import io.lanu.models.Customer

interface ICustomersDao {
    suspend fun save(customer: Customer)
    suspend fun findAll(): List<Customer>
    suspend fun findOneById(id: String): Customer?
    suspend fun removeById(id: String): DeleteResult
}
