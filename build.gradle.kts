plugins {
    application
    kotlin("jvm") version "1.4.0"
}

version = "1.0.2"
group = "org.gradle.sample"

application {
    mainClass.set("org.gradle.sample.app.MainKt")
}

repositories {
    mavenCentral()
}

val imageIdForName by tasks.creating(DockerImageIdForName::class) {
    filteredImageName.set("alpine:3.4")
}

val printImageId by tasks.creating {
    dependsOn(imageIdForName)
    doLast {
        logger.quiet("Resolved image ID ${imageIdForName.imageId.get()} for name ${imageIdForName.filteredImageName.get()}")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
}


open class DockerImageIdForName : com.bmuschko.gradle.docker.tasks.AbstractDockerRemoteApiTask {
    @Input
    val filteredImageName : Property<String> = project.objects.property(String::class)

    @Internal
    val imageId : Property<String> = project.objects.property(String::class)

    constructor() {
        onNext(Action {
            this.withGroovyBuilder {
                imageId.set(getProperty("id") as String)
            }
        })
    }

    override fun runRemoteCommand() {
        val images = getDockerClient().listImagesCmd()
                .withImageNameFilter(filteredImageName.get())
                .exec()

        for(image in images) {
            nextHandler.execute(image)
        }
    }
}