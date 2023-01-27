import configuration.DatasourceConfiguration
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import org.jooq.DSLContext
import routes.userRoutes

//5 define variable which will be used to access to / manipulation with database
lateinit var dslContext: DSLContext
//4(Ktor server) define global variable for storing applicationConfiguration
lateinit var applicationConfig: ApplicationConfig

//6 we removed generated code from main method and initialize our global dslContext variable with DslContext created by our DatasourceConfiguration class
//5 (Ktor server) we need to modify body of file, first we need set main method to return EngineMain
fun main(args: Array<String>): Unit = EngineMain.main(args)

//6(Ktor server) definition of main module which will be defined in configuration as main entry point of application, application.conf is passed as parameter to this module method
fun Application.main(environment: ApplicationEnvironment) {
    //store configuration to global var
    applicationConfig = environment.config
    //pass application config to method, by this your connection could be configured based on environment variables
    dslContext = DatasourceConfiguration.createDSLContext(applicationConfig)

    //calling configuration modules such as Serialization configuration and Routing configuration
    configureCORS()
    configureSerialization()
    configureRouting()
}

//7(Ktor server) Serialization configuration by installing ContentNegotiation plugin configured to json, to tell server how handle and parse input/output data, in our case json objects
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

//8(Ktor server) Routing configuration where we specify all routes which will be accessible by application service
fun Application.configureRouting() {
    routing {
        userRoutes()
    }
}

//2. CORS configuration implementation, you could configure different routes and different hosts by standalone configuration, for start, lets just allow all Host requesting our service. IMPORTANT: this is not recommended for production
//Additional info could be found here https://ktor.io/docs/cors.html
fun Application.configureCORS() {
    install(CORS) {
        anyHost()
    }
}