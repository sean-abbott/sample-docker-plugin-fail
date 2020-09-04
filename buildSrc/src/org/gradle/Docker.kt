import com.bmuschko.gradle.docker.tasks.AbstractDockerRemoteApiTask
import org.gradle.api.provider.Property

open class DockerImageIdForName : AbstractDockerRemoteApiTask {
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


