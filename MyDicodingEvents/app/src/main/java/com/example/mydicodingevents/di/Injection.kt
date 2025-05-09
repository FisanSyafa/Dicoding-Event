package com.example.mydicodingevents.di

import android.content.Context
import com.example.mydicodingevents.data.EventRepository
import com.example.mydicodingevents.data.local.room.EventDatabase
import com.example.mydicodingevents.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository{
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance( dao, appExecutors)
    }
}