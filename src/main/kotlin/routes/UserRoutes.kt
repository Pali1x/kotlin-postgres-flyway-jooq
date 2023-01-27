package routes

import dto.CreateUser
import dto.UpdateUser
import exception.UserNotFoundException
import exception.UserWithGivenEmailAlreadyExistsException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import service.UserServiceImpl
import util.getDashedTenantId
import java.util.*

//11(Ktor server) User Routes definition for handling application service calls on defined endpoints, definitions calls UserService for data manipulation handling

fun Routing.userRoutes() {
    val userService = UserServiceImpl()
    //4. (Authentication) we need to add our routes to authenticate method to be secured from outside, this must be done for all routes definitions if there are any other, otherwise they will be public
    authenticate {
        route("/user") {
            get {
                val tenantId = getDashedTenantId(call.request.header("authorization")!!)
                call.respond(userService.getAllUsers(tenantId))
            }

            get("/{id}") {
                val id = UUID.fromString(call.parameters["id"]!!)
                val tenantId = getDashedTenantId(call.request.header("authorization")!!)

                try {
                    call.respond(userService.getUserById(tenantId, id))
                } catch (e: UserNotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message.toString())
                }
            }

            post {
                val requestBody = call.receive<CreateUser>()
                val tenantId = getDashedTenantId(call.request.header("authorization")!!)

                try {
                    call.respond(userService.createUser(tenantId, requestBody))
                } catch (e: UserWithGivenEmailAlreadyExistsException) {
                    call.respond(HttpStatusCode.Conflict, e.message.toString())
                }
            }

            put("/{id}") {
                val requestBody = call.receive<UpdateUser>()
                val tenantId = getDashedTenantId(call.request.header("authorization")!!)

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
                val tenantId = getDashedTenantId(call.request.header("authorization")!!)

                try {
                    call.respond(HttpStatusCode.NoContent, userService.deleteUser(tenantId, id))
                } catch (e: UserNotFoundException) {
                    call.respond(HttpStatusCode.NotFound, e.message.toString())
                }
            }
        }
    }
}