package com.example.mydicodingevents.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mydicodingevents.ui.preferences.SettingPreferences

class SettingsModelFactory (private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory(
) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}