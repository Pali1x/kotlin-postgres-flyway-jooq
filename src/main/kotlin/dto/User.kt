package dto

import jooq.generated.enums.TypeUser
import java.util.*

//First partial implementation of DTO for data transfer to database, it will be modified in future for request serialization
//This DTO is used for data transfer in direction out of service, here you could also define calculated fields like fullname

data class User(
    val id: UUID,
    val userType: TypeUser,
    val firstName: String,
    val lastName: String,
    val fullName: String = "$firstName $lastName",
    val email: String
)
