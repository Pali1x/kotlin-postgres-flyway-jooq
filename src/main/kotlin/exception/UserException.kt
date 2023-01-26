package exception

//3. (DAO) We need to define exceptions for easier identification of issue during debugging, they are used in UserDaoImpl class
class CreateUserException(message: String) : Exception(message)
class GetUserException(message: String) : Exception(message)
class GetUserListException(message: String) : Exception(message)
class UpdateUserException(message: String) : Exception(message)
class DeleteUserException(message: String) : Exception(message)
//3. (DAO) - END

class UserNotFoundException(message: String) : Exception(message)
class UserWithGivenEmailAlreadyExistsException(message: String) : Exception(message)