@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.rickandmorty.android.library)
    id("com.apollographql.apollo3")
}

android {
    namespace = "com.rickandmorty.network"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.apollo3.lib)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.logging)

    testImplementation(projects.core.testing)
    testImplementation(libs.apollo3.mockserver)
}

apollo {
    service("rick-and-morty-service") {
        packageName.set("io.davidosemwota.rickandmorty.network.graphql")
    }
}

