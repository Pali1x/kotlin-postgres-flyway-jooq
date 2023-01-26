package dto

import jooq.generated.enums.TypeUser

//First partial implementation of DTO for data transfer to database, it will be modified in future for request serialization
//DTO should contain only fields which are needed for given action, for example you could see that CreateUser does not have ID, because ID will be provided by database itself
data class CreateUser(
    val userType: TypeUser,
    val firstName: String,
    val lastName: String,
    val email: String
)
