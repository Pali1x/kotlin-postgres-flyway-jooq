package dao

import dslContext
import dto.User
import exception.*
import jooq.generated.tables.records.TUserRecord
import jooq.generated.tables.references.T_USER
import org.jooq.impl.DSL
import org.jooq.impl.DSL.asterisk
import java.util.*

//2. (DAO) Implementation of Data Access Object interface, here is whole logic for data manipulation between our application service and Database

class UserDaoImpl : UserDao {
    override fun create(newUser: TUserRecord): User {
        try {
            val newRecord = dslContext.newRecord(T_USER)
            with(newRecord) {
                cFirstName = newUser.cFirstName
                cLastName = newUser.cLastName
                cUserType = newUser.cUserType
                cEmail = newUser.cEmail
                cTenantId = newUser.cTenantId
                store()
            }

            return convertToUser(newRecord)
        } catch (e: Exception) {
            throw CreateUserException("Unable to create user. Exception: $e")
        }
    }

    override fun getAll(tenantId: UUID): List<User> {
        try {
            return dslContext.select(asterisk()).from(T_USER).where(T_USER.C_TENANT_ID.equal(tenantId))
                .fetchInto(T_USER)
                .toList()
                .map { convertToUser(it) }
        } catch (e: Exception) {
            throw GetUserListException("Unable to get User List. Exception: $e")
        }
    }

    override fun getById(tenantId: UUID, userId: UUID): User? {
        try {
            with(T_USER) {
                val user =
                    dslContext.select(DSL.asterisk()).from(T_USER).where(C_TENANT_ID.equal(tenantId))
                        .and(C_ID.equal(userId)).fetchOneInto(
                            T_USER
                        ) ?: return null

                return convertToUser(user)
            }

        } catch (e: Exception) {
            throw GetUserException("Unable to get User by ID. Exception: $e")
        }
    }

    override fun getByEmail(tenantId: UUID, email: String): User? {
        try {
            with(T_USER) {
                val user =
                    dslContext.select(DSL.asterisk()).from(T_USER).where(C_TENANT_ID.equal(tenantId))
                        .and(C_EMAIL.equal(email)).fetchOneInto(
                            T_USER
                        ) ?: return null

                return convertToUser(user)
            }

        } catch (e: Exception) {
            throw GetUserException("Unable to get User by Email. Exception: $e")
        }
    }

    override fun update(updatedUser: TUserRecord): User {
        try {
            with(T_USER) {
                val record = dslContext.select(asterisk())
                    .from(T_USER)
                    .where(C_TENANT_ID.equal(updatedUser.cTenantId))
                    .and(C_ID.equal(updatedUser.cId))
                    .fetchOneInto(T_USER)!!

                with(record) {
                    cFirstName = updatedUser.cFirstName!!
                    cLastName = updatedUser.cLastName!!
                    cEmail = updatedUser.cEmail!!
                    cUserType = updatedUser.cUserType!!
                    update()
                }
                return convertToUser(record)
            }
        } catch (e: Exception) {
            throw UpdateUserException("Unable to update User. Exception: $e")
        }
    }

    override fun deleteById(tenantId: UUID, userId: UUID): Boolean {
        try {
            with(T_USER) {
                return dslContext.deleteFrom(T_USER).where(C_TENANT_ID.equal(tenantId), C_ID.equal(userId))
                    .execute() != 0
            }
        } catch (e: Exception) {
            throw DeleteUserException("Unable to delete User by ID. Exception: $e")
        }
    }

    private fun convertToUser(record: TUserRecord): User {
        with(record) {
            return User(
                id = cId!!,
                userType = cUserType!!,
                firstName = cFirstName!!,
                lastName = cLastName!!,
                email = cEmail!!
            )
        }
    }
}
