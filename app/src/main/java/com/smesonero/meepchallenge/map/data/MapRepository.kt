package com.smesonero.meepchallenge.map.data

import arrow.core.Either
import com.google.android.gms.maps.model.BitmapDescriptorFactory.*
import com.smesonero.meepchallenge.base.ApiError
import com.smesonero.meepchallenge.map.domain.ResourceInMap
import com.smesonero.meepchallenge.ws.service.ResourceService
import com.smesonero.meepchallenge.ws.wsdata.WSResource
import javax.inject.Inject

class MapRepository @Inject constructor(resourceService: ResourceService){

    val resourceService = resourceService
    val lowerLatLong = "38.711046,-9.160096"
    val upperLatLong = "38.739429,-9.137115"

    suspend fun getMapResources(): Either<ApiError, List<ResourceInMap>>
    {
        var ret = resourceService.getLisboaResourcesList(lowerLatLong,upperLatLong)
        var domainList = listOf<ResourceInMap>()
        if (ret.isRight()){
            var wsList :List<WSResource> = listOf()
            ret.map{wsList = it}
            domainList = mapResourceToDomain(wsList)
        }
        return ret.map { list: List<WSResource> -> domainList }
    }

    private fun mapResourceToDomain(wsList :List<WSResource>): List<ResourceInMap> {

        val hashMap:HashMap<Int, Float> = HashMap()
        val listColors: List<Float> = listOf(
            HUE_GREEN, HUE_BLUE, HUE_RED, HUE_YELLOW, HUE_ORANGE,
            HUE_VIOLET, HUE_AZURE, HUE_ROSE, HUE_CYAN, HUE_MAGENTA)
        var index=0

        wsList.forEach{
            if(hashMap.isEmpty() || !hashMap.containsKey(it.companyZoneId)){
                hashMap.put(it.companyZoneId, listColors.get(index))
                index++
            }
        }

        val domainList = mutableListOf<ResourceInMap>()
        wsList.forEach{
            val wsResource = ResourceInMap(it.id, it.name, it.x, it.y, it.scheduledArrival, it.companyZoneId, it.lat, it.lon,
                hashMap.get(it.companyZoneId)!!
            )
            domainList.add(wsResource)
        }
        return domainList.toList()
    }
}