import configuration.DatasourceConfiguration
import org.jooq.DSLContext

//5 define variable which will be used to access to / manipulation with database
lateinit var dslContext: DSLContext

//6 we removed generated code from main method and initialize our global dslContext variable with DslContext created by our DatasourceConfiguration class
fun main(args: Array<String>) {
    dslContext = DatasourceConfiguration.createDSLContext()

}