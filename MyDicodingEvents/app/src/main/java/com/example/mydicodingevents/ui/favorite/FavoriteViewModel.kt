package com.example.mydicodingevents.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydicodingevents.data.EventRepository
import com.example.mydicodingevents.data.local.entity.EventFavEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getAllFavorite(): LiveData<List<EventFavEntity>>?
            = eventRepository.getAllFavorite()

    fun getFavoriteById(id: Int): LiveData<EventFavEntity>? {
        return eventRepository.getFavoriteById(id)
    }
    fun insertFavorite(favoriteEventEntity: EventFavEntity) {
        viewModelScope.launch {
            eventRepository.insertFavorite(favoriteEventEntity)
        }
    }
    fun deleteFavoriteById(id: Int) {
        viewModelScope.launch {
            eventRepository.deleteFavoriteById(id)
        }
    }
}