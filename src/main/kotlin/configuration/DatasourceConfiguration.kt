package configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.MappedSchema
import org.jooq.conf.RenderMapping
import org.jooq.conf.Settings
import org.jooq.impl.DSL


//1. We define new class with few methods which solve database connection and create DSLContext for database manipulation and access
data class DatasourceConfiguration(
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val schema: String
) {
    companion object {

        //2. for beginning we create this helper method to create our Datasource configuration based on hardcoded values, we will modify this by HOCON implementation later

        //9(Ktor server) method modification to use HOCON configuration instead fixed constants
        private fun createDataSourceConfig(applicationConfig: ApplicationConfig): DatasourceConfiguration {
            return DatasourceConfiguration(
                applicationConfig.property("datasource.jdbcUrl").getString(),
                applicationConfig.property("datasource.username").getString(),
                applicationConfig.property("datasource.password").getString(),
                applicationConfig.property("datasource.schema").getString()
            )
        }

        //3. this helper method will create Hikari Configuration which is used for HikariDatasourceCreation which is in final returned from method
        private fun createDataSource(dataSourceConfig: DatasourceConfiguration): HikariDataSource {
            val hikariConfig = HikariConfig()
            hikariConfig.username = dataSourceConfig.username
            hikariConfig.password = dataSourceConfig.password
            hikariConfig.jdbcUrl = dataSourceConfig.jdbcUrl
            hikariConfig.schema = dataSourceConfig.schema
            hikariConfig.maximumPoolSize = 10

            return HikariDataSource(hikariConfig)
        }

        //4 .this method will create DSL context for us, we are able to access database tables and manipulate with data in it
        //10(Ktor server) passing application configuration to method and to helper method
        fun createDSLContext(applicationConfig: ApplicationConfig): DSLContext {
            val dataSourceConfig = createDataSourceConfig(applicationConfig)
            val dataSource = createDataSource(dataSourceConfig)

            val settings = Settings()
                .withRenderMapping(
                    RenderMapping()
                        .withSchemata(
                            MappedSchema().withInput(dataSourceConfig.schema)
                                .withOutput(dataSourceConfig.schema)
                        )
                )

            return DSL.using(dataSource, SQLDialect.POSTGRES, settings)
        }
    }
}