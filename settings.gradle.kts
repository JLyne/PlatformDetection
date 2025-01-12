rootProject.name = "PlatformDetection"
include(":PlatformDetection-common")
include(":PlatformDetection-paper")
include(":PlatformDetection-velocity")
project(":PlatformDetection-paper").projectDir = file("PlatformDetection-paper")
project(":PlatformDetection-velocity").projectDir = file("PlatformDetection-velocity")