@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.rickandmorty.android.library)
}

android {
    namespace = "io.davidosemwota.domain"

}

dependencies {

    testImplementation(libs.junit4)
}