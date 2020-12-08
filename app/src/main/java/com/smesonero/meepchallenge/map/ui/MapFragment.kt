package com.smesonero.meepchallenge.map.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.smesonero.meepchallenge.R
import com.smesonero.meepchallenge.map.data.Definition.Companion.lowerLat
import com.smesonero.meepchallenge.map.data.Definition.Companion.lowerLong
import com.smesonero.meepchallenge.map.data.Definition.Companion.upperLat
import com.smesonero.meepchallenge.map.data.Definition.Companion.upperLong
import com.smesonero.meepchallenge.map.domain.ResourceInMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_first.*
import timber.log.Timber
import java.lang.StringBuilder


@AndroidEntryPoint
class MapFragment : Fragment() {

    val mapViewModel: MapViewModel by viewModels()
    lateinit var googleMap: GoogleMap
    val markers = hashMapOf<String, ResourceInMap>()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel.resourcesLiveData.observe(viewLifecycleOwner, Observer {
            Timber.d("resources loaded " + it.size)
            addMarkers(it)
        })

        mapViewModel.exceptionLiveData.observe(viewLifecycleOwner, Observer {
            Timber.d("API exception " + it)
            showError(it)
        })


        val map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(::loadedMap)
    }

    private fun loadedMap(map: GoogleMap) {

        googleMap = map
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(calculateCenter(), 14f)
        googleMap.moveCamera(cameraUpdate)

        googleMap.setOnCameraMoveStartedListener {
            if (it == REASON_GESTURE) {
                googleMap.clear()
                clearTextDetail()
            }
        }

        googleMap.setOnCameraIdleListener {
            mapViewModel.getMapInfo()
        }
    }

    //todo put in method class
    private fun calculateCenter(): LatLng? {
        val latDiff = (upperLat.toDouble()-lowerLat.toDouble())/2
        val longDiff = (upperLong.toDouble()-lowerLong.toDouble())/2
        return LatLng(lowerLat.toDouble()+latDiff, (lowerLong.toDouble()+longDiff))
    }

    private fun showError(mapApiError: MapApiError?) {
        when (mapApiError) {
            //todo add fragmentDialog, With network error for example a title: "Network error". with http error "petition error" and below the detail from mapApiError description
            is MapHttpError -> Timber.e("show map http error")
            is MapNetworkError -> Timber.e("show map network error")
            is MapUnknownApiError -> Timber.e("show map unknwon error")
        }
    }

    private fun addMarkers(list: List<ResourceInMap>) {

        list.forEach {
            val resource = it
            val latLng = LatLng(resource.lat, resource.lon)
            val title = it.name
            val marker = MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(it.descriptorMarkerColorId))

            var addedMarker=googleMap.addMarker(
                marker
            )
            markers.put(addedMarker.id, it)
        }
        googleMap.setOnMarkerClickListener {selected->
            markers.get(selected.id).let (::changeTextDetail)
        }
    }

    private fun changeTextDetail(resource: ResourceInMap?):Boolean {

        var textDetail:String = StringBuilder().append(resource?.name).append("\n").append("(").append(
            resource?.x
        ).append(", ").append(resource?.y).append(")").toString()
        mapMarkerDetail.text = textDetail
        return false
    }
    private fun clearTextDetail(){
        mapMarkerDetail.text=""
    }
}