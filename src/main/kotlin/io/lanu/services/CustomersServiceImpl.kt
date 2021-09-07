package io.lanu.services

import com.mongodb.client.result.DeleteResult
import io.lanu.dao.ICustomersDao
import io.lanu.models.Customer

class CustomersServiceImpl(private val customersDao: ICustomersDao) : ICustomersService {
    override suspend fun saveCustomer(customer: Customer) = customersDao.save(customer)

    override suspend fun findAll(): List<Customer> = customersDao.findAll()

    override suspend fun findOneById(id: String): Customer? = customersDao.findOneById(id)

    override suspend fun deleteById(id: String): DeleteResult = customersDao.removeById(id)
}


