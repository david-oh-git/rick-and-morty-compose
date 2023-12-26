@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.rickandmorty.android.library)
}

android {
    namespace = "com.rickandmorty.testing"
}

dependencies {

    api(libs.junit4)
    api(libs.truth)
}