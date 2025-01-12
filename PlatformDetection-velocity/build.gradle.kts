import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    alias(libs.plugins.shadow)
    id("platform-detection.java-conventions")
}

dependencies {
    implementation(project(":PlatformDetection-common"))

    compileOnly(libs.velocityApi)
    compileOnly(libs.vivecraftVelocityExtensions)

    annotationProcessor(libs.velocityApi)
}

tasks {
    shadowJar {
        archiveClassifier = ""
    }

    build {
        dependsOn(shadowJar)
    }

    processResources {
        expand("version" to project.version)
    }
}
