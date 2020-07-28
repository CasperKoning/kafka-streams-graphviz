extra["isReleaseVersion"] = !version.toString().endsWith("SNAPSHOT")

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
            pom {
                name.set("Kafka Streams Graphviz")
                description.set("Library glueing together Kafka Streams Topology and Graphviz")
                url.set("https://github.com/casperkoning/kafka-streams-graphviz")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("casperkoning")
                        name.set("Casper Koning")
                        email.set("casper.e.koning@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:https://github.com:CasperKoning/kafka-streams-graphviz.git")
                    url.set("https://github.com:CasperKoning/kafka-streams-graphviz.git")
                    developerConnection.set("scm:git:git://github.com:CasperKoning/kafka-streams-graphviz.git")
                }
            }
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
    setRequired({
        (project.extra["isReleaseVersion"] as Boolean) && gradle.taskGraph.hasTask("publish")
    })
    sign(publishing.publications["mavenJava"])
}
