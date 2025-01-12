import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    alias(libs.plugins.pluginYmlPaper)
    id("platform-detection.java-conventions")
}

dependencies {
    implementation(project(":PlatformDetection-common"))

    compileOnly(libs.paperApi)
    compileOnly(libs.placeholderApi)
    compileOnly(libs.vivecraftSpigotExtensions)
}

tasks {
    // Include common classes in jar
    jar {
        from(project(":PlatformDetection-common").sourceSets.main.get().output)
    }
}

paper {
    name = rootProject.name
    main = "uk.co.notnull.platformdetection.PlatformDetectionPaper"
    apiVersion = libs.versions.paperApi.get().replace(Regex("\\-R\\d.\\d-SNAPSHOT"), "")
    authors = listOf("Jim (AnEnragedPigeon)")
    description = "Detects the platform/mod players are using"
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP

    serverDependencies {
        register("PlaceholderAPI") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.AFTER
        }
        register("floodgate") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.AFTER
        }
        register("Vivecraft-Spigot-Extensions") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.AFTER
        }
    }
}
