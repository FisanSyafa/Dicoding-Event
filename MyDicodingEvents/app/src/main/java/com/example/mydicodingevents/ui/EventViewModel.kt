package com.example.mydicodingevents.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydicodingevents.data.remote.response.ListEventsItem
import com.example.mydicodingevents.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> get() = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> get() = _finishedEvents

    private val _searchResults = MutableLiveData<List<ListEventsItem>>()
    val searchResults: LiveData<List<ListEventsItem>> get() = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> = _messageError

    companion object {
        private const val TAG = "EventViewModel"
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun findEvents() {
        if (_upcomingEvents.value != null) {
            return
        }
        _isLoading.value = true
        _messageError.value = null
        viewModelScope.launch {
            try {
                val responseUpcoming = ApiConfig.getApiService().getUpcomingEvents()
                _isLoading.value = false
                if (responseUpcoming.isSuccessful) {
                    responseUpcoming.body()?.let {
                        _upcomingEvents.value = it.listEvents
                    }
                } else {
                    val msgError = "Error : ${responseUpcoming.message()}"
                    _messageError.value = msgError
                    Log.e(TAG, msgError)
                }
                fetchFinishedEvents()
            } catch (e: Exception) {
                _isLoading.value = false
                val msgError = "Error : ${e.message}"
                _messageError.value = msgError
                Log.e(TAG, msgError)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    private suspend fun fetchFinishedEvents() {
        if (_finishedEvents.value != null) {
            return
        }
        _isLoading.value = true
        _messageError.value = null
        try {
            val responseFinished = ApiConfig.getApiService().getFinishedEvents()
            _isLoading.value = false
            if (responseFinished.isSuccessful) {
                responseFinished.body()?.let {
                    _finishedEvents.value = it.listEvents
                }
            } else {
                val msgError = "Error : ${responseFinished.message()}"
                _messageError.value = msgError
                Log.e(TAG, msgError)
            }
        } catch (e: Exception) {
            _isLoading.value = false
            val msgError = "Error : ${e.message}"
            _messageError.value = msgError
            Log.e(TAG, msgError)
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun findUpcomingEvent() {
        if (_upcomingEvents.value != null) {
            return
        }
        _isLoading.value = true
        _messageError.value = null

        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getUpcomingEvents()
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _upcomingEvents.value = it.listEvents
                    }
                } else {
                    val msgError = "Error : ${response.message()}"
                    _messageError.value = msgError
                    Log.e(TAG, msgError)
                }
            } catch (e: Exception) {
                _isLoading.value = false
                val msgError = "Error : ${e.message}"
                _messageError.value = msgError
                Log.e(TAG, msgError)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun findFinishedEvent() {
        if (_finishedEvents.value != null) {
            return
        }
        _isLoading.value = true
        _messageError.value = null

        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getFinishedEvents()
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _finishedEvents.value = it.listEvents
                    }
                } else {
                    val msgError = "Error : ${response.message()}"
                    _messageError.value = msgError
                    Log.e(TAG, msgError)
                }
            } catch (e: Exception) {
                _isLoading.value = false
                val msgError = "Error : ${e.message}"
                _messageError.value = msgError
                Log.e(TAG, msgError)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun searchEvents(query: String) {
        if (_searchResults.value != null) {
            return
        }
        _isLoading.value = true
        _messageError.value = null

        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().searchEvents(active = -1,query = query)
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _searchResults.value = it.listEvents
                    }
                } else {
                    val msgError = "Error : ${response.message()}"
                    _messageError.value = msgError
                    Log.e(TAG, msgError)
                }
            } catch (e: Exception) {
                _isLoading.value = false
                val msgError = "Error : ${e.message}"
                _messageError.value = msgError
                Log.e(TAG, msgError)
            }
        }
    }
}