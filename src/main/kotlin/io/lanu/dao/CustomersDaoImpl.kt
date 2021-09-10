package io.lanu.dao

import com.mongodb.client.result.DeleteResult
import io.ktor.application.*
import io.lanu.models.Customer
import io.lanu.models.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

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

    override suspend fun registerUser(username: String, hashedPass: String) {
        mongoClient.getDatabase(dbName).getCollection<User>().insertOne(User(username, hashedPass))
    }

    override suspend fun findUserByUsername(userRequest: LoginRequest): User? =
        mongoClient.getDatabase(dbName).getCollection<User>().findOne(User::username eq userRequest.username)

    private fun getCollection() : CoroutineCollection<Customer> = mongoClient.getDatabase(dbName).getCollection()
}

data class User(val username: String, val pass: String)
