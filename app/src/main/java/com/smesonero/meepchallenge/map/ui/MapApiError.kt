package com.smesonero.meepchallenge.map.ui

sealed class MapApiError

data class MapHttpError(val description: String) : MapApiError()

data class MapNetworkError(val description: String) : MapApiError()

data class MapUnknownApiError(val description: String) : MapApiError()