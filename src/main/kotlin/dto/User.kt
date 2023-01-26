package dto

import jooq.generated.enums.TypeUser
import kotlinx.serialization.Serializable
import serializer.UUIDSerializer
import java.util.*

//First partial implementation of DTO for data transfer to database, it will be modified in future for request serialization
//This DTO is used for data transfer in direction out of service, here you could also define calculated fields like fullname
//13(Ktor server) we need to annotate data classes with @Serializable, in some cases we need to specify serializer to tell server how to negotiate with data
@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val userType: TypeUser,
    val firstName: String,
    val lastName: String,
    val fullName: String = "$firstName $lastName",
    val email: String
)
