package com.smesonero.meepchallenge.map.domain

import arrow.core.Either
import com.smesonero.meepchallenge.base.ApiError
import com.smesonero.meepchallenge.map.data.MapRepository
import com.smesonero.meepchallenge.ws.wsdata.WSResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetMapResourcesUseCase @Inject constructor(mapRepository: MapRepository){

    private val mapRepository=mapRepository
    private val job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)

    operator fun invoke(onResult: (Either<ApiError, List<ResourceInMap>>)->Unit )
    {
        scope.launch {
            val result = mapRepository.getMapResources()
            onResult(result)
        }
    }
}