package com.korashiGroup.salamtakPatient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.repository.ImageServicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageServicesViewModel @Inject constructor(
    private val repository: ImageServicesRepository
) : ViewModel() {

    private val _imageServicesList = MutableStateFlow<List<ImageServices>>(emptyList())
    val imageServicesList: StateFlow<List<ImageServices>> = _imageServicesList.asStateFlow()

    private val _imageMedicalServicesList = MutableStateFlow<List<ImageServices>>(emptyList())
    val imageMedicalServicesList: StateFlow<List<ImageServices>> = _imageMedicalServicesList.asStateFlow()

    init {
        fetchImageServices()
        fetchMedicalServices()
    }

    private fun fetchImageServices() {
        viewModelScope.launch {
            repository.imageServicesList.collect { list ->
                _imageServicesList.value = list
            }
        }
        repository.loadImageServices()
    }

    private fun fetchMedicalServices() {
        viewModelScope.launch {
            repository.imageMedicalServicesList.collect { list ->
                _imageMedicalServicesList.value = list
            }
        }
        repository.loadMedicalServices()
    }
}