import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//(jooq) 1. add import for jooq configuration Logging
import org.jooq.meta.jaxb.Logging

// (refactor) 1. move group and version to top
group = "org.example"
version = "1.0-SNAPSHOT"

// (flyway) 2. initialize constants for database connection, will be used for flyway (database migration and maintenance) and jooq (boiler-plate code generation)
val dbUrl = "jdbc:postgresql://localhost:5432/postgres"
val dbUser = "admin"
val dbPassword = "admin_password"
val dbSchema = "my"

val postgresVersion = "42.5.1"
//(jooq) 2. initialize constant for jooq version
val jooqVersion = "3.15.3"

plugins {
    kotlin("jvm") version "1.8.0"
    application
    // (flyway) 3. add flyway plugin to be able run gradle flyway tasks main to remember: gradle flywayMigrate; gradle flywayClean
    id("org.flywaydb.flyway") version "9.11.0"
    //(jooq) 3. add jooq plugin to be able to run database content generation, generating of table entities, types, DSL api data manipulation
    id("nu.studer.jooq") version "6.0.1"
}

repositories {
    mavenCentral()
}

dependencies {
    //(flyway) 4. add flyway and postgres dependencies to be able to configure and operate with them in project
    implementation("org.flywaydb:flyway-core:9.11.0")
    implementation("org.postgresql:postgresql:$postgresVersion")
    //(jooq) 4. add jooq generator dependency on postgres database and jooq API
    jooqGenerator("org.postgresql:postgresql:$postgresVersion")
    api("org.jooq:jooq:$jooqVersion")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

//(flyway) 5. configure flyway to connect to database by predefined database credentials and to find flyway sql scripts for db migration and maintenance
flyway {
    url = dbUrl
    //(flyway) to run migration on dedicated (predefined database schema we set schemas array where we want to apply migration. IF schema does not exists it will be automatically created)
    schemas = arrayOf(dbSchema)
    user = dbUser
    password = dbPassword
    baselineVersion = "-1"
    table = "flyway_schema_history"
    cleanDisabled = false
    // The file name prefix for sql migrations (default: "V")
    sqlMigrationPrefix = "V"
    // The file name separator for sql migrations (default = "__")
    sqlMigrationSeparator = "__"
    // The file name suffix for sql migrations (default = ".sql")
    sqlMigrationSuffixes = arrayOf(".sql")
    // Locations to scan recursively for migrations. (default = db/migration)
    // locations = arrayOf("db/migration")
    outOfOrder = false
    // Whether to automatically call baseline when migrate is executed against a non-empty schema with no metadata table. (default = false)
    // Be careful when enabling this as it removes the safety net that ensures Flyway does not migrate the wrong database in case of a configuration mistake!
    baselineOnMigrate = true
}

//(jooq) 5. add jooq configuration, as JDBC configuration from which database generate conetent, where to store it
// jooqGenerate runs before project compilation, but could be run manually by gradle generateJooq
jooq {
    version.set(jooqVersion)
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)

            jooqConfiguration.apply {
                logging = Logging.INFO
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = dbUrl
                    user = dbUser
                    password = dbPassword
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = dbSchema
                        excludes = "key_vault|flyway_schema_history"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isRelations = true
                        isPojos = true
                        isPojosEqualsAndHashCode = true
                    }
                    target.apply {
                        packageName = "jooq.generated"
                    }
                }
            }
        }
    }
}
