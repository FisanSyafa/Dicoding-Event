package com.example.mydicodingevents.data

import androidx.lifecycle.LiveData
import com.example.mydicodingevents.data.local.entity.EventFavEntity
import com.example.mydicodingevents.data.local.room.EventDao
import com.example.mydicodingevents.utils.AppExecutors

class EventRepository private constructor(
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {
    fun getAllFavorite(): LiveData<List<EventFavEntity>>? {
        return eventDao.getAllFavorite()
    }
    fun getFavoriteById(id: Int): LiveData<EventFavEntity>? {
        return  eventDao.getFavoriteById(id)
    }
    fun deleteFavoriteById(id: Int) {
        appExecutors.diskIO.execute {
            eventDao.deleteFavoriteById(id)
        }
    }
    fun insertFavorite(favoriteEventEntity: EventFavEntity) {
        appExecutors.diskIO.execute {
            eventDao.insertFavorite(favoriteEventEntity)
        }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(eventDao, appExecutors)
            }.also { instance = it }
    }
}