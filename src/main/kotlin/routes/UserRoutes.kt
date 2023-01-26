package routes

import dto.CreateUser
import dto.UpdateUser
import exception.UserNotFoundException
import exception.UserWithGivenEmailAlreadyExistsException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import service.UserServiceImpl
import java.util.*

//11(Ktor server) User Routes definition for handling application service calls on defined endpoints, definitions calls UserService for data manipulation handling

fun Routing.userRoutes() {
    val userService = UserServiceImpl()
    //we don't have jwt token authorization right now, thus we don't have tenantId yet
    val tenantId = UUID.fromString("6c336af3-66c4-42b3-b63b-c350f8659067")

    route("/user") {
        get {
            call.respond(userService.getAllUsers(tenantId))
        }

        get("/{id}") {
            val id = UUID.fromString(call.parameters["id"]!!)

            try {
                call.respond(userService.getUserById(tenantId, id))
            } catch (e: UserNotFoundException) {
                call.respond(HttpStatusCode.NotFound, e.message.toString())
            }
        }

        post {
            val requestBody = call.receive<CreateUser>()

            try {
                call.respond(userService.createUser(tenantId, requestBody))
            } catch (e: UserWithGivenEmailAlreadyExistsException) {
                call.respond(HttpStatusCode.Conflict, e.message.toString())
            }
        }

        put("/{id}") {
            val requestBody = call.receive<UpdateUser>()

            try {
                call.respond(userService.updateUser(tenantId, requestBody))
            } catch (e: UserNotFoundException) {
                call.respond(HttpStatusCode.NotFound, e.message.toString())
            } catch (e: UserWithGivenEmailAlreadyExistsException) {
                call.respond(HttpStatusCode.Conflict, e.message.toString())
            }
        }

        delete("/{id}") {
            val id = UUID.fromString(call.parameters["id"]!!)

            try {
                call.respond(HttpStatusCode.NoContent, userService.deleteUser(tenantId, id))
            } catch (e: UserNotFoundException) {
                call.respond(HttpStatusCode.NotFound, e.message.toString())
            }
        }
    }
}