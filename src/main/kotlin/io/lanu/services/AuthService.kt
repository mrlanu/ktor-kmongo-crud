package io.lanu.services

import io.lanu.dao.ICustomersDao
import io.lanu.dao.User
import io.lanu.models.LoginRequest
import org.mindrot.jbcrypt.BCrypt

interface IAuthService {
    suspend fun registerUser(userRequest: LoginRequest)
    suspend fun findOneByUsername(userRequest: LoginRequest): User?
}


class AuthServiceImpl(val dao: ICustomersDao): IAuthService{
    override suspend fun registerUser(userRequest: LoginRequest){
        val hashedPass = BCrypt.hashpw(userRequest.password, BCrypt.gensalt())
        dao.registerUser(userRequest.username, hashedPass)
    }


    override suspend fun findOneByUsername(userRequest: LoginRequest): User? =
        dao.findUserByUsername(userRequest)

}
