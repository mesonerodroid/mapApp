package com.smesonero.meepchallenge

import arrow.core.Either
import com.smesonero.meepchallenge.map.data.MapRepository
import com.smesonero.meepchallenge.map.domain.GetMapResourcesUseCase
import com.smesonero.meepchallenge.map.ui.MapViewModel
import com.smesonero.meepchallenge.ws.service.ResourceService
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test

class MapViewModelTest{

    @RelaxedMockK
    lateinit var service: ResourceService

    private lateinit var repository: MapRepository
    private lateinit var viewModel: MapViewModel

    @RelaxedMockK
    lateinit var getMapResourcesUseCase :GetMapResourcesUseCase

    @Before
    fun init(){
        MockKAnnotations.init(this)

        repository= MapRepository(service)
        viewModel = MapViewModel(getMapResourcesUseCase)
    }

    @Test
    fun `use case is called properly, and resources are obtained` () {
        every {
            getMapResourcesUseCase(any())
        } answers {
            Either.Right
        }
        viewModel.getMapInfo()

        verify (exactly = 1){
            getMapResourcesUseCase(any())
        }
    }
}