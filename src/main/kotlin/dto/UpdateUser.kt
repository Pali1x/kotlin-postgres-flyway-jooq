package dto

import jooq.generated.enums.TypeUser
import java.util.*

//First partial implementation of DTO for data transfer to database, it will be modified in future for request serialization
//DTO should contain only fields which are needed for given action, for example you could see that UpdateUser does not have fullName or createdAt field

data class UpdateUser(
    val id: UUID,
    val userType: TypeUser,
    val firstName: String,
    val lastName: String,
    val email: String
)
