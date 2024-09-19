package com.korashiGroup.salamtakPatient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.korashiGroup.salamtakPatient.model.healthy.ItemHealth
import com.korashiGroup.salamtakPatient.pagination.DoctorsPagingSource
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PagingViewmodel @Inject constructor(private val repos: RegisterRepositry) :ViewModel(){
    var citId=1
    var areaId=1
    var healthType=1
//    val list=Pager(PagingConfig(1)){
//        DoctorsPagingSource(repos,citId,areaId,healthType)
//    }.flow.cachedIn(viewModelScope)


    fun getDoctorsPagingData(cityId: Int, areaId: Int, healthType: Int): Flow<PagingData<ItemHealth>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, // Number of items to load per page
                prefetchDistance = 2,
                enablePlaceholders = false // Optional: whether to enable placeholders
            ),
            pagingSourceFactory = { DoctorsPagingSource(repos, cityId, areaId, healthType) }
        ).flow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000), // Optional: cache timeout
                initialValue = PagingData.empty() // Optional: initial empty data
            )
    }

}