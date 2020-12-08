package com.smesonero.meepchallenge.map.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smesonero.meepchallenge.base.HttpError
import com.smesonero.meepchallenge.base.NetworkError
import com.smesonero.meepchallenge.base.UnknownApiError
import com.smesonero.meepchallenge.map.domain.GetMapResourcesUseCase
import com.smesonero.meepchallenge.map.domain.ResourceInMap
import timber.log.Timber

class MapViewModel @ViewModelInject constructor(

    private val getMapResourcesUseCase: GetMapResourcesUseCase
) : ViewModel(), LifecycleObserver {

    val resourcesLiveData = MutableLiveData<List<ResourceInMap>>()
    val exceptionLiveData = MutableLiveData<MapApiError>()

    fun getMapInfo() {

        getMapResourcesUseCase {
            it.fold(
                { apiError ->
                    when (apiError) {
                        is HttpError -> exceptionLiveData.postValue(MapHttpError(apiError.body))
                        is NetworkError -> exceptionLiveData.postValue(MapNetworkError(apiError.throwable.localizedMessage))
                        is UnknownApiError -> exceptionLiveData.postValue(MapUnknownApiError(apiError.throwable.localizedMessage))
                    }
                },
                { resourceList ->
                    Timber.e("Success")
                    resourcesLiveData.postValue(resourceList)
                })
        }
    }
}
