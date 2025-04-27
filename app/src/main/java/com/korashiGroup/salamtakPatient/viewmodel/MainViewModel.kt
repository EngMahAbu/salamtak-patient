package com.korashiGroup.salamtakPatient.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.korashiGroup.salamtakPatient.notification.model.MenuIconUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor():ViewModel(){


    private val _iconUpdateLiveData = MutableLiveData<MenuIconUpdate>()
    val iconUpdateLiveData: LiveData<MenuIconUpdate> = _iconUpdateLiveData


    fun updateBottomNavIcon( menuItemId: Int, iconResId: Int,state:Boolean) {
        _iconUpdateLiveData.value = MenuIconUpdate(menuItemId,iconResId,state)
    }

}