plugins {
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.apache.kafka:kafka-streams:2.5.0")
    
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}
