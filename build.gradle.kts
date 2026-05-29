plugins {
    java
    alias(libs.plugins.spotless)
    alias(libs.plugins.shadow)
}

/* Project Properties */
val projectGroup        = project.property("project_group")         as String
val projectId           = project.property("project_id")            as String
val projectVersion      = project.property("project_version")       as String
val projectName         = project.property("project_name")          as String
val projectDescription  = project.property("project_description")   as String
val projectAuthors      = project.property("project_authors")       as String

group = projectGroup
version = projectVersion

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

spotless {
    java {
        palantirJavaFormat()
    }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.paper.api)

    implementation(libs.configurate.yaml)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    processResources {
        filteringCharset = "UTF-8"

        inputs.property("version", project.version)
        inputs.property("id", projectId)
        inputs.property("name", projectName)
        inputs.property("description", projectDescription)
        inputs.property("authors", projectAuthors)

        filesMatching("paper-plugin.yml") {
            expand(
                "version" to project.version,
                "id" to projectId,
                "name" to projectName,
                "description" to projectDescription,
                "authors" to projectAuthors
            )
        }
    }

    shadowJar {
        archiveClassifier.set("")

        relocate("org.spongepowered.configurate", "cc.maicra999.unboom.libs.org.spongepowered.configurate")
        relocate("org.yaml.snakeyaml", "cc.maicra999.unboom.libs.org.yaml.snakeyaml")
        relocate("io.leangen.geantyref", "cc.maicra999.unboom.libs.io.leangen.geantyref")
    }

    build {
        dependsOn(spotlessApply, shadowJar)
    }
}
