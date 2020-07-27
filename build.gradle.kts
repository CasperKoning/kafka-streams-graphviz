group = "xyz.casperkoning"
version = "0.0.1-SNAPSHOT"

plugins {
    `java-library`
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
