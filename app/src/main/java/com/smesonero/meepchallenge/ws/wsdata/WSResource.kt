package com.smesonero.meepchallenge.ws.wsdata

data class WSResource(
    val id :String,
    val name :String,
    val x :Double,
    val y :Double,
    val scheduledArrival:Int,
    val companyZoneId: Int,
    val lat: Double,
    val lon : Double
)