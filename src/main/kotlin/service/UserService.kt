package service

import dto.CreateUser
import dto.UpdateUser
import dto.User
import java.util.*

//1. (Service) we define interface with definition what should our service do

interface UserService {
    fun createUser(tenantId: UUID, user: CreateUser): User

    fun getAllUsers(tenantId: UUID): List<User>

    fun getUserById(tenantId: UUID, userId: UUID): User

    fun updateUser(tenantId: UUID, updatedUser: UpdateUser): User

    fun deleteUser(tenantId: UUID, userId: UUID)
}