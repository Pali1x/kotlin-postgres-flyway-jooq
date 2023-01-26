package service

import dao.UserDaoImpl
import dto.CreateUser
import dto.UpdateUser
import dto.User
import exception.UserNotFoundException
import exception.UserWithGivenEmailAlreadyExistsException
import jooq.generated.tables.records.TUserRecord
import java.util.*

//2. (Service) Service class with Interface logic implementation

class UserServiceImpl : UserService {
    private val userDao = UserDaoImpl()

    override fun createUser(tenantId: UUID, user: CreateUser): User {
        if (userDao.getByEmail(tenantId, user.email) != null) {
            throw UserWithGivenEmailAlreadyExistsException("User with given email: ${user.email} already exists.")
        }

        val userRecord = TUserRecord(
            cTenantId = tenantId,
            cUserType = user.userType,
            cFirstName = user.firstName,
            cLastName = user.lastName,
            cEmail = user.email
        )

        return userDao.create(userRecord)
    }

    override fun getAllUsers(tenantId: UUID): List<User> {
        return userDao.getAll(tenantId)
    }

    override fun getUserById(tenantId: UUID, userId: UUID): User {
        return userDao.getById(tenantId, userId) ?: throw UserNotFoundException("User with ID: $userId does not exist.")
    }

    override fun updateUser(tenantId: UUID, updatedUser: UpdateUser): User {
        val existingUser = getUserById(tenantId, updatedUser.id)

        if (existingUser.email != updatedUser.email && userDao.getByEmail(tenantId, updatedUser.email) != null) {
            throw UserWithGivenEmailAlreadyExistsException("User with given email: ${updatedUser.email} already exists.")
        }

        val userRecord = TUserRecord(
            cId = updatedUser.id,
            cTenantId = tenantId,
            cUserType = updatedUser.userType,
            cFirstName = updatedUser.firstName,
            cLastName = updatedUser.lastName,
            cEmail = updatedUser.email
        )

        return userDao.update(userRecord)
    }

    override fun deleteUser(tenantId: UUID, userId: UUID) {
        if (!userDao.deleteById(tenantId, userId)) {
            throw UserNotFoundException("User with ID: $userId does not exist.")
        }
    }
}