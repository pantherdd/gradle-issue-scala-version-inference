plugins {
    scala
    application
}

val scalaLibraryVersion = "2.13.12"

repositories {
    ivy {
        url = uri(projectDir.resolve("ivy-repo"))
        layout("ivy")
        content {
            includeVersion("org.scala-lang", "scala-library", scalaLibraryVersion)
        }
    }
    mavenCentral()
}

dependencies {
    implementation("org.scala-lang:scala-library:${scalaLibraryVersion}")

    testImplementation("org.scalatest:scalatest_2.13:3.2.17")
    testRuntimeOnly("org.scalatestplus:junit-5-10_2.13:3.2.17.0")
    testRuntimeOnly("org.junit.platform:junit-platform-engine:1.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.0")
}

tasks.test {
    useJUnitPlatform {
        includeEngines("scalatest")
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

application {
    mainClass.set("demo.App")
}
