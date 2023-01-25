import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// (refactor) 1. move group and version to top
group = "org.example"
version = "1.0-SNAPSHOT"

// (flyway) 2. initialize constants for database connection, will be used for flyway (database migration and maintenance) and jooq (boiler-plate code generation)
val dbUrl = "jdbc:postgresql://localhost:5432/postgres"
val dbUser = "admin"
val dbPassword = "admin_password"
val dbSchema = "my"

val postgresVersion = "42.5.1"

plugins {
    kotlin("jvm") version "1.8.0"
    application
    // (flyway) 3. add flyway plugin to be able run gradle flyway tasks main to remember: gradle flywayMigrate; gradle flywayClean
    id("org.flywaydb.flyway") version "9.11.0"
}

repositories {
    mavenCentral()
}

dependencies {
    //(flyway) 4. add flyway and postgres dependencies to be able to configure and operate with them in project
    implementation("org.flywaydb:flyway-core:9.11.0")
    implementation("org.postgresql:postgresql:$postgresVersion")
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
