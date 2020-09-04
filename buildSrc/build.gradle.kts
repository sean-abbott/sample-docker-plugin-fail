plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
   implementation("com.bmuschko:gradle-docker-plugin:6.6.1")
}

