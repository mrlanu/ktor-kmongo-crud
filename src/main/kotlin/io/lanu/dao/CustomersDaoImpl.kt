package io.lanu.dao

import com.mongodb.client.result.DeleteResult
import io.ktor.application.*
import io.lanu.models.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection

const val dbName = "travian"

class CustomersDaoImpl(private val mongoClient: CoroutineClient) : ICustomersDao {


    override suspend fun save(customer: Customer) = coroutineScope {
        val def = async(Dispatchers.Default) {
            var storedCustomer : Customer? = null
            val customerId = customer.id
            val isSuccess = getCollection().insertOne(customer).wasAcknowledged()
            if (isSuccess){
                storedCustomer = getCollection().findOneById(customerId)
            } else {
                println("Error")
            }
            storedCustomer
        }
    }

    override suspend fun findAll(): List<Customer> = getCollection().find().toList()

    override suspend fun findOneById(id: String): Customer? = getCollection().findOneById(id)

    override suspend fun removeById(id: String): DeleteResult = getCollection().deleteOneById(id)

    private fun getCollection() : CoroutineCollection<Customer> = mongoClient.getDatabase(dbName).getCollection()
}
