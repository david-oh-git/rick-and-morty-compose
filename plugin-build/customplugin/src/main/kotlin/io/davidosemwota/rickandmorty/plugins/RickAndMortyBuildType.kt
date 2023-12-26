package io.davidosemwota.rickandmorty.plugins

enum class RickAndMortyBuildType(val applicationSuffix: String? = null, val isMinifyedEnabled: Boolean = false) {
    DEBUG(".debug"),
    RELEASE(isMinifyedEnabled = true)
}