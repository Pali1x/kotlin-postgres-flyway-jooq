package dto

import jooq.generated.enums.TypeUser
import kotlinx.serialization.Serializable

//First partial implementation of DTO for data transfer to database, it will be modified in future for request serialization
//DTO should contain only fields which are needed for given action, for example you could see that CreateUser does not have ID, because ID will be provided by database itself
//13(Ktor server) we need to annotate data classes with @Serializable, in some cases we need to specify serializer to tell server how to negotiate with data
@Serializable
data class CreateUser(
    val userType: TypeUser,
    val firstName: String,
    val lastName: String,
    val email: String
)
