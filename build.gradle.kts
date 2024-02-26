plugins {
    scala
    application
}

val scalaLibraryVersion = "2.13.12"

val ivyCustom = repositories.ivy {
    url = uri(projectDir.resolve("ivy-repo"))
    layout("ivy")
    content {
        includeVersion("org.scala-lang", "scala-library", scalaLibraryVersion)
    }
}
val mavenCentral = repositories.mavenCentral()

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
    }
}

application {
    mainClass.set("demo.App")
}

// Reverse-engineered from `ScalaRuntime.inferScalaClasspath` and `ScalaRuntimeHelper.SCALA_JAR_PATTERN`
fun forceInferScalaClasspath(classpath: FileCollection, version: String): FileCollection {
    val scalaJarPattern = Regex("(scala3?-(library|library_3)-)(\\d.*)(.jar)")
    val scalaJarReplace = "$1${Regex.escapeReplacement(version)}$4"
    val invalidClasspathThatScalaVersionCanBeInferredFrom = classpath.map {
        it.resolveSibling(scalaJarPattern.replace(it.name, scalaJarReplace))
    }
    return scalaRuntime.inferScalaClasspath(invalidClasspathThatScalaVersionCanBeInferredFrom)
}
tasks.withType<ScalaCompile> {
    scalaClasspath = forceInferScalaClasspath(classpath, scalaLibraryVersion)
}
tasks.withType<ScalaDoc> {
    scalaClasspath = forceInferScalaClasspath(classpath, scalaLibraryVersion)
}
