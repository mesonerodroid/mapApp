package com.smesonero.meepchallenge.map.domain

data class ResourceInMap(
    val id :String,
    val name :String,
    val x :Double,
    val y :Double,
    val scheduledArrival:Int,
    val companyZoneId: Int,
    val lat: Double,
    val lon : Double,
    val descriptorMarkerColorId: Float
)