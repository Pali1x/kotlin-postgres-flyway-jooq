package dto

import jooq.generated.enums.TypeUser
import kotlinx.serialization.Serializable
import serializer.UUIDSerializer
import java.util.*

//First partial implementation of DTO for data transfer to database, it will be modified in future for request serialization
//DTO should contain only fields which are needed for given action, for example you could see that UpdateUser does not have fullName or createdAt field
//13(Ktor server) we need to annotate data classes with @Serializable, in some cases we need to specify serializer to tell server how to negotiate with data
@Serializable
data class UpdateUser(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val userType: TypeUser,
    val firstName: String,
    val lastName: String,
    val email: String
)
