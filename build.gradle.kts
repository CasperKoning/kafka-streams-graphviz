plugins {
    `java-library`
    `maven-publish`
    `signing`
    id("net.researchgate.release") version "2.8.1"
}

repositories {
    jcenter()
}

dependencies {
    api("guru.nidi:graphviz-java:0.17.0")
    implementation("org.apache.kafka:kafka-streams:2.5.0")
    
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "myLocalRepo"
            url = uri("file://${buildDir}/repo")
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
