package dao

import dto.User
import jooq.generated.tables.records.TUserRecord
import java.util.*

//1. (DAO) Definition of interface where we define all operations which we want to do with given Data Access Object.

interface UserDao {
    fun create(newUser: TUserRecord): User

    fun getAll(tenantId: UUID): List<User>

    fun getById(tenantId: UUID, userId: UUID): User?

    fun getByEmail(tenantId: UUID, email: String): User?

    fun update(updatedUser: TUserRecord): User

    fun deleteById(tenantId: UUID, userId: UUID): Boolean
}