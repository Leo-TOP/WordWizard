plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)
    implementation("org.postgresql:postgresql:42.7.2")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.12.1")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "wordwizard.App"
}
